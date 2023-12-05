package UtilTest;

import fxibBackend.inits.*;
import fxibBackend.repository.*;
import fxibBackend.util.DataScheduler;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class DataSchedulerTest {
    @Mock
    private AboutEntityRepository aboutEntityRepository;
    @Mock
    private AboutInit aboutInit;
    @Mock
    private PartnerEntityRepository partnerEntityRepository;
    @Mock
    private PartnerInit partnerInit;
    @Mock
    private PricingEntityRepository pricingEntityRepository;
    @Mock
    private PricingInit pricingInit;
    @Mock
    private RoleEntityRepository roleEntityRepository;
    @Mock
    private RoleInit roleInit;
    @Mock
    private TopicEntityRepository topicEntityRepository;
    @Mock
    private TopicInit topicInit;
    @Mock
    private TradeEntityRepository tradeEntityRepository;
    @Mock
    private TradingAccountEntityRepository tradingAccountEntityRepository;
    @Mock
    private TradingAccountsInit tradingAccountsInit;
    @InjectMocks
    private DataScheduler dataScheduler;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testScheduler() throws IOException {
        Mockito.when(aboutEntityRepository.count()).thenReturn(0L);
        Mockito.when(partnerEntityRepository.count()).thenReturn(0L);
        Mockito.when(pricingEntityRepository.count()).thenReturn(0L);
        Mockito.when(roleEntityRepository.count()).thenReturn(0L);
        Mockito.when(topicEntityRepository.count()).thenReturn(0L);
        Mockito.when(tradeEntityRepository.count()).thenReturn(0L);
        Mockito.when(tradingAccountEntityRepository.count()).thenReturn(0L);

        Mockito.doNothing().when(aboutInit).aboutContentInit();
        Mockito.doNothing().when(partnerInit).partnerContentInit();
        Mockito.doNothing().when(pricingInit).pricingContentInit();
        Mockito.doNothing().when(roleInit).rolesInit();
        Mockito.doNothing().when(topicInit).topicInit();
        Mockito.doNothing().when(tradingAccountsInit).initTradingAccounts();

        dataScheduler.dataCheckScheduler();

        Mockito.verify(aboutInit).aboutContentInit();
        Mockito.verify(partnerInit).partnerContentInit();
        Mockito.verify(pricingInit).pricingContentInit();
        Mockito.verify(roleInit).rolesInit();
        Mockito.verify(topicInit).topicInit();
        Mockito.verify(tradingAccountsInit).initTradingAccounts();
    }

    @Test
    public void testSchedulerWhenNothingIsMissing() throws IOException {
        Mockito.when(aboutEntityRepository.count()).thenReturn(1L);
        Mockito.when(partnerEntityRepository.count()).thenReturn(1L);
        Mockito.when(pricingEntityRepository.count()).thenReturn(1L);
        Mockito.when(roleEntityRepository.count()).thenReturn(1L);
        Mockito.when(topicEntityRepository.count()).thenReturn(1L);
        Mockito.when(tradeEntityRepository.count()).thenReturn(1L);
        Mockito.when(tradingAccountEntityRepository.count()).thenReturn(1L);

        dataScheduler.dataCheckScheduler();

        String actualOutput = outputStream.toString();
        String expectedMessage = "Nothing is missing. Everything is fine ! ";
        assert actualOutput.contains(expectedMessage);
    }

}
