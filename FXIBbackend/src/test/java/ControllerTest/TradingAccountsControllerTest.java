package ControllerTest;

import fxibBackend.controller.TradingAccountsController;
import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradingAccountDTO;
import fxibBackend.dto.TradingAccountsDTOS.Footer.TradingAccountFooterDTO;
import fxibBackend.service.TradingAccountService;
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

class TradingAccountsControllerTest {

    @Mock
    private TradingAccountService tradingAccountService;
    private TradingAccountsController tradingAccountsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        tradingAccountsController = new TradingAccountsController(tradingAccountService);
    }

    @Test
    void testGetAllTradingAccounts() {
        List<TradingAccountDTO> mockTradingAccounts = new ArrayList<>();
        mockTradingAccounts.add(new TradingAccountDTO());
        mockTradingAccounts.add(new TradingAccountDTO());

        when(tradingAccountService.getAllMyFxBookTradingAccounts()).thenReturn(mockTradingAccounts);

        ResponseEntity<List<TradingAccountDTO>> response = tradingAccountsController.getAllTradingAccounts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTradingAccounts, response.getBody());
    }

    @Test
    void testGetAllTradingAccountsFooter() {
        List<TradingAccountFooterDTO> mockTradingAccountsFooter = new ArrayList<>();
        mockTradingAccountsFooter.add(new TradingAccountFooterDTO());
        mockTradingAccountsFooter.add(new TradingAccountFooterDTO());

        when(tradingAccountService.getFooterTradingAccounts()).thenReturn(mockTradingAccountsFooter);

        ResponseEntity<List<TradingAccountFooterDTO>> response = tradingAccountsController.getAllTradingAccountsFooter();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTradingAccountsFooter, response.getBody());
    }

}
