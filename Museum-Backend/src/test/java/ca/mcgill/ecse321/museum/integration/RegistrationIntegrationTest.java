package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
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
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        HttpEntity<String> entity = new HttpEntity<String>(headers);
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
    public void testViewUnauthorizedVisitor() {
        VisitorDto visitor = createVisitorAndLogin();
        long visitorId = visitor.getUserId() + 1;

        // TestRestTemplate does not pass session so we pass the sessionId we get from login
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = client.exchange("/api/profile/visitor/" + visitorId,
                HttpMethod.GET, entity, String.class);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "Response has correct status");
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("You are not authorized to view this profile.", response.getBody(),
                "Response has correct message");
    }

    @Test
    public void testUpdateVisitorInformation() {
        VisitorDto visitor = createVisitorAndLogin();
        long visitorId = visitor.getUserId();

        Map<String, String> updatedCredentials = new HashMap<>();
        updatedCredentials.put("name", "Fernando Alonso");
        updatedCredentials.put("email", "fernando.alonso@gmail.com");
        updatedCredentials.put("oldPassword", "#BrazilGp2022");
        updatedCredentials.put("newPassword", "#AbuDhabiGp2022");


        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", visitor.getSessionId());
        HttpEntity<?> entity = new HttpEntity<>(updatedCredentials, headers);

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
        ResponseEntity<String> response =
                client.postForEntity("/api/auth/login", visitor, String.class);
        List<String> session = response.getHeaders().get("Set-Cookie");

        String sessionId = session.get(0);
        visitor.setSessionId(sessionId);

        return visitor;
    }
}
