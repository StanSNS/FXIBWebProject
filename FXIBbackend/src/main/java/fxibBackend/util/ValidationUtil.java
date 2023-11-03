package fxibBackend.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;


@Component
public class ValidationUtil {

    /**
     * initializing dependencies with lombok @RequiredArgsConstructor
     */
    private final Validator validator;

    /**
     * Initializes a new instance of the ValidationUtil class.
     */
    public ValidationUtil() {
        validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    /**
     * Validates an entity using Java Bean Validation.
     *
     * @param entity The entity to be validated.
     * @param <E>    The type of the entity.
     * @return True if the entity is valid, otherwise false.
     */
    public <E> boolean isValid(E entity) {
        return validator.validate(entity).isEmpty();
    }
}