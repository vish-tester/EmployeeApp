package com.app.dmorg.employees.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.app.dmorg.employees.model.Employee;
import com.app.dmorg.employees.repository.EmployeeRepository;
import com.app.dmorg.employees.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
    
    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }
    
    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
    
    @Override
    public boolean isEmailExists(String email) {
        return employeeRepository.existsByEmail(email);
    }
    
    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
        employeeRepository.delete(employee);
    }
    
    @Override
    public void deleteAllEmployees() {
        employeeRepository.deleteAll();
    }

    @Override
    public Employee updateEmployee(Long id, Employee updatedEmployee) {
       Employee employee =  employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + id));
       employee.setFirstName(updatedEmployee.getFirstName());
       employee.setLastName(updatedEmployee.getLastName());
       employee.setEmail(updatedEmployee.getEmail());
       employee.setLocation(updatedEmployee.getLocation());
       employee.setAge(updatedEmployee.getAge());
       return employeeRepository.save(employee);
    }

}