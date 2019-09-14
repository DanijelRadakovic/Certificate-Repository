package megatravel.com.cerrepo.domain.cert;

public class CertificateDistribution {
    private String serialNumber;
    private boolean privateKey;
    private boolean keystore;
    private boolean truststore;
    private String hostname;
    private String destination;

    public CertificateDistribution() {
    }

    public CertificateDistribution(String serialNumber, boolean privateKey, boolean keystore,
                                   boolean truststore, String hostname, String destination) {
        this.serialNumber = serialNumber;
        this.privateKey = privateKey;
        this.keystore = keystore;
        this.truststore = truststore;
        this.hostname = hostname;
        this.destination = destination;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public boolean isKeystore() {
        return keystore;
    }

    public void setKeystore(boolean keystore) {
        this.keystore = keystore;
    }

    public boolean isTruststore() {
        return truststore;
    }

    public void setTruststore(boolean truststore) {
        this.truststore = truststore;
    }

    public boolean isPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(boolean privateKey) {
        this.privateKey = privateKey;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }
}
