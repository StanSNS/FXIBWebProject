package fxibBackend.inits;

import fxibBackend.entity.DataEntity.AboutEntity;
import fxibBackend.repository.AboutEntityRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static fxibBackend.constants.InitConst.*;

@Component
@RequiredArgsConstructor
public class AboutInit {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final AboutEntityRepository aboutEntityRepository;

    /**
     * Initializes the about content if no records exist in the AboutEntityRepository.
     * Creates and saves default AboutEntity records with titles and descriptions.
     */
    @PostConstruct
    public void aboutContentInit() {
        // Check if there are no existing AboutEntity records
        if (aboutEntityRepository.count() == 0) {
            // Create default AboutEntity records with titles and descriptions
            AboutEntity firstAboutEntity = new AboutEntity(FIRST_ABOUT_TITLE, FIRST_ABOUT_DESCRIPTION);
            AboutEntity secondAboutEntity = new AboutEntity(SECOND_ABOUT_TITLE, SECOND_ABOUT_DESCRIPTION);

            // Save the default AboutEntity records to the repository
            aboutEntityRepository.save(firstAboutEntity);
            aboutEntityRepository.save(secondAboutEntity);
        }
    }

}
