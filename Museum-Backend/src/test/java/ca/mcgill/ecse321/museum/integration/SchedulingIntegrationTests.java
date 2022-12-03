package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dao.*;
import ca.mcgill.ecse321.museum.dto.*;
import ca.mcgill.ecse321.museum.integration.utilities.UserUtilities;
import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Timestamp;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchedulingIntegrationTests {

  private static final String FIRST_VALID_MANAGER_NAME = "admin";
  private static final String FIRST_VALID_MANAGER_EMAIL = "admin@mail.ca";

  private static final String VALID_PASSWORD = "#BrazilGp2022";

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private MuseumRepository museumRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private VisitorRepository visitorRepository;

  @Autowired
  private ManagerRepository managerRepository;

  @BeforeEach
  @AfterEach
  public void clearDatabase() {
    scheduleOfTimePeriodRepository.deleteAll();
    timePeriodRepository.deleteAll();
    employeeRepository.deleteAll();
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();
    visitorRepository.deleteAll();
    managerRepository.deleteAll();
  }

  /**
   * Test to successfully get an employee's schedule
   *
   * @author VZ
   */
  @Test
  public void testGetScheduleByEmployee() {
    Employee employeeShifts = createEmployeeWithShifts();
    EmployeeDto employeeDto = DtoUtility.convertToDto(employeeShifts);

    HttpEntity<?> entity = new HttpEntity<>(loginSetupEmployee(employeeShifts));

    Long id = employeeDto.getMuseumUserId();
    ScheduleDto scheduleDto = employeeDto.getSchedule();
    Long scheduleId = scheduleDto.getScheduleId();
    ResponseEntity<ScheduleDto> response = client.exchange(
        "/api/scheduling/employee/schedule/" + id, HttpMethod.GET, entity, ScheduleDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(scheduleId, response.getBody().getScheduleId(),
        "Response has correct schedule ID");

  }

  /**
   * Test to get a schedule by an employee that doesn't exist
   *
   * @author VZ
   */
  @Test
  public void testGetScheduleByEmployeeInvalidId() {
    Employee employeeShifts = createEmployeeWithShifts();

    HttpEntity<?> entity = new HttpEntity<>(loginSetupEmployee(employeeShifts));

    ResponseEntity<String> response = client.exchange("/api/scheduling/employee/schedule/" + -1,
        HttpMethod.GET, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("You are not authorized to view this schedule"));
  }

  /**
   * Test to successfully get the shifts of an employee
   *
   * @author VZ
   */
  @Test
  public void testGetAllShiftsByEmployee() {
    EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
    Long id = employeeDto.getMuseumUserId();

    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());

    ResponseEntity<TimePeriodDto[]> response = client.exchange(
        "/api/scheduling/employee/shifts/" + id, HttpMethod.GET, entity, TimePeriodDto[].class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(2, response.getBody().length, "Response has correct number of shifts");
  }

  /**
   * Test to get the shifts of an employee that doesn't exist
   *
   * @author VZ
   */
  @Test
  public void testGetAllShiftsByEmployeeInvalidId() {

    Employee employeeShifts = createEmployeeWithShifts();
    EmployeeDto employeeDto = DtoUtility.convertToDto(employeeShifts);

    HttpEntity<?> entity = new HttpEntity<>(loginSetupEmployee(employeeShifts));

    ResponseEntity<String> response = client.exchange("/api/scheduling/employee/shifts/" + -1,
        HttpMethod.GET, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("You are not authorized to view this schedule"));
  }

  /**
   * Test to get the shifts of an employee that doesn't have any shifts
   *
   * @author VZ
   */
  @Test
  public void testGetAllShiftsByEmployeeNoShift() {
    EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithoutShifts());
    Long id = employeeDto.getMuseumUserId();

    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());

    ResponseEntity<String> response = client.exchange("/api/scheduling/employee/shifts/" + id,
        HttpMethod.GET, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("[]", response.getBody(), "Response has correct number of shifts");
  }

  /**
   * Test to successfully get the schedule of a museum
   *
   * @author VZ
   */
  @Test
  public void testGetScheduleByMuseum() {
    MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
    Long id = museumDto.getMuseumId();
    ScheduleDto scheduleDto = museumDto.getSchedule();
    Long scheduleId = scheduleDto.getScheduleId();
    ResponseEntity<ScheduleDto> response = client.getForEntity("/api/scheduling/museum/schedule/" + id,
        ScheduleDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(scheduleId, response.getBody().getScheduleId(),
        "Response has correct schedule ID");
  }

  /**
   * Test to get the schedule of a museum that doesn't exist
   *
   * @author VZ
   */
  @Test
  public void testGetScheduleByMuseumInvalidId() {
    ResponseEntity<String> response = client.getForEntity("/api/scheduling/museum/schedule/" + -1, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Museum doesn't exist"));
  }

  /**
   * Test to successfully get the shifts of a museum
   *
   * @author VZ
   */
  @Test
  public void testGetAllShiftsByMuseum() {
    MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
    Long id = museumDto.getMuseumId();

    ManagerDto manager = createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", manager.getSessionId());
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<TimePeriodDto[]> response = client.exchange(
        "/api/scheduling/museum/shifts/" + id, HttpMethod.GET, entity, TimePeriodDto[].class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(2, response.getBody().length, "Response has correct number of shifts");
  }

  /**
   * Test to get the shifts of a museum that doesn't exist
   *
   * @author VZ
   */
  @Test
  public void testGetAllShiftsByMuseumInvalidId() {

    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());

    ResponseEntity<String> response = client.exchange("/api/scheduling/museum/shifts/" + -1,
        HttpMethod.GET, entity, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Museum doesn't exist!"));
  }

  /**
   * Test to get the shifts of a museum that doesn't have any shifts
   *
   * @author VZ
   */
  @Test
  public void testGetAllShiftsByMuseumNoShift() {
    MuseumDto museumDto = createMuseumDto(createMuseumWithoutShifts());

    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());

    ResponseEntity<String> response = client.exchange("/api/scheduling/museum/shifts/" + -1,
        HttpMethod.GET, entity, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
  }

  /**
   * Test to successfully create, get, edit and delete a time period
   *
   * @author VZ
   */
  @Test
  public void testCreateGetAndEditTimePeriod() {
    HttpHeaders headers = loginSetupManager();

    Long id = testCreateTimePeriod(headers);
    testGetTimePeriod(headers, id);
    testEditTimePeriod(headers, id);
    testDeleteTimePeriod(headers, id);
  }

  /**
   * helper method to successfully create a time period
   *
   * @return
   */
  public Long testCreateTimePeriod(HttpHeaders headers) {
    HttpEntity<?> entity = new HttpEntity<>(new TimePeriodDto("2022-11-17 08:30:00", "2022-11-17 17:35:00"), headers);
    ResponseEntity<TimePeriodDto> response = client.exchange("/api/scheduling/shift/create",
        HttpMethod.POST, entity, TimePeriodDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("2022-11-17 08:30:00", response.getBody().getStartDate(),
        "Response has correct start time");
    assertEquals("2022-11-17 17:35:00", response.getBody().getEndDate(),
        "Response has correct end time");
    return response.getBody().getTimePeriodId();
  }

  /**
   * helper method to successfully get a time period
   *
   * @param id
   */

  public void testGetTimePeriod(HttpHeaders headers, Long id) {
    HttpEntity<?> entity = new HttpEntity<>(headers);
    ResponseEntity<TimePeriodDto> response = client.exchange("/api/scheduling/shift/" + id, HttpMethod.GET, entity,
        TimePeriodDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("2022-11-17 08:30:00", response.getBody().getStartDate(),
        "Response has correct start time");
    assertEquals("2022-11-17 17:35:00", response.getBody().getEndDate(),
        "Response has correct end time");
  }

  /**
   * helper method to successfully edit a time period
   *
   * @param id
   */
  public void testEditTimePeriod(HttpHeaders headers, Long id) {
    HttpEntity<?> entity = new HttpEntity<>(new TimePeriodDto("2023-12-12 08:30:00", "2023-12-12 17:35:00"), headers);
    ResponseEntity<TimePeriodDto> response = client.exchange("/api/scheduling/shift/edit/" + id,
        HttpMethod.POST, entity, TimePeriodDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("2023-12-12 08:30:00", response.getBody().getStartDate(),
        "Response has correct start time");
    assertEquals("2023-12-12 17:35:00", response.getBody().getEndDate(),
        "Response has correct end time");
    assertEquals(id, response.getBody().getTimePeriodId(), "Response has correct time period ID");
  }

  /**
   * helper method to successfully delete a time period
   *
   * @param id
   */
  public void testDeleteTimePeriod(HttpHeaders headers, Long id) {
    // once we delete the timeperiod, we try to get it, but it doesn't exist anymore
    // so
    // we should get a 404 not found reponse.

    HttpEntity<?> entity = new HttpEntity<>(headers);
    client.exchange(("/api/scheduling/shift/delete/" + id), HttpMethod.DELETE, entity,
        TimePeriodDto.class);
    ResponseEntity<String> response = client.exchange("/api/scheduling/shift/" + id, HttpMethod.GET, entity,
        String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Time period does not exist"));
  }

  /**
   * Test to create an invalid time period with start date after end date
   *
   * @author VZ
   */

  @Test
  public void testCreateInvalidTimePeriod() {
    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(new TimePeriodDto("2022-11-17 08:30:00", "2022-11-17 07:35:00"), headers);
    ResponseEntity<String> response = client.exchange("/api/scheduling/shift/create", HttpMethod.POST, entity,
        String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Start date cannot be after end date"));
  }

  /**
   * Test to get a time period that doesn't exist
   *
   * @author VZ
   */
  @Test
  public void testGetInvalidTimePeriod() {
    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(new TimePeriodDto("2022-11-17 08:30:00", "2022-11-17 07:35:00"), headers);
    ResponseEntity<String> response = client.exchange("/api/scheduling/shift/" + -1, HttpMethod.GET, entity,
        String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Time period does not exist"));
  }

  /**
   * Test to edit a time period that doesn't exist
   *
   * @author VZ
   */
  @Test
  public void testEditInvalidTimePeriod() {
    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(
        new TimePeriodDto(1L, "2023-12-12 08:30:00", "2023-12-12 17:35:00"), headers);
    ResponseEntity<String> response = client.exchange("/api/scheduling/shift/edit/" + 1, HttpMethod.POST, entity,
        String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Time period does not exist"));
  }

  /**
   * Test to delete a time period that doesn't exist
   *
   * @author VZ
   */
  @Test
  public void testDeleteInvalidTimePeriod() {
    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = client.exchange("/api/scheduling/shift/delete/" + -1,
        HttpMethod.DELETE, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Time period does not exist"));
  }

  /**
   * Test to add a time period to an employee schedule
   *
   * @author VZ
   */
  @Test
  public void testAddTimePeriodToEmployeeSchedule() {
    EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
    Long employeeId = employeeDto.getMuseumUserId();

    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(headers);

    Long timePeriodId = testCreateTimePeriod(headers);
    ResponseEntity<EmployeeDto> response = client.exchange(
        "/api/scheduling/employee/" + employeeId + "/add/shift/" + timePeriodId,
        HttpMethod.POST, entity, EmployeeDto.class);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(employeeId, response.getBody().getMuseumUserId(),
        "Response has correct employee ID");
  }

  /**
   * Test to add a time period that doesn't exist to an employee schedule
   *
   * @author VZ
   */
  @Test
  public void testAddTimePeriodToEmployeeScheduleInvalidTimePeriod() {
    EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
    Long employeeId = employeeDto.getMuseumUserId();
    Long timePeriodId = 1L;
    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());

    ResponseEntity<String> response = client.exchange(
        "/api/scheduling/employee/" + employeeId + "/add/shift/" + timePeriodId,
        HttpMethod.POST, entity, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("There is no such time period"));
  }

  /**
   * Test to delete a time period from an employee's schedule
   *
   * @author VZ
   */
  @Test
  public void testDeleteTimePeriodFromEmployeeSchedule() {
    EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
    Long employeeId = employeeDto.getMuseumUserId();
    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(headers);
    Long timePeriodId = testCreateTimePeriod(headers);
    ResponseEntity<EmployeeDto> response = client.exchange(
        "/api/scheduling/employee/" + employeeId + "/add/shift/" + timePeriodId,
        HttpMethod.POST, entity, EmployeeDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(employeeId, response.getBody().getMuseumUserId(),
        "Response has correct employee ID");
    ResponseEntity<String> response2 = client.exchange(
        "/api/scheduling/employee/" + employeeId + "/remove/shift/" + timePeriodId,
        HttpMethod.DELETE, entity, String.class);
    assertNotNull(response2);
    assertEquals(HttpStatus.OK, response2.getStatusCode(), "The response has the correct status");
    assertNotNull(response2.getBody(), "Response has body");
    assertTrue(response2.getBody().contains("Shift deleted"));
  }

  /**
   * Test to delete a time period that doesn't exist from an employee's schedule
   *
   * @author VZ
   */
  @Test
  public void testDeleteTimePeriodFromEmployeeScheduleInvalidTimePeriod() {
    EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
    Long employeeId = employeeDto.getMuseumUserId();
    Long timePeriodId = -1L;
    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response = client.exchange(
        "/api/scheduling/employee/" + employeeId + "/remove/shift/" + timePeriodId,
        HttpMethod.DELETE, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("There is no such time period"));
  }

  /**
   * Test to add a time period to a museum's schedule
   *
   * @author VZ
   */
  @Test
  public void testAddTimePeriodToMuseumSchedule() {
    MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
    Long museumId = museumDto.getMuseumId();
    HttpHeaders headers = loginSetupManager();
    Long timePeriodId = testCreateTimePeriod(headers);
    HttpEntity<?> entity = new HttpEntity<>(headers);
    ResponseEntity<MuseumDto> response = client.exchange(
        "/api/scheduling/museum/" + museumId + "/add/shift/" + timePeriodId,
        HttpMethod.POST, entity, MuseumDto.class);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(museumId, response.getBody().getMuseumId(), "Response has correct museum ID");
  }

  /**
   * Test to add a time period that doesn't exist to a museum's schedule
   *
   * @author VZ
   */
  @Test
  public void testAddTimePeriodToMuseumScheduleInvalidTimePeriod() {
    MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
    Long museumId = museumDto.getMuseumId();
    Long timePeriodId = 1L;
    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());
    ResponseEntity<String> response = client.exchange(
        "/api/scheduling/museum/" + museumId + "/add/shift/" + timePeriodId,
        HttpMethod.POST, entity, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Time period doesn't exist!"));
  }

  /**
   * Test to delete a time period from a museum's schedule
   *
   * @author VZ
   */
  @Test
  public void testRemoveTimePeriodFromMuseumSchedule() {
    MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
    Long museumId = museumDto.getMuseumId();
    HttpHeaders headers = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(headers);

    Long timePeriodId = testCreateTimePeriod(headers);
    ResponseEntity<MuseumDto> response = client.exchange(
        "/api/scheduling/museum/" + museumId + "/add/shift/" + timePeriodId,
        HttpMethod.POST, entity, MuseumDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(museumId, response.getBody().getMuseumId(), "Response has correct museum ID");
    ResponseEntity<String> response2 = client.exchange(
        "/api/scheduling/museum/" + museumId + "/remove/shift/" + timePeriodId,
        HttpMethod.DELETE, entity, String.class);
    assertNotNull(response2);
    assertEquals(HttpStatus.OK, response2.getStatusCode(), "The response has the correct status");
    assertNotNull(response2.getBody(), "Response has body");
    assertTrue(response2.getBody().contains("Shift deleted"));
  }

  /**
   * Test to delete a time period that doesn't exist from a museum's schedule
   *
   * @author VZ
   */
  @Test
  public void testRemoveTimePeriodFromMuseumScheduleInvalidTimePeriod() {
    MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
    Long museumId = museumDto.getMuseumId();
    Long timePeriodId = -1L;
    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());
    ResponseEntity<String> response = client.exchange(
        "/api/scheduling/museum/" + museumId + "/remove/shift/" + timePeriodId,
        HttpMethod.DELETE, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
        "The response has the correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertTrue(response.getBody().contains("Time period doesn't exist!"));
  }

  /**
   * helper method to convert an employee to an employeeDto
   *
   * @param employee
   * @return
   * @author VZ
   */

  public EmployeeDto createEmployeeDto(Employee employee) {
    return DtoUtility.convertToDto(employee);
  }

  /**
   * helper method to convert a museum to a museumDto
   *
   * @param museum
   * @return
   * @author VZ
   */

  public MuseumDto createMuseumDto(Museum museum) {
    return DtoUtility.convertToDto(museum);
  }

  /**
   * Helper method to create an employee without shifts
   *
   * @return
   * @author VZ
   */
  public Employee createEmployeeWithoutShifts() {
    // CREATE THE EMPLOYEE
    Employee employee = new Employee();
    Schedule schedule = new Schedule();
    employee.setEmail("asdf@gmail.com");
    employee.setPassword("asdf");
    employee.setSchedule(schedule);
    employee.setName("asdf");
    employeeRepository.save(employee);
    return employee;
  }

  /**
   * Helper method to create an employee with 2 shifts
   *
   * @return
   * @author VZ
   */
  public Employee createEmployeeWithShifts() {
    // CREATE THE EMPLOYEE
    Employee employee = new Employee();
    Schedule schedule = new Schedule();
    employee.setEmail("asdf@gmail.com");
    employee.setPassword("asdf");
    employee.setSchedule(schedule);
    employee.setName("asdf");
    employeeRepository.save(employee);
    employee = addTimePeriodsToEmployee(employee);
    return employee;
  }

  /**
   * Helper method to add 2 shifts to an employee
   *
   * @param employee
   * @return
   * @author VZ
   */
  public Employee addTimePeriodsToEmployee(Employee employee) {
    // CREATE THE SHIFTS
    TimePeriod shift1 = new TimePeriod();
    shift1.setStartDate(Timestamp.valueOf("2022-10-28 08:30:00.0"));
    shift1.setEndDate(Timestamp.valueOf("2022-10-28 17:35:00.0"));

    TimePeriod shift2 = new TimePeriod();
    shift2.setStartDate(Timestamp.valueOf("2022-10-29 08:30:00.0"));
    shift2.setEndDate(Timestamp.valueOf("2022-10-29 17:35:00.0"));

    // SAVE THE SHIFTS
    timePeriodRepository.save(shift1);
    timePeriodRepository.save(shift2);

    // GIVE SHIFTS TO EMPLOYEE
    ScheduleOfTimePeriod sotp1 = new ScheduleOfTimePeriod();
    sotp1.setSchedule(employee.getSchedule());
    sotp1.setTimePeriod(shift1);
    ScheduleOfTimePeriod sotp2 = new ScheduleOfTimePeriod();
    sotp2.setSchedule(employee.getSchedule());
    sotp2.setTimePeriod(shift2);
    // SAVE SOTP
    scheduleOfTimePeriodRepository.save(sotp1);
    scheduleOfTimePeriodRepository.save(sotp2);

    return employee;
  }

  /**
   * Helper method to create a museum with 2 shifts
   *
   * @return
   */

  public Museum createMuseumWithShifts() {
    Museum museum = new Museum();
    Schedule schedule = new Schedule();
    museum.setName("RQ");
    museum.setSchedule(schedule);
    museum.setVisitFee(10);
    museumRepository.save(museum);
    museum = addTimePeriodsToMuseum(museum);
    return museum;
  }

  /**
   * helper method to create a museum without shifts
   *
   * @return
   * @author VZ
   */

  public Museum createMuseumWithoutShifts() {
    Museum museum = new Museum();
    Schedule schedule = new Schedule();
    museum.setName("RQ");
    museum.setSchedule(schedule);
    museum.setVisitFee(10);
    museumRepository.save(museum);
    return museum;
  }

  /**
   * helper method to add 2 shifts to a museum
   *
   * @param museum
   * @return
   * @author VZ
   */
  public Museum addTimePeriodsToMuseum(Museum museum) {

    // CREATE THE SHIFTS
    TimePeriod shift1 = new TimePeriod();
    shift1.setStartDate(Timestamp.valueOf("2022-10-28 08:30:00.0"));
    shift1.setEndDate(Timestamp.valueOf("2022-10-28 17:35:00.0"));

    TimePeriod shift2 = new TimePeriod();
    shift2.setStartDate(Timestamp.valueOf("2022-10-29 08:30:00.0"));
    shift2.setEndDate(Timestamp.valueOf("2022-10-29 17:35:00.0"));

    // SAVE THE SHIFTS
    timePeriodRepository.save(shift1);
    timePeriodRepository.save(shift2);

    // GIVE SHIFTS TO EMPLOYEE
    ScheduleOfTimePeriod sotp1 = new ScheduleOfTimePeriod();
    sotp1.setSchedule(museum.getSchedule());
    sotp1.setTimePeriod(shift1);
    ScheduleOfTimePeriod sotp2 = new ScheduleOfTimePeriod();
    sotp2.setSchedule(museum.getSchedule());
    sotp2.setTimePeriod(shift2);
    // SAVE SOTP
    scheduleOfTimePeriodRepository.save(sotp1);
    scheduleOfTimePeriodRepository.save(sotp2);

    return museum;
  }

  /**
   * Create a visitor and login
   *
   * @param newVisitor - the visitor to login
   * @return the logged in visitor
   * @author Kevin
   */

  public VisitorDto createVisitorAndLogin(Visitor newVisitor) {
    visitorRepository.save(newVisitor);
    VisitorDto visitor = UserUtilities.createVisitorDto(newVisitor);
    ResponseEntity<String> response = client.postForEntity("/api/auth/login", visitor, String.class);
    List<String> session = response.getHeaders().get("Set-Cookie");

    String sessionId = session.get(0);
    visitor.setSessionId(sessionId);

    return visitor;
  }

  /**
   * Create employee and login
   *
   * @param newEmployee - employee to login
   * @return EmployeeDto - the logged in employee
   * @author Kevin
   */

  public EmployeeDto createEmployeeAndLogin(Employee newEmployee) {
    employeeRepository.save(newEmployee);
    EmployeeDto employee = UserUtilities.createEmployeeDto(newEmployee);
    ResponseEntity<String> response = client.postForEntity("/api/auth/login", employee, String.class);
    List<String> session = response.getHeaders().get("Set-Cookie");

    String sessionId = session.get(0);
    employee.setSessionId(sessionId);

    return employee;
  }

  /**
   * Create a manager and login
   *
   * @param newManager - the manager to login
   * @return managerDto - the logged in manager
   * @author Kevin
   */

  public ManagerDto createManagerAndLogin(Manager newManager) {
    managerRepository.save(newManager);
    ManagerDto manager = UserUtilities.createManagerDto(newManager);
    ResponseEntity<String> response = client.postForEntity("/api/auth/login", manager, String.class);
    List<String> session = response.getHeaders().get("Set-Cookie");

    String sessionId = session.get(0);
    manager.setSessionId(sessionId);

    return manager;
  }

  public ManagerDto loginManager(Manager manager) {
    ManagerDto managerDto = UserUtilities.createManagerDto(manager);
    ResponseEntity<String> response = client.postForEntity("/api/auth/login", managerDto, String.class);
    List<String> session = response.getHeaders().get("Set-Cookie");

    String sessionId = session.get(0);
    managerDto.setSessionId(sessionId);

    return managerDto;
  }

  /**
   * Create a museum and login
   *
   * @param newMuseum - the museum to login
   * @return museumDto - the logged in museum
   * @author Kevin
   */
  public HttpHeaders loginSetupManager() {
    ManagerDto manager = createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", manager.getSessionId());
    return headers;
  }

  // public HttpHeaders loginSetupManager(ManagerDto manager) {
  // ResponseEntity<String> response =
  // client.postForEntity("/api/auth/login", manager, String.class);

  // HttpHeaders headers = new HttpHeaders();
  // headers.set("Cookie", manager.getSessionId());
  // return headers;
  // }

  public HttpHeaders loginSetupEmployee(Employee employee) {
    EmployeeDto employeeLogin = createEmployeeAndLogin(employee);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", employeeLogin.getSessionId());
    return headers;
  }

}
