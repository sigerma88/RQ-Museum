package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Timestamp;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.dto.TimePeriodDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SchedulingIntegrationTests {
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

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        scheduleOfTimePeriodRepository.deleteAll();
        timePeriodRepository.deleteAll();
        employeeRepository.deleteAll();
        museumRepository.deleteAll();
        scheduleRepository.deleteAll();
    }

    /**
     * Test to successfully get an employee's schedule
     * 
     * @author VZ
     */
    @Test
    public void testGetScheduleByEmployee() {
        EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
        Long id = employeeDto.getMuseumUserId();
        ScheduleDto scheduleDto = employeeDto.getSchedule();
        Long scheduleId = scheduleDto.getScheduleId();
        ResponseEntity<ScheduleDto> response = client.getForEntity("/employee/schedule/" + id, ScheduleDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(scheduleId, response.getBody().getScheduleId(), "Response has correct schedule ID");

    }

    /**
     * Test to get a schedule by an employee that doesn't exist
     * 
     * @author VZ
     */
    @Test
    public void testGetScheduleByEmployeeInvalidId() {
        ResponseEntity<String> response = client.getForEntity("/employee/schedule/" + -1, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertTrue(response.getBody().contains("There is no such employee"));
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
        ResponseEntity<TimePeriodDto[]> response = client.getForEntity("/employee/shifts/" + id, TimePeriodDto[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
        ResponseEntity<String> response = client.getForEntity("/employee/shifts/" + -1, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertTrue(response.getBody().contains("There is no such employee"));
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
        ResponseEntity<String> response = client.getForEntity("/employee/shifts/" + id, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(response.getBody(), "Employee has no shift");

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
        ResponseEntity<ScheduleDto> response = client.getForEntity("/museum/schedule/" + id, ScheduleDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(scheduleId, response.getBody().getScheduleId(), "Response has correct schedule ID");
    }

    /**
     * Test to get the schedule of a museum that doesn't exist
     * 
     * @author VZ
     */
    @Test
    public void testGetScheduleByMuseumInvalidId() {
        ResponseEntity<String> response = client.getForEntity("/museum/schedule/" + -1, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        ResponseEntity<TimePeriodDto[]> response = client.getForEntity("/museum/shifts/" + id, TimePeriodDto[].class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
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
        ResponseEntity<String> response = client.getForEntity("/museum/shifts/" + -1, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        Long id = museumDto.getMuseumId();
        ResponseEntity<String> response = client.getForEntity("/museum/shifts/" + id, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(response.getBody(), "Museum's schedule has no shift!");
    }

    /**
     * Test to successfully create, get, edit and delete a time period
     * 
     * @author VZ
     */
    @Test
    public void testCreateGetAndEditTimePeriod() {
        Long id = testCreateTimePeriod();
        testGetTimePeriod(id);
        testEditTimePeriod(id);
        testDeleteTimePeriod(id);
    }

    /**
     * helper method to successfully create a time period
     * 
     * @return
     */
    public Long testCreateTimePeriod() {
        ResponseEntity<TimePeriodDto> response = client.postForEntity("/shift/create",
                new TimePeriodDto("2022-11-17 08:30:00", "2022-11-17 17:35:00"), TimePeriodDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("2022-11-17 08:30:00", response.getBody().getStartDate().toString(),
                "Response has correct start time");
        assertEquals("2022-11-17 17:35:00", response.getBody().getEndDate().toString(),
                "Response has correct end time");
        return response.getBody().getTimePeriodId();
    }

    /**
     * helper method to successfully get a time period
     * 
     * @param id
     */

    public void testGetTimePeriod(Long id) {
        ResponseEntity<TimePeriodDto> response = client.getForEntity("/shift/" + id, TimePeriodDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("2022-11-17 08:30:00", response.getBody().getStartDate().toString(),
                "Response has correct start time");
        assertEquals("2022-11-17 17:35:00", response.getBody().getEndDate().toString(),
                "Response has correct end time");
    }

    /**
     * helper method to successfully edit a time period
     * 
     * @param id
     */
    public void testEditTimePeriod(Long id) {
        ResponseEntity<TimePeriodDto> response = client.postForEntity("/shift/edit/" + id,
                new TimePeriodDto(id, "2023-12-12 08:30:00", "2023-12-12 17:35:00"), TimePeriodDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("2023-12-12 08:30:00", response.getBody().getStartDate().toString(),
                "Response has correct start time");
        assertEquals("2023-12-12 17:35:00", response.getBody().getEndDate().toString(),
                "Response has correct end time");
        assertEquals(id, response.getBody().getTimePeriodId(), "Response has correct time period ID");
    }

    /**
     * helper method to successfully delete a time period
     * 
     * @param id
     */
    public void testDeleteTimePeriod(Long id) {
        client.delete(("/shift/delete/" + id), TimePeriodDto.class);
        ResponseEntity<String> response = client.getForEntity("/shift/" + id, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        ResponseEntity<String> response = client.postForEntity("/shift/create",
                new TimePeriodDto("2022-11-17 08:30:00", "2022-11-17 07:35:00"), String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
        ResponseEntity<String> response = client.getForEntity("/shift/" + -1, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        ResponseEntity<String> response = client.postForEntity("/shift/edit/" + 1,
                new TimePeriodDto(1L, "2023-12-12 08:30:00", "2023-12-12 17:35:00"), String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        ResponseEntity<String> response = client.exchange("/shift/delete/" + -1, HttpMethod.DELETE, null, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
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
        Long timePeriodId = testCreateTimePeriod();
        ResponseEntity<EmployeeDto> response = client.postForEntity("/employee/" + employeeId + "/add/shift/" + timePeriodId, null,
                EmployeeDto.class);
                    
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(employeeId, response.getBody().getMuseumUserId(), "Response has correct employee ID");
    }

    /**
     * Test to add a time period that doesn't exist to an employee schedule 
     * @author VZ
     */
    @Test
    public void testAddTimePeriodToEmployeeScheduleInvalidTimePeriod() {
        EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
        Long employeeId = employeeDto.getMuseumUserId();
        Long timePeriodId = 1L;
        ResponseEntity<String> response = client.postForEntity("/employee/" + employeeId + "/add/shift/" + timePeriodId, null,
                String.class);
                    
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertTrue(response.getBody().contains("There is no such time period"));
    }

    /**
     * Test to delete a time period from an employee's schedule
     * 
     * @author VZ
     */
    @Test
    public void testDeleteTimePeriodFromEmployeeSchedule(){
        EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
        Long employeeId = employeeDto.getMuseumUserId();
        Long timePeriodId = testCreateTimePeriod();
        ResponseEntity<EmployeeDto> response = client.postForEntity("/employee/" + employeeId + "/add/shift/" + timePeriodId, null,
                EmployeeDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(employeeId, response.getBody().getMuseumUserId(), "Response has correct employee ID");
        ResponseEntity<String> response2 = client.exchange("/employee/" + employeeId + "/remove/shift/" + timePeriodId, 
        HttpMethod.DELETE, null, String.class);
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody(), "Response has body");
        assertTrue(response2.getBody().contains("Shift deleted"));
    }

    /**
     * Test to delete a time period that doesn't exist from an employee's schedule
     * @author VZ
     */
    @Test
    public void testDeleteTimePeriodFromEmployeeScheduleInvalidTimePeriod() {
        EmployeeDto employeeDto = createEmployeeDto(createEmployeeWithShifts());
        Long employeeId = employeeDto.getMuseumUserId();
        Long timePeriodId = -1L;
        ResponseEntity<String> response = client.exchange("/employee/" + employeeId + "/remove/shift/" + timePeriodId, 
        HttpMethod.DELETE, null, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertTrue(response.getBody().contains("There is no such time period"));
    }

    /**
     * Test to add a time period to a museum's schedule 
     * @author VZ
     */
    @Test
    public void testAddTimePeriodToMuseumSchedule() {
        MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
        Long museumId = museumDto.getMuseumId();
        Long timePeriodId = testCreateTimePeriod();
        ResponseEntity<MuseumDto> response = client.postForEntity("/museum/" + museumId + "/add/shift/" + timePeriodId, null,
                MuseumDto.class);
                    
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(museumId, response.getBody().getMuseumId(), "Response has correct museum ID");
    }

    /**
     * Test to add a time period that doesn't exist to a museum's schedule
     * @author VZ
     */
    @Test
    public void testAddTimePeriodToMuseumScheduleInvalidTimePeriod() { 
        MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
        Long museumId = museumDto.getMuseumId();
        Long timePeriodId = 1L;
        ResponseEntity<String> response = client.postForEntity("/museum/" + museumId + "/add/shift/" + timePeriodId, null,
                String.class);
                    
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertTrue(response.getBody().contains("Time period doesn't exist!"));
    } 

    /**
     * Test to delete a time period from a museum's schedule
     * @author VZ
     */
    @Test
    public void testRemoveTimePeriodFromMuseumSchedule(){
        MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
        Long museumId = museumDto.getMuseumId();
        Long timePeriodId = testCreateTimePeriod();
        ResponseEntity<MuseumDto> response = client.postForEntity("/museum/" + museumId + "/add/shift/" + timePeriodId, null,
                MuseumDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(museumId, response.getBody().getMuseumId(), "Response has correct museum ID");
        ResponseEntity<String> response2 = client.exchange("/museum/" + museumId + "/remove/shift/" + timePeriodId, 
        HttpMethod.DELETE, null, String.class);
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody(), "Response has body");
        assertTrue(response2.getBody().contains("Shift deleted"));
    }

    /**
     * Test to delete a time period that doesn't exist from a museum's schedule
     * @author VZ
     */
    @Test
    public void testRemoveTimePeriodFromMuseumScheduleInvalidTimePeriod() {
        MuseumDto museumDto = createMuseumDto(createMuseumWithShifts());
        Long museumId = museumDto.getMuseumId();
        Long timePeriodId = -1L;
        ResponseEntity<String> response = client.exchange("/museum/" + museumId + "/remove/shift/" + timePeriodId, 
        HttpMethod.DELETE, null, String.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
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
     * @author VZ
     * @return
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
     * @author VZ
     * @param museum
     * @return
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

}
