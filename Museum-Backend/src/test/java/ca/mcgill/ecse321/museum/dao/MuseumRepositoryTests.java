package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Kieyan
 * Museum Repository test class
 * Here we test the museum repository interface by saving a loan into the database, querying for it,
 * and then checking if the results are consistent
 */
@SpringBootTest
public class MuseumRepositoryTests {

    @Autowired
    MuseumRepository museumRepository;
    @Autowired
    ScheduleRepository scheduleRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @AfterEach
    public void clearDatabase() {
        museumRepository.deleteAll();
        scheduleRepository.deleteAll();
        employeeRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadMuseum() {

        // Create objects
        Schedule schedule = new Schedule();
        Museum museum = new Museum();
        Employee employee = new Employee();
        employee.setName("Tim");
        employee.setEmail("Tim@gmail.com");
        employee.setPassword("123123");
        museum.setName("The Louvre");
        museum.setVisitFee(19.99);
        employee.setSchedule(schedule);
        schedule.setEmployee(employee);
        schedule.setMuseum(museum);
        museum.setSchedule(schedule);

        // Save object
        employeeRepository.save(employee);
        scheduleRepository.save(schedule);
        museumRepository.save(museum);
        long id = museum.getMuseumId();


        // Read object from database
        Museum museumFromDB = museumRepository.findMuseumByMuseumId(id);

        // Assert that object has correct attributes
        Assert.assertEquals(museum.getName(), museumFromDB.getName()); // test name
        Assert.assertEquals(museum.getMuseumId(), museumFromDB.getMuseumId()); // test id
        Assert.assertEquals(museum.getSchedule().getScheduleId(), museumFromDB.getSchedule().getScheduleId()); // test schedule
        Assert.assertEquals(museum.getSchedule().getEmployee().getMuseumUserId(), museumFromDB.getSchedule().getEmployee().getMuseumUserId()); // test employee linked to schedule
        Assert.assertEquals(museum.getVisitFee(), museumFromDB.getVisitFee(),  0.001); // test visit fee


    }

}
