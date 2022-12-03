package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.integration.utilities.UserUtilities;
import ca.mcgill.ecse321.museum.model.Manager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MuseumIntegrationTests {
  private static final String FIRST_VALID_MANAGER_NAME = "admin";
  private static final String FIRST_VALID_MANAGER_EMAIL = "admin@mail.ca";

  private static final String VALID_PASSWORD = "#BrazilGp2022";

  @Autowired
  private TestRestTemplate client;
  @Autowired
  private MuseumRepository museumRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;
  @Autowired
  private ManagerRepository managerRepository;

  /**
   * Clear the database before and after each test
   *
   * @author VZ
   */
  @BeforeEach
  @AfterEach
  public void clearDatabase() {
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();
    managerRepository.deleteAll();
  }

  /**
   * Test to successfully create, get and edit a museum
   *
   * @author VZ
   */
  @Test
  public void testCreateGetAndEditMuseum() {
    HttpHeaders header = loginSetupManager();
    Long id = testCreateMuseum(header);
    testGetMuseum(header, id);
    testEditMuseum(header, id);
  }

  /**
   * helper test method that returns the id of the created museum
   *
   * @return id of the created museum
   * @author VZ
   */
  public Long testCreateMuseum(HttpHeaders header) {
    HttpEntity<?> entity = new HttpEntity<>(header);
    ResponseEntity<MuseumDto> response = client.exchange("/api/museum/app?name=Museum&visitFee=10",
        HttpMethod.POST, entity, MuseumDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("Museum", response.getBody().getName(), "Response has correct name");
    assertEquals(10, response.getBody().getVisitFee(), "Response has correct visit fee");
    assertTrue(response.getBody().getMuseumId() > 0, "Response has valid ID");

    return response.getBody().getMuseumId();
  }

  /**
   * helper test method gets a museum by id
   *
   * @param id of the museum to get
   * @author VZ
   */
  public void testGetMuseum(HttpHeaders header, Long id) {
    HttpEntity<?> entity = new HttpEntity<>(header);
    ResponseEntity<MuseumDto> response =
        client.exchange("/api/museum/" + id, HttpMethod.GET, entity, MuseumDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("Museum", response.getBody().getName(), "Response has correct name");
    assertEquals(10, response.getBody().getVisitFee(), "Response has correct visit fee");
    assertEquals(id, response.getBody().getMuseumId(), "Response has correct ID");
  }

  /**
   * helper test method that edits a museum given its id
   *
   * @param id of the museum to edit
   * @author VZ
   */
  public void testEditMuseum(HttpHeaders header, Long id) {
    MuseumDto museumDto = new MuseumDto();
    museumDto.setMuseumId(id);
    HttpEntity<?> entity = new HttpEntity<>(header);
    ResponseEntity<MuseumDto> response =
        client.exchange("/api/museum/app/edit/" + id + "/?name=RQ&visitFee=20", HttpMethod.POST,
            entity, MuseumDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("RQ", response.getBody().getName(), "Response has correct name");
    assertEquals(20, response.getBody().getVisitFee(), "Response has correct visit fee");
    assertEquals(id, response.getBody().getMuseumId(), "Response has correct ID");
  }

  /**
   * Test to unsuccessfully get a museum with an invalid id
   *
   * @author VZ
   */
  @Test
  public void testGetMuseumWithInvalidId() {
    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());
    ResponseEntity<String> response =
        client.exchange("/api/museum/1", HttpMethod.GET, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("There is no such museum", response.getBody(),
        "Response has correct error message");
  }

  /**
   * Test to unsuccessfuly create a museum with no name
   *
   * @author VZ
   */
  @Test
  public void testCreateMuseumWithInvalidName() {
    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());
    ResponseEntity<String> response =
        client.exchange("/api/museum/app?name=&visitFee=10", HttpMethod.POST, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("Name cannot be empty!", response.getBody(), "Response has correct error message");
  }

  /**
   * Test to unsuccessfully create an invalid museum without a visit fee
   *
   * @author VZ
   */
  @Test
  public void testCreateInvalidMuseumWithoutVisitFee() {
    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());
    ResponseEntity<String> response =
        client.exchange("/api/museum/app?name=Museum", HttpMethod.POST, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
  }

  /**
   * Test to unsuccessfully create a museum with an invalid visit fee
   *
   * @author VZ
   */
  @Test
  public void testCreateInvalidMuseumWithInvalidVisitFee() {
    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());
    ResponseEntity<String> response = client.exchange("/api/museum/app?name=Museum&visitFee=-10",
        HttpMethod.POST, entity, String.class);

    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("Visit fee cannot be negative or null!", response.getBody());
  }

  /**
   * Test to successfully edit a valid museum without a name
   *
   * @author VZ
   */
  @Test
  public void testEditValidMuseumWithoutName() {
    HttpHeaders header = loginSetupManager();

    HttpEntity<?> entity = new HttpEntity<>(header);

    Long id = testCreateMuseum(header);
    MuseumDto museumDto = new MuseumDto();
    museumDto.setMuseumId(id);

    ResponseEntity<MuseumDto> response = client.exchange(
        "/api/museum/app/edit/" + id + "/?visitFee=20", HttpMethod.POST, entity, MuseumDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("Museum", response.getBody().getName(), "Response has correct name");
    assertEquals(20, response.getBody().getVisitFee(), "Response has correct visit fee");
    assertEquals(id, response.getBody().getMuseumId(), "Response has correct ID");
  }

  /**
   * Test to successfully edit a valid museum without a visit fee
   */
  @Test
  public void testEditValidMuseumWithoutVisitFee() {
    HttpHeaders header = loginSetupManager();

    HttpEntity<?> entity = new HttpEntity<>(header);

    Long id = testCreateMuseum(header);
    MuseumDto museumDto = new MuseumDto();
    museumDto.setMuseumId(id);
    ResponseEntity<MuseumDto> response = client.exchange("/api/museum/app/edit/" + id + "/?name=RQ",
        HttpMethod.POST, entity, MuseumDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("RQ", response.getBody().getName(), "Response has correct name");
    assertEquals(10, response.getBody().getVisitFee(), "Response has correct visit fee");
    assertEquals(id, response.getBody().getMuseumId(), "Response has correct ID");
  }

  /**
   * Test to unsuccessfully edit an invalid museum with an invalid visit fee
   *
   * @author VZ
   */
  @Test
  public void testEditInvalidMuseumWithInvalidVisitFee() {
    HttpHeaders header = loginSetupManager();
    Long id = testCreateMuseum(header);
    HttpEntity<?> entity = new HttpEntity<>(header);

    MuseumDto museumDto = new MuseumDto();
    museumDto.setMuseumId(id);
    ResponseEntity<String> response =
        client.exchange("/api/museum/app/edit/" + id + "/?name=RQ&visitFee=-10", HttpMethod.POST,
            entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("Visit fee cannot be negative!", response.getBody());
  }

  /**
   * Test get all museums successfully
   *
   * @author VZ
   */
  @Test
  public void testGetAllMuseums() {
    HttpHeaders header = loginSetupManager();
    HttpEntity<?> entity = new HttpEntity<>(header);

    testCreateMuseum(header);
    testCreateMuseum(header);
    testCreateMuseum(header);
    ResponseEntity<MuseumDto[]> response =
        client.exchange("/api/museum", HttpMethod.GET, entity, MuseumDto[].class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(3, response.getBody().length, "Response has correct number of museums");
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
    ManagerDto manager = createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", manager.getSessionId());
    return headers;
  }
}
