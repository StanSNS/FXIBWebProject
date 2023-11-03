package fxibBackend.service;

import fxibBackend.dto.TradingAccountsDTOS.AccountPage.TradingAccountDTO;
import fxibBackend.dto.TradingAccountsDTOS.Footer.TradingAccountFooterDTO;
import fxibBackend.repository.TradingAccountEntityRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TradingAccountService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final TradingAccountEntityRepository tradingAccountEntityRepository;
    private final ModelMapper modelMapper;

    /**
     * Retrieves a list of trading accounts from MyFxBook.
     *
     * @return A list of trading account DTOs.
     */
    public List<TradingAccountDTO> getAllMyFxBookTradingAccounts() {
        return tradingAccountEntityRepository
                .findAll()
                .stream()
                .map(tradingAccountEntity -> modelMapper
                        .map(tradingAccountEntity, TradingAccountDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of trading accounts with additional gain information.
     *
     * @return A list of trading account footer DTOs with gain percentage.
     */
    public List<TradingAccountFooterDTO> getFooterTradingAccounts() {
        return tradingAccountEntityRepository.findAll().stream().map(tradingAccountEntity -> {
            TradingAccountFooterDTO tradingAccountFooterDTO = modelMapper.map(tradingAccountEntity, TradingAccountFooterDTO.class);
            double gain = tradingAccountEntity.getProfit() / tradingAccountEntity.getDeposits() * 100;
            DecimalFormat df = new DecimalFormat("#.##");
            double roundedNumber = Double.parseDouble(df.format(gain));
            tradingAccountFooterDTO.setGain(roundedNumber);
            return tradingAccountFooterDTO;
        }).collect(Collectors.toList());
    }


}
