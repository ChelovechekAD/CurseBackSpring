package it.academy.cursebackspring.exceptions.handlers;

import it.academy.cursebackspring.dto.ExceptionResponse;
import it.academy.cursebackspring.dto.ValidationErrorResponse;
import it.academy.cursebackspring.dto.Violation;
import it.academy.cursebackspring.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class HandlerExceptions {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> notFoundExceptions(Exception e) {
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({ExistException.class, CategoryDeleteException.class, ProductUsedInOrdersException.class})
    public ResponseEntity<?> existExceptions(Exception e) {
        return buildExceptionResponse(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({PasswordMatchException.class, WrongPasswordException.class})
    public ResponseEntity<?> token(Exception e) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unAuthExceptions(UnauthorizedException e) {
        loggingWarnException(e);
        return buildExceptionResponse(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(AccessDeniedCustomException.class)
    public ResponseEntity<?> accessExceptions(AccessDeniedCustomException e) {
        loggingWarnException(e);
        return buildExceptionResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptions(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        loggingWarnException(e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintValidationException(
            ConstraintViolationException e
    ) {
        final List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .collect(Collectors.toList());
        log.warn(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, RefreshTokenInvalidException.class,
            RequestParamInvalidException.class,})
    public ResponseEntity<?> httpMessageNotReadableException(Exception e) {
        loggingWarnException(e);
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class, NoResourceFoundException.class})
    public ResponseEntity<?> internalAuthenticationServiceException(Exception e) {
        loggingWarnException(e);
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalServerError(Exception e) throws Exception {
        log.error("Error {}: {}", e.getClass(), e.getLocalizedMessage());
        if (e instanceof AccessDeniedException || e instanceof AuthenticationException) {
            throw e;
        }
        return buildExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private void loggingWarnException(Exception e) {
        log.warn("Warn {}: {}", e.getClass(), e.getLocalizedMessage());
    }

    private ResponseEntity<Object> buildExceptionResponse(HttpStatusCode code, Exception e) {
        var exceptionResponse = ExceptionResponse.builder()
                .HttpStatusCode(code.value())
                .ExceptionMessage(e.getMessage())
                .TimeStamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(code).body(exceptionResponse);
    }

}
