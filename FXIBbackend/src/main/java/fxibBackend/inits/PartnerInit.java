package fxibBackend.inits;


import fxibBackend.entity.DataEntity.PartnerEntity;
import fxibBackend.repository.PartnerEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static fxibBackend.constants.InitConst.*;

@Component
@RequiredArgsConstructor
public class PartnerInit {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final PartnerEntityRepository partnerEntityRepository;

    /**
     * Initializes partner content if no records exist in the PartnerEntityRepository.
     * Creates and saves default PartnerEntity records with titles, lines, and link URLs.
     */
    @PostConstruct
    public void partnerContentInit() {
        // Check if there are no existing PartnerEntity records
        if (partnerEntityRepository.count() == 0) {
            // Create and save default PartnerEntity records with titles, lines, and link URLs
            PartnerEntity admiralPartnerEntity = new PartnerEntity(
                    ADMIRAL_MARKETS_TITLE
                    , ADMIRAL_MARKETS_FIRST_LINE
                    , ADMIRAL_MARKETS_SECOND_LINE
                    , ADMIRAL_MARKETS_THIRD_LINE
                    , ADMIRAL_MARKETS_FOURTH_LINE
                    , ADMIRAL_MARKETS_FIFTH_LINE
                    , ADMIRAL_MARKETS_SIXTH_LINE
                    , ADMIRAL_MARKETS_SEVENTH_LINE_BLANK
                    , ADMIRAL_MARKETS_LINK_URL);
            partnerEntityRepository.save(admiralPartnerEntity);
            // Repeat the same process for other partner entities (e.g., icMarketsPartnerEntity, bdSwissPartnerEntity, etc.)

            PartnerEntity icMarketsPartnerEntity = new PartnerEntity(
                    ICMARKETS_TITLE
                    , ICMARKETS_FIRST_LINE
                    , ICMARKETS_SECOND_LINE
                    , ICMARKETS_THIRD_LINE
                    , ICMARKETS_FOURTH_LINE
                    , ICMARKETS_FIFTH_LINE
                    , ICMARKETS_SIXTH_LINE
                    , ICMARKETS_SEVENTH_LINE_BLANK
                    , ICMARKETS_LINK_URL);
            partnerEntityRepository.save(icMarketsPartnerEntity);

            PartnerEntity bdSwissPartnerEntity = new PartnerEntity(
                    BDSWISS_TITLE
                    , BDSWISS_FIRST_LINE
                    , BDSWISS_SECOND_LINE
                    , BDSWISS_THIRD_LINE
                    , BDSWISS_FOURTH_LINE
                    , BDSWISS_FIFTH_LINE
                    , BDSWISS_SIXTH_LINE
                    , BDSWISS_SEVENTH_LINE_BLANK
                    , BDSWISS_LINK_URL);
            partnerEntityRepository.save(bdSwissPartnerEntity);

            PartnerEntity trueForexFundsPartnerEntity = new PartnerEntity(
                    TRUE_FOREX_FUNDS_TITLE
                    , TRUE_FOREX_FUNDS_FIRST_LINE
                    , TRUE_FOREX_FUNDS_SECOND_LINE
                    , TRUE_FOREX_FUNDS_THIRD_LINE
                    , TRUE_FOREX_FUNDS_FOURTH_LINE
                    , TRUE_FOREX_FUNDS_FIFTH_LINE
                    , TRUE_FOREX_FUNDS_SIXTH_LINE
                    , TRUE_FOREX_FUNDS_SEVENTH_LINE
                    , TRUE_FOREX_FUNDS_LINK_URL);
            partnerEntityRepository.save(trueForexFundsPartnerEntity);

            PartnerEntity myForexFundsPartnerEntity = new PartnerEntity(
                    MY_FOREX_FUNDS_TITLE
                    , MY_FOREX_FUNDS_FIRST_LINE
                    , MY_FOREX_FUNDS_SECOND_LINE
                    , MY_FOREX_FUNDS_THIRD_LINE
                    , MY_FOREX_FUNDS_FOURTH_LINE
                    , MY_FOREX_FUNDS_FIFTH_LINE
                    , MY_FOREX_FUNDS_SIXTH_LINE
                    , MY_FOREX_FUNDS_SEVENTH_LINE
                    , MY_FOREX_FUNDS_LINK_URL);
            partnerEntityRepository.save(myForexFundsPartnerEntity);

            PartnerEntity ftmoPartnerEntity = new PartnerEntity(
                    FTMO_TITLE
                    , FTMO_FIRST_LINE
                    , FTMO_SECOND_LINE
                    , FTMO_THIRD_LINE
                    , FTMO_FOURTH_LINE
                    , FTMO_FIFTH_LINE
                    , FTMO_SIXTH_LINE
                    , FTMO_SEVENTH_LINE
                    , FTMO_LINK_URL);
            partnerEntityRepository.save(ftmoPartnerEntity);
        }
    }

}
