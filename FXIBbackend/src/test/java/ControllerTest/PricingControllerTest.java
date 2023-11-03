package ControllerTest;

import fxibBackend.controller.DataController.PricingController;
import fxibBackend.dto.InitDTOS.PricingDTO;
import fxibBackend.service.DataServices.PricingService;
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

class PricingControllerTest {

    @Mock
    private PricingService pricingService;

    private PricingController pricingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pricingController = new PricingController(pricingService);
    }

    @Test
    void testGetAllPricingsSuccess() {
        List<PricingDTO> mockPricings = new ArrayList<>();
        mockPricings.add(new PricingDTO());
        mockPricings.add(new PricingDTO());

        when(pricingService.getAllPricingData()).thenReturn(mockPricings);

        ResponseEntity<List<PricingDTO>> response = pricingController.getAllPricings();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPricings, response.getBody());
    }

    @Test
    void testGetAllPricingsEmpty() {
        when(pricingService.getAllPricingData()).thenReturn(new ArrayList<>());

        ResponseEntity<List<PricingDTO>> response = pricingController.getAllPricings();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
