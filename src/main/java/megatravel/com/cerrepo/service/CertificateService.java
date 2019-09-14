package megatravel.com.cerrepo.service;

import megatravel.com.cerrepo.config.AppConfig;
import megatravel.com.cerrepo.domain.cert.Certificate;
import megatravel.com.cerrepo.domain.enums.RevokeReason;
import megatravel.com.cerrepo.repository.CertificateStorage;
import megatravel.com.cerrepo.repository.CertificateRepository;
import megatravel.com.cerrepo.util.exception.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateService {

    @Autowired
    private AppConfig config;

    @Autowired
    private CertificateStorage repository;

    @Autowired
    private CertificateRepository certificateRepository;


    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    public List<Certificate> findAllCA() {
        return validate(certificateRepository.findAllCA());
    }

    public List<Certificate> findAllActive() {
        return validate(certificateRepository.findAllActive());
    }

    public List<Certificate> findAllActiveCA() {
        return validate(certificateRepository.findAllActiveCA());
    }

    public List<Certificate> findAllClients() {
        return validate(certificateRepository.findAllClients());
    }

    public List<Certificate> findAllActiveClients() {
        return validate(certificateRepository.findAllActiveClients());
    }

    public void remove(Long id, RevokeReason revokeReason) {
        Certificate cert = certificateRepository.findById(id).orElseThrow(() ->
                new GeneralException("Certificate with id: " + id + ".", HttpStatus.BAD_REQUEST));
        cert.setActive(false);
        cert.setRevokeReason(revokeReason);
        certificateRepository.save(cert);
        repository.removeCertificate(cert.getSerialNumber());
    }

    private List<Certificate> validate(List<Certificate> certs) {
        List<Certificate> result = new ArrayList<>(certs.size());
        List<Certificate> invalid = new ArrayList<>(certs.size());
        java.security.cert.Certificate[] chain;
        for (Certificate cert : certs) {
            chain = repository.getCertificateChain(cert.getSerialNumber(), false).getChain();
            try {
                ((X509Certificate) chain[0]).checkValidity();
                result.add(cert);
            } catch (CertificateExpiredException | CertificateNotYetValidException e) {
                cert.setActive(false);
                invalid.add(cert);
            }
        }
        certificateRepository.saveAll(invalid);
        return result;
    }
}
