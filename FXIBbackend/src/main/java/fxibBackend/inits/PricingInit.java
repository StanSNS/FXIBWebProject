package fxibBackend.inits;

import fxibBackend.entity.DataEntity.PricingEntity;
import fxibBackend.repository.PricingEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static fxibBackend.constants.InitConst.*;

@Component
@RequiredArgsConstructor
public class PricingInit {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final PricingEntityRepository pricingEntityRepository;

    /**
     * Initializes pricing content if no records exist in the PricingEntityRepository.
     * Creates and saves default PricingEntity records with prices, durations, and link URLs.
     */
    @PostConstruct
    public void pricingContentInit() {
        // Check if there are no existing PricingEntity records
        if (pricingEntityRepository.count() == 0) {
            // Create and save default PricingEntity records with prices, durations, and link URLs
            PricingEntity firstPricingEntity = new PricingEntity();
            firstPricingEntity.setPrice(PRICING_PRICE_ONE);
            firstPricingEntity.setDuration(PRICING_DURATION_ONE);
            firstPricingEntity.setLinkURL(PRICING_LINK_URL_ONE);
            pricingEntityRepository.save(firstPricingEntity);

            // Repeat the same process for other pricing entities (e.g., secondPricingEntity, thirdPricingEntity, etc.)
            PricingEntity secondPricingEntity = new PricingEntity();
            secondPricingEntity.setPrice(PRICING_PRICE_TWO);
            secondPricingEntity.setDuration(PRICING_DURATION_TWO);
            secondPricingEntity.setLinkURL(PRICING_LINK_URL_TWO);
            pricingEntityRepository.save(secondPricingEntity);


            PricingEntity thirdPricingEntity = new PricingEntity();
            thirdPricingEntity.setPrice(PRICING_PRICE_THREE);
            thirdPricingEntity.setDuration(PRICING_DURATION_THREE);
            thirdPricingEntity.setLinkURL(PRICING_LINK_URL_THREE);
            pricingEntityRepository.save(thirdPricingEntity);
        }
    }

}