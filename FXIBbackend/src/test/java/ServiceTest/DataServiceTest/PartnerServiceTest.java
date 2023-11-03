package ServiceTest.DataServiceTest;

import fxibBackend.dto.InitDTOS.PartnerDTO;
import fxibBackend.entity.DataEntity.PartnerEntity;
import fxibBackend.exception.DataValidationException;
import fxibBackend.repository.PartnerEntityRepository;
import fxibBackend.service.DataServices.PartnerService;
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
public class PartnerServiceTest {
    @InjectMocks
    private PartnerService partnerService;

    @Mock
    private PartnerEntityRepository partnerEntityRepository;

    @Mock
    private ValidationUtil validationUtil;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void testGetAllPartnerData() {
        PartnerEntity partnerEntity1 = new PartnerEntity();
        PartnerEntity partnerEntity2 = new PartnerEntity();
        List<PartnerEntity> partnerEntities = List.of(partnerEntity1, partnerEntity2);

        when(partnerEntityRepository.findAll()).thenReturn(partnerEntities);

        PartnerDTO partnerDTO1 = new PartnerDTO();
        PartnerDTO partnerDTO2 = new PartnerDTO();

        when(modelMapper.map(partnerEntity1, PartnerDTO.class)).thenReturn(partnerDTO1);
        when(modelMapper.map(partnerEntity2, PartnerDTO.class)).thenReturn(partnerDTO2);

        when(validationUtil.isValid(partnerDTO1)).thenReturn(true);
        when(validationUtil.isValid(partnerDTO2)).thenReturn(true);

        List<PartnerDTO> result = partnerService.getAllPartnerData();

        assertEquals(2, result.size());
        assertEquals(partnerDTO1, result.get(0));
        assertEquals(partnerDTO2, result.get(1));
    }

    @Test
    void testGetAllPartnerDataWithInvalidData() {
        PartnerEntity partnerEntity1 = new PartnerEntity();
        List<PartnerEntity> partnerEntities = List.of(partnerEntity1);

        when(partnerEntityRepository.findAll()).thenReturn(partnerEntities);

        PartnerDTO partnerDTO1 = new PartnerDTO();

        when(modelMapper.map(partnerEntity1, PartnerDTO.class)).thenReturn(partnerDTO1);

        when(validationUtil.isValid(partnerDTO1)).thenReturn(false);

        assertThrows(DataValidationException.class, () -> partnerService.getAllPartnerData());
    }

}
