package fxibBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.JWT_AUTHENTICATION_FAILURE;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class JwtAuthenticationException extends RuntimeException {

    public JwtAuthenticationException() {
        super(JWT_AUTHENTICATION_FAILURE);
    }
}
