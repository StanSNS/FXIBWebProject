package ExceptionTest;

import fxibBackend.constants.ErrorConst;
import fxibBackend.exception.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ExceptionTests {

    @Test
    public void accessDeniedExceptionTest() {
        AccessDeniedException exception = new AccessDeniedException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.ACCESS_DENIED);
    }

    @Test
    public void dataValidationExceptionTest() {
        DataValidationException exception = new DataValidationException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.VALIDATION_FAILED);
    }

    @Test
    public void internalErrorExceptionTest() {
        InternalErrorException exception = new InternalErrorException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.INTERNAL_ERROR);
    }

    @Test
    public void jwtAuthenticationExceptionTest() {
        JwtAuthenticationException exception = new JwtAuthenticationException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.JWT_AUTHENTICATION_FAILURE);
    }

    @Test
    public void missingParameterExceptionTest() {
        MissingParameterException exception = new MissingParameterException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.MISSING_PARAMETER);
    }

    @Test
    public void resourceAlreadyExistsExceptionTest() {
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.RESOURCE_ALREADY_EXISTS);
    }

    @Test
    public void resourceNotFoundExceptionTest() {
        ResourceNotFoundException exception = new ResourceNotFoundException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.RESOURCE_NOT_FOUND);
    }

    @Test
    public void emailFailureSendExceptionTest() {
        EmailSendingException exception = new EmailSendingException();
        assertThat(exception.getMessage()).isEqualTo(ErrorConst.EMAIL_SENDING_FAILURE);
    }

}
