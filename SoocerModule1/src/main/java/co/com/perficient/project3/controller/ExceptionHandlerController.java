package co.com.perficient.project3.controller;

import jakarta.persistence.NonUniqueResultException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandlerController {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<String>> constraintViolations(ConstraintViolationException ex) {
        logger.error(ex.getMessage());
        List<String> errors = ex.getConstraintViolations().stream()
                .map(constraintViolation -> constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage())
                .toList();
        return ResponseEntity.internalServerError().body(errors);
    }

    @ExceptionHandler(NonUniqueResultException.class)
    public ResponseEntity<String> nonUniqueResult(NonUniqueResultException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> noSuchElement(NoSuchElementException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<String> sqlException(SQLException ex) {
        String message = ex.getMessage().substring(ex.getMessage().indexOf("Detail"));
        logger.error(message);
        return ResponseEntity.internalServerError().body(message);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> illegalArgument(IllegalArgumentException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }
}
