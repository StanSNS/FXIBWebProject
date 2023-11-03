package InitTest;

import fxibBackend.inits.TradingAccountJSONObjects.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSONObjectTests {

    @Test
    public void myFxBookAccountJSONTest() {
        MyFxBookAccountJSON account = new MyFxBookAccountJSON();
        account.setId(1L);
        account.setAccountId(1001L);
        account.setDeposits(1000.50);
        account.setProfit(500.25);
        account.setBalance(1500.75);
        account.setEquity(1400.60);
        account.setLastUpdateDate("2023-10-30");
        account.setCreationDate("2023-01-01");
        account.setFirstTradeDate("2023-02-15");
        account.setCurrency("USD");
        MyFxBookServerJSON server = new MyFxBookServerJSON();
        server.setName("ServerName");
        account.setServer(server);

        TradeHistoryJSON history = new TradeHistoryJSON();
        ArrayList<TradeJSON> tradeJSONS = new ArrayList<>();
        history.setHistory(tradeJSONS);
        account.setTradingHistory(history);

        assertEquals(1L, account.getId());
        assertEquals(1001L, account.getAccountId());
        assertEquals(1000.50, account.getDeposits());
        assertEquals(500.25, account.getProfit());
        assertEquals(1500.75, account.getBalance());
        assertEquals(1400.60, account.getEquity());
        assertEquals("2023-10-30", account.getLastUpdateDate());
        assertEquals("2023-01-01", account.getCreationDate());
        assertEquals("2023-02-15", account.getFirstTradeDate());
        assertEquals("USD", account.getCurrency());
        assertEquals("ServerName", account.getServer().getName());
        assertEquals(0, account.getTradingHistory().getHistory().size());
    }

    @Test
    public void testMyFxBookServerJSON() {
        MyFxBookServerJSON server = new MyFxBookServerJSON();
        server.setName("Server1");

        assertEquals("Server1", server.getName());
    }

    @Test
    public void testTradeHistoryJSON() {
        TradeHistoryJSON history = new TradeHistoryJSON();
        history.setHistory(new ArrayList<>());

        assertEquals(0, history.getHistory().size());
    }

    @Test
    public void testTradeJSON() {
        TradeJSON trade = new TradeJSON();
        trade.setOpenTime("Open");
        trade.setCloseTime("Close");
        trade.setSymbol("Symbol");
        trade.setAction("Action");
        trade.setPips(1.12);
        trade.setProfit(1.10);
        trade.setCommission(1.9);

        assertEquals("Open", trade.getOpenTime());
        assertEquals("Close", trade.getCloseTime());
        assertEquals("Symbol", trade.getSymbol());
        assertEquals("Action", trade.getAction());
        assertEquals(1.12, trade.getPips());
        assertEquals(1.10, trade.getProfit());
        assertEquals(1.9, trade.getCommission());
    }

    @Test
    public void testTradingAccountResponseJSON() {
        TradingAccountResponseJSON tradingAccountResponseJSON = new TradingAccountResponseJSON();
        ArrayList<MyFxBookAccountJSON> myFxBookAccountJSONS = new ArrayList<>();
        tradingAccountResponseJSON.setAccounts(myFxBookAccountJSONS);

        assertEquals(0, tradingAccountResponseJSON.getAccounts().size());
    }

}
