package com.app.dmorg.employees.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class GlobalExceptionHandlerTest {

    @Test
    void handleDuplicateEmailException_shouldReturnConflictStatus() {
        // Arrange
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        String errorMessage = "Email already exists";
        DuplicateEmailException exception = new DuplicateEmailException(errorMessage);
        
        // Act
        ResponseEntity<Object> response = handler.handleDuplicateEmailException(exception);
        
        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        
        @SuppressWarnings("unchecked")
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertEquals(errorMessage, errorMap.get("error"));
    }
}