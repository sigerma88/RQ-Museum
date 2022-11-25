package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.integration.utilities.UserUtilities;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the registration rest controller
 *
 * @author Kevin
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RegistrationIntegrationTest {

    private static final String FIRST_VISITOR_VALID_EMAIL = "sebastien.vettel@gmail.com";
    private static final String FIRST_VALID_VISITOR_NAME = "Sebastien Vettel";

    private static final String SECOND_VISITOR_VALID_EMAIL = "george.russel@gmail.com";
    private static final String SECOND_VALID_VISITOR_NAME = "George Russel";

    private static final String FIRST_VALID_EMPLOYEE_NAME = "Valterri Bottas";
    private static final String FIRST_VALID_EMPLOYEE_EMAIL = "valterri.bottas@museum.ca";

    private static final String INVALID_EMPLOYEE_NAME = "OscarPiastri";

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
     * Delete all employees, managers and visitors from the database
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
     * Test the creation of a visitor
     *
     * @author Kevin
     */

    @Test
    public void testRegisterVisitor() {
        VisitorDto visitor =
                UserUtilities.createVisitorDto(UserUtilities.createVisitor(FIRST_VALID_VISITOR_NAME,
                        FIRST_VISITOR_VALID_EMAIL, VALID_PASSWORD));

        ResponseEntity<VisitorDto> response =
                client.postForEntity("/api/profile/visitor/register", visitor, VisitorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Sebastien Vettel", response.getBody().getName(), "Response has correct name");
        assertEquals("sebastien.vettel@gmail.com", response.getBody().getEmail(),
                "Response has correct email");
        assertEquals("#BrazilGp2022", response.getBody().getPassword(),
                "Response has correct password");
    }

    /**
     * Test the creation of a visitor with an invalid email
     *
     * @author Kevin
     */

    @Test
    public void testRegisterVisitorInvalidEmail() {
        VisitorDto visitor = UserUtilities.createVisitorDto(UserUtilities.createVisitor(
                FIRST_VISITOR_VALID_EMAIL, FIRST_VALID_VISITOR_NAME, VALID_PASSWORD));
        visitor.setEmail("sebastien.vettelgmail.com");

        ResponseEntity<String> response =
                client.postForEntity("/api/profile/visitor/register", visitor, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
    }

    /**
     * Test the creation of a visitor with empty fields
     *
     * @author Kevin
     */

    @Test
    public void testRegisterVisitorEmptyFields() {
        VisitorDto visitor = new VisitorDto();

        ResponseEntity<String> response =
                client.postForEntity("/api/profile/visitor/register", visitor, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
    }

    /**
     * Test the creation of a visitor with an invalid password
     *
     * @author Kevin
     */

    @Test
    public void testRegisterVisitorInvalidPassword() {
        VisitorDto visitor = UserUtilities.createVisitorDto(UserUtilities.createVisitor(
                FIRST_VISITOR_VALID_EMAIL, FIRST_VALID_VISITOR_NAME, VALID_PASSWORD));
        visitor.setPassword("123");

        ResponseEntity<String> response =
                client.postForEntity("/api/profile/visitor/register", visitor, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
    }

    /**
     * Test the view visitor
     *
     * @author Kevin
     */

    @Test
    public void testViewVisitorProfile() {
        Visitor visitor = UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                FIRST_VALID_VISITOR_NAME, VALID_PASSWORD);
        visitorRepository.save(visitor);
        long visitorId = visitor.getMuseumUserId();

        HttpEntity<?> entity = new HttpEntity<>(loginSetupVisitor(visitor));
        ResponseEntity<VisitorDto> response = client.exchange("/api/profile/visitor/" + visitorId,
                HttpMethod.GET, entity, VisitorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(visitor.getEmail(), response.getBody().getEmail(),
                "Response has correct email");
        assertEquals(visitor.getName(), response.getBody().getName(), "Response has correct name");
        assertEquals(visitor.getPassword(), response.getBody().getPassword(),
                "Response has correct password");

    }

    /**
     * Test the view visitor without login
     *
     * @author Kevin
     */


    @Test
    public void testViewVisitorProfileWithoutLogin() {
        Visitor visitor = UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                FIRST_VALID_VISITOR_NAME, VALID_PASSWORD);
        visitorRepository.save(visitor);
        VisitorDto visitorDto = UserUtilities.createVisitorDto(visitor);
        long visitorId = visitorDto.getMuseumUserId();

        ResponseEntity<String> response =
                client.getForEntity("/api/profile/visitor/" + visitorId, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Response has correct status");
        assertEquals("You are not logged in.", response.getBody(), "Response has correct message");
    }

    /**
     * Test the view unauthorized visitor
     *
     * @author Kevin
     */

    @Test
    public void testViewUnauthorizedVisitor() {
        Visitor visitor = UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                FIRST_VALID_VISITOR_NAME, VALID_PASSWORD);
        long visitorId = visitor.getMuseumUserId() + 1;

        // TestRestTemplate does not pass session so we pass the sessionId we get from login
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(loginSetupVisitor(visitor));

        ResponseEntity<String> response = client.exchange("/api/profile/visitor/" + visitorId,
                HttpMethod.GET, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You are not authorized to view this profile.", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test the update visitor
     *
     * @author Kevin
     */
    @Test
    public void testUpdateVisitorInformation() {
        Visitor visitor = UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                FIRST_VALID_VISITOR_NAME, VALID_PASSWORD);
        visitorRepository.save(visitor);
        long visitorId = visitor.getMuseumUserId();

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("name", "Fernando Alonso");
        updatedCredentials.put("email", "fernando.alonso@gmail.com");
        updatedCredentials.put("oldPassword", "#BrazilGp2022");
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");


        HttpEntity<?> entity = new HttpEntity<>(updatedCredentials, loginSetupVisitor(visitor));

        ResponseEntity<VisitorDto> response = client.exchange(
                "/api/profile/visitor/edit/" + visitorId, HttpMethod.PUT, entity, VisitorDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Fernando Alonso", response.getBody().getName(), "Response has correct name");
        assertEquals("fernando.alonso@gmail.com", response.getBody().getEmail(),
                "Response has correct email");
        assertEquals("#AbuDhabiGp2022", response.getBody().getPassword(),
                "Response has correct password");
    }

    /**
     * Test the update visitor without login
     *
     * @author Kevin
     */

    @Test
    public void testUpdateVisitorInformationWithoutLogin() {
        Visitor visitor = UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                FIRST_VALID_VISITOR_NAME, VALID_PASSWORD);
        visitorRepository.save(visitor);
        VisitorDto visitorDto = UserUtilities.createVisitorDto(visitor);
        long visitorId = visitorDto.getMuseumUserId();

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("name", "Fernando Alonso");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials);

        ResponseEntity<String> response = client.exchange("/api/profile/visitor/edit/" + visitorId,
                HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You are not logged in.", response.getBody(), "Response has correct message");
    }

    /**
     * Test the update unauthorized visitor
     *
     * @author Kevin
     */

    @Test
    public void testUpdateUnauthorizedVisitor() {
        VisitorDto firstVisitor =
                createVisitorAndLogin(UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                        FIRST_VALID_VISITOR_NAME, VALID_PASSWORD));
        Visitor secondVisitor = UserUtilities.createVisitor(SECOND_VISITOR_VALID_EMAIL,
                SECOND_VALID_VISITOR_NAME, VALID_PASSWORD);
        long firstVisitorId = firstVisitor.getMuseumUserId();

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("name", "Fernando Alonso");

        HttpEntity<Map<String, String>> entity =
                new HttpEntity<>(updatedCredentials, loginSetupVisitor(secondVisitor));

        ResponseEntity<String> response =
                client.exchange("/api/profile/visitor/edit/" + firstVisitorId, HttpMethod.PUT,
                        entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Not allowed to edit this account", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test the update visitor with invalid email
     *
     * @author Kevin
     */

    @Test
    public void testUpdateVisitorInformationWithInvalidEmail() {
        VisitorDto visitor =
                createVisitorAndLogin(UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                        FIRST_VALID_VISITOR_NAME, VALID_PASSWORD));
        long visitorId = visitor.getMuseumUserId();

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("email", "fernando.alonsogmail");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<String> response = client.exchange("/api/profile/visitor/edit/" + visitorId,
                HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Invalid email. ", response.getBody(), "Response has correct message");
    }

    /**
     * Test the update visitor with invalid password
     *
     * @author Kevin
     */

    @Test
    public void testUpdateVisitorInformationWithInvalidPassword() {
        VisitorDto visitor =
                createVisitorAndLogin(UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                        FIRST_VALID_VISITOR_NAME, VALID_PASSWORD));
        long visitorId = visitor.getMuseumUserId();

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", "#BrazilGp2022");
        updatedCredentials.put("newPassword", "AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);
        ResponseEntity<String> response = client.exchange("/api/profile/visitor/edit/" + visitorId,
                HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(
                "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
                response.getBody(), "Response has correct message");
    }

    /**
     * Test the update visitor with wrong old password
     *
     * @author Kevin
     */

    @Test
    public void testUpdateVisitorWithWrongPassword() {
        VisitorDto visitor =
                createVisitorAndLogin(UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                        FIRST_VALID_VISITOR_NAME, VALID_PASSWORD));
        long visitorId = visitor.getMuseumUserId();

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", "#BrasilGp2022");
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);
        ResponseEntity<String> response = client.exchange("/api/profile/visitor/edit/" + visitorId,
                HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Old password incorrect", response.getBody(), "Response has correct message");
    }

    /**
     * Test register employee
     *
     * @author Kevin
     */

    @Test
    public void testEmployeeRegister() {
        ManagerDto manager =
                createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<String> entity = new HttpEntity<>(FIRST_VALID_EMPLOYEE_NAME, headers);

        ResponseEntity<EmployeeDto> response = client.exchange("/api/profile/employee/register",
                HttpMethod.POST, entity, EmployeeDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(FIRST_VALID_EMPLOYEE_NAME, response.getBody().getName(),
                "Response has correct name");
        assertEquals(FIRST_VALID_EMPLOYEE_EMAIL, response.getBody().getEmail(),
                "Response has correct email");
    }

    /**
     * Test register employee with invalid name
     *
     * @author Kevin
     */

    @Test
    public void testEmployeeRegisterWithInvalidName() {
        ManagerDto manager =
                createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<String> entity = new HttpEntity<>(INVALID_EMPLOYEE_NAME, headers);

        ResponseEntity<String> response = client.exchange("/api/profile/employee/register",
                HttpMethod.POST, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Name must be in the format of Firstname Lastname", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test user that are unauthorized to register employee
     *
     * @author Kevin
     */

    @Test
    public void testUnauthorizedRegisterEmployee() {
        VisitorDto visitor =
                createVisitorAndLogin(UserUtilities.createVisitor(FIRST_VISITOR_VALID_EMAIL,
                        FIRST_VALID_VISITOR_NAME, VALID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        HttpEntity<String> entity = new HttpEntity<>(FIRST_VALID_EMPLOYEE_NAME, headers);

        ResponseEntity<String> response = client.exchange("/api/profile/employee/register",
                HttpMethod.POST, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You must be a manager to register an employee", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test get employee information without login
     *
     * @author Kevin
     */

    @Test
    public void testRegisterEmployeeWithoutLogin() {
        HttpEntity<String> entity = new HttpEntity<>(FIRST_VALID_EMPLOYEE_NAME);

        ResponseEntity<String> response = client.exchange("/api/profile/employee/register",
                HttpMethod.POST, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You must be logged in to register an employee", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test edit employee information
     *
     * @author Kevin
     */

    @Test
    public void testEditEmployee() {
        EmployeeDto employee =
                createEmployeeAndLogin(UserUtilities.createEmployee(FIRST_VALID_EMPLOYEE_NAME,
                        FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", employee.getPassword());
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<EmployeeDto> response =
                client.exchange("/api/profile/employee/edit/" + employee.getMuseumUserId(),
                        HttpMethod.PUT, entity, EmployeeDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(FIRST_VALID_EMPLOYEE_NAME, response.getBody().getName(),
                "Response has correct name");
        assertEquals(FIRST_VALID_EMPLOYEE_EMAIL, response.getBody().getEmail(),
                "Response has correct email");
        assertEquals("#AbuDhabiGp2022", response.getBody().getPassword(),
                "Response has correct password");
    }

    /**
     * Test edit employee information with invalid id
     *
     * @author Kevin
     */

    @Test
    public void testEditEmployeeWithInvalidId() {
        EmployeeDto employee =
                createEmployeeAndLogin(UserUtilities.createEmployee(FIRST_VALID_EMPLOYEE_NAME,
                        FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", employee.getPassword());
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        Long invalidId = employee.getMuseumUserId() + 1;

        ResponseEntity<String> response = client.exchange("/api/profile/employee/edit/" + invalidId,
                HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You can only edit your own information", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test edit employee information with invalid old password
     *
     * @author Kevin
     */

    @Test
    public void testEditEmployeeWithInvalidOldPassword() {
        EmployeeDto employee =
                createEmployeeAndLogin(UserUtilities.createEmployee(FIRST_VALID_EMPLOYEE_NAME,
                        FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", "invalidPassword");
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<String> response =
                client.exchange("/api/profile/employee/edit/" + employee.getMuseumUserId(),
                        HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Old password incorrect", response.getBody(), "Response has correct message");
    }

    /**
     * Test edit employee information with invalid new password
     *
     * @author Kevin
     */

    @Test
    public void testEditEmployeeWithInvalidNewPassword() {
        EmployeeDto employee =
                createEmployeeAndLogin(UserUtilities.createEmployee(FIRST_VALID_EMPLOYEE_NAME,
                        FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", employee.getPassword());
        updatedCredentials.put("newPassword", "AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<String> response =
                client.exchange("/api/profile/employee/edit/" + employee.getMuseumUserId(),
                        HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(
                "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
                response.getBody(), "Response has correct message");
    }

    /**
     * Test edit employee information without login
     *
     * @author Kevin
     */

    @Test
    public void testEditEmployeeWithoutLogin() {
        EmployeeDto employee = UserUtilities.createEmployeeDto(UserUtilities.createEmployee(
                FIRST_VALID_EMPLOYEE_NAME, FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", employee.getPassword());
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials);

        ResponseEntity<String> response =
                client.exchange("/api/profile/employee/edit/" + employee.getMuseumUserId(),
                        HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You must be logged in to edit an employee", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test view employee information with valid id
     *
     * @author Kevin
     */

    @Test
    public void testViewEmployeeWithValidId() {
        EmployeeDto employee =
                createEmployeeAndLogin(UserUtilities.createEmployee(FIRST_VALID_EMPLOYEE_NAME,
                        FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<EmployeeDto> entity = new HttpEntity<>(employee, headers);

        ResponseEntity<EmployeeDto> response =
                client.exchange("/api/profile/employee/" + employee.getMuseumUserId(),
                        HttpMethod.GET, entity, EmployeeDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(FIRST_VALID_EMPLOYEE_NAME, response.getBody().getName(),
                "Response has correct name");
        assertEquals(FIRST_VALID_EMPLOYEE_EMAIL, response.getBody().getEmail(),
                "Response has correct email");
        assertEquals(VALID_PASSWORD, response.getBody().getPassword(),
                "Response has correct password");
    }

    /**
     * Test view employee information with invalid id
     *
     * @author Kevin
     */

    @Test
    public void viewEmployeeWithoutLogin() {
        EmployeeDto employee = UserUtilities.createEmployeeDto(UserUtilities.createEmployee(
                FIRST_VALID_EMPLOYEE_NAME, FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                client.exchange("/api/profile/employee/" + employee.getMuseumUserId(),
                        HttpMethod.GET, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You are not logged in", response.getBody(), "Response has correct message");
    }

    /**
     * Test view employee information without login
     *
     * @author Kevin
     */

    @Test
    public void testViewEmployeeWithInvalidId() {
        EmployeeDto employee =
                createEmployeeAndLogin(UserUtilities.createEmployee(FIRST_VALID_EMPLOYEE_NAME,
                        FIRST_VALID_EMPLOYEE_EMAIL, VALID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response =
                client.exchange("/api/profile/employee/" + employee.getMuseumUserId() + 1,
                        HttpMethod.GET, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You can only view your own information", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test edit manager information
     *
     * @author Kevin
     */

    @Test
    public void testEditManagerInformation() {
        ManagerDto manager =
                createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", manager.getPassword());
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<ManagerDto> response =
                client.exchange("/api/profile/manager/edit/" + manager.getMuseumUserId(),
                        HttpMethod.PUT, entity, ManagerDto.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");

    }

    /**
     * Test edit manager information without login
     *
     * @author Kevin
     */

    @Test
    public void testEditManagerWithoutLogin() {
        ManagerDto manager =
                UserUtilities.createManagerDto(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", manager.getPassword());
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials);

        ResponseEntity<String> response =
                client.exchange("/api/profile/manager/edit/" + manager.getMuseumUserId(),
                        HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You must be logged in to edit a manager", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test edit manager information with invalid role
     *
     * @author Kevin
     */

    @Test
    public void testEditManagerWithInvalidRole() {
        EmployeeDto employee =
                createEmployeeAndLogin(UserUtilities.createEmployee(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", employee.getPassword());
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employee.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<String> response =
                client.exchange("/api/profile/manager/edit/" + employee.getMuseumUserId(),
                        HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You must be a manager to edit a manager", response.getBody(),
                "Response has correct message");
    }

    /**
     * Test edit manager information with wrong old password
     *
     * @author Kevin
     */

    @Test
    public void testEditManagerWithWrongOldPassword() {
        ManagerDto manager =
                createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", "wrongPassword");
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<String> response =
                client.exchange("/api/profile/manager/edit/" + manager.getMuseumUserId(),
                        HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Old password incorrect", response.getBody(), "Response has correct message");
    }

    /**
     * Test edit manager information with invalid new password
     *
     * @author Kevin
     */

    @Test
    public void testEditManagerWithInvalidNewPassword() {
        ManagerDto manager =
                createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("oldPassword", manager.getPassword());
        updatedCredentials.put("newPassword", "invalidPassword");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(updatedCredentials, headers);

        ResponseEntity<String> response =
                client.exchange("/api/profile/manager/edit/" + manager.getMuseumUserId(),
                        HttpMethod.PUT, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(
                "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
                response.getBody(), "Response has correct message");
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
        ResponseEntity<String> response =
                client.postForEntity("/api/auth/login", visitor, String.class);
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
     */

    public ManagerDto createManagerAndLogin(Manager newManager) {
        managerRepository.save(newManager);
        ManagerDto manager = UserUtilities.createManagerDto(newManager);
        ResponseEntity<String> response =
                client.postForEntity("/api/auth/login", manager, String.class);
        List<String> session = response.getHeaders().get("Set-Cookie");

        String sessionId = session.get(0);
        manager.setSessionId(sessionId);

        return manager;
    }

    /**
     * Create a museum and login
     *
     * @param newMuseum - the museum to login
     * @return museumDto - the logged in museum
     * @author Kevin
     */
    public HttpHeaders loginSetupManager() {
        ManagerDto manager =
                createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
                        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", manager.getSessionId());
        return headers;
    }

    public HttpHeaders loginSetupEmployee(Employee employee) {
        EmployeeDto employeeLogin = createEmployeeAndLogin(employee);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", employeeLogin.getSessionId());
        return headers;
    }

    /**
     * Create a museum and login
     *
     * @param newMuseum - the museum to login
     * @return museumDto - the logged in museum
     * @author Kevin
     */
    public HttpHeaders loginSetupVisitor(Visitor newVisitor) {
        VisitorDto visitor = createVisitorAndLogin(newVisitor);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        return headers;
    }


}
