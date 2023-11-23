package fxibBackend.service;

import fxibBackend.dto.UserDetailsDTO.LocationDTO;
import fxibBackend.entity.LocationEntity;
import fxibBackend.exception.DataValidationException;
import fxibBackend.exception.EmailSendingException;
import fxibBackend.repository.LocationEntityRepository;
import fxibBackend.util.ValidationUtil;
import io.ipgeolocation.api.Geolocation;
import io.ipgeolocation.api.IPGeolocationAPI;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import static fxibBackend.constants.ConfigConst.GEOLOCATION_API_KEY;
import static fxibBackend.constants.ResponseConst.LOCATION_DIFFERENCE_EMAIL_SENT_SUCCESSFULLY;

@Service
@RequiredArgsConstructor
public class LocationService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final LocationEntityRepository locationEntityRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final EmailService emailService;

    /**
     * Retrieves the initial location for a user and stores it in the database.
     *
     * @param username The username of the user for whom the initial location is retrieved.
     * @return The initial location entity.
     * @throws DataValidationException If the location data is not valid.
     */
    public LocationEntity getTheInitialLocation(String username) {
        LocationDTO locationDTO = currentLocation(username);
        if (validationUtil.isValid(locationDTO)) {
            LocationEntity locationEntity = modelMapper.map(locationDTO, LocationEntity.class);
            locationEntityRepository.save(locationEntity);
            return locationEntity;
        }
        throw new DataValidationException();
    }

    /**
     * Checks if the current location is different from the original location and sends an email if a difference is detected.
     *
     * @param originalLocation The original location entity.
     * @param username         The username of the user.
     * @return True if the locations are different; otherwise, false.
     * @throws MessagingException If there is an issue with sending the email.
     */
    public boolean isLocationDifferent(LocationEntity originalLocation, String username) throws MessagingException {
        LocationDTO originalLocationDTO = modelMapper.map(originalLocation, LocationDTO.class);
        LocationDTO currentLocationDTO = currentLocation(username);
        if (!equalsLocationDTO(originalLocationDTO, currentLocationDTO)) {
            String sendLocationDifferenceEmail = emailService.sendLocationDifferenceEmail(originalLocationDTO, currentLocationDTO);
            if (!sendLocationDifferenceEmail.equals(LOCATION_DIFFERENCE_EMAIL_SENT_SUCCESSFULLY)) {
                throw new EmailSendingException();
            }
            return true;
        }
        return false;
    }

    /**
     * Retrieves the current location for a user.
     *
     * @param username The username of the user for whom the current location is retrieved.
     * @return The current location DTO.
     * @throws DataValidationException If the location data is not valid.
     */
    public LocationDTO currentLocation(String username) {
        Geolocation geolocation = new IPGeolocationAPI(GEOLOCATION_API_KEY).getGeolocation();

        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setContinent(geolocation.getContinentName());
        locationDTO.setCity(geolocation.getCity());
        locationDTO.setCountry(geolocation.getCountryName());
        locationDTO.setIp(geolocation.getIP());
        locationDTO.setCountryFlagURL(geolocation.getCountryFlag());
        locationDTO.setUsername(username);

        if (validationUtil.isValid(locationDTO)) {
            return locationDTO;
        }
        throw new DataValidationException();
    }

    /**
     * Checks if two LocationDTO objects are equal based on various location properties.
     *
     * @param originalLocationDTO  The original location DTO.
     * @param currentLocationDTO   The current location DTO.
     * @return True if the locations are equal; otherwise, false.
     */
    public boolean equalsLocationDTO(LocationDTO originalLocationDTO, LocationDTO currentLocationDTO) {
        boolean areContinentEqual = originalLocationDTO.getContinent().equals(currentLocationDTO.getContinent());
        boolean areCitiesEqual = originalLocationDTO.getCity().equals(currentLocationDTO.getCity());
        boolean areCountriesEqual = originalLocationDTO.getCountry().equals(currentLocationDTO.getCountry());
        boolean areCountryFlagsURLEqual = originalLocationDTO.getCountryFlagURL().equals(currentLocationDTO.getCountryFlagURL());
        boolean areUsernamesEqual = originalLocationDTO.getUsername().equals(currentLocationDTO.getUsername());
        return areCountriesEqual && areCitiesEqual && areContinentEqual && areCountryFlagsURLEqual && areUsernamesEqual;
    }


}
