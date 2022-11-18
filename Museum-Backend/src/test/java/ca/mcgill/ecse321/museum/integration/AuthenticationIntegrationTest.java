package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
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
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Visitor;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegrationTest {
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

  /**
   * Test to see if a visitor can login
   * 
   * @author Kevin
   */

  @Test
  public void testLogin() {

    Visitor visitor = createVisitorAndSave();

    ResponseEntity<String> response =
        client.postForEntity("/api/auth/login", visitor, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("logged in", response.getBody(), "Response has correct name");
  }

  /**
   * Test whe visitor login with wrong password
   * 
   * @author Kevin
   */

  @Test
  public void testLoginWrongPassword() {
    Visitor visitor = createVisitor();
    visitor.setPassword("Speed123#$");

    ResponseEntity<String> response =
        client.postForEntity("/api/auth/login", visitor, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
  }

  /**
   * Test login with invalid email
   * 
   * @author Kevin
   */

  @Test
  public void testLoginInvalidEmail() {
    Visitor visitor = createVisitor();
    visitorRepository.save(visitor);
    visitor.setEmail("fernando.alonso@gmail.com");

    ResponseEntity<String> response =
        client.postForEntity("/api/auth/login", visitor, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
  }

  /**
   * Test login when already logged in
   * 
   * @author Kevin
   */

  @Test
  public void testLoginWhenLoggedin() {
    VisitorDto visitor = createVisitorAndLogin();
    HttpHeaders headers = new HttpHeaders();

    Visitor visitorLogin = new Visitor();
    visitorLogin.setEmail(visitor.getEmail());
    visitorLogin.setPassword(visitor.getPassword());

    headers.set("Cookie", visitor.getSessionId());
    HttpEntity<Visitor> entity = new HttpEntity<Visitor>(visitorLogin, headers);

    ResponseEntity<String> response =
        client.exchange("/api/auth/login", HttpMethod.POST, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Cannot login while logged in");
  }

  /**
   * Test user logout
   * 
   * @author Kevin
   */

  @Test
  public void testLogout() {
    VisitorDto visitor = createVisitorAndLogin();
    HttpHeaders headers = new HttpHeaders();

    headers.set("Cookie", visitor.getSessionId());
    HttpEntity<?> entity = new HttpEntity<>(headers);

    ResponseEntity<String> response =
        client.exchange("/api/auth/logout", HttpMethod.POST, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("logged out", response.getBody(), "Response has correct name");
  }

  /**
   * Test logout when not logged in
   * 
   * @author Kevin
   */

  @Test
  public void testLogoutWhenNotLoggedin() {
    VisitorDto visitor = createVisitorAndLogin();

    ResponseEntity<String> response =
        client.postForEntity("/api/auth/logout", visitor, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Cannot logout when not logged in", response.getBody(),
        "Response has correct name");
  }

  /**
   * Create a visitor Dto
   * 
   * @param visitor
   * @return visitorDto
   */

  public VisitorDto createVisitorDto(Visitor visitor) {
    return DtoUtility.convertToDto(visitor);
  }

  /**
   * Create a visitor
   * 
   * @return visitor
   */

  public Visitor createVisitor() {
    Visitor visitor = new Visitor();
    visitor.setEmail("sebastien.vettel@gmail.com");
    visitor.setPassword("#BrazilGp2022");
    visitor.setName("Sebastien Vettel");

    return visitor;
  }

  /**
   * Create a visitor and save it
   * 
   * @return visitor
   */

  public Visitor createVisitorAndSave() {
    Visitor visitor = createVisitor();
    visitorRepository.save(visitor);
    return visitor;
  }

  /**
   * Create a visitor and login
   * 
   * @return visitorDto
   */

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
