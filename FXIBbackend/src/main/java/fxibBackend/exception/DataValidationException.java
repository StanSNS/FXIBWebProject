package fxibBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.VALIDATION_FAILED;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class DataValidationException extends RuntimeException {

    public DataValidationException() {
        super(VALIDATION_FAILED);
    }
}
