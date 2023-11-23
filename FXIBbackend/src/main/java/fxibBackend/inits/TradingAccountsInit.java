package fxibBackend.inits;


import com.google.gson.Gson;
import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradeDTO;
import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradingAccountDTO;
import fxibBackend.entity.DataEntity.TradingAccount.TradeEntity;
import fxibBackend.entity.DataEntity.TradingAccount.TradingAccountEntity;
import fxibBackend.exception.DataValidationException;
import fxibBackend.inits.TradingAccountJSONObjects.MyFxBookAccountJSON;
import fxibBackend.inits.TradingAccountJSONObjects.TradeHistoryJSON;
import fxibBackend.inits.TradingAccountJSONObjects.TradeJSON;
import fxibBackend.inits.TradingAccountJSONObjects.TradingAccountResponseJSON;
import fxibBackend.repository.TradeEntityRepository;
import fxibBackend.repository.TradingAccountEntityRepository;
import fxibBackend.util.CustomDateFormatter;
import fxibBackend.util.ValidationUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static fxibBackend.constants.ConfigConst.*;

@Component
@RequiredArgsConstructor
public class TradingAccountsInit {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final TradeEntityRepository tradeEntityRepository;
    private final TradingAccountEntityRepository tradingAccountEntityRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CustomDateFormatter customDateFormatter;

    private final String sessionID = MYFXBOOK_SESSION_ID;

    /**
     * Initializes trading accounts and their associated trades if no records exist in the repositories.
     * Fetches trading account data from MyFxBook and saves it as TradingAccountEntity and TradeEntity records.
     *
     * @throws IOException if there's an issue with network communication.
     */
    @PostConstruct
    public void initTradingAccounts() throws IOException {
        // Check if there are no existing records in the repositories
        if (tradeEntityRepository.count() == 0 || tradingAccountEntityRepository.count() == 0) {
            // Fetch all MyFxBook trading accounts and their associated trades
            TradingAccountResponseJSON allMyFxBookTradingAccounts = getAllMyFxBookTradingAccounts();

            for (MyFxBookAccountJSON currentAccount : allMyFxBookTradingAccounts.getAccounts()) {
                // Map MyFxBook account data to a TradingAccountDTO
                TradingAccountDTO tradingAccountDTO = modelMapper.map(currentAccount, TradingAccountDTO.class);
                tradingAccountDTO.setResponseIdentity(currentAccount.getId());
                tradingAccountDTO.setAccountIdentity(currentAccount.getAccountId());
                tradingAccountDTO.setServer(currentAccount.getServer().getName());

                List<TradeEntity> tradeEntityList = new ArrayList<>();

                for (TradeJSON currentTrade : currentAccount.getTradingHistory().getHistory()) {
                    // Map trade data to a TradeDTO and create a TradeEntity
                    TradeDTO tradeDTO = modelMapper.map(currentTrade, TradeDTO.class);
                    if (!validationUtil.isValid(tradeDTO)) {
                        throw new DataValidationException();
                    }
                    TradeEntity tradeEntity = modelMapper.map(tradeDTO, TradeEntity.class);
                    tradeEntity.setOpenTime(customDateFormatter.formatCustomDateTime(tradeDTO.getOpenTime()));
                    tradeEntity.setCloseTime(customDateFormatter.formatCustomDateTime(tradeDTO.getCloseTime()));

                    tradeEntityRepository.save(tradeEntity);
                    tradeEntityList.add(tradeEntity);
                }

                // Map the trading account data to a TradingAccountEntity and save it
                TradingAccountEntity tradingAccountEntity = modelMapper.map(tradingAccountDTO, TradingAccountEntity.class);
                tradingAccountEntity.setCreationDate(customDateFormatter.formatCustomDateTime(tradingAccountDTO.getCreationDate()));
                tradingAccountEntity.setFirstTradeDate(customDateFormatter.formatCustomDateTime(tradingAccountDTO.getFirstTradeDate()));
                tradingAccountEntity.setLastUpdateDate(customDateFormatter.formatCustomDateTime(tradingAccountDTO.getLastUpdateDate()));
                tradingAccountEntity.setTrades(tradeEntityList);

                tradingAccountEntityRepository.save(tradingAccountEntity);
            }
        }
    }


    /**
     * Retrieves all trading accounts from MyFxBook.
     *
     * @return A TradingAccountResponseJSON containing trading account information.
     * @throws IOException if there's an issue with network communication.
     */
    public TradingAccountResponseJSON getAllMyFxBookTradingAccounts() throws IOException {
        // Construct the request URL and fetch data from MyFxBook
        String requestURL = MYFXBOOK_GET_ALL_ACCOUNTS_URL + sessionID;
        TradingAccountResponseJSON tradingAccountResponseJSON = gson.fromJson(parseRawDataToJSON(requestURL), TradingAccountResponseJSON.class);

        // Fetch trading history for each trading account
        for (MyFxBookAccountJSON currentTradingAccount : tradingAccountResponseJSON.getAccounts()) {
            currentTradingAccount.setTradingHistory(getAllTradesForAccountWithID(currentTradingAccount.getId()));
        }

        return tradingAccountResponseJSON;
    }


    /**
     * Retrieves trade history for a specific trading account using its account ID.
     *
     * @param accountID The unique account ID of the trading account.
     * @return A TradeHistoryJSON containing trade history information.
     * @throws IOException if there's an issue with network communication.
     */
    public TradeHistoryJSON getAllTradesForAccountWithID(long accountID) throws IOException {
        // Construct the request URL and fetch trade history data from MyFxBook
        String getHistoryURL = String.format(MYFXBOOK_GET_ALL_ACCOUNT_TRADES_URL, sessionID, accountID);
        return gson.fromJson(parseRawDataToJSON(getHistoryURL), TradeHistoryJSON.class);
    }

    /**
     * Parses raw JSON data from a URL and returns it as a JSON string.
     *
     * @param urlData The URL containing the raw JSON data to be fetched and parsed.
     * @return A JSON string containing the fetched data.
     * @throws IOException if there's an issue with network communication.
     */
    public String parseRawDataToJSON(String urlData) throws IOException {
        // Fetch raw JSON data from the provided URL and parse it into a JSON string
        URL url = new URL(urlData);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        reader.close();
        return jsonBuilder.toString();
    }


}
