package InitTest;

import com.google.gson.Gson;
import fxibBackend.inits.TradingAccountsInit;
import fxibBackend.inits.TradingAccountJSONObjects.*;
import fxibBackend.repository.TradeEntityRepository;
import fxibBackend.repository.TradingAccountEntityRepository;
import fxibBackend.util.CustomDateFormatter;
import fxibBackend.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TradingAccountsInitTest {

    @Mock
    private TradeEntityRepository tradeEntityRepository;
    @Mock
    private TradingAccountEntityRepository tradingAccountEntityRepository;
    @Mock
    private Gson gson;
    @Mock
    private CustomDateFormatter customDateFormatter;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private ValidationUtil validationUtil;
    private TradingAccountsInit tradingAccountsInit;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        tradingAccountsInit = new TradingAccountsInit(
                tradeEntityRepository,
                tradingAccountEntityRepository,
                gson,
                modelMapper,
                validationUtil,
                customDateFormatter
        );
    }

    @Test
    void testGetAllMyFxBookTradingAccounts() throws IOException {
        when(gson.fromJson(anyString(), eq(TradingAccountResponseJSON.class))).thenReturn(createMockTradingAccountResponseJSON());

        TradingAccountResponseJSON response = tradingAccountsInit.getAllMyFxBookTradingAccounts();

        assertNotNull(response);
        assertEquals(2, response.getAccounts().size());
    }

    @Test
    void testGetAllTradesForAccountWithID() throws IOException {
        when(gson.fromJson(anyString(), eq(TradeHistoryJSON.class))).thenReturn(createMockTradeHistoryJSON());

        TradeHistoryJSON tradeHistoryJSON = tradingAccountsInit.getAllTradesForAccountWithID(123L);

        assertNotNull(tradeHistoryJSON);
        assertEquals(3, tradeHistoryJSON.getHistory().size());
    }

    private TradingAccountResponseJSON createMockTradingAccountResponseJSON() {
        TradingAccountResponseJSON response = new TradingAccountResponseJSON();
        List<MyFxBookAccountJSON> accounts = new ArrayList<>();
        MyFxBookAccountJSON account1 = new MyFxBookAccountJSON();
        MyFxBookAccountJSON account2 = new MyFxBookAccountJSON();
        accounts.add(account1);
        accounts.add(account2);
        response.setAccounts(accounts);
        return response;
    }

    private TradeHistoryJSON createMockTradeHistoryJSON() {
        TradeHistoryJSON tradeHistoryJSON = new TradeHistoryJSON();
        List<TradeJSON> trades = new ArrayList<>();
        TradeJSON trade1 = new TradeJSON();
        TradeJSON trade2 = new TradeJSON();
        TradeJSON trade3 = new TradeJSON();
        trades.add(trade1);
        trades.add(trade2);
        trades.add(trade3);
        tradeHistoryJSON.setHistory(trades);
        return tradeHistoryJSON;
    }

}
