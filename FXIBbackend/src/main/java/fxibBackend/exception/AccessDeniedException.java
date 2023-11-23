package fxibBackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.ACCESS_DENIED;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException() {
        super(ACCESS_DENIED);
        Logger logger = LoggerFactory.getLogger(AccessDeniedException.class);
        logger.error(ACCESS_DENIED, this);
    }
}

