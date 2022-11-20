package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestMuseumService {

  @Mock
  private MuseumRepository museumRepository;
  @Mock
  private ScheduleRepository scheduleRepository;
  @Mock
  private TimePeriodRepository timePeriodRepository;
  @Mock
  private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;

  @InjectMocks
  private MuseumService museumService;

  private static final long MUSEUM_ID = 1;
  private static final String MUSEUM_NAME = "RQ museum";
  private static final double MUSEUM_VISITFEE = 10.0;

  private static final long NON_EXISTING_MUSEUM_ID = 2;

  private static final long SCHEDULE_ID = 1;
  private static final long SECOND_SCHEDULE_ID = 2;

  private static final long TIMEPERIOD_ID = 1;
  private static final Timestamp STARTDATE = Timestamp.valueOf("2022-01-01 08:30:00.0");
  private static final Timestamp ENDDATE = Timestamp.valueOf("2022-01-01 17:30:00.0");

  private static final long SCHEDULE_OF_TIME_PERIOD_ID = 1;

  /**
   * This method is called before each test and is used to set up the mock objects
   *
   * @author VZ
   */

  @BeforeEach
  public void setMockOutput() {
    lenient().when(museumRepository.findMuseumByMuseumId(MUSEUM_ID)).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(MUSEUM_ID)) {
        Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);
        museum.setSchedule(scheduleRepository.findScheduleByScheduleId(SCHEDULE_ID));
        return museum;
      } else {
        return null;
      }
    });

    lenient().when(scheduleRepository.findScheduleByScheduleId(SCHEDULE_ID))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(SCHEDULE_ID)) {
            Schedule schedule = new Schedule();
            schedule.setScheduleId(SCHEDULE_ID);
            return schedule;
          } else {
            return null;
          }
        });

    lenient().when(scheduleRepository.findScheduleByScheduleId(SECOND_SCHEDULE_ID))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(SECOND_SCHEDULE_ID)) {
            Schedule schedule = new Schedule();
            schedule.setScheduleId(SECOND_SCHEDULE_ID);
            return schedule;
          } else {
            return null;
          }
        });

    lenient().when(timePeriodRepository.findTimePeriodByTimePeriodId(TIMEPERIOD_ID))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(TIMEPERIOD_ID)) {
            TimePeriod timePeriod = new TimePeriod();
            timePeriod.setTimePeriodId(TIMEPERIOD_ID);
            timePeriod.setStartDate(STARTDATE);
            timePeriod.setEndDate(ENDDATE);
            return timePeriod;
          } else {
            return null;
          }
        });

    lenient()
        .when(scheduleOfTimePeriodRepository
            .findScheduleOfTimePeriodByScheduleOfTimePeriodId(SCHEDULE_OF_TIME_PERIOD_ID))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(SCHEDULE_OF_TIME_PERIOD_ID)) {
            ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
            scheduleOfTimePeriod.setScheduleOfTimePeriodId(SCHEDULE_OF_TIME_PERIOD_ID);
            scheduleOfTimePeriod.setSchedule(scheduleRepository.findScheduleByScheduleId(SCHEDULE_ID));
            scheduleOfTimePeriod
                .setTimePeriod(timePeriodRepository.findTimePeriodByTimePeriodId(TIMEPERIOD_ID));
            return scheduleOfTimePeriod;
          } else {
            return null;
          }
        });

    // whenever anything is saved, just return the parameter object
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    lenient().when(museumRepository.save(any(Museum.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(scheduleRepository.save(any(Schedule.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(scheduleOfTimePeriodRepository.save(any(ScheduleOfTimePeriod.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(timePeriodRepository.save(any(TimePeriod.class))).thenAnswer(returnParameterAsAnswer);
  }

  /**
   * This method is used to test the creation of a museum
   */
  @Test
  public void testCreateMuseum() {
    assertEquals(0, museumService.getAllMuseums().size());
    String name = "The Louvre";
    double visitFee = 10.0;
    Museum museum = null;
    Schedule schedule = new Schedule();
    try {
      museum = museumService.createMuseum(name, visitFee, schedule);
    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(museum);
    assertEquals(name, museum.getName());
    assertEquals(visitFee, museum.getVisitFee());
    assertEquals(schedule, museum.getSchedule());
  }

  /**
   * This test checks if the museum service throws an exception when the museum
   * name is null
   */
  @Test
  public void testCreateMuseumNullName() {
    assertEquals(0, museumService.getAllMuseums().size());
    String name = null;
    double visitFee = 10.0;
    Schedule schedule = new Schedule();
    Museum museum = null;
    String error = null;
    try {
      museum = museumService.createMuseum(name, visitFee, schedule);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(museum);
    assertEquals("Name cannot be empty!", error);
    assertEquals(0, museumService.getAllMuseums().size());
  }

  /**
   * test for creating a museum with an empty name
   */
  @Test
  public void testCreateMuseumEmptyName() {
    assertEquals(0, museumService.getAllMuseums().size());
    String name = "";
    double visitFee = 10.0;
    Schedule schedule = new Schedule();
    Museum museum = null;
    String error = null;
    try {
      museum = museumService.createMuseum(name, visitFee, schedule);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(museum);
    assertEquals("Name cannot be empty!", error);
    assertEquals(0, museumService.getAllMuseums().size());
  }

  /**
   * Test for creating a museum with name [space]
   */
  @Test
  public void testCreateMuseumSpacesName() {
    assertEquals(0, museumService.getAllMuseums().size());
    String name = " ";
    double visitFee = 10.0;
    Schedule schedule = new Schedule();
    Museum museum = null;
    String error = null;
    try {
      museum = museumService.createMuseum(name, visitFee, schedule);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(museum);
    assertEquals("Name cannot be empty!", error);
    assertEquals(0, museumService.getAllMuseums().size());
  }

  /**
   * test method for creating a museum that doesn't exist.
   */
  @Test
  public void testCreateNonExistingMuseum() {
    assertNull(museumService.getMuseum(NON_EXISTING_MUSEUM_ID));
  }

  /**
   * test for creating a museum with a negative visit fee
   */
  @Test
  public void testCreateMuseumWithNegativeVisitFee() {
    String name = "The Louvre";
    double visitFee = -10.0;
    Schedule schedule = new Schedule();
    Museum museum = null;
    String error = null;
    try {

      museum = museumService.createMuseum(name, visitFee, schedule);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(museum);
    assertEquals("Visit fee cannot be negative!", error);
  }

  /**
   * test for creating a museum with no schedule
   */
  @Test
  public void testCreateMuseumWithNoSchedule() {
    String name = "The Louvre";
    double visitFee = 10.0;
    Schedule schedule = null;
    Museum museum = null;
    String error = null;
    try {
      museum = museumService.createMuseum(name, visitFee, schedule);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(museum);
    assertEquals("Schedule cannot be null!", error);
  }

  /**
   * test for editing a museum's attributes
   */
  @Test
  public void testEditMuseum() {
    Schedule schedule = new Schedule();
    Museum museum = null;
    String name = "L";
    double visitFee = 20.0;
    Schedule new_schedule = new Schedule();
    try {
      museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
      museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);
    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(museum);
    assertEquals(name, museum.getName());
    assertEquals(visitFee, museum.getVisitFee());
    assertEquals(new_schedule, museum.getSchedule());

  }

  @Test
  public void testEditMuseumNullName() {
    Schedule schedule = new Schedule();
    Museum museum = null;
    String name = null;
    double visitFee = 20.0;
    Schedule new_schedule = new Schedule();
    String error = null;

    try {
      museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
      museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);

    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNotNull(museum);
    assertEquals("Name cannot be empty!", error);
  }
}
