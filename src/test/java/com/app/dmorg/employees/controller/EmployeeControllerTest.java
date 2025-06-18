package com.app.dmorg.employees.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.app.dmorg.employees.exception.DuplicateEmailException;
import com.app.dmorg.employees.model.Employee;
import com.app.dmorg.employees.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService employeeService;

    private Employee employee;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setLocation("New York");
        employee.setAge(30);
        
        employee2 = new Employee();
        employee2.setId(2L);
        employee2.setFirstName("Jane");
        employee2.setLastName("Smith");
        employee2.setEmail("jane.smith@example.com");
        employee2.setLocation("Boston");
        employee2.setAge(28);
    }

    @Test
    void getAllEmployees_shouldReturnAllEmployees() throws Exception {
        // Arrange
        List<Employee> employees = Arrays.asList(employee, employee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // Act & Assert
        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
    
    @Test
    void getEmployeeById_shouldReturnEmployee_whenEmployeeExists() throws Exception {
        // Arrange
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        // Act & Assert
        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }
    
    @Test
    void getEmployeeById_shouldReturn404_whenEmployeeDoesNotExist() throws Exception {
        // Arrange
        when(employeeService.getEmployeeById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEmployee_shouldReturn201_whenEmailIsUnique() throws Exception {
        // Arrange
        when(employeeService.isEmailExists(employee.getEmail())).thenReturn(false);
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employee);

        // Act & Assert
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void createEmployee_shouldReturn409_whenEmailExists() throws Exception {
        // Arrange
        when(employeeService.isEmailExists(employee.getEmail())).thenReturn(true);
        when(employeeService.createEmployee(any(Employee.class)))
                .thenThrow(new DuplicateEmailException("Employee with email " + employee.getEmail() + " already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isConflict());
    }
    
    @Test
    void updateEmployee_shouldReturnUpdatedEmployee_whenEmailIsUnique() throws Exception {
        // Arrange
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setFirstName("John Updated");
        updatedEmployee.setLastName("Doe Updated");
        updatedEmployee.setEmail("updated@example.com");
        
        when(employeeService.isEmailExists(updatedEmployee.getEmail())).thenReturn(false);
        when(employeeService.updateEmployee(eq(1L), any(Employee.class))).thenReturn(updatedEmployee);
        
        // Act & Assert
        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John Updated"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }
    
    @Test
    void updateEmployee_shouldReturn409_whenEmailExists() throws Exception {
        // Arrange
        Employee updatedEmployee = new Employee();
        updatedEmployee.setId(1L);
        updatedEmployee.setFirstName("John Updated");
        updatedEmployee.setLastName("Doe Updated");
        updatedEmployee.setEmail("duplicate@example.com");
        
        when(employeeService.isEmailExists(updatedEmployee.getEmail())).thenReturn(true);
        
        // Act & Assert
        mockMvc.perform(put("/api/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isConflict());
    }
    
    @Test
    void deleteEmployee_shouldReturn204_whenEmployeeExists() throws Exception {
        // Arrange
        doNothing().when(employeeService).deleteEmployee(1L);
        
        // Act & Assert
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void deleteAllEmployees_shouldReturn204() throws Exception {
        // Arrange
        doNothing().when(employeeService).deleteAllEmployees();
        
        // Act & Assert
        mockMvc.perform(delete("/api/employees"))
                .andExpect(status().isNoContent());
    }
}