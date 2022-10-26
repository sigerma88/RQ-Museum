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
import ca.mcgill.ecse321.museum.model.Employee;

@SpringBootTest
public class ScheduleRepositoryTests {
    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MuseumRepository museumRepository;

    

    // @AfterEach
    // public void clearDatabase() {
    //   if(museumRepository != null) {
    //     museumRepository.deleteAll();
    //   } else {
    //     scheduleRepository.deleteAll();
    //   }
    // }

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
        System.out.println("Schedule ID memory object initially: " + schedule.getScheduleId());

        //create Museum associated with Schedule
        String name = "Rougon-Macquart";
        double visitFee = 6.99;
        Museum museum = new Museum();
        museum.setName(name);
        museum.setVisitFee(visitFee);
        museum.setSchedule(schedule);
        System.out.println("Museum ID memory object initially: " + museum.getMuseumId());

        //save Museum
        museum = museumRepository.save(museum);
        long museumId = museum.getMuseumId();
        long scheduleId = museum.getSchedule().getScheduleId();
        System.out.println("Schedule ID memory object after save: " + schedule.getScheduleId());
        System.out.println("Museum ID memory object after save: " + museum.getMuseumId());

        //read Museum from database
        museum = museumRepository.findMuseumByMuseumId(museumId);
        System.out.println("Museum ID databse object after load: " + museum.getMuseumId());
        System.out.println("Schedule ID database object after load: " + museum.getSchedule().getScheduleId());

        //read Schedule from database
        Schedule repoSchedule = scheduleRepository.findScheduleByScheduleId(scheduleId);
        System.out.println("Schedule ID database object after load: " + repoSchedule.getScheduleId());
        System.out.println("Schedule ID memory object after load: " + schedule.getScheduleId());

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
}