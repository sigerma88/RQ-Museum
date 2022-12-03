package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.MuseumUserDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.integration.utilities.UserUtilities;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This class tests the authentication rest controller
 *
 * @author Kevin
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthenticationIntegrationTest {
  private static final String FIRST_VISITOR_VALID_EMAIL = "sebastien.vettel@gmail.com";
  private static final String FIRST_VALID_VISITOR_NAME = "Sebastien Vettel";
  private static final String FIRST_VALID_VISITOR_PASSWORD = "#BrazilGp2022";

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private VisitorRepository visitorRepository;

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
  }

  /**
   * Test to see if a visitor can login
   *
   * @author Kevin
   */

  @Test
  public void testLogin() {

    Visitor visitor = UserUtilities.createVisitor(FIRST_VALID_VISITOR_NAME,
        FIRST_VISITOR_VALID_EMAIL, FIRST_VALID_VISITOR_PASSWORD);

    visitorRepository.save(visitor);

    ResponseEntity<MuseumUserDto> response =
        client.postForEntity("/api/auth/login", visitor, MuseumUserDto.class);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("sebastien.vettel@gmail.com", response.getBody().getEmail(),
        "Response body is correct");
    assertEquals("Sebastien Vettel", response.getBody().getName());
  }

  /**
   * Test when visitor login with wrong password
   *
   * @author Kevin
   */

  @Test
  public void testLoginWrongPassword() {
    Visitor visitor = UserUtilities.createVisitor(FIRST_VALID_VISITOR_NAME,
        FIRST_VISITOR_VALID_EMAIL, FIRST_VALID_VISITOR_PASSWORD);
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
    Visitor visitor = UserUtilities.createVisitor(FIRST_VALID_VISITOR_NAME,
        FIRST_VISITOR_VALID_EMAIL, FIRST_VALID_VISITOR_PASSWORD);
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
    Visitor visitor = UserUtilities.createVisitor(FIRST_VALID_VISITOR_NAME,
        FIRST_VISITOR_VALID_EMAIL, FIRST_VALID_VISITOR_PASSWORD);

    Visitor visitorLogin = new Visitor();
    visitorLogin.setEmail(visitor.getEmail());
    visitorLogin.setPassword(visitor.getPassword());

    HttpEntity<Visitor> entity = new HttpEntity<Visitor>(visitorLogin, loginSetupVisitor(visitor));

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
    VisitorDto visitor = createVisitorAndLogin(UserUtilities.createVisitor(FIRST_VALID_VISITOR_NAME,
        FIRST_VISITOR_VALID_EMAIL, FIRST_VALID_VISITOR_PASSWORD));
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
