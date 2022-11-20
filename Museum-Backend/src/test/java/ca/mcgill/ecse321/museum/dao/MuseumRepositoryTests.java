package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Museum Repository test class
 * Here we test the museum repository interface by saving a museum into the database, querying for it,
 * and then checking if the results are consistent
 *
 * @author Kieyan
 */
@SpringBootTest
public class MuseumRepositoryTests {
  @Autowired
  MuseumRepository museumRepository;

  @Autowired
  ScheduleRepository scheduleRepository;

  @AfterEach
  public void clearDatabase() {
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();
  }

  @Test
  public void testPersistAndLoadMuseum() {
    // Create objects
    String name = "The Louvre";
    double visitFee = 19.99;
    Schedule schedule = new Schedule();
    Museum museum = new Museum();
    museum.setName(name);
    museum.setVisitFee(visitFee);
    museum.setSchedule(schedule);

    // Save object
    museumRepository.save(museum);
    long id = museum.getMuseumId();

    // Read object from database
    Museum museumFromDB = museumRepository.findMuseumByMuseumId(id);

    // Assert that museumFromDB is not NULL
    assertNotNull(museumFromDB);

    // Assert that object has correct attributes
    assertEquals(name, museumFromDB.getName()); // test name
    assertEquals(museum.getMuseumId(), museumFromDB.getMuseumId()); // test id
    assertEquals(schedule.getScheduleId(), museumFromDB.getSchedule().getScheduleId()); // test schedule
    assertEquals(visitFee, museumFromDB.getVisitFee()); // test visit fee
  }
}
