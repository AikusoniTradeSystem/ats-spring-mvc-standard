package io.github.aikusonitradesystem.mvcstandard.advice;

import io.github.aikusonitradesystem.core.constants.ErrorCode;
import io.github.aikusonitradesystem.core.exception.ATSException;
import io.github.aikusonitradesystem.core.exception.ATSRuntimeException;
import io.github.aikusonitradesystem.mvcstandard.model.view.ATSResponseBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ATSRuntimeException.class)
    public ResponseEntity<ATSResponseBody<Void>> handle(ATSRuntimeException exception) {
        log.error("ATSRuntimeException : ", exception);
        return ATSResponseBody.<Void>error(exception.getErrorCode(), exception.getErrorAlias(), exception.getMessage())
                .toResponseEntity();
    }

    @ExceptionHandler(ATSException.class)
    public ResponseEntity<ATSResponseBody<Void>> handle(ATSException exception) {
        log.error("ATSException : ", exception);
        return ATSResponseBody.<Void>error(exception.getErrorCode(), exception.getErrorAlias(), exception.getMessage())
                .toResponseEntity();
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ATSResponseBody<Void>> handle(RuntimeException exception) {
        log.error("RuntimeException : ", exception);
        return ATSResponseBody.<Void>error(ErrorCode.INTERNAL_SERVER_ERROR, "REH999998", exception.getMessage())
                .toResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ATSResponseBody<Void>> handle(Exception exception) {
        log.error("Exception : ", exception);
        return ATSResponseBody.<Void>error(ErrorCode.INTERNAL_SERVER_ERROR, "REH999999", exception.getMessage())
                .toResponseEntity();
    }
}
