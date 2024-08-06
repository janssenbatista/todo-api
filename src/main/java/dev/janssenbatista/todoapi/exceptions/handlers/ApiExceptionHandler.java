package dev.janssenbatista.todoapi.exceptions.handlers;

import dev.janssenbatista.todoapi.exceptions.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleException(RuntimeException exception) {
        if (exception instanceof BadRequestException badRequestException) {
            return ResponseEntity.badRequest().body(badRequestException.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }
}
