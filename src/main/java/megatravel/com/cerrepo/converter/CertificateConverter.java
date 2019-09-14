package megatravel.com.cerrepo.converter;

import megatravel.com.cerrepo.domain.cert.CertificateDistribution;
import megatravel.com.cerrepo.domain.dto.cer.CertificateDTO;
import megatravel.com.cerrepo.domain.dto.cer.CertificateDistributionDTO;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

public class CertificateConverter extends AbstractConverter {

    public static CertificateDistribution toEntity(CertificateDistributionDTO cert) {
        return new CertificateDistribution(cert.getSerialNumber(), cert.isPrivateKey(), cert.isKeystore(),
                cert.isTruststore(), cert.getHostname(), cert.getDestination());
    }

    public static List<CertificateDTO> fromListX509ToDTO(List<X509Certificate> certs) {
        return certs.stream().map(cert -> new CertificateDTO(0L, cert.getSerialNumber().toString(),
                cert.getSubjectDN().toString(), true)).collect(Collectors.toList());
    }
}
