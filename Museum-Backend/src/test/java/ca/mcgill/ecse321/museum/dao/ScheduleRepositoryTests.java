package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;  

import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Employee;

@SpringBootTest
public class ScheduleRepositoryTests {
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private MuseumRepository museumRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @AfterEach
  public void clearDatabase() {
    museumRepository.deleteAll();
    employeeRepository.deleteAll();
    scheduleRepository.deleteAll();
  }

  @Test
  public void testPersistAndLoadSchedule() {
    //create Schedule
    Schedule schedule = new Schedule();

    //save Schedule
    scheduleRepository.save(schedule);
    long scheduleId = schedule.getScheduleId();

    //read Schedule from database
    schedule = scheduleRepository.findScheduleByScheduleId(scheduleId);

    //assert that Schedule has correct attributes
    assertNotNull(schedule);
  }

    @Test
    public void testPersistAndLoadScheduleForMuseum() {
    //create Schedule
    Schedule schedule = new Schedule();

    //create Museum associated with Schedule
    String name = "Rougon-Macquart";
    double visitFee = 6.99;
    Museum museum = new Museum();
    museum.setName(name);
    museum.setVisitFee(visitFee);
    museum.setSchedule(schedule);

    //save Museum
    museum = museumRepository.save(museum);
    long museumId = museum.getMuseumId();
    long scheduleId = museum.getSchedule().getScheduleId();

    //read Museum from database
    museum = museumRepository.findMuseumByMuseumId(museumId);

    //read Schedule from database
    Schedule repoSchedule = scheduleRepository.findScheduleByScheduleId(scheduleId);

    //assert that object has correct attributes
    assertNotNull(museum);
    assertEquals(museumId, museum.getMuseumId());
    assertEquals(name, museum.getName());
    assertEquals(visitFee, museum.getVisitFee());

    assertNotNull(museum.getSchedule());
    assertNotNull(repoSchedule);
    assertEquals(scheduleId, schedule.getScheduleId());
    assertEquals(scheduleId, repoSchedule.getScheduleId());
  }

  @Test
  public void testPersistAndLoadScheduleForEmployee() {
    //create Schedule
    Schedule schedule = new Schedule();

    //create Employee associated with Schedule
    String email = "emile.zola@Assommoir.fr";
    String name = "Émile Zola";
    String password = "Gervaise";
    Employee employee = new Employee();
    employee.setEmail(email);
    employee.setName(name);
    employee.setPassword(password);
    employee.setSchedule(schedule);

    //save Employee
    employee = employeeRepository.save(employee);
    long employeeId = employee.getMuseumUserId();
    long scheduleId = employee.getSchedule().getScheduleId();

    //read Employee from database
    employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);

    //read Schedule from database
    Schedule repoSchedule = scheduleRepository.findScheduleByScheduleId(scheduleId);

    //assert that object has correct attributes
    assertNotNull(employee);
    assertEquals(employeeId, employee.getMuseumUserId());
    assertEquals(email, employee.getEmail());
    assertEquals(name, employee.getName());
    assertEquals(password, employee.getPassword());

    assertNotNull(employee.getSchedule());
    assertNotNull(repoSchedule);
    assertEquals(scheduleId, schedule.getScheduleId());
    assertEquals(scheduleId, repoSchedule.getScheduleId());
  }
}