package com.javaspringboot.Project2.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.javaspringboot.Project2.advice.HttpResponse;
import com.javaspringboot.Project2.exception.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class ExceptionHandling implements ErrorController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username or password incorrect. Please try again";

    private static final String EMPTY_RESULT = "We have a problem";

    private static final String DEVICE_EXIST = "%s already exists";

    private static final String EMAIL_EXIST = "Email is already taken!";

    private static final String INVALID_FORMAT = "Invalid format : %s";

    private static final String USER_NOT_FOUND_WITH_USERNAME = "No found user with username %s";

    private static final String CUSTOMER_NOT_FOUND_WITH_PHONE = "No found customer with phone %s";


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException(BadCredentialsException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, INCORRECT_CREDENTIALS);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistException(EmailExistException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, EMAIL_EXIST);
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<HttpResponse> missingServletRequestParameterException(MissingServletRequestParameterException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HttpResponse> userNotFoundException(UserNotFoundException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(NOT_FOUND, String.format(USER_NOT_FOUND_WITH_USERNAME,exception.getLocalizedMessage()));
    }

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<HttpResponse> customerNotFoundException(CustomerNotFoundException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(NOT_FOUND, String.format(CUSTOMER_NOT_FOUND_WITH_PHONE,exception.getLocalizedMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<HttpResponse> entityNotFoundException(EntityNotFoundException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(NOT_FOUND, EMPTY_RESULT);
    }

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<HttpResponse> notFoundException(ChangeSetPersister.NotFoundException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(NOT_FOUND, exception.getLocalizedMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST,exception.getFieldError().getField()+" "+ exception.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HttpResponse> noSuchElementException(NoSuchElementException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(NOT_FOUND,exception.getLocalizedMessage());
    }


    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<HttpResponse> emptyResultDataAccessException(EmptyResultDataAccessException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<HttpResponse> invalidFormatException(InvalidFormatException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, String.format(INVALID_FORMAT,exception.getLocalizedMessage()));

//        return createHttpResponse(BAD_REQUEST, "Date format is incorrect (yyyy-MM-dd)");
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<HttpResponse> sQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST,exception.getLocalizedMessage());
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpResponse> illegalArgumentException(IllegalArgumentException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getLocalizedMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        LOGGER.warn(exception.getMessage());
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<HttpResponse> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        LOGGER.warn(exception.getMessage());
        return createHttpResponse(BAD_REQUEST, exception.getCause().getLocalizedMessage(),exception.getLocalizedMessage());
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException exception) {
        LOGGER.warn(exception.getMessage());
        List<HttpResponse> errors = new ArrayList<>();
        Stream<ConstraintViolation<?>> violationStream = exception.getConstraintViolations().stream();
        violationStream.forEach(violation -> {
            errors.add(new HttpResponse(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST, violation.getPropertyPath().toString(), violation.getMessageTemplate()));
        });
        return new ResponseEntity(errors.get(0), HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                httpStatus.getReasonPhrase().toUpperCase(), message), httpStatus);
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus,String reason, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus,
                reason.toUpperCase(), message), httpStatus);
    }
}
