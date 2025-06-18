package com.app.dmorg.employees.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.app.dmorg.employees.model.Employee;
import com.app.dmorg.employees.repository.EmployeeRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(EmployeeRepository employeeRepository) {
        return args -> {
            // Only add sample data if the repository is empty
            if (employeeRepository.count() == 0) {
                Employee emp1 = new Employee();
                emp1.setFirstName("John");
                emp1.setLastName("Doe");
                emp1.setEmail("john.doe@example.com");
                emp1.setLocation("New York");
                emp1.setAge(30);
                employeeRepository.save(emp1);

                Employee emp2 = new Employee();
                emp2.setFirstName("Jane");
                emp2.setLastName("Smith");
                emp2.setEmail("jane.smith@example.com");
                emp2.setLocation("San Francisco");
                emp2.setAge(28);
                employeeRepository.save(emp2);

                Employee emp3 = new Employee();
                emp3.setFirstName("Michael");
                emp3.setLastName("Johnson");
                emp3.setEmail("michael.johnson@example.com");
                emp3.setLocation("Chicago");
                emp3.setAge(35);
                employeeRepository.save(emp3);
            }
        };
    }
}