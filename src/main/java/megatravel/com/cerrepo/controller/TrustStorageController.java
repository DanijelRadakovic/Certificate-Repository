package megatravel.com.cerrepo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import megatravel.com.cerrepo.converter.CertificateConverter;
import megatravel.com.cerrepo.domain.dto.cer.CertificateDTO;
import megatravel.com.cerrepo.domain.dto.cer.TrustStorageDTO;
import megatravel.com.cerrepo.service.TrustStorageService;
import org.everit.json.schema.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/trust")
public class TrustStorageController extends ValidationController {

    private static final Logger logger = LoggerFactory.getLogger(TrustStorageController.class);

    @Autowired
    private TrustStorageService trustStorageService;


    /**
     * GET /api/trust/{id}
     *
     * @param id of requested certificate
     * @return trust storage with requested id
     */
    @GetMapping("{id}")
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<List<CertificateDTO>> getCertsFromTruststorage(@PathVariable String id) {
        logger.info("action=getTruststore certId={} status=success", id);
        return new ResponseEntity<>(CertificateConverter.fromListX509ToDTO(trustStorageService.
                getCertsFromTrustStorage(id)), HttpStatus.OK);
    }

    /**
     * POST /api/trust
     *
     * @param request that needs to be processed
     * @return message about action results
     */
    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    //@PreAuthorize("hasAuthority('SECADMIN')")
    public ResponseEntity<String> save(@RequestBody String request) throws IOException, ValidationException {
        validateJSON(request, "truststore.json");
        TrustStorageDTO dto = new ObjectMapper().readValue(request, TrustStorageDTO.class);
        logger.info("action=updateTruststore certSN={}", dto.getTarget());
        trustStorageService.updateTrustStorage(dto.getTarget(), dto.getSerialNumbers());
        return new ResponseEntity<>("Truststore successfully updated!", HttpStatus.OK);
    }
}
