package ServiceTest.DataServiceTest;

import fxibBackend.dto.InitDTOS.AboutDTO;
import fxibBackend.entity.DataEntity.AboutEntity;
import fxibBackend.exception.DataValidationException;
import fxibBackend.repository.AboutEntityRepository;
import fxibBackend.service.DataServices.AboutService;
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
public class AboutServiceTest {

    @InjectMocks
    private AboutService aboutService;
    @Mock
    private AboutEntityRepository aboutEntityRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void testGetAllAboutData() {
        AboutEntity aboutEntity1 = new AboutEntity();
        AboutEntity aboutEntity2 = new AboutEntity();
        List<AboutEntity> aboutEntities = List.of(aboutEntity1, aboutEntity2);

        when(aboutEntityRepository.findAll()).thenReturn(aboutEntities);

        AboutDTO aboutDTO1 = new AboutDTO();
        AboutDTO aboutDTO2 = new AboutDTO();

        when(modelMapper.map(aboutEntity1, AboutDTO.class)).thenReturn(aboutDTO1);
        when(modelMapper.map(aboutEntity2, AboutDTO.class)).thenReturn(aboutDTO2);

        when(validationUtil.isValid(aboutDTO1)).thenReturn(true);
        when(validationUtil.isValid(aboutDTO2)).thenReturn(true);

        List<AboutDTO> result = aboutService.getAllAboutData();

        assertEquals(2, result.size());
        assertEquals(aboutDTO1, result.get(0));
        assertEquals(aboutDTO2, result.get(1));
    }

    @Test
    void testGetAllAboutDataWithInvalidData() {
        AboutEntity aboutEntity1 = new AboutEntity();
        List<AboutEntity> aboutEntities = List.of(aboutEntity1);

        when(aboutEntityRepository.findAll()).thenReturn(aboutEntities);

        AboutDTO aboutDTO1 = new AboutDTO();

        when(modelMapper.map(aboutEntity1, AboutDTO.class)).thenReturn(aboutDTO1);

        when(validationUtil.isValid(aboutDTO1)).thenReturn(false);

        assertThrows(DataValidationException.class, () -> aboutService.getAllAboutData());
    }

}
