package fxibBackend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static fxibBackend.constants.ErrorConst.ACCESS_DENIED;
import static fxibBackend.constants.ErrorConst.EMAIL_SENDING_FAILURE;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class EmailSendingException extends RuntimeException {

    public EmailSendingException() {
        super(EMAIL_SENDING_FAILURE);
        Logger logger = LoggerFactory.getLogger(EmailSendingException.class);
        logger.error(EMAIL_SENDING_FAILURE, this);
    }
}
