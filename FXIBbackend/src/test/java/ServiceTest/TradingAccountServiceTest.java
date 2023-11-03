package ServiceTest;

import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradingAccountDTO;
import fxibBackend.dto.TradingAccountsDTOS.Footer.TradingAccountFooterDTO;

import fxibBackend.entity.DataEntity.TradingAccount.TradingAccountEntity;
import fxibBackend.repository.TradingAccountEntityRepository;
import fxibBackend.service.TradingAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TradingAccountServiceTest {

    @InjectMocks
    private TradingAccountService tradingAccountService;
    @Mock
    private TradingAccountEntityRepository tradingAccountEntityRepository;
    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllMyFxBookTradingAccounts() {
        List<TradingAccountEntity> accountEntities = new ArrayList<>();
        accountEntities.add(new TradingAccountEntity());
        accountEntities.add(new TradingAccountEntity());

        List<TradingAccountDTO> expectedDTOs = new ArrayList<>();
        expectedDTOs.add(new TradingAccountDTO());
        expectedDTOs.add(new TradingAccountDTO());

        Mockito.when(tradingAccountEntityRepository.findAll()).thenReturn(accountEntities);
        Mockito.when(modelMapper.map(accountEntities.get(0), TradingAccountDTO.class)).thenReturn(expectedDTOs.get(0));
        Mockito.when(modelMapper.map(accountEntities.get(1), TradingAccountDTO.class)).thenReturn(expectedDTOs.get(1));

        List<TradingAccountDTO> actualDTOs = tradingAccountService.getAllMyFxBookTradingAccounts();

        assertEquals(expectedDTOs.size(), actualDTOs.size());
        for (int i = 0; i < expectedDTOs.size(); i++) {
            assertEquals(expectedDTOs.get(i), actualDTOs.get(i));
        }
    }

    @Test
    void testGetFooterTradingAccounts() {
        List<TradingAccountEntity> accountEntities = new ArrayList<>();
        TradingAccountEntity account1 = new TradingAccountEntity();
        account1.setProfit(5000.0);
        account1.setDeposits(10000.0);

        TradingAccountEntity account2 = new TradingAccountEntity();
        account2.setProfit(7500.0);
        account2.setDeposits(15000.0);

        accountEntities.add(account1);
        accountEntities.add(account2);

        List<TradingAccountFooterDTO> expectedDTOs = new ArrayList<>();
        TradingAccountFooterDTO dto1 = new TradingAccountFooterDTO();
        dto1.setGain(50.0);
        TradingAccountFooterDTO dto2 = new TradingAccountFooterDTO();
        dto2.setGain(50.0);

        expectedDTOs.add(dto1);
        expectedDTOs.add(dto2);

        Mockito.when(tradingAccountEntityRepository.findAll()).thenReturn(accountEntities);
        Mockito.when(modelMapper.map(accountEntities.get(0), TradingAccountFooterDTO.class)).thenReturn(dto1);
        Mockito.when(modelMapper.map(accountEntities.get(1), TradingAccountFooterDTO.class)).thenReturn(dto2);

        List<TradingAccountFooterDTO> actualDTOs = tradingAccountService.getFooterTradingAccounts();

        assertEquals(expectedDTOs.size(), actualDTOs.size());
        for (int i = 0; i < expectedDTOs.size(); i++) {
            assertEquals(expectedDTOs.get(i), actualDTOs.get(i));
        }
    }

}
