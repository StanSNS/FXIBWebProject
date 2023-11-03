package ServiceTest.DataServiceTest;

import fxibBackend.dto.InitDTOS.PricingDTO;
import fxibBackend.entity.DataEntity.PricingEntity;
import fxibBackend.exception.DataValidationException;
import fxibBackend.repository.PricingEntityRepository;
import fxibBackend.service.DataServices.PricingService;
import fxibBackend.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PricingServiceTest {

    @InjectMocks
    private PricingService pricingService;

    @Mock
    private PricingEntityRepository pricingEntityRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void testGetAllPricingData() {
        PricingEntity pricingEntity1 = new PricingEntity();
        PricingEntity pricingEntity2 = new PricingEntity();
        List<PricingEntity> pricingEntities = List.of(pricingEntity1, pricingEntity2);

        when(pricingEntityRepository.findAll()).thenReturn(pricingEntities);

        PricingDTO pricingDTO1 = new PricingDTO();
        PricingDTO pricingDTO2 = new PricingDTO();

        when(modelMapper.map(pricingEntity1, PricingDTO.class)).thenReturn(pricingDTO1);
        when(modelMapper.map(pricingEntity2, PricingDTO.class)).thenReturn(pricingDTO2);

        when(validationUtil.isValid(pricingDTO1)).thenReturn(true);
        when(validationUtil.isValid(pricingDTO2)).thenReturn(true);

        List<PricingDTO> result = pricingService.getAllPricingData();

        assertEquals(2, result.size());
        assertEquals(pricingDTO1, result.get(0));
        assertEquals(pricingDTO2, result.get(1));
    }

    @Test
    void testGetAllPricingDataWithInvalidData() {
        PricingEntity pricingEntity1 = new PricingEntity();
        List<PricingEntity> pricingEntities = List.of(pricingEntity1);

        when(pricingEntityRepository.findAll()).thenReturn(pricingEntities);

        PricingDTO pricingDTO1 = new PricingDTO();

        when(modelMapper.map(pricingEntity1, PricingDTO.class)).thenReturn(pricingDTO1);

        when(validationUtil.isValid(pricingDTO1)).thenReturn(false);

        assertThrows(DataValidationException.class, () -> pricingService.getAllPricingData());
    }

}