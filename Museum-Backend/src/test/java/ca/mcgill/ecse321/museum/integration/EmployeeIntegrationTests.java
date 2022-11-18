package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;

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

    /**
     * Test to successfully get an employee
     * @author vz
     */
    @Test
    public void testGetEmployee() {
        Long id = createEmployeeDto();
        
        ResponseEntity<EmployeeDto> response = client.getForEntity("/employee/" + id, EmployeeDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(id, response.getBody().getMuseumUserId(), "Response has correct message");
    }

    /**
     * Test to unsuccessfully get an employee that has an invalid id
     * @author VZ
     */
    @Test
    public void testGetEmployeeInvalidId(){
        ResponseEntity<String> response = client.getForEntity("/employee/" + -1, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Employee does not exist", response.getBody(), "Response has correct message");
    }

    public Long createEmployeeDto(){
        EmployeeDto employeeDto = null;
        Employee employee = new Employee();
        Schedule schedule = new Schedule();
        employee.setEmail("asdf@gmail.com");
        employee.setPassword("asdf");
        employee.setSchedule(schedule);
        employee.setName("asdf");
        employeeRepository.save(employee);
        employeeDto = DtoUtility.convertToDto(employee);
        return employeeDto.getMuseumUserId();
    }
}
