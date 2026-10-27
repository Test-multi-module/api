package net.testproj.auth.controllers;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

@RestControllerAdvice(basePackages = "net.testproj.auth.controllers")
public class AuthValidationExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public void handleValidation(MethodArgumentNotValidException exception, HttpServletResponse response)
            throws IOException {
        // TODO: Define a consistent error response for auth API clients.
        response.sendError(exception.getStatusCode().value(), exception.getBody().getDetail());
    }
}
