package fxibBackend.service.DataServices;

import fxibBackend.dto.InitDTOS.PricingDTO;
import fxibBackend.exception.DataValidationException;
import fxibBackend.repository.PricingEntityRepository;
import fxibBackend.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PricingService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final ModelMapper modelMapper;
    private final PricingEntityRepository pricingEntityRepository;
    private final ValidationUtil validationUtil;

    /**
     * Retrieves a list of PricingDTO objects representing pricing information.
     * Maps PricingEntity objects to PricingDTO using ModelMapper and validates the DTOs.
     *
     * @return List of PricingDTO objects.
     * @throws DataValidationException if any of the mapped DTOs is invalid.
     */
    public List<PricingDTO> getAllPricingData() {
        return pricingEntityRepository
                .findAll()
                .stream()
                .map(pricingEntity -> {
                    PricingDTO pricingDTO = modelMapper.map(pricingEntity, PricingDTO.class);
                    if (validationUtil.isValid(pricingDTO)) {
                        return pricingDTO;
                    }
                    throw new DataValidationException();
                }).collect(Collectors.toList());
    }


}
