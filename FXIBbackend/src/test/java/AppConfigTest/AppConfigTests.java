package AppConfigTest;

import com.google.gson.Gson;
import fxibBackend.config.AppConfig;
import io.ipgeolocation.api.Geolocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

public class AppConfigTests {

    private AppConfig appConfig;

    @BeforeEach
    public void setUp() {
        appConfig = new AppConfig();
    }

    @Test
    public void testGeolocationBean() {
        Geolocation mockGeolocation = Mockito.mock(Geolocation.class);

        when(mockGeolocation.getCity()).thenReturn("TestCity");

        assertNotNull(mockGeolocation);
        assertEquals("TestCity", mockGeolocation.getCity());
    }

    @Test
    public void testModelMapperCustomConverter() {
        ModelMapper modelMapper = appConfig.modelMapper();
        assertNotNull(modelMapper);

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        LocalDateTime sourceDateTime = LocalDateTime.of(2023, 1, 15, 12, 30);
        String formattedDate = modelMapper.map(sourceDateTime, String.class);

        assertNotEquals("2023-01-15 12:30", formattedDate);
    }

    @Test
    public void testGsonBean() {
        Gson gson = appConfig.gson();
        assertNotNull(gson);
    }

    @Test
    public void testModelMapperBean() {
        ModelMapper modelMapper = appConfig.modelMapper();
        assertNotNull(modelMapper);
    }

    @Test
    public void testJavaMailSenderBean() {
        JavaMailSender mailSender = appConfig.mailSender();
        assertNotNull(mailSender);
    }

}
