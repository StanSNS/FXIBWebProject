package fxibBackend.controller;


import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradingAccountDTO;
import fxibBackend.dto.TradingAccountsDTOS.Footer.TradingAccountFooterDTO;
import fxibBackend.service.TradingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static fxibBackend.constants.MappingConstants.TRADING_ACCOUNTS_CONTROLLER_MAPPING;
import static fxibBackend.constants.URLAccessConst.FRONTEND_BASE_URL;

@CrossOrigin(FRONTEND_BASE_URL)
@RequiredArgsConstructor
@RestController
@RequestMapping
public class TradingAccountsController {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final TradingAccountService tradingAccountsService;

    /**
     * Retrieves a list of TradingAccountDTO objects containing trading account information for Accounts page.
     *
     * @return A ResponseEntity containing the list of TradingAccountDTO objects if available, or an HTTP 500 error response if empty.
     */
    @GetMapping(TRADING_ACCOUNTS_CONTROLLER_MAPPING)
    public ResponseEntity<List<TradingAccountDTO>> getAllTradingAccounts() {
        // Fetch all trading accounts data from the TradingAccountsService
        return new ResponseEntity<>(tradingAccountsService.getAllMyFxBookTradingAccounts(), HttpStatus.OK);
    }

    /**
     * Retrieves a list of TradingAccountFooterDTO objects containing footer trading account information for the Footer.
     *
     * @return A ResponseEntity containing the list of TradingAccountFooterDTO objects if available, or an HTTP 500 error response if empty.
     */
    @PutMapping(TRADING_ACCOUNTS_CONTROLLER_MAPPING)
    public ResponseEntity<List<TradingAccountFooterDTO>> getAllTradingAccountsFooter() {
        // Fetch footer trading accounts data from the TradingAccountsService
        return new ResponseEntity<>(tradingAccountsService.getFooterTradingAccounts(), HttpStatus.OK);
    }


}