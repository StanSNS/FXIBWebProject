package fxibBackend.controller.DataController;

import fxibBackend.dto.InitDTOS.PricingDTO;
import fxibBackend.service.DataServices.PricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fxibBackend.constants.MappingConstants.PRICING_CONTROLLER_MAPPING;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;

@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping(PRICING_CONTROLLER_MAPPING)
public class PricingController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final PricingService pricingService;

    /**
     * Retrieves a list of PricingDTO objects containing pricing information for Pricing page.
     *
     * @return A ResponseEntity containing the list of PricingDTO objects if available, or an HTTP 500 error response if empty.
     */
    @GetMapping
    public ResponseEntity<List<PricingDTO>> getAllPricings() {
        // Fetch all PricingDTO data from the PricingService
        List<PricingDTO> pricingDTOS = pricingService.getAllPricingData();

        // Check if the list is empty and return the appropriate response
        if (pricingDTOS.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return a ResponseEntity with the PricingDTO list and an HTTP OK status
        return new ResponseEntity<>(pricingDTOS, HttpStatus.OK);
    }


}