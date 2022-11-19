package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;

/**
 * This is the test class for the EmployeeService class
 * 
 * @author Siger
 * @author VZ
 */
@ExtendWith(MockitoExtension.class)
public class TestEmployeeService {
  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ScheduleRepository scheduleRepository;

  @Mock
  private TimePeriodRepository timePeriodRepository;

  @Mock
  private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;

  @InjectMocks
  private EmployeeService employeeService;

  private static final long EMPLOYEE_ID = 1;
  private static final String EMPLOYEE_EMAIL = "emile.zola@assommoir.com";
  private static final String EMPLOYEE_NAME = "Emile Zola";
  private static final String EMPLOYEE_PASSWORD = "Password123";

  private static final long SECOND_EMPLOYEE_ID = 2;
  private static final String SECOND_EMPLOYEE_EMAIL = "victor.hugo@miserables.com";
  private static final String SECOND_EMPLOYEE_NAME = "Victor Hugo";
  private static final String SECOND_EMPLOYEE_PASSWORD = "Password123";

  private static final long NON_EXISTING_EMPLOYEE_ID = 3;

  private static final long SCHEDULE_ID = 1;
  private static final long SECOND_SCHEDULE_ID = 2;

  private static final long TIME_PERIOD_ID = 1;
  private static final long NON_EXISTING_TIME_PERIOD_ID = 2;
  private static final Timestamp STARTDATE = Timestamp.valueOf("2022-10-28 08:30:00.0");
  private static final Timestamp ENDDATE = Timestamp.valueOf("2022-10-28 17:35:00.0");

  private static final long SCHEDULE_OF_TIME_PERIOD_ID = 1;

  /**
   * This method sets up the mock objects
   * There is an employee with an empty schedule and an employee with a non empty
   * schedule.
   * A non empty schedule has a schedule of time period with a time period.
   * 
   * @author Siger
   */
  @BeforeEach
  public void setMockOutput() {
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

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

    lenient().when(timePeriodRepository.findTimePeriodByTimePeriodId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(TIME_PERIOD_ID)) {
            TimePeriod timePeriod = new TimePeriod();
            timePeriod.setTimePeriodId(TIME_PERIOD_ID);
            timePeriod.setStartDate(STARTDATE);
            timePeriod.setEndDate(ENDDATE);
            return timePeriod;
          } else {
            return null;
          }
        });

    lenient().when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleOfTimePeriodId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(SCHEDULE_OF_TIME_PERIOD_ID)) {
            ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
            scheduleOfTimePeriod.setScheduleOfTimePeriodId(SCHEDULE_OF_TIME_PERIOD_ID);
            scheduleOfTimePeriod.setSchedule(scheduleRepository.findScheduleByScheduleId(SECOND_SCHEDULE_ID));
            scheduleOfTimePeriod.setTimePeriod(timePeriodRepository.findTimePeriodByTimePeriodId(TIME_PERIOD_ID));
            return scheduleOfTimePeriod;
          } else {
            return null;
          }
        });

    lenient().when(employeeRepository.findEmployeeByMuseumUserId(EMPLOYEE_ID))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(EMPLOYEE_ID)) {
            Employee employee = new Employee();
            employee.setMuseumUserId(EMPLOYEE_ID);
            employee.setEmail(EMPLOYEE_EMAIL);
            employee.setName(EMPLOYEE_NAME);
            employee.setPassword(EMPLOYEE_PASSWORD);
            employee.setSchedule(scheduleRepository.findScheduleByScheduleId(SCHEDULE_ID));
            return employee;
          } else {
            return null;
          }
        });

    lenient().when(employeeRepository.findEmployeeByMuseumUserId(SECOND_EMPLOYEE_ID))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(SECOND_EMPLOYEE_ID)) {
            Employee employee = new Employee();
            employee.setMuseumUserId(SECOND_EMPLOYEE_ID);
            employee.setEmail(SECOND_EMPLOYEE_EMAIL);
            employee.setName(SECOND_EMPLOYEE_NAME);
            employee.setPassword(SECOND_EMPLOYEE_PASSWORD);
            employee.setSchedule(scheduleRepository.findScheduleByScheduleId(SECOND_SCHEDULE_ID));
            return employee;
          } else {
            return null;
          }
        });

    lenient().when(employeeRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
      Iterable<Employee> employees = new ArrayList<Employee>();

      Employee employee = new Employee();
      employee.setMuseumUserId(EMPLOYEE_ID);
      employee.setEmail(EMPLOYEE_EMAIL);
      employee.setName(EMPLOYEE_NAME);
      employee.setPassword(EMPLOYEE_PASSWORD);
      employee.setSchedule(scheduleRepository.findScheduleByScheduleId(SCHEDULE_ID));
      ((ArrayList<Employee>) employees).add(employee);

      Employee secondEmployee = new Employee();
      secondEmployee.setMuseumUserId(SECOND_EMPLOYEE_ID);
      secondEmployee.setEmail(SECOND_EMPLOYEE_EMAIL);
      secondEmployee.setName(SECOND_EMPLOYEE_NAME);
      secondEmployee.setPassword(SECOND_EMPLOYEE_PASSWORD);
      secondEmployee.setSchedule(scheduleRepository.findScheduleByScheduleId(SECOND_SCHEDULE_ID));
      ((ArrayList<Employee>) employees).add(secondEmployee);

      return employees;
    });

    lenient().when(scheduleRepository.save(any(Schedule.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(timePeriodRepository.save(any(TimePeriod.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(scheduleOfTimePeriodRepository.save(any(ScheduleOfTimePeriod.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(employeeRepository.save(any(Employee.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(employeeRepository.saveAll(any(Iterable.class))).thenAnswer(returnParameterAsAnswer);
  }

  /**
   * Test method for getting an employee by id.
   * 
   * @author Siger
   */
  @Test
  public void testGetEmployee() {
    Employee employee = employeeService.getEmployee(EMPLOYEE_ID);
    assertEquals(EMPLOYEE_ID, employee.getMuseumUserId());
    assertEquals(EMPLOYEE_EMAIL, employee.getEmail());
    assertEquals(EMPLOYEE_NAME, employee.getName());
    assertEquals(EMPLOYEE_PASSWORD, employee.getPassword());
    assertEquals(SCHEDULE_ID, employee.getSchedule().getScheduleId());
  }

  /**
   * Test method for getting an employee by id when the employee does not exist.
   * 
   * @author Siger and VZ
   * 
   */
  @Test
  public void testGetEmployeeNonExisting() {
    assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployee(NON_EXISTING_EMPLOYEE_ID));
  }

  /**
   * Test method for getting an employee by id when id is null.
   * 
   * @author Siger
   */
  @Test
  public void testGetEmployeeNull() {
    String error = null;
    try {
      employeeService.getEmployee(null);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals("Employee id cannot be null", error);
  }

  /**
   * Test method for getting all employees.
   * 
   * @author Siger
   */
  @Test
  public void testGetAllEmployees() {
    assertEquals(2, employeeService.getAllEmployees().size());
  }

  /**
   * Test method for deleting an employee by id when the employee has an empty
   * schedule.
   * 
   * @author Siger
   */
  @Test
  public void testDeleteEmployeeWithEmptySchedule() {
    try {
      employeeService.deleteEmployee(EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  /**
   * Test method for deleting an employee by id when the employee has a non empty
   * schedule.
   * 
   * @author Siger
   */
  @Test
  public void testDeleteEmployeeWithNonEmptySchedule() {
    try {
      employeeService.deleteEmployee(SECOND_EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  /**
   * Test method for deleting an employee by id when the employee does not exist.
   * 
   * @author Siger
   */
  @Test
  public void testDeleteEmployeeNonExisting() {
    String error = null;
    try {
      employeeService.deleteEmployee(NON_EXISTING_EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals("Employee does not exist", error);
  }

  /**
   * Test method for getting an employee's schedule
   * 
   * @author VZ
   */
  @Test
  public void testGetEmployeeSchedule() {
    Schedule schedule = null;
    try {
      schedule = employeeService.getEmployeeSchedule(EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(schedule);
    assertEquals(SCHEDULE_ID, schedule.getScheduleId());

  }

  /**
   * Test method for getting an employee's schedule when the employee does not
   * exist.
   * 
   * @author VZ
   */
  @Test
  public void testGetEmployeeNonExistingSchedule() {
    try {
      employeeService.getEmployeeSchedule(NON_EXISTING_EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      assertEquals("There is no such employee", e.getMessage());
    }
  }

  /**
   * Test method for getting an employee's timeperiods
   * 
   * @author VZ
   */
  @Test
  public void testGetEmployeeTimePeriods() {

    // CREATE TIME PERIODS
    final TimePeriod timePeriod = new TimePeriod();
    final Long id1 = 1L;
    final Timestamp startDate = new Timestamp(0);
    final Timestamp endDate = new Timestamp(1);
    timePeriod.setTimePeriodId(id1);
    timePeriod.setStartDate(startDate);
    timePeriod.setEndDate(endDate);

    final TimePeriod timePeriod2 = new TimePeriod();
    final Long id2 = 2L;
    final Timestamp startDate2 = new Timestamp(2);
    final Timestamp endDate2 = new Timestamp(3);
    timePeriod2.setTimePeriodId(id2);
    timePeriod2.setStartDate(startDate2);
    timePeriod2.setEndDate(endDate2);

    // CREATE SCHEDULE
    final Schedule schedule = new Schedule();
    final Long scheduleId = 1L;
    schedule.setScheduleId(scheduleId);

    // CREATE SCHEDULEOFTIMEPERIODS
    final List<ScheduleOfTimePeriod> scheduleOfTimePeriods = new ArrayList<>();
    final ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
    scheduleOfTimePeriod.setSchedule(schedule);
    scheduleOfTimePeriod.setTimePeriod(timePeriod);
    final ScheduleOfTimePeriod scheduleOfTimePeriod2 = new ScheduleOfTimePeriod();
    scheduleOfTimePeriod2.setSchedule(schedule);
    scheduleOfTimePeriod2.setTimePeriod(timePeriod2);
    scheduleOfTimePeriods.add(scheduleOfTimePeriod);
    scheduleOfTimePeriods.add(scheduleOfTimePeriod2);

    // CREATE EMPLOYEE WITH SCHEDULE
    final Employee employee = new Employee();
    final Long employeeId = 1L;
    final String employeeEmail = "email";
    final String employeeName = "Employee";
    final String employeePassword = "password";
    employee.setMuseumUserId(employeeId);
    employee.setEmail(employeeEmail);
    employee.setName(employeeName);
    employee.setPassword(employeePassword);
    employee.setSchedule(schedule);

    List<TimePeriod> testTimePeriods = null;

    when(employeeRepository.findEmployeeByMuseumUserId(employeeId))
        .thenAnswer((InvocationOnMock invocation) -> employee);
    when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodBySchedule(schedule))
        .thenAnswer((InvocationOnMock invocation) -> scheduleOfTimePeriods);

    try {
      testTimePeriods = employeeService.getEmployeeTimePeriods(employeeId);
    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(testTimePeriods);
    assertEquals(2, testTimePeriods.size());

  }

  /**
   * Test method for getting an employee's timeperiods when the employee does not
   * exist.
   * 
   * @author VZ
   */
  @Test
  public void testGetEmployeeNonExistingTimePeriods() {
    try {
      employeeService.getEmployeeTimePeriods(NON_EXISTING_EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      assertEquals("There is no such employee", e.getMessage());
    }
  }

  /**
   * Test method for getting an employee's timeperiods when employee's schedule
   * has no shift
   * 
   * @author VZ
   */
  @Test
  public void testGetEmployeeTimePeriodsWithoutShifts() {
    // CREATE SCHEDULE
    final Schedule schedule = new Schedule();
    final Long scheduleId = 1L;
    schedule.setScheduleId(scheduleId);

    // CREATE EMPLOYEE
    final Employee employee = new Employee();
    final Long employeeId = 1L;
    employee.setMuseumUserId(employeeId);
    employee.setSchedule(schedule);

    List<TimePeriod> timePeriods = new ArrayList<>();
    when(employeeRepository.findEmployeeByMuseumUserId(employeeId))
        .thenAnswer((InvocationOnMock invocation) -> employee);
    when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodBySchedule(schedule))
        .thenAnswer((InvocationOnMock invocation) -> null);

    try {
      timePeriods = employeeService.getEmployeeTimePeriods(employeeId);
    } catch (IllegalArgumentException e) {

    }
    assertNull(timePeriods);
  }

  /**
   * Test for adding an existing timeperiod to an employee's schedule
   * 
   * @author VZ
   */
  @Test
  public void testAddEmployeeTimePeriodAssociation() {

    // CREATE EMPLOYEE WITH SCHEDULE
    final Employee employee = new Employee();
    final Schedule schedule = new Schedule();
    employee.setMuseumUserId(EMPLOYEE_ID);
    employee.setSchedule(schedule);
    employee.setEmail(EMPLOYEE_EMAIL);
    employee.setName(EMPLOYEE_NAME);
    employee.setPassword(EMPLOYEE_PASSWORD);

    // CREATE TIMEPERIOD WE WANT TO ADD
    final TimePeriod timePeriod = new TimePeriod();
    timePeriod.setTimePeriodId(TIME_PERIOD_ID);
    timePeriod.setStartDate(STARTDATE);
    timePeriod.setEndDate(ENDDATE);

    // ADD SHIFT TO EMPLOYEE SCHEDULE
    final ScheduleOfTimePeriod sotp = new ScheduleOfTimePeriod();
    sotp.setSchedule(schedule);
    sotp.setTimePeriod(timePeriod);

    Employee testEmployee = null;

    when(employeeRepository.findEmployeeByMuseumUserId(EMPLOYEE_ID))
        .thenAnswer((InvocationOnMock invocation) -> employee);
    when(timePeriodRepository.findTimePeriodByTimePeriodId(TIME_PERIOD_ID))
        .thenAnswer((InvocationOnMock invocation) -> timePeriod);
    // test that we get back a museum
    testEmployee = employeeService.addEmployeeTimePeriodAssociation(EMPLOYEE_ID, TIME_PERIOD_ID);
    assertNotNull(testEmployee);
    // test that service actually saved museum
    verify(employeeRepository, times(1)).save(employee);

  }

  /**
   * Test for adding a an existing timeperiod to a non existing employee's
   * schedule
   * 
   * @author VZ
   */
  @Test
  public void testAddEmployeeTimePeriodAssociationInvalidEmployeeId() {
    try {
      employeeService.addEmployeeTimePeriodAssociation(NON_EXISTING_EMPLOYEE_ID, TIME_PERIOD_ID);
    } catch (IllegalArgumentException e) {
      assertEquals("There is no such employee", e.getMessage());
    }
  }

  /**
   * Test for adding a non existing timeperiod to an employee's schedule
   * 
   * @author VZ
   */
  @Test
  public void testAddEmployeeTimePeriodAssociationInvalidTimePeriod() {
    try {
      employeeService.addEmployeeTimePeriodAssociation(EMPLOYEE_ID, NON_EXISTING_TIME_PERIOD_ID);
    } catch (IllegalArgumentException e) {
      assertEquals("There is no such time period", e.getMessage());
    }
  }

  /**
   * Test for removing an existing timeperiod from an employee's schedule
   * 
   * @author VZ
   */
  @Test
  public void testDeleteEmployeeTimePeriodAssociation() {

    // CREATE EMPLOYEE WITH SCHEDULE
    final Employee employee = new Employee();
    final Schedule schedule = new Schedule();
    employee.setMuseumUserId(EMPLOYEE_ID);
    employee.setSchedule(schedule);
    employee.setEmail(EMPLOYEE_EMAIL);
    employee.setName(EMPLOYEE_NAME);
    employee.setPassword(EMPLOYEE_PASSWORD);

    // CREATE TIMEPERIOD WE WANT TO ADD
    final TimePeriod timePeriod = new TimePeriod();
    timePeriod.setTimePeriodId(TIME_PERIOD_ID);
    timePeriod.setStartDate(STARTDATE);
    timePeriod.setEndDate(ENDDATE);

    // ADD SHIFT TO EMPLOYEE SCHEDULE
    final ScheduleOfTimePeriod sotp = new ScheduleOfTimePeriod();
    sotp.setSchedule(schedule);
    sotp.setTimePeriod(timePeriod);

    Employee testEmployee = null;

    when(employeeRepository.findEmployeeByMuseumUserId(EMPLOYEE_ID))
        .thenAnswer((InvocationOnMock invocation) -> employee);
    when(timePeriodRepository.findTimePeriodByTimePeriodId(TIME_PERIOD_ID))
        .thenAnswer((InvocationOnMock invocation) -> timePeriod);
    when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, timePeriod))
        .thenAnswer((InvocationOnMock invocation) -> sotp);
    // test that we get back an employee
    testEmployee = employeeService.deleteEmployeeTimePeriodAssociation(EMPLOYEE_ID, TIME_PERIOD_ID);
    assertNotNull(testEmployee);
    // test that service actually saved employee
    verify(employeeRepository, times(1)).save(employee);

  }

  /**
   * Test for removing a timeperiod from a non existing employee's schedule
   * 
   * @author VZ
   */
  @Test
  public void testDeleteEmployeeTimePeriodAssociationInvalidEmployeeId() {
    try {
      employeeService.deleteEmployeeTimePeriodAssociation(NON_EXISTING_EMPLOYEE_ID, TIME_PERIOD_ID);
    } catch (IllegalArgumentException e) {
      assertEquals("There is no such employee", e.getMessage());
    }
  }

  /**
   * Test for removing an existing timeperiod from an employee's schedule that
   * doesn't
   * contain the mentioned timeperiod
   * 
   * @author VZ
   */
  @Test
  public void testDeleteEmployeeTimePeriodAssociationWithoutTimePeriod() {
    // CREATE EMPLOYEE WITH SCHEDULE
    final Employee employee = new Employee();
    final Schedule schedule = new Schedule();
    employee.setMuseumUserId(EMPLOYEE_ID);
    employee.setSchedule(schedule);
    employee.setEmail(EMPLOYEE_EMAIL);
    employee.setName(EMPLOYEE_NAME);
    employee.setPassword(EMPLOYEE_PASSWORD);

    // CREATE TIMEPERIOD WE WANT TO ADD
    final TimePeriod timePeriod = new TimePeriod();
    timePeriod.setTimePeriodId(TIME_PERIOD_ID);
    timePeriod.setStartDate(STARTDATE);
    timePeriod.setEndDate(ENDDATE);

    when(employeeRepository.findEmployeeByMuseumUserId(EMPLOYEE_ID))
        .thenAnswer((InvocationOnMock invocation) -> employee);
    when(timePeriodRepository.findTimePeriodByTimePeriodId(TIME_PERIOD_ID))
        .thenAnswer((InvocationOnMock invocation) -> timePeriod);
    when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, timePeriod))
        .thenAnswer((InvocationOnMock invocation) -> null);

    try {
      employeeService.deleteEmployeeTimePeriodAssociation(EMPLOYEE_ID, TIME_PERIOD_ID);
    } catch (IllegalArgumentException e) {
      assertEquals("Time period does not exist in employee's schedule", e.getMessage());
    }
  }

  /**
   * Test for removing a non existing timeperiod from an employee's schedule
   * 
   * @author VZ
   */
  @Test
  public void testDeleteEmployeeTimePeriodAssociationInvalidTimePeriodId() {
    try {
      employeeService.deleteEmployeeTimePeriodAssociation(EMPLOYEE_ID, NON_EXISTING_TIME_PERIOD_ID);
    } catch (IllegalArgumentException e) {
      assertEquals("There is no such time period", e.getMessage());
    }
  }
}