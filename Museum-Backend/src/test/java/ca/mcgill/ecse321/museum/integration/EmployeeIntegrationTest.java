package ca.mcgill.ecse321.museum.integration;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.Schedule;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    private static final String FIRST_EMPLOYEE_VALID_EMAIL = "sebastien.vettel@gmail.com";
    private static final String FIRST_EMPLOYEE_NAME = "Sebastien Vettel";

    private static final String SECOND_EMPLOYEE_VALID_EMAIL = "george.russel@gmail.com";
    private static final String SECOND_EMPLOYEE_NAME = "George Russel";

    private static final String THIRD_EMPLOYEE_VALID_NAME = "Shawn Cui";
    private static final String THIRD_EMPLOYEE_VALID_EMAIL = "shawn.cui@gmail.com";

    private static final String FIRST_VALID_MANAGER_NAME = "admin";
    private static final String FIRST_VALID_MANAGER_EMAIL = "admin@mail.ca";

    private static final String VALID_PASSWORD = "#BrazilGp2022";

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @Autowired
    private ManagerRepository managerRepository;

    /**
     * Delete all the employees and visitors in the database
     * 
     * @author Kevin
     */
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
        visitorRepository.deleteAll();
        managerRepository.deleteAll();
    }

    /**
     * Test get all employees
     * 
     * @author Kevin
     */

    @Test
    public void testGetAllEmployees() {

        ManagerDto manager = createManagerAndLogin(createManager(FIRST_VALID_MANAGER_NAME,
                FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD));

        Employee firstEmployee =
                createEmployee(FIRST_EMPLOYEE_NAME, FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);
        Employee secondEmployee =
                createEmployee(SECOND_EMPLOYEE_NAME, SECOND_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);
        Employee thirdEmployee = createEmployee(THIRD_EMPLOYEE_VALID_NAME,
                THIRD_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);

        createEmployeeAndSave(firstEmployee);
        createEmployeeAndSave(secondEmployee);
        createEmployeeAndSave(thirdEmployee);


        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);


        ResponseEntity<EmployeeDto[]> responseEntity =
                client.exchange("/api/employee/", HttpMethod.GET, entity, EmployeeDto[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, responseEntity.getBody().length);
        assertEquals(FIRST_EMPLOYEE_NAME, responseEntity.getBody()[0].getName());
        assertEquals(FIRST_EMPLOYEE_VALID_EMAIL, responseEntity.getBody()[0].getEmail());
        assertEquals(VALID_PASSWORD, responseEntity.getBody()[0].getPassword());
        assertEquals(SECOND_EMPLOYEE_NAME, responseEntity.getBody()[1].getName());
        assertEquals(SECOND_EMPLOYEE_VALID_EMAIL, responseEntity.getBody()[1].getEmail());
        assertEquals(VALID_PASSWORD, responseEntity.getBody()[1].getPassword());
        assertEquals(THIRD_EMPLOYEE_VALID_NAME, responseEntity.getBody()[2].getName());
        assertEquals(THIRD_EMPLOYEE_VALID_EMAIL, responseEntity.getBody()[2].getEmail());
        assertEquals(VALID_PASSWORD, responseEntity.getBody()[2].getPassword());
    }

    /**
     * Test get employee when not logged in
     * 
     * @author Kevin
     */

    @Test
    public void testGetAllEmployeeNotLoggedIn() {
        ManagerDto manager = createManagerDto(createManager(FIRST_VALID_MANAGER_NAME,
                FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD));

        Employee firstEmployee =
                createEmployee(FIRST_EMPLOYEE_NAME, FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);
        Employee secondEmployee =
                createEmployee(SECOND_EMPLOYEE_NAME, SECOND_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);
        Employee thirdEmployee = createEmployee(THIRD_EMPLOYEE_VALID_NAME,
                THIRD_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);

        createEmployeeAndSave(firstEmployee);
        createEmployeeAndSave(secondEmployee);
        createEmployeeAndSave(thirdEmployee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(headers);


        ResponseEntity<String> responseEntity =
                client.exchange("/api/employee/", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("You are not logged in", responseEntity.getBody(),
                "Correct response body message");
    }

    /**
     * Test get employee when not logged in as manager
     * 
     * @author Kevin
     */

    @Test
    public void testGetAllEmployeeNotManager() {
        EmployeeDto employee = createEmployeeAndLogin(createEmployee(THIRD_EMPLOYEE_VALID_NAME,
                THIRD_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD));

        Employee firstEmployee =
                createEmployee(FIRST_EMPLOYEE_NAME, FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);
        Employee secondEmployee =
                createEmployee(SECOND_EMPLOYEE_NAME, SECOND_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);

        createEmployeeAndSave(firstEmployee);
        createEmployeeAndSave(secondEmployee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<?> entity = new HttpEntity<>(headers);


        ResponseEntity<String> responseEntity =
                client.exchange("/api/employee/", HttpMethod.GET, entity, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals("You are not the manager", responseEntity.getBody(),
                "Correct response body message");
    }

    /**
     * Test delete employee
     * 
     * @author Kevin
     */

    @Test
    public void testDeleteEmployee() {
        ManagerDto manager = createManagerAndLogin(createManager(FIRST_VALID_MANAGER_NAME,
                FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD));

        Employee firstEmployee =
                createEmployee(FIRST_EMPLOYEE_NAME, FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);

        createEmployeeAndSave(firstEmployee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<?> entity = new HttpEntity<>(headers);


        ResponseEntity<String> responseEntity =
                client.exchange("/api/employee/" + firstEmployee.getMuseumUserId(),
                        HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Employee deleted", responseEntity.getBody(), "Correct response body message");

    }

    /**
     * Test delete employee when not logged in
     * 
     * @author Kevin
     */

    @Test
    public void testDeleteInvalidEmployee() {
        ManagerDto manager = createManagerAndLogin(createManager(FIRST_VALID_MANAGER_NAME,
                FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD));

        Employee firstEmployee =
                createEmployee(FIRST_EMPLOYEE_NAME, FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);

        createEmployeeAndSave(firstEmployee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                client.exchange("/api/employee/" + firstEmployee.getMuseumUserId() + 1,
                        HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Employee does not exist", responseEntity.getBody(),
                "Correct response body message");
    }

    /**
     * Test delete employee when not logged in
     * 
     * @author Kevin
     */

    @Test
    public void testDeleteEmployeeNotLoggedIn() {
        ManagerDto manager = createManagerDto(createManager(FIRST_VALID_MANAGER_NAME,
                FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD));

        Employee firstEmployee =
                createEmployee(FIRST_EMPLOYEE_NAME, FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);

        createEmployeeAndSave(firstEmployee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                client.exchange("/api/employee/" + firstEmployee.getMuseumUserId(),
                        HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("You are not logged in", responseEntity.getBody(),
                "Correct response body message");
    }

    @Test
    public void testDeleteEmployeeNotManager() {
        EmployeeDto employee = createEmployeeAndLogin(createEmployee(THIRD_EMPLOYEE_VALID_NAME,
                THIRD_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD));

        Employee firstEmployee =
                createEmployee(FIRST_EMPLOYEE_NAME, FIRST_EMPLOYEE_VALID_EMAIL, VALID_PASSWORD);

        createEmployeeAndSave(firstEmployee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity =
                client.exchange("/api/employee/" + firstEmployee.getMuseumUserId(),
                        HttpMethod.DELETE, entity, String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("You are not the manager", responseEntity.getBody(),
                "Correct response body message");
    }

    /**
     * Create employeeDto
     * 
     * @param employee - employee
     * @return employeeDto - the employeeDto created
     * @author Kevin
     */

    public EmployeeDto createEmployeeDto(Employee employee) {
        return DtoUtility.convertToDto(employee);
    }

    public Employee createEmployee(String name, String email, String password) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(password);
        employee.setSchedule(new Schedule());

        return employee;
    }

    /**
     * Create employee and save
     * 
     * @param employee - employee to save
     * @return employee - the saved employee
     * @author Kevin
     */

    public Employee createEmployeeAndSave(Employee employee) {
        employeeRepository.save(employee);
        return employee;
    }

    /**
     * Create a managerDTO
     * 
     * @param manager - the manager to be created
     * @return ManagerDto
     * @author Kevin
     */

    public ManagerDto createManagerDto(Manager manager) {
        return DtoUtility.convertToDto(manager);
    }

    /**
     * Create a manager
     * 
     * @param name - name of the manager
     * @param email - email of the manager
     * @param password - password of the manager
     * @return Manager - the manager created
     * @author Kevin
     * 
     */

    public Manager createManager(String name, String email, String password) {
        Manager manager = new Manager();
        manager.setName(name);
        manager.setEmail(email);
        manager.setPassword(password);

        return manager;
    }

    /**
     * Create a manager and save
     * 
     * @param manager - the manager to save
     * @return Manager - the saved manager
     * @author Kevin
     * 
     */

    public Manager createManagerAndSave(Manager manager) {
        managerRepository.save(manager);
        return manager;
    }

    /**
     * Create employee and login
     * 
     * @param newEmployee - employee to login
     * @return EmployeeDto - the logged in employee
     * @author Kevin
     */

    public EmployeeDto createEmployeeAndLogin(Employee newEmployee) {
        EmployeeDto employee = createEmployeeDto(createEmployeeAndSave(newEmployee));
        ResponseEntity<String> response =
                client.postForEntity("/api/auth/login", employee, String.class);
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
     * 
     */

    public ManagerDto createManagerAndLogin(Manager newManager) {
        ManagerDto manager = createManagerDto(createManagerAndSave(newManager));
        ResponseEntity<String> response =
                client.postForEntity("/api/auth/login", manager, String.class);
        List<String> session = response.getHeaders().get("Set-Cookie");

        String sessionId = session.get(0);
        manager.setSessionId(sessionId);

        return manager;
    }
}
