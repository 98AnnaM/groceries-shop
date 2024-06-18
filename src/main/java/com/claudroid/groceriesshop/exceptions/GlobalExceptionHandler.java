package com.claudroid.groceriesshop.exceptions;

import com.claudroid.groceriesshop.validation.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> onValidationFailure(MethodArgumentNotValidException exc) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        exc.getFieldErrors().forEach(fe -> apiError.addFieldWithError(fe.getDefaultMessage()));

        return ResponseEntity.badRequest().body(apiError);
    }

    @ExceptionHandler({InvalidInputExistsException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> onValidationInputFailure(Exception exc) {

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST);
        apiError.addFieldWithError(exc.getMessage());

        return ResponseEntity.badRequest().body(apiError);
    }
}
