package com.app.dmorg.employees.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DuplicateEmailExceptionTest {

    @Test
    void constructor_shouldSetMessage() {
        // Arrange
        String errorMessage = "Test error message";
        
        // Act
        DuplicateEmailException exception = new DuplicateEmailException(errorMessage);
        
        // Assert
        assertEquals(errorMessage, exception.getMessage());
    }
}