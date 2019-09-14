package megatravel.com.cerrepo.service;

import megatravel.com.cerrepo.config.AppConfig;
import megatravel.com.cerrepo.repository.CertificateStorage;
import megatravel.com.cerrepo.util.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

@Service
public class TrustStorageService {

    @Autowired
    private AppConfig config;

    @Autowired
    private CertificateStorage storage;

    public void updateTrustStorage(String target, List<String> serialNumbers) {
        char[] password = config.getTruststorePassword().toCharArray();
        List<X509Certificate> certs = storage.getCertificates(serialNumbers);
        KeyStore trustStore = emptyTrustStorage(target);

        try {
            for (X509Certificate cert : certs) {
                trustStore.setCertificateEntry(cert.getSerialNumber().toString(), cert);
            }
            trustStore.store(new FileOutputStream(Paths.get(config.getTruststoreDirectory(),
                    target + ".p12").toString()), password);

        } catch (CertificateException | NoSuchAlgorithmException | IOException | KeyStoreException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<X509Certificate> getCertsFromTrustStorage(String serialNumber) {
        char[] password = config.getTruststorePassword().toCharArray();
        List<X509Certificate> certs = new ArrayList<>();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(Paths.get(config.getTruststoreDirectory(),
                    serialNumber + ".p12").toString()), password);

            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                certs.add((X509Certificate) keyStore.getCertificate(aliases.nextElement()));
            }
            return certs;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private KeyStore emptyTrustStorage(String serialNumber) {
        char[] password = config.getTruststorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(Paths.get(config.getTruststoreDirectory(),
                    serialNumber + ".p12").toString()), password);

            List<String> aliases = Collections.list(keyStore.aliases());

            for (String alias : aliases) {
                keyStore.deleteEntry(alias);
            }
            return keyStore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
