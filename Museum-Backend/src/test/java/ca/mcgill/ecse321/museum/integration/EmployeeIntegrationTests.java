package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTests {
    
    @Autowired
    private TestRestTemplate client;
    @Autowired 
    private EmployeeRepository employeeRepository;
    @Autowired 
    private ScheduleRepository scheduleRepository;
    
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
        scheduleRepository.deleteAll();

    }

    
}
