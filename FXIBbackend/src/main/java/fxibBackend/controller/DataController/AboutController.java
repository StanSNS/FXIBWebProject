package fxibBackend.controller.DataController;


import fxibBackend.dto.InitDTOS.AboutDTO;
import fxibBackend.service.DataServices.AboutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static fxibBackend.constants.MappingConstants.ABOUT_CONTROLLER_MAPPING;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;

@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping(ABOUT_CONTROLLER_MAPPING)
public class AboutController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final AboutService aboutService;

    /**
     * Retrieves a list of AboutDTO objects containing information about the "About" page.
     *
     * @return A ResponseEntity containing the list of AboutDTO objects if available, or an HTTP 500 error response if empty.
     */
    @GetMapping
    public ResponseEntity<List<AboutDTO>> getAllAbouts() {
        // Fetch all AboutDTO data from the AboutService
        List<AboutDTO> allAboutData = aboutService.getAllAboutData();

        // Check if the list is empty and return the appropriate response
        if (allAboutData.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        // Return a ResponseEntity with the AboutDTO list and an HTTP OK status
        return new ResponseEntity<>(allAboutData, HttpStatus.OK);
    }

}