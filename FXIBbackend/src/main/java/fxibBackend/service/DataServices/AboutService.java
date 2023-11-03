package fxibBackend.service.DataServices;

import fxibBackend.dto.InitDTOS.AboutDTO;
import fxibBackend.exception.DataValidationException;
import fxibBackend.repository.AboutEntityRepository;
import fxibBackend.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AboutService {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final ModelMapper modelMapper;
    private final AboutEntityRepository aboutEntityRepository;
    private final ValidationUtil validationUtil;

    /**
     * Retrieves a list of AboutDTO objects representing information about the application.
     * Maps AboutEntity objects to AboutDTO using ModelMapper and validates the DTOs.
     *
     * @return List of AboutDTO objects.
     * @throws DataValidationException if any of the mapped DTOs is invalid.
     */
    public List<AboutDTO> getAllAboutData() {
        return aboutEntityRepository
                .findAll()
                .stream()
                .map(aboutEntity -> {
                    AboutDTO aboutDTO = modelMapper.map(aboutEntity, AboutDTO.class);
                    if (validationUtil.isValid(aboutDTO)) {
                        return aboutDTO;
                    }
                    throw new DataValidationException();
                })
                .collect(Collectors.toList());
    }


}
