package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Visitor;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RegistrationIntegrationTest {
    @Autowired
    private TestRestTemplate client;

    @Autowired
    private TestRestTemplate loginClient;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private VisitorRepository visitorRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        employeeRepository.deleteAll();
        visitorRepository.deleteAll();
    }

    @Test
    public void testRegisterVisitor() {
        // TODO
        VisitorDto visitor = createVisitorDto(createVisitor());

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

    @Test
    public void testRegisterVisitorInvalidEmail() {
        VisitorDto visitor = createVisitorDto(createVisitor());
        visitor.setEmail("sebastien.vettelgmail.com");

        ResponseEntity<String> response =
                client.postForEntity("/api/profile/visitor/register", visitor, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
    }

    @Test
    public void testRegisterVisitorEmptyFields() {
        VisitorDto visitor = new VisitorDto();

        ResponseEntity<String> response =
                client.postForEntity("/api/profile/visitor/register", visitor, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
    }

    @Test
    public void testRegisterVisitorInvalidPassword() {
        VisitorDto visitor = createVisitorDto(createVisitor());
        visitor.setPassword("123");

        ResponseEntity<String> response =
                client.postForEntity("/api/profile/visitor/register", visitor, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Response has correct status");
    }


    @Test
    public void testViewVisitorProfile() {
        VisitorDto visitor = createVisitorAndLogin();
        long visitorId = visitor.getUserId();

        ResponseEntity<String> response =
                client.getForEntity("/api/profile/visitor/" + visitorId, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    }

    @Test
    public void testViewVisitorProfileWithoutLogin() {
        VisitorDto visitor = createVisitorDto(createVisitorAndSave());
        long visitorId = visitor.getUserId();

        ResponseEntity<String> response =
                client.getForEntity("/api/profile/visitor/" + visitorId, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Response has correct status");
        assertEquals("You are not logged in.", response.getBody(), "Response has correct message");
    }

    @Test
    // test user can only view their own information
    public void testViewVisitorProfileWithLogin() {
        VisitorDto visitor = createVisitorAndLogin();
        long visitorId = visitor.getUserId() + 1;

        ResponseEntity<String> response =
                client.getForEntity("/api/profile/visitor/" + visitorId, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You are not authorized to view this profile.", response.getBody(),
                "Response has correct message");
    }

    public VisitorDto createVisitorDto(Visitor visitor) {
        return DtoUtility.convertToDto(visitor);
    }

    public Visitor createVisitor() {
        Visitor visitor = new Visitor();
        visitor.setEmail("sebastien.vettel@gmail.com");
        visitor.setPassword("#BrazilGp2022");
        visitor.setName("Sebastien Vettel");

        return visitor;
    }

    public Visitor createVisitorAndSave() {
        Visitor visitor = createVisitor();
        visitorRepository.save(visitor);
        return visitor;
    }

    public VisitorDto createVisitorAndLogin() {
        VisitorDto visitor = createVisitorDto(createVisitorAndSave());
        client.postForEntity("/api/auth/login", visitor, String.class);
        return visitor;
    }
}
