package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dao.*;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.integration.utilities.UserUtilities;
import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import org.springframework.http.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class LoanIntegrationTest {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private LoanRepository loanRepository;
  @Autowired
  private ArtworkRepository artworkRepository;
  @Autowired
  private VisitorRepository visitorRepository;
  @Autowired
  private MuseumRepository museumRepository;
  @Autowired
  private RoomRepository roomRepository;
  @Autowired
  private ManagerRepository managerRepository;

  @BeforeEach
  @AfterEach
  public void clearDatabase() {
    loanRepository.deleteAll();
    artworkRepository.deleteAll();
    visitorRepository.deleteAll();
    roomRepository.deleteAll();
    museumRepository.deleteAll();
    managerRepository.deleteAll();
  }

  /**
   * Test suite that combines successfully creating a loan and getting that same
   * loan
   *
   * @author Eric
   */
  @Test
  public void testCreateAndGetLoan() {
    Visitor visitor = createVisitor();

    HttpHeaders headers = loginSetupVisitor(visitor);

    Long id = testCreateLoan(headers, visitor);
    testGetLoan(headers, id);
  }

  /**
   * Test to create a loan successfully
   *
   * @param Long - loanId
   * @author Eric
   */
  private Long testCreateLoan(HttpHeaders headers, Visitor visitor) {
    Artwork artwork = createArtwork();

    Loan loan = new Loan();
    loan.setRequestAccepted(null);
    loan.setArtwork(artwork);
    loan.setVisitor(visitor);
    LoanDto loanDto = DtoUtility.convertToDto(loan);

    HttpEntity<?> entity = new HttpEntity<>(loanDto, headers);

    ResponseEntity<LoanDto> response = client.exchange("/api/loan/create/", HttpMethod.POST, entity, LoanDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(null, response.getBody().getRequestAccepted(),
        "Response has correct requestAccepted");
    assertEquals(visitor.getMuseumUserId(), response.getBody().getVisitorDto().getMuseumUserId(),
        "Response has correct visitorDto");
    assertEquals(artwork.getArtworkId(), response.getBody().getArtworkDto().getArtworkId(),
        "Response has correct artworkDto");
    assertTrue(response.getBody().getLoanId() > 0, "Response has valid ID");

    return response.getBody().getLoanId();
  }

  /**
   * Test to get loan successfully given loanId
   *
   * @param loanId - Long loanId used to find loan
   * @author Eric
   */
  private void testGetLoan(HttpHeaders header, Long LoanId) {
    HttpEntity<?> entity = new HttpEntity<>(header);
    ResponseEntity<LoanDto> response = client.exchange("/api/loan/" + LoanId, HttpMethod.GET, entity, LoanDto.class);

    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
  }

  /**
   * Test to create a loan unsuccessfully due to duplicate loan
   *
   * @author Eric
   */
  @Test
  public void testCreateLoanDuplicate() {

    Artwork artwork = createArtwork();
    Visitor visitor = createVisitor();

    Loan loan = new Loan();
    loan.setRequestAccepted(null);
    loan.setArtwork(artwork);
    loan.setVisitor(visitor);
    loanRepository.save(loan);

    Loan loan2 = new Loan();
    loan2.setRequestAccepted(null);
    loan2.setArtwork(artwork);
    loan2.setVisitor(visitor);
    LoanDto loanDto = DtoUtility.convertToDto(loan2);

    HttpEntity<?> entity = new HttpEntity<>(loanDto, loginSetupVisitor(visitor));

    ResponseEntity<String> response = client.exchange("/api/loan/create/", HttpMethod.POST, entity, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Cannot create a duplicate loan request", response.getBody());
  }

  /**
   * Test to patch existing loan to requestAccepted false
   *
   * @author Eric
   */
  @Test
  public void testPatchLoanSuccessfullyToFalse() {
    Artwork artwork = createArtwork();
    Visitor visitor = createVisitor();
    Manager manager = createManager();

    Loan loan = new Loan();
    loan.setRequestAccepted(false);
    loan.setArtwork(artwork);
    loan.setVisitor(visitor);
    loanRepository.save(loan);
    LoanDto loanDto = DtoUtility.convertToDto(loan);

    HttpEntity<LoanDto> request = new HttpEntity<LoanDto>(loanDto, loginSetupManager(manager));

    ResponseEntity<LoanDto> response = client.exchange("/api/loan/edit/", HttpMethod.PUT, request, LoanDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(false, response.getBody().getRequestAccepted(),
        "Response has correct requestAccepted");
    assertEquals(visitor.getMuseumUserId(), response.getBody().getVisitorDto().getMuseumUserId(),
        "Response has correct visitorDto");
    assertEquals(artwork.getArtworkId(), response.getBody().getArtworkDto().getArtworkId(),
        "Response has correct artworkDto");
    assertTrue(response.getBody().getLoanId() > 0, "Response has valid ID");

  }

  /**
   * Test suite that combines successfully creating a loan and patching that loan
   * requestAccepted to
   * false
   *
   * @author Eric
   */
  @Test
  public void testPatchLoanSuccessfullyToTrue() {
    Artwork artwork = createArtwork();
    Room room = artwork.getRoom();
    Visitor visitor = createVisitor();
    Manager manager = createManager();

    Loan loan = new Loan();
    loan.setRequestAccepted(true);
    loan.setArtwork(artwork);
    loan.setVisitor(visitor);
    loanRepository.save(loan);
    LoanDto loanDto = DtoUtility.convertToDto(loan);

    HttpEntity<LoanDto> request = new HttpEntity<LoanDto>(loanDto, loginSetupManager(manager));

    ResponseEntity<LoanDto> response = client.exchange("/api/loan/edit/", HttpMethod.PUT, request, LoanDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(true, response.getBody().getRequestAccepted(),
        "Response has correct requestAccepted");
    assertEquals(visitor.getMuseumUserId(), response.getBody().getVisitorDto().getMuseumUserId(),
        "Response has correct visitorDto");
    assertEquals(artwork.getArtworkId(), response.getBody().getArtworkDto().getArtworkId(),
        "Response has correct artworkDto");
    assertNull(response.getBody().getArtworkDto().getRoom(),
        "Artwork is no longer associated to room");
    assertTrue(response.getBody().getLoanId() > 0, "Response has valid ID");
    assertEquals(0, roomRepository.findRoomByRoomId(room.getRoomId()).getCurrentNumberOfArtwork(),
        "Room that previously had artwork has now 1 less artwork");

  }

  /**
   * Test to get all loans
   *
   * @author Eric
   */
  @Test
  public void testGetAllLoans() {

    Artwork artwork = createArtwork();
    Visitor visitor = createVisitor();
    Manager manager = createManager();

    Loan loan = new Loan();
    loan.setRequestAccepted(null);
    loan.setArtwork(artwork);
    loan.setVisitor(visitor);
    loanRepository.save(loan);

    // Creating an artwork
    Artwork artwork2 = new Artwork();
    artwork2.setName("La");
    artwork2.setArtist("Da Vinci");
    artwork2.setIsAvailableForLoan(true);
    artwork2.setLoanFee(110.99);
    artwork2.setImage(".jpg");
    artwork2.setIsOnLoan(false);
    artwork2.setRoom(artwork.getRoom());
    artworkRepository.save(artwork2);

    Loan loan2 = new Loan();
    loan2.setRequestAccepted(null);
    loan2.setArtwork(artwork2);
    loan2.setVisitor(visitor);
    loanRepository.save(loan2);

    // Creating an artwork
    Artwork artwork3 = new Artwork();
    artwork3.setName("Bruh");
    artwork3.setArtist("Monet");
    artwork3.setIsAvailableForLoan(true);
    artwork3.setLoanFee(110.99);
    artwork3.setImage(".org/wikipedia/commons/thumb/6/6b/La_Joconde.jpg/800px-La_Joconde.jpg");
    artwork3.setIsOnLoan(false);
    artwork3.setRoom(artwork.getRoom());
    artworkRepository.save(artwork3);

    Loan loan3 = new Loan();
    loan3.setRequestAccepted(null);
    loan3.setArtwork(artwork3);
    loan3.setVisitor(visitor);
    loanRepository.save(loan3);

    HttpEntity<?> request = new HttpEntity<>(loginSetupManager(manager));

    ResponseEntity<LoanDto[]> response = client.exchange("/api/loan/", HttpMethod.GET, request, LoanDto[].class);

    assertNotNull(response);
    assertEquals(HttpStatus.FOUND, response.getStatusCode());
    assertEquals(3, response.getBody().length, "Request has the expected number of elements");
  }

  /**
   * Test to delete loan successfully
   *
   * @author Eric
   */
  @Test
  public void testDeleteLoan() {
    Artwork artwork = createArtwork();
    Visitor visitor = createVisitor();

    Loan loan = new Loan();
    loan.setRequestAccepted(null);
    loan.setArtwork(artwork);
    loan.setVisitor(visitor);
    loanRepository.save(loan);
    Long loanId = loan.getLoanId();

    HttpEntity<?> request = new HttpEntity<>(loginSetupVisitor(visitor));

    ResponseEntity<String> response = client.exchange("/api/loan/delete/" + loanId, HttpMethod.DELETE, request,
        String.class);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Loan deleted", response.getBody());
  }

  /**
   * Test to delete loan given a loanId of a loan that does not exist
   *
   * @author Eric
   */
  @Test
  public void testDeleteLoanWithNonExistingLoan() {
    Long loanId = (long) -1;

    HttpEntity<?> request = new HttpEntity<>(loginSetupVisitor(createVisitor()));

    ResponseEntity<String> response = client.exchange("/api/loan/delete/" + loanId, HttpMethod.DELETE, request,
        String.class);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull("Loan does not exist", response.getBody());
  }

  /**
   * Helper method to create artwork associated to a room associated to a museum
   * associated to a
   * schedule
   *
   * @return artwork
   * @author Eric
   */
  public Artwork createArtwork() {
    // Create a schedule
    Schedule schedule = new Schedule();

    // Creating a museum
    Museum museum = new Museum();
    museum.setName("Rougon-Macquart");
    museum.setVisitFee(12.5);
    museum.setSchedule(schedule);
    museumRepository.save(museum);

    // Creating a room
    Room room = new Room();
    room.setRoomName("Room 1");
    room.setRoomType(RoomType.Small);
    room.setCurrentNumberOfArtwork(1);
    room.setMuseum(museum);
    roomRepository.save(room);

    // Creating an artwork
    Artwork artwork = new Artwork();
    Long artworkId = (long) 1;
    artwork.setArtworkId(artworkId);
    artwork.setName("La Joconde");
    artwork.setArtist("Leonardo Da Vinci");
    artwork.setIsAvailableForLoan(true);
    artwork.setLoanFee(110.99);
    artwork.setImage(
        "https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/La_Joconde.jpg/800px-La_Joconde.jpg");
    artwork.setIsOnLoan(false);
    artwork.setRoom(room);
    return artworkRepository.save(artwork);

  }

  /**
   * Helper method to create visitor
   *
   * @return visitor
   * @author Eric
   */
  public Visitor createVisitor() {
    // Creating a visitor
    Visitor visitor = new Visitor();
    Long visitorId = (long) 1;
    visitor.setMuseumUserId(visitorId);
    visitor.setEmail("Please@email.com");
    visitor.setName("Please");
    visitor.setPassword("password");
    return visitorRepository.save(visitor);
  }

  /**
   * Helper method to create manager
   *
   * @return manager
   * @author Eric
   */
  public Manager createManager() {
    // Creating a manager
    Manager manager = new Manager();
    Long managerId = (long) 1;
    manager.setMuseumUserId(managerId);
    manager.setEmail("Please@email.com");
    manager.setName("Please");
    manager.setPassword("password");
    return managerRepository.save(manager);
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

  /**
   * Create an manager and login
   *
   * @param newManager - the manager to login
   * @return the logged in manager
   * @author Eric
   */

  public ManagerDto createManagerAndLogin(Manager newManager) {
    ManagerDto manager = UserUtilities.createManagerDto(newManager);
    ResponseEntity<String> response = client.postForEntity("/api/auth/login", manager, String.class);
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
   * @author Eric
   */
  public HttpHeaders loginSetupManager(Manager newManager) {
    ManagerDto manager = createManagerAndLogin(newManager);

    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", manager.getSessionId());
    return headers;
  }
}
