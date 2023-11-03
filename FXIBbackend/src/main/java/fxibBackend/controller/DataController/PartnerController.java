package fxibBackend.controller.DataController;

import fxibBackend.dto.InitDTOS.PartnerDTO;
import fxibBackend.service.DataServices.PartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fxibBackend.constants.MappingConstants.PARTNERS_CONTROLLER_MAPPING;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;


@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping(PARTNERS_CONTROLLER_MAPPING)
public class PartnerController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final PartnerService partnerService;

    /**
     * Retrieves a list of PartnerDTO objects containing information about partners page.
     *
     * @return A ResponseEntity containing the list of PartnerDTO objects if available, or an HTTP 500 error response if empty.
     */
    @GetMapping
    public ResponseEntity<List<PartnerDTO>> getAllPartners() {
        // Fetch all PartnerDTO data from the PartnerService
        List<PartnerDTO> partnerDTOS = partnerService.getAllPartnerData();

        // Check if the list is empty and return the appropriate response
        if (partnerDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return a ResponseEntity with the PartnerDTO list and an HTTP OK status
        return new ResponseEntity<>(partnerDTOS, HttpStatus.OK);
    }


}

