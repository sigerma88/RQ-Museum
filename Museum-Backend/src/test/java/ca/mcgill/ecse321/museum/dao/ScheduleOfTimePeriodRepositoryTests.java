package ca.mcgill.ecse321.museum.dao;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;  

import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;

@SpringBootTest
public class ScheduleOfTimePeriodRepositoryTests {
  @Autowired
  private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @AfterEach
  public void clearDatabase() {
    scheduleOfTimePeriodRepository.deleteAll();
    scheduleRepository.deleteAll();
    timePeriodRepository.deleteAll();
  }

  @Test
  public void testPersistAndLoadScheduleOfTimePeriod() {
    //create 2 Schedule
    Schedule scheduleOne = new Schedule();
    Schedule scheduleTwo = new Schedule();

    //save 2 Schedule
    scheduleOne = scheduleRepository.save(scheduleOne);
    scheduleTwo = scheduleRepository.save(scheduleTwo);

    //create 2 TimePeriod
    Timestamp startDateOne = Timestamp.valueOf("2022-10-28 08:30:00.0");
    Timestamp endDateOne = Timestamp.valueOf("2022-10-28 17:35:00.0");
    TimePeriod timePeriodOne = new TimePeriod();
    timePeriodOne.setStartDate(startDateOne);
    timePeriodOne.setEndDate(endDateOne);

    Timestamp startDateTwo = Timestamp.valueOf("2022-10-29 08:30:00.0");
    Timestamp endDateTwo = Timestamp.valueOf("2022-10-29 17:35:00.0");
    TimePeriod timePeriodTwo = new TimePeriod();
    timePeriodTwo.setStartDate(startDateTwo);
    timePeriodTwo.setEndDate(endDateTwo);

    //save 2 TimePeriod
    timePeriodOne = timePeriodRepository.save(timePeriodOne);
    timePeriodTwo = timePeriodRepository.save(timePeriodTwo);
    
    //create 3 ScheduleOfTimePeriod
    ScheduleOfTimePeriod scheduleOfTimePeriodOne = new ScheduleOfTimePeriod();
    scheduleOfTimePeriodOne.setSchedule(scheduleOne);
    scheduleOfTimePeriodOne.setTimePeriod(timePeriodOne);

    ScheduleOfTimePeriod scheduleOfTimePeriodTwo = new ScheduleOfTimePeriod();
    scheduleOfTimePeriodTwo.setSchedule(scheduleTwo);
    scheduleOfTimePeriodTwo.setTimePeriod(timePeriodOne);

    ScheduleOfTimePeriod scheduleOfTimePeriodThree = new ScheduleOfTimePeriod();
    scheduleOfTimePeriodThree.setSchedule(scheduleOne);
    scheduleOfTimePeriodThree.setTimePeriod(timePeriodTwo);

    //save first ScheduleOfTimePeriod
    scheduleOfTimePeriodRepository.save(scheduleOfTimePeriodOne);
    long scheduleOfTimePeriodOneId = scheduleOfTimePeriodOne.getScheduleOfTimePeriodId();
    long scheduleOneIdFromFirst = scheduleOfTimePeriodOne.getSchedule().getScheduleId();
    long timePeriodOneIdFromFirst = scheduleOfTimePeriodOne.getTimePeriod().getTimePeriodId();    
    
    //save second ScheduleOfTimePeriod
    scheduleOfTimePeriodRepository.save(scheduleOfTimePeriodTwo);
    long scheduleOfTimePeriodTwoId = scheduleOfTimePeriodTwo.getScheduleOfTimePeriodId();
    long scheduleTwoIdFromSecond = scheduleOfTimePeriodTwo.getSchedule().getScheduleId();
    long timePeriodOneIdFromSecond = scheduleOfTimePeriodTwo.getTimePeriod().getTimePeriodId();
    
    //save third ScheduleOfTimePeriod
    scheduleOfTimePeriodRepository.save(scheduleOfTimePeriodThree);
    long scheduleOfTimePeriodThreeId = scheduleOfTimePeriodThree.getScheduleOfTimePeriodId();
    long scheduleOneIdFromThird = scheduleOfTimePeriodThree.getSchedule().getScheduleId();
    long timePeriodTwoIdFromThird = scheduleOfTimePeriodThree.getTimePeriod().getTimePeriodId();

    //read ScheduleOfTimePeriod from database
    scheduleOfTimePeriodOne = scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleOfTimePeriodId(scheduleOfTimePeriodOneId);
    scheduleOfTimePeriodTwo = scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleOfTimePeriodId(scheduleOfTimePeriodTwoId);

    //read Schedule from database
    scheduleOne = scheduleRepository.findScheduleByScheduleId(scheduleOneIdFromFirst);
    scheduleTwo = scheduleRepository.findScheduleByScheduleId(scheduleTwoIdFromSecond);

    //read TimePeriod from database
    timePeriodOne = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodOneIdFromFirst);
    timePeriodTwo = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodTwoIdFromThird);

    //assert that scheduleOfTimePeriod are not null
    assertNotNull(scheduleOfTimePeriodOne);
    assertNotNull(scheduleOfTimePeriodTwo);
    assertNotNull(scheduleOfTimePeriodThree);

    //assert that schedule are not null
    assertNotNull(scheduleOne);
    assertNotNull(scheduleTwo);

    //assert that schedule are right
    assertEquals(scheduleOneIdFromFirst, scheduleOne.getScheduleId());
    assertEquals(scheduleOneIdFromThird, scheduleOne.getScheduleId());
    assertEquals(scheduleTwoIdFromSecond, scheduleTwo.getScheduleId());

    //assert that timePeriod are not null
    assertNotNull(timePeriodOne);
    assertNotNull(timePeriodTwo);

    //assert that timePeriod are right
    assertEquals(timePeriodOneIdFromFirst, timePeriodOne.getTimePeriodId());
    assertEquals(timePeriodOneIdFromSecond, timePeriodOne.getTimePeriodId());
    assertEquals(timePeriodTwoIdFromThird, timePeriodTwo.getTimePeriodId());

    //assert that ScheduleOfTimePeriodOne has correct associations
    assertEquals(scheduleOneIdFromFirst, scheduleOfTimePeriodOne.getSchedule().getScheduleId());
    assertEquals(timePeriodOneIdFromFirst, scheduleOfTimePeriodOne.getTimePeriod().getTimePeriodId());

    //assert that ScheduleOfTimePeriodTwo has correct associations
    assertEquals(scheduleTwoIdFromSecond, scheduleOfTimePeriodTwo.getSchedule().getScheduleId());
    assertEquals(timePeriodOneIdFromSecond, scheduleOfTimePeriodTwo.getTimePeriod().getTimePeriodId());

    //assert that ScheduleOfTimePeriodThree has correct associations
    assertEquals(scheduleOneIdFromThird, scheduleOfTimePeriodThree.getSchedule().getScheduleId());
    assertEquals(timePeriodTwoIdFromThird, scheduleOfTimePeriodThree.getTimePeriod().getTimePeriodId());
  }
}
