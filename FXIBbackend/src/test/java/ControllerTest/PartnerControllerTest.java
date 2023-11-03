package ControllerTest;

import fxibBackend.controller.DataController.PartnerController;
import fxibBackend.dto.InitDTOS.PartnerDTO;
import fxibBackend.service.DataServices.PartnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class PartnerControllerTest {

    @Mock
    private PartnerService partnerService;
    private PartnerController partnerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        partnerController = new PartnerController(partnerService);
    }

    @Test
    void testGetAllPartners() {
        List<PartnerDTO> mockPartners = new ArrayList<>();
        mockPartners.add(new PartnerDTO());
        mockPartners.add(new PartnerDTO());

        when(partnerService.getAllPartnerData()).thenReturn(mockPartners);
        ResponseEntity<List<PartnerDTO>> response = partnerController.getAllPartners();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPartners, response.getBody());

        when(partnerService.getAllPartnerData()).thenReturn(new ArrayList<>());
        response = partnerController.getAllPartners();
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}
