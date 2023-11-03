package UtilTest;

import fxibBackend.entity.QuestionEntity;
import fxibBackend.util.ValidationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ValidationUtilTest {

    private ValidationUtil validationUtil;

    @BeforeEach
    public void setUp() {
        validationUtil = new ValidationUtil();
    }

    @Test
    public void testIsValidWithValidEntity() {
        QuestionEntity entity = new QuestionEntity();
        entity.setContent("Valid content");
        entity.setDate("2023-09-28");
        entity.setWriter("user123");
        entity.setSolved(true);

        boolean isValid = validationUtil.isValid(entity);

        assertFalse(isValid, "Entity should be valid");
    }

    @Test
    public void testIsValidWithInvalidEntity() {
        QuestionEntity entity = new QuestionEntity();

        boolean isValid = validationUtil.isValid(entity);

        assertFalse(isValid, "Entity should be invalid");
    }
}
