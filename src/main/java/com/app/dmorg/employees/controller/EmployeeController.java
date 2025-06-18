package com.app.dmorg.employees.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.dmorg.employees.exception.DuplicateEmailException;
import com.app.dmorg.employees.model.Employee;
import com.app.dmorg.employees.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        if (employee.getEmail() != null && employeeService.isEmailExists(employee.getEmail())) {
            throw new DuplicateEmailException("Employee with email " + employee.getEmail() + " already exists");
        }
        Employee savedEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    
    @DeleteMapping
    public ResponseEntity<Void> deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return ResponseEntity.noContent().build(); // 204 No Content
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee updatedEmployee) {
    	if (updatedEmployee.getEmail() != null && employeeService.isEmailExists(updatedEmployee.getEmail())) {
            throw new DuplicateEmailException("Employee with email " + updatedEmployee.getEmail() + " already exists");
        }
    	
    	Employee employee = employeeService.updateEmployee(id, updatedEmployee);
    	return new ResponseEntity<Employee> (employee, HttpStatus.CREATED);
    }

}
