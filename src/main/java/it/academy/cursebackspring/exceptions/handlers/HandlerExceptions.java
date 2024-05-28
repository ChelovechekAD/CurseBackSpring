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

    @ExceptionHandler({NotFoundException.class, NoResourceFoundException.class})
    public ResponseEntity<?> notFoundExceptions(Exception e) {
        return buildExceptionResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler({ExistException.class, CategoryDeleteException.class, ProductUsedInOrdersException.class})
    public ResponseEntity<?> existExceptions(Exception e) {
        return buildExceptionResponse(HttpStatus.CONFLICT, e);
    }

    @ExceptionHandler({PasswordMatchException.class, RefreshTokenInvalidException.class, RequestParamInvalidException.class,
            WrongPasswordException.class})
    public ResponseEntity<?> authExceptions(Exception e) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> unAuthExceptions(UnauthorizedException e) {
        return buildExceptionResponse(HttpStatus.UNAUTHORIZED, e);
    }

    @ExceptionHandler(AccessDeniedCustomException.class)
    public ResponseEntity<?> accessExceptions(AccessDeniedCustomException e) {
        return buildExceptionResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validationExceptions(MethodArgumentNotValidException e) {
        final List<Violation> violations = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());
        log.error(e.getLocalizedMessage());
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
        log.error(e.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return buildExceptionResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> accessDeniedSpringException(AccessDeniedException e) {
        return buildExceptionResponse(HttpStatus.FORBIDDEN, e);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> internalServerError(Exception e) {
        return buildExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    private ResponseEntity<Object> buildExceptionResponse(HttpStatusCode code, Exception e) {
        var exceptionResponse = ExceptionResponse.builder()
                .HttpStatusCode(code.value())
                .ExceptionMessage(e.getMessage())
                .TimeStamp(LocalDateTime.now())
                .build();
        log.error("Error: {}.", e.getLocalizedMessage());
        return ResponseEntity.status(code).body(exceptionResponse);
    }

}
