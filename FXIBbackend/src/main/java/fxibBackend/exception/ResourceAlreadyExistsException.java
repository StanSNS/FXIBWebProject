package fxibBackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.ACCESS_DENIED;
import static fxibBackend.constants.ErrorConst.RESOURCE_ALREADY_EXISTS;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException extends RuntimeException {

    public ResourceAlreadyExistsException() {
        super(RESOURCE_ALREADY_EXISTS);
        Logger logger = LoggerFactory.getLogger(ResourceAlreadyExistsException.class);
        logger.error(RESOURCE_ALREADY_EXISTS, this);
    }
}