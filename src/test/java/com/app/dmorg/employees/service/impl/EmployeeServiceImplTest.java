package com.app.dmorg.employees.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.app.dmorg.employees.model.Employee;
import com.app.dmorg.employees.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setLocation("New York");
        employee.setAge(30);
    }

    @Test
    void getAllEmployees_shouldReturnAllEmployees() {
        // Arrange
        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setEmail("jane.doe@example.com");
        
        List<Employee> employees = Arrays.asList(employee, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(2, result.size());
        assertEquals(employees, result);
        verify(employeeRepository).findAll();
    }
    
    @Test
    void getEmployeeById_shouldReturnEmployee_whenEmployeeExists() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        Optional<Employee> result = employeeService.getEmployeeById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
        verify(employeeRepository).findById(1L);
    }
    
    @Test
    void getEmployeeById_shouldReturnEmpty_whenEmployeeDoesNotExist() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Employee> result = employeeService.getEmployeeById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(employeeRepository).findById(99L);
    }

    @Test
    void isEmailExists_shouldReturnTrue_whenEmailExists() {
        // Arrange
        String email = "john.doe@example.com";
        when(employeeRepository.existsByEmail(email)).thenReturn(true);

        // Act
        boolean result = employeeService.isEmailExists(email);

        // Assert
        assertTrue(result);
        verify(employeeRepository).existsByEmail(email);
    }

    @Test
    void isEmailExists_shouldReturnFalse_whenEmailDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";
        when(employeeRepository.existsByEmail(email)).thenReturn(false);

        // Act
        boolean result = employeeService.isEmailExists(email);

        // Assert
        assertFalse(result);
        verify(employeeRepository).existsByEmail(email);
    }

    @Test
    void createEmployee_shouldSaveAndReturnEmployee() {
        // Arrange
        when(employeeRepository.save(employee)).thenReturn(employee);

        // Act
        Employee savedEmployee = employeeService.createEmployee(employee);

        // Assert
        assertNotNull(savedEmployee);
        assertEquals(employee.getEmail(), savedEmployee.getEmail());
        verify(employeeRepository).save(employee);
    }
    
    @Test
    void deleteEmployee_shouldDeleteEmployee_whenEmployeeExists() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        
        // Act
        employeeService.deleteEmployee(1L);
        
        // Assert
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    }
    
    @Test
    void deleteEmployee_shouldThrowException_whenEmployeeDoesNotExist() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.deleteEmployee(99L);
        });
        
        assertEquals("Employee not found with ID: 99", exception.getMessage());
        verify(employeeRepository).findById(99L);
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
    
    @Test
    void deleteAllEmployees_shouldCallRepositoryDeleteAll() {
        // Act
        employeeService.deleteAllEmployees();
        
        // Assert
        verify(employeeRepository).deleteAll();
    }
    
    @Test
    void updateEmployee_shouldUpdateAndReturnEmployee_whenEmployeeExists() {
        // Arrange
        Employee updatedEmployee = new Employee();
        updatedEmployee.setFirstName("John Updated");
        updatedEmployee.setLastName("Doe Updated");
        updatedEmployee.setEmail("updated@example.com");
        updatedEmployee.setLocation("Boston");
        updatedEmployee.setAge(35);
        
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        
        // Act
        Employee result = employeeService.updateEmployee(1L, updatedEmployee);
        
        // Assert
        assertNotNull(result);
        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(employee);
        
        // Verify employee was updated with new values
        assertEquals("John Updated", employee.getFirstName());
        assertEquals("Doe Updated", employee.getLastName());
        assertEquals("updated@example.com", employee.getEmail());
        assertEquals("Boston", employee.getLocation());
        assertEquals(35, employee.getAge());
    }
    
    @Test
    void updateEmployee_shouldThrowException_whenEmployeeDoesNotExist() {
        // Arrange
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());
        
        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            employeeService.updateEmployee(99L, employee);
        });
        
        assertEquals("Employee not found with ID: 99", exception.getMessage());
        verify(employeeRepository).findById(99L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }
}