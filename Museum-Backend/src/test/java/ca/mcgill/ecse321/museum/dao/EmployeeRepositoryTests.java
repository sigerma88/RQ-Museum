package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;

/**
 * Test the persistence layer for the EmployeeRepository. Testing reading and writing of 
 * objects, attributes and references to the database.
 * 
 * @author Eric
 */
@SpringBootTest
public class EmployeeRepositoryTests {
  @Autowired
  private EmployeeRepository employeeRepository;

  @AfterEach
  public void clearDatabase() {
    employeeRepository.deleteAll();
  }

  @Test
  public void testPersistAndLoadEmployee() {
    //create employee
    Employee employee = new Employee();
    String name = "Broseph";
    String email = "aEmail";
    String password = "aPassword";

    employee.setName(name);
    employee.setEmail(email);
    employee.setPassword(password);

    //create schedule
    Schedule schedule = new Schedule();

    //associate employee to schedule

    employee.setSchedule(schedule);

    //save employee
    employee = employeeRepository.save(employee);
    long id = employee.getMuseumUserId();

    //reset employee
    employee = null;

    //read employee from database
    employee = employeeRepository.findEmployeeByMuseumUserId(id);

    //assert that employee has correct attributes
    assertNotNull(employee);
    assertEquals(id, employee.getMuseumUserId());
    assertEquals(name, employee.getName());
    assertEquals(email, employee.getEmail());
    assertEquals(password, employee.getPassword());
  }
}
