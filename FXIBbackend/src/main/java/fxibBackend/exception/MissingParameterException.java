package fxibBackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.ACCESS_DENIED;
import static fxibBackend.constants.ErrorConst.MISSING_PARAMETER;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class MissingParameterException extends RuntimeException {

    public MissingParameterException() {
        super(MISSING_PARAMETER);
        Logger logger = LoggerFactory.getLogger(MissingParameterException.class);
        logger.error(MISSING_PARAMETER, this);
    }
}