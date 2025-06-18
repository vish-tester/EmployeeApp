package com.app.dmorg.employees;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class EmployeesApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring context loads successfully
	}
	
	@Test
	void applicationStarts() {
		// Test the main method for coverage
		EmployeesApplication.main(new String[] {});
	}
}