package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;

@ExtendWith(MockitoExtension.class)
public class TestEmployeeService {

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ScheduleRepository scheduleRepository;

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

  private static final long NONE_EXISTING_EMPLOYEE_ID = 3;

  private static final long SCHEDULE_ID = 1;
  private static final long SECOND_SCHEDULE_ID = 2;

  @BeforeEach
  public void setMockOutput() {
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    lenient().when(scheduleRepository.findScheduleByScheduleId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(SCHEDULE_ID)) {
        Schedule schedule = new Schedule();
        schedule.setScheduleId(SCHEDULE_ID);
        return schedule;
      } else {
        return null;
      }
    });

    lenient().when(employeeRepository.findEmployeeByMuseumUserId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
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
    lenient().when(employeeRepository.save(any(Employee.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(employeeRepository.saveAll(any(Iterable.class))).thenAnswer(returnParameterAsAnswer);
  }

  @Test
  public void testGetEmployee() {
    Employee employee = employeeService.getEmployee(EMPLOYEE_ID);
    assertEquals(EMPLOYEE_ID, employee.getMuseumUserId());
    assertEquals(EMPLOYEE_EMAIL, employee.getEmail());
    assertEquals(EMPLOYEE_NAME, employee.getName());
    assertEquals(EMPLOYEE_PASSWORD, employee.getPassword());
    assertEquals(SCHEDULE_ID, employee.getSchedule().getScheduleId());
  }

  @Test
  public void testGetEmployeeNonExisting() {
    assertNull(employeeService.getEmployee(NONE_EXISTING_EMPLOYEE_ID));
  }

  @Test
  public void testGetAllEmployees() {
    assertEquals(2, employeeService.getAllEmployees().size());
  }

  @Test
  public void testDeleteEmployee() {
    try {
      employeeService.deleteEmployee(EMPLOYEE_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

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
