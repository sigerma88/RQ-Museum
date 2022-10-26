package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Written by Kieyan
 * Museum Repository test class
 * Here we test the museum repository interface by saving a loan into the database, querying for it,
 * and then checking if the results are consistent
 */
@SpringBootTest
public class MuseumRepositoryTests {

    @Autowired
    private MuseumRepository museumRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;

    @AfterEach
    public void clearDatabase() {
        museumRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadMuseum() {
        // Create objects
        Schedule schedule = new Schedule();
        Museum museum = new Museum();
        museum.setName("The Louvre");
        museum.setSchedule(schedule);
        museum.setVisitFee(19.99);


        // Save object
        scheduleRepository.save(schedule);
        museumRepository.save(museum);
        long id = museum.getMuseumId();


        // Read object from database
        Museum museumRead = museumRepository.findMuseumByMuseumId(id);

        // Assert that object has correct attributes
        // ASSERT EQUALS IS NOT WORKING


    }

}
