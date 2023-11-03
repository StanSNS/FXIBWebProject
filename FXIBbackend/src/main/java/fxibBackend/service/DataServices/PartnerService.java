package fxibBackend.service.DataServices;

import fxibBackend.dto.InitDTOS.PartnerDTO;
import fxibBackend.exception.DataValidationException;
import fxibBackend.repository.PartnerEntityRepository;
import fxibBackend.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PartnerService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final ModelMapper modelMapper;
    private final PartnerEntityRepository partnerEntityRepository;
    private final ValidationUtil validationUtil;

    /**
     * Retrieves a list of PartnerDTO objects representing information about partners.
     * Maps PartnerEntity objects to PartnerDTO using ModelMapper and validates the DTOs.
     *
     * @return List of PartnerDTO objects.
     * @throws DataValidationException if any of the mapped DTOs is invalid.
     */
    public List<PartnerDTO> getAllPartnerData() {
        return partnerEntityRepository
                .findAll()
                .stream()
                .map(partnerEntity -> {
                    PartnerDTO partnerDTO = modelMapper.map(partnerEntity, PartnerDTO.class);
                    if (validationUtil.isValid(partnerDTO)) {
                        return partnerDTO;
                    }
                    throw new DataValidationException();
                }).collect(Collectors.toList());
    }


}
