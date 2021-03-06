package com.devops3.exception;



import com.devops3.dto.ErrorDTO;
import com.devops3.dto.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@EnableWebMvc
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(UnauthorizedUserException.class)
    public ResponseEntity<?> handleUnautorizedUserException(UnauthorizedUserException ex) {
        ErrorDTO dto = new ErrorDTO();
        dto.setStatus(Status.FAILURE);
        dto.setError(buildExceptionResponse(ex, ex.getLocalizedMessage()));
        dto.setResponseCode(HttpStatus.UNAUTHORIZED.value());

        // 401
        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(TokenRefreshException.class)
    public ResponseEntity<?> handleTokenRefreshException(TokenRefreshException ex) {
        ErrorDTO dto = new ErrorDTO();
        dto.setStatus(Status.FAILURE);
        dto.setError(buildExceptionResponse(ex, ex.getLocalizedMessage()));
        dto.setResponseCode(HttpStatus.FORBIDDEN.value());

        // 403
        return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);
    }

    /*@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<EntityDTO> handleGenericException(RuntimeException ex) {
        EntityDTO dto = new EntityDTO();
        dto.setStatus(Status.FAILURE);
        dto.setError(buildExceptionResponse(ex, ex.getLocalizedMessage()));
        dto.setResponseCode(HttpStatus.NOT_ACCEPTABLE.value());

        // 406
        return new ResponseEntity<EntityDTO>(dto, HttpStatus.NOT_ACCEPTABLE);
    }*/

    private ExceptionResponse buildExceptionResponse(Exception ex, String errorCode) {
        ExceptionResponse response = new ExceptionResponse();
        response.setErrorMessage(ex.getMessage());
        response.setErrorCode(errorCode);
        response.setTimestamp(LocalDateTime.now());
        return response;
    }


}
