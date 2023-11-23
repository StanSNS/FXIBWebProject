package fxibBackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.ACCESS_DENIED;
import static fxibBackend.constants.ErrorConst.INTERNAL_ERROR;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalErrorException extends RuntimeException {

    public InternalErrorException() {
        super(INTERNAL_ERROR);
        Logger logger = LoggerFactory.getLogger(InternalErrorException.class);
        logger.error(INTERNAL_ERROR, this);
    }
}

