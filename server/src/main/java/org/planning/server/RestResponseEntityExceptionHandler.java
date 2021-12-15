package org.planning.server;

import org.planning.exceptions.EntityAlreadyExistException;
import org.planning.exceptions.NotFoundException;
import org.planning.exceptions.ValidationException;
import org.planning.server.api.v1.dto.ExceptionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(value = { ValidationException.class, IllegalArgumentException.class })
    protected ResponseEntity<Object> handleValidationException(RuntimeException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ExceptionDto.of(ex.getMessage()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { NotFoundException.class })
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ExceptionDto.of(ex.getMessage()),
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { EntityAlreadyExistException.class })
    protected ResponseEntity<Object> handleConflictExceptions(RuntimeException ex, WebRequest request) {
        LOG.error(ex.getMessage(), ex);
        return handleExceptionInternal(ex, ExceptionDto.of(ex.getMessage()),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

}
