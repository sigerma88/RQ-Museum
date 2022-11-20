package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test the persistence layer for the ScheduleOfTimePeriodRepository. Testing reading and writing of
 * objects, attributes and references to the database.
 * ScheduleOfTimePeriodRepository is associated with Schedule and TimePeriod.
 *
 * @author Siger
 */
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
    ScheduleOfTimePeriod scheduleOfTimePeriodOne = new ScheduleOfTimePeriod(); //scheduleOne, timePeriodOne
    scheduleOfTimePeriodOne.setSchedule(scheduleOne);
    scheduleOfTimePeriodOne.setTimePeriod(timePeriodOne);

    ScheduleOfTimePeriod scheduleOfTimePeriodTwo = new ScheduleOfTimePeriod(); //scheduleTwo, timePeriodOne
    scheduleOfTimePeriodTwo.setSchedule(scheduleTwo);
    scheduleOfTimePeriodTwo.setTimePeriod(timePeriodOne);

    ScheduleOfTimePeriod scheduleOfTimePeriodThree = new ScheduleOfTimePeriod(); //scheduleOne, timePeriodTwo
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

    //reset
    scheduleOfTimePeriodOne = null;
    scheduleOfTimePeriodTwo = null;
    scheduleOfTimePeriodThree = null;
    scheduleOne = null;
    scheduleTwo = null;
    timePeriodOne = null;
    timePeriodTwo = null;

    //read ScheduleOfTimePeriod from database
    scheduleOfTimePeriodOne = scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleOfTimePeriodId(scheduleOfTimePeriodOneId);
    scheduleOfTimePeriodTwo = scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleOfTimePeriodId(scheduleOfTimePeriodTwoId);
    scheduleOfTimePeriodThree = scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleOfTimePeriodId(scheduleOfTimePeriodThreeId);

    //read Schedule from database
    scheduleOne = scheduleRepository.findScheduleByScheduleId(scheduleOneIdFromFirst);
    scheduleTwo = scheduleRepository.findScheduleByScheduleId(scheduleTwoIdFromSecond);

    //read TimePeriod from database
    timePeriodOne = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodOneIdFromFirst);
    timePeriodTwo = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodTwoIdFromThird);

    //assert that scheduleOfTimePeriod exist in database
    assertNotNull(scheduleOfTimePeriodOne);
    assertNotNull(scheduleOfTimePeriodTwo);
    assertNotNull(scheduleOfTimePeriodThree);

    //assert that schedule exist in database
    assertNotNull(scheduleOne);
    assertNotNull(scheduleTwo);

    //assert that schedule are right
    assertEquals(scheduleOneIdFromFirst, scheduleOne.getScheduleId());
    assertEquals(scheduleOneIdFromThird, scheduleOne.getScheduleId());
    assertEquals(scheduleTwoIdFromSecond, scheduleTwo.getScheduleId());

    //assert that timePeriod exist in database
    assertNotNull(timePeriodOne);
    assertNotNull(timePeriodTwo);

    //assert that timePeriod are right
    assertEquals(timePeriodOne.getStartDate(), startDateOne);
    assertEquals(timePeriodOne.getEndDate(), endDateOne);
    assertEquals(timePeriodTwo.getStartDate(), startDateTwo);
    assertEquals(timePeriodTwo.getEndDate(), endDateTwo);

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
