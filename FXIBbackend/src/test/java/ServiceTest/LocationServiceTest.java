package ServiceTest;

import fxibBackend.dto.UserDetailsDTO.LocationDTO;
import fxibBackend.repository.LocationEntityRepository;
import fxibBackend.service.EmailService;
import fxibBackend.service.LocationService;
import fxibBackend.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class LocationServiceTest {

    @Mock
    private LocationEntityRepository locationEntityRepository;
    @Mock
    private ValidationUtil validationUtil;
    @Mock
    private EmailService emailService;
    private LocationService locationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        locationService = new LocationService(locationEntityRepository, null, validationUtil, emailService);
    }

    @Test
    public void testEqualsLocationDTO() {
        LocationDTO locationDTO1 = new LocationDTO();
        locationDTO1.setContinent("Continent1");
        locationDTO1.setCity("City1");
        locationDTO1.setCountry("Country1");
        locationDTO1.setCountryFlagURL("Flag1");
        locationDTO1.setUsername("User1");

        LocationDTO locationDTO2 = new LocationDTO();
        locationDTO2.setContinent("Continent1");
        locationDTO2.setCity("City1");
        locationDTO2.setCountry("Country1");
        locationDTO2.setCountryFlagURL("Flag1");
        locationDTO2.setUsername("User1");

        assertTrue(locationService.equalsLocationDTO(locationDTO1, locationDTO2));
    }

}
