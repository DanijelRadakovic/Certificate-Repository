package megatravel.com.cerrepo.service;

import megatravel.com.cerrepo.config.AppConfig;
import megatravel.com.cerrepo.domain.cert.CerChanPrivateKey;
import megatravel.com.cerrepo.repository.CertificateStorage;
import megatravel.com.cerrepo.task.RemoveExpiredCertificateTask;
import megatravel.com.cerrepo.task.TaskManager;
import megatravel.com.cerrepo.util.DirectoryWatcher;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AccessDescription;
import org.bouncycastle.asn1.x509.AuthorityInformationAccess;
import org.bouncycastle.asn1.x509.Extension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class WatchService {

    private static final Logger logger = LoggerFactory.getLogger(DirectoryWatcher.class);
    private static final Map<String, String> mapper = new HashMap<>();

    @Autowired
    private AppConfig config;

    @Autowired
    private CertificateStorage repository;

    @Autowired
    private TaskManager executor;


    public void scheduled() {

    }

    @PostConstruct
    public void init() {
        new Thread(new DirectoryWatcher(config.getKeystoreDirectory(),
                new WatchEvent.Kind<?>[]{StandardWatchEventKinds.ENTRY_CREATE},
                (watchEvent, directory) -> {
                    final Path changed = (Path) watchEvent.context();
                    String fullPath = Paths.get(directory, changed.toString()).toString();
                    String change = filterChange(changed.toString());
                    if (change != null) {
                        executor.addTask(new RemoveExpiredCertificateTask(change,
                                getValidationPeriod(fullPath, change), TimeUnit.SECONDS));
                    }
                })).start();
    }

//    @PostConstruct
//    public void init() {
//        new Thread(new DirectoryWatcher(config.getKeystoreDirectory(),
//                new WatchEvent.Kind<?>[]{StandardWatchEventKinds.ENTRY_CREATE},
//                (watchEvent, directory) -> {
//                    final Path changed = (Path) watchEvent.context();
//                    String fullPath = Paths.get(directory, changed.toString()).toString();
//                    String change = filterChange(changed.toString());
//                    if (change != null) {
//                        processAIAField(fullPath, change);
//                    }
//                }))
//                .start();
//    }

    private String filterChange(String change) {
        String[] tokens = change.split("\\.");
        StringBuilder result = new StringBuilder();
        if (tokens.length >= 2 && (tokens[tokens.length - 1].equals("p12")
                || tokens[tokens.length - 1].equals("pfx"))) {
            for (int i = 0; i < tokens.length - 1; i++) {
                result.append(tokens[i]);
            }
            return result.toString();
        }
        return null;
    }


    private long getValidationPeriod(String keystorePath, String serialNumber) {
        CerChanPrivateKey chanPrivateKey = repository.getCertificateChain(keystorePath, serialNumber, false);
        X509Certificate certificate = (X509Certificate) chanPrivateKey.getChain()[0];
        LocalDateTime notAfter = LocalDateTime.ofInstant(certificate.getNotAfter().toInstant(), ZoneId.systemDefault());
        LocalDateTime notBefore = LocalDateTime.ofInstant(certificate.getNotBefore().toInstant(),
                ZoneId.systemDefault());
        return Duration.between(notBefore, notAfter).getSeconds();

    }

    private void processAIAField(String keystorePath, String serialNumber) {
        try {
            CerChanPrivateKey chanPrivateKey = repository.getCertificateChain(keystorePath,
                    serialNumber, false);
            Certificate[] chain = chanPrivateKey.getChain();
            X509Certificate cert;
            X509Certificate parent;
            for (int i = 0; i < chain.length; i++) {
                cert = (X509Certificate) chain[i];
                if (i + 1 < chain.length) parent = (X509Certificate) chain[i + 1];
                else parent = null;

                byte[] extensionValue = cert.getExtensionValue(Extension.authorityInfoAccess.getId());
                ASN1OctetString aIAOc = ASN1OctetString.getInstance(extensionValue);
                AuthorityInformationAccess aIA = AuthorityInformationAccess.getInstance(aIAOc.getOctets());
                for (int j = 0; j < aIA.getAccessDescriptions().length; j++) {
                    AccessDescription accessDescription = aIA.getAccessDescriptions()[j];
                    if (AccessDescription.id_ad_caIssuers.equals(accessDescription.getAccessMethod())
                            && parent != null) {
                        String[] tokens = accessDescription.getAccessLocation().getName().toString().split("/");
                        mapper.put(tokens[tokens.length - 1], parent.getSerialNumber().toString());
                    }
                }
            }
        } catch (Exception ignored) {
            logger.warn("action=parsingAIAField status=failure");
        }
    }
}
