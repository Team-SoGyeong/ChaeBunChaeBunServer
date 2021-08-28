package com.sogyeong.cbcb.auth.common.exception;

import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorResponseHandler {
    @ExceptionHandler(InvalidAuthException.class)
    protected ResponseEntity<ErrorResponse> handlleIllegal(InvalidAuthException e){
        final ErrorResponse response= new ErrorResponse(e.getMessage(), e.getHttpStatus());
        return new  ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    protected ResponseEntity<ErrorResponse> handlleIllegal(ResourceAlreadyExistsException e){
        final ErrorResponse response= new ErrorResponse(e.getMessage(), e.getHttpStatus());
        return new  ResponseEntity<>(response, e.getHttpStatus());
    }

    @ExceptionHandler(UserNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handlleIllegal(UserNotFoundException e){
        final ErrorResponse response= new ErrorResponse(e.getMessage(), e.getHttpStatus());
        return new  ResponseEntity<>(response, e.getHttpStatus());
    }

}
