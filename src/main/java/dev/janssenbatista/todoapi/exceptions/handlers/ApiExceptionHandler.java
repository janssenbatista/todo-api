package dev.janssenbatista.todoapi.exceptions.handlers;

import dev.janssenbatista.todoapi.exceptions.BadRequestException;
import dev.janssenbatista.todoapi.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
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
        if (exception instanceof NotFoundException notFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
        }
        return ResponseEntity.internalServerError().build();
    }
}
