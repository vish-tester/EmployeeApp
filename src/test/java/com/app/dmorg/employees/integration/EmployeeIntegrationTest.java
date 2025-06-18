package com.app.dmorg.employees.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.app.dmorg.employees.model.Employee;
import com.app.dmorg.employees.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class EmployeeIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();

        employee = new Employee();
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setLocation("New York");
        employee.setAge(30);

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Test
    void shouldCreateEmployeeWhenEmailIsUnique() {
        // Arrange
        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);

        // Act
        ResponseEntity<Employee> response = restTemplate.postForEntity(
                "/api/employees", request, Employee.class);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(employee.getEmail(), response.getBody().getEmail());
    }

    @Test
    void shouldReturn409WhenEmailAlreadyExists() {
        // Arrange
        employeeRepository.save(employee);
        
        Employee duplicateEmployee = new Employee();
        duplicateEmployee.setFirstName("Jane");
        duplicateEmployee.setLastName("Smith");
        duplicateEmployee.setEmail("john.doe@example.com"); // Same email
        duplicateEmployee.setLocation("Boston");
        duplicateEmployee.setAge(25);

        HttpEntity<Employee> request = new HttpEntity<>(duplicateEmployee, headers);

        // Act
        ResponseEntity<Object> response = restTemplate.postForEntity(
                "/api/employees", request, Object.class);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }
}