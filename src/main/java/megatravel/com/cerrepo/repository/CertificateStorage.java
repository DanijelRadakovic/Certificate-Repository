package megatravel.com.cerrepo.repository;

import megatravel.com.cerrepo.config.AppConfig;
import megatravel.com.cerrepo.domain.cert.CerChanPrivateKey;
import megatravel.com.cerrepo.util.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

@Repository
public class CertificateStorage {

    @Autowired
    private AppConfig config;


    public CerChanPrivateKey getCertificateChain(String serialNumber, boolean privateKey) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(Paths.get(config.getKeystoreDirectory(),
                    serialNumber + ".p12").toString()), password);
            Certificate[] certs = keyStore.getCertificateChain(serialNumber);

            if (!privateKey) return new CerChanPrivateKey(certs, null);

            Key key = keyStore.getKey(serialNumber, serialNumber.toCharArray());
            if (key instanceof PrivateKey) {
                return new CerChanPrivateKey(certs, (PrivateKey) key);
            } else {
                throw new GeneralException("Error occurred while reading certificate.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException | UnrecoverableKeyException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public CerChanPrivateKey getCertificateChain(String keystorePath, String serialNumber, boolean privateKey) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(keystorePath), password);
            Certificate[] certs = keyStore.getCertificateChain(serialNumber);

            if (!privateKey) return new CerChanPrivateKey(certs, null);

            Key key = keyStore.getKey(serialNumber, serialNumber.toCharArray());
            if (key instanceof PrivateKey) {
                return new CerChanPrivateKey(certs, (PrivateKey) key);
            } else {
                throw new GeneralException("Error occurred while reading certificate.",
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException | UnrecoverableKeyException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void removeCertificate(String serialNumber) {
        char[] password = config.getKeystorePassword().toCharArray();
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(new FileInputStream(Paths.get(config.getKeystoreDirectory(),
                    serialNumber + ".p12").toString()), password);

            if (keyStore.containsAlias(serialNumber)) {
                keyStore.deleteEntry(serialNumber);
            }
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException |
                CertificateException e) {
            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<X509Certificate> getCertificates(List<String> serialNumbers) {
//        List<X509Certificate> certs = new ArrayList<>(serialNumbers.size());
//        char[] password = config.getKeystorePassword().toCharArray();
//        try {
//            KeyStore keyStore = KeyStore.getInstance("PKCS12");
//            keyStore.load(new FileInputStream(config.getKeystore()), password);
//
//            serialNumbers.forEach(s -> {
//                try {
//                    certs.add((X509Certificate) keyStore.getCertificate(s));
//                } catch (KeyStoreException e) {
//                    throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//                }
//            });
//            return certs;
//        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
//            e.printStackTrace();
//            throw new GeneralException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
        // TODO implement search certificate in keystore by serialNumbers
        return null;
    }
}
