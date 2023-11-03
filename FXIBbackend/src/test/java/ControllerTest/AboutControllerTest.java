package ControllerTest;

import fxibBackend.controller.DataController.AboutController;
import fxibBackend.dto.InitDTOS.AboutDTO;
import fxibBackend.service.DataServices.AboutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AboutControllerTest {

    private MockMvc mockMvc;
    @Mock
    private AboutService aboutService;
    @InjectMocks
    private AboutController aboutController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(aboutController).build();
    }

    @Test
    public void testGetAllAbouts() throws Exception {
        AboutDTO aboutDTO1 = new AboutDTO();
        aboutDTO1.setTitle("About 1");

        AboutDTO aboutDTO2 = new AboutDTO();
        aboutDTO2.setTitle("About 2");

        List<AboutDTO> aboutList = Arrays.asList(aboutDTO1, aboutDTO2);

        when(aboutService.getAllAboutData()).thenReturn(aboutList);

        mockMvc.perform(get("/about")).andExpect(status().isOk());
    }

    @Test
    public void testGetAllAboutsEmpty() throws Exception {
        when(aboutService.getAllAboutData()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/about"))
                .andExpect(status().isInternalServerError());
    }

}
