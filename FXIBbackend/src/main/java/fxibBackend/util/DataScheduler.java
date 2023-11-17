package fxibBackend.util;

import fxibBackend.inits.*;
import fxibBackend.inits.TradingAccountsInit;
import fxibBackend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DataScheduler {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final AboutEntityRepository aboutEntityRepository;
    private final AboutInit aboutInit;
    private final PartnerEntityRepository partnerEntityRepository;
    private final PartnerInit partnerInit;
    private final PricingEntityRepository pricingEntityRepository;
    private final PricingInit pricingInit;
    private final RoleEntityRepository roleEntityRepository;
    private final RoleInit roleInit;
    private final TopicEntityRepository topicEntityRepository;
    private final TopicInit topicInit;
    private final TradeEntityRepository tradeEntityRepository;
    private final TradingAccountEntityRepository tradingAccountEntityRepository;
    private final TradingAccountsInit tradingAccountsInit;


    /**
     * Scheduled method to run periodically and checks if certain data is missing in the database.
     * If any data is missing, it initializes and inserts the missing data into the database.
     *
     * @throws IOException If there is an issue with reading or writing data.
     */
    @Scheduled(fixedDelay = 10800000) //3 Hours
    public void dataCheckScheduler() throws IOException {
        String outputMessage = "This data has been inserted into the database: ";
        boolean isSomethingMissing = false;
        if (aboutEntityRepository.count() == 0) {
            aboutInit.aboutContentInit();
            outputMessage += "Abouts ";
            isSomethingMissing = true;
        }
        if (partnerEntityRepository.count() == 0) {
            partnerInit.partnerContentInit();
            outputMessage += "Partners ";
            isSomethingMissing = true;
        }
        if (pricingEntityRepository.count() == 0) {
            pricingInit.pricingContentInit();
            outputMessage += "Pricings ";
            isSomethingMissing = true;
        }
        if (roleEntityRepository.count() == 0) {
            roleInit.rolesInit();
            outputMessage += "Roles ";
            isSomethingMissing = true;
        }
        if (topicEntityRepository.count() == 0) {
            topicInit.topicInit();
            outputMessage += "Topics ";
            isSomethingMissing = true;
        }
        if (tradeEntityRepository.count() == 0 || tradingAccountEntityRepository.count() == 0) {
            tradingAccountsInit.initTradingAccounts();
            outputMessage += "Trading Accounts ";
            isSomethingMissing = true;
        }

        if (!isSomethingMissing) {
            outputMessage = "Nothing is missing. Everything is fine ! ";
        }
        System.out.println(outputMessage);
    }

}
