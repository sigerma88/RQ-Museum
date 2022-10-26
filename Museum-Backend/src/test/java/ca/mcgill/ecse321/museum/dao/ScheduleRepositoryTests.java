package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;  

import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Museum;

@SpringBootTest
public class ScheduleRepositoryTests {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MuseumRepository museumRepository;

    @AfterEach
    public void clearDatabase() {
      if(museumRepository != null) {
        museumRepository.deleteAll();
      } else {
        scheduleRepository.deleteAll();
      }
    }

    @Test
    public void testPersistandLoadSchedule() {
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
    public void testPersistandLoadScheduleForMuseum() {

      //create Schedule
      Schedule schedule = new Schedule();
      
      //save Schedule
      // scheduleRepository.save(schedule);
      // long scheduleId = schedule.getScheduleId();

      //create Museum associated with Schedule
      Museum museum = new Museum();
      museum.setName("Rougon-Macquart");
      museum.setVisitFee(6.99);
      museum.setSchedule(schedule);

      //save Museum
      museumRepository.save(museum);
      long museumId = museum.getMuseumId();
      long scheduleId = museum.getSchedule().getScheduleId();

      //read object from database
      schedule = scheduleRepository.findScheduleByScheduleId(scheduleId);
      museum = museumRepository.findMuseumByMuseumId(museumId);

      //assert that object has correct attributes
      assertNotNull(schedule);
      assertNotNull(museum);
      assertEquals(schedule, museum.getSchedule());
  }
}