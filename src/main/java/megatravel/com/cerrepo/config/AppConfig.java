package megatravel.com.cerrepo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${pki.keystore.dir}")
    private String keystoreDirectory;

    @Value("${pki.keystore.password}")
    private String keystorePassword;

    @Value("${pki.truststore.dir}")
    private String truststoreDirectory;

    @Value("${pki.truststore.password}")
    private String truststorePassword;

    @Value("${pki.sftp.username}")
    private String sftpUsername;

    @Value("${pki.sftp.repo.location}")
    private String repositoryLocation;

    @Value("${pki.sftp.repo.hostname}")
    private String repositoryHostname;

    public String getKeystoreDirectory() {
        return keystoreDirectory;
    }

    public void setKeystoreDirectory(String keystoreDirectory) {
        this.keystoreDirectory = keystoreDirectory;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public String getTruststoreDirectory() {
        return truststoreDirectory;
    }

    public void setTruststoreDirectory(String truststoreDirectory) {
        this.truststoreDirectory = truststoreDirectory;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }

    public void setTruststorePassword(String truststorePassword) {
        this.truststorePassword = truststorePassword;
    }

    public String getSftpUsername() {
        return sftpUsername;
    }

    public void setSftpUsername(String sftpUsername) {
        this.sftpUsername = sftpUsername;
    }

    public String getRepositoryLocation() {
        return repositoryLocation;
    }

    public void setRepositoryLocation(String repositoryLocation) {
        this.repositoryLocation = repositoryLocation;
    }

    public String getRepositoryHostname() {
        return repositoryHostname;
    }

    public void setRepositoryHostname(String repositoryHostname) {
        this.repositoryHostname = repositoryHostname;
    }
}
