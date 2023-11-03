package fxibBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.MISSING_PARAMETER;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingParameterException extends RuntimeException {

    public MissingParameterException() {
        super(MISSING_PARAMETER);
    }
}