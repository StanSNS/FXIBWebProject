package fxibBackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.RESOURCE_ALREADY_EXISTS;
import static fxibBackend.constants.ErrorConst.RESOURCE_NOT_FOUND;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super(RESOURCE_NOT_FOUND);
        Logger logger = LoggerFactory.getLogger(ResourceNotFoundException.class);
        logger.error(RESOURCE_NOT_FOUND, this);
    }
}
