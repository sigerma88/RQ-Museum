package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

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

  private static final Long EMPLOYEE_ID = 0L;
  private static final String EMPLOYEE_EMAIL = "emile.zola@assommoir.com";
  private static final String EMPLOYEE_NAME = "Emile Zola";
  private static final String EMPLOYEE_PASSWORD = "Password123";

  private static final Long SECOND_EMPLOYEE_ID = 1L;
  private static final String SECOND_EMPLOYEE_EMAIL = "victor.hugo@miserables.com";
  private static final String SECOND_EMPLOYEE_NAME = "Victor Hugo";
  private static final String SECOND_EMPLOYEE_PASSWORD = "Password123";

  private static final Long NONE_EXISTING_EMPLOYEE_ID = 2L;

  private static final Long SCHEDULE_ID = 0L;
  private static final Long SECOND_SCHEDULE_ID = 1L;

  private static final Long TIME_PERIOD_ID = 0L;
  private static final Timestamp STARTDATE = Timestamp.valueOf("2022-10-28 08:30:00.0");
  private static final Timestamp ENDDATE = Timestamp.valueOf("2022-10-28 17:35:00.0");

  private static final Long SCHEDULE_OF_TIME_PERIOD_ID = 0L;

  /**
   * This method sets up the mock objects There is an employee with an empty schedule and an
   * employee with a non empty schedule. A non empty schedule has a schedule of time period with a
   * time period.
   * 
   * @author Siger
   */
  @BeforeEach
  public void setMockOutput() {
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    lenient().when(scheduleRepository.findScheduleByScheduleId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(SCHEDULE_ID)) {
            Schedule schedule = new Schedule();
            schedule.setScheduleId(SCHEDULE_ID);
            return schedule;
          } else if (invocation.getArgument(0).equals(SECOND_SCHEDULE_ID)) {
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

    lenient()
        .when(scheduleOfTimePeriodRepository
            .findScheduleOfTimePeriodByScheduleOfTimePeriodId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(SCHEDULE_OF_TIME_PERIOD_ID)) {
            ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
            scheduleOfTimePeriod.setScheduleOfTimePeriodId(SCHEDULE_OF_TIME_PERIOD_ID);
            scheduleOfTimePeriod
                .setSchedule(scheduleRepository.findScheduleByScheduleId(SECOND_SCHEDULE_ID));
            scheduleOfTimePeriod
                .setTimePeriod(timePeriodRepository.findTimePeriodByTimePeriodId(TIME_PERIOD_ID));
            return scheduleOfTimePeriod;
          } else {
            return null;
          }
        });

    lenient().when(employeeRepository.findEmployeeByMuseumUserId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(EMPLOYEE_ID)) {
            Employee employee = new Employee();
            employee.setMuseumUserId(EMPLOYEE_ID);
            employee.setEmail(EMPLOYEE_EMAIL);
            employee.setName(EMPLOYEE_NAME);
            employee.setPassword(EMPLOYEE_PASSWORD);
            employee.setSchedule(scheduleRepository.findScheduleByScheduleId(SCHEDULE_ID));
            return employee;
          } else if (invocation.getArgument(0).equals(SECOND_EMPLOYEE_ID)) {
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

    lenient().when(scheduleRepository.save(any(Schedule.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(timePeriodRepository.save(any(TimePeriod.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(scheduleOfTimePeriodRepository.save(any(ScheduleOfTimePeriod.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(employeeRepository.save(any(Employee.class)))
        .thenAnswer(returnParameterAsAnswer);
    lenient().when(employeeRepository.saveAll(any(Iterable.class)))
        .thenAnswer(returnParameterAsAnswer);
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
   * @author Siger
   */
  @Test
  public void testGetEmployeeNonExisting() {
    assertNull(employeeService.getEmployee(NONE_EXISTING_EMPLOYEE_ID));
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
      employeeService.deleteEmployee(NONE_EXISTING_EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals("Employee does not exist", error);
  }
}