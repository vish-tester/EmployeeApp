package com.app.dmorg.employees.service;

import java.util.List;
import java.util.Optional;

import com.app.dmorg.employees.model.Employee;

public interface EmployeeService {
	 List<Employee> getAllEmployees();
	 Optional<Employee> getEmployeeById(Long id);
	 Employee createEmployee(Employee employee);
	 boolean isEmailExists(String email);
	 void deleteEmployee(Long id);
	 void deleteAllEmployees();
	 Employee updateEmployee(Long id, Employee updatedEmployee);
}
