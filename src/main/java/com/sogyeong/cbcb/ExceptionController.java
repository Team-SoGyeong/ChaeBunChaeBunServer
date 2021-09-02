package com.sogyeong.cbcb;

import com.sogyeong.cbcb.defaults.entity.response.BasicResponse;
import com.sogyeong.cbcb.defaults.entity.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    // 400
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<? extends BasicResponse>  BadRequestException(final RuntimeException ex) {
        log.warn("error", ex);
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    // 401
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<? extends BasicResponse> handleAccessDeniedException(final AccessDeniedException ex) {
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(ex.getMessage(),HttpStatus.UNAUTHORIZED));
    };

    //404
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<? extends BasicResponse> handleError404(final NoHandlerFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND));
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<? extends BasicResponse> handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

}
