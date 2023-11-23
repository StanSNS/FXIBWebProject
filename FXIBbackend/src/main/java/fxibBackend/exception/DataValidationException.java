package fxibBackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.ACCESS_DENIED;
import static fxibBackend.constants.ErrorConst.VALIDATION_FAILED;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class DataValidationException extends RuntimeException {

    public DataValidationException() {
        super(VALIDATION_FAILED);
        Logger logger = LoggerFactory.getLogger(DataValidationException.class);
        logger.error(VALIDATION_FAILED, this);
    }
}
