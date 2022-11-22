package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.integration.utilities.UserUtilities;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketIntegrationTests {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private VisitorRepository visitorRepository;

  // visitor 1 attributes
  private static final String VISITOR_EMAIL_1 = "bob@mail.com";
  private static final String VISITOR_NAME_1 = "Bob Barbier";
  private static final String VISITOR_PASSWORD_1 = "MyPassword";

  // visitor 2 attributes
  private static final String VISITOR_EMAIL_2 = "marie@mail.com";
  private static final String VISITOR_NAME_2 = "Marie B";
  private static final String VISITOR_PASSWORD_2 = "password";
  private static final String VISIT_DATE_2 = "2023-04-20";
  private static final String VISIT_DATE_1 = "2023-10-05";

  @BeforeEach
  public void setUp() {
    // clear all repositories
    ticketRepository.deleteAll();
    visitorRepository.deleteAll();
  }

  @AfterEach
  public void clearDatabase() {
    ticketRepository.deleteAll();
    visitorRepository.deleteAll();
  }

  /**
   * Test to get all tickets possessed by an invalid visitor
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketByInvalidVisitor() {
    Visitor visitor =
        UserUtilities.createVisitor(VISITOR_EMAIL_1, VISITOR_NAME_1, VISITOR_PASSWORD_1);
    visitorRepository.save(visitor);
    HttpEntity<?> entity = new HttpEntity<>(loginSetupVisitor(visitor));
    ResponseEntity<String> response =
        client.exchange("/api/ticket/visitor/" + -1 + "/", HttpMethod.GET, entity, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertEquals("You are not authorized to view this page", response.getBody(),
        "Correct response body message");
  }

  /**
   * Test to get all tickets possessed by an existing visitor
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketsByVisitor() {
    Visitor visitor =
        UserUtilities.createVisitor(VISITOR_EMAIL_1, VISITOR_NAME_1, VISITOR_PASSWORD_1);
    long visitorId = visitorRepository.save(visitor).getMuseumUserId();
    visitorRepository.save(visitor);

    createTicket(VISIT_DATE_1, visitor);
    createTicket(VISIT_DATE_2, visitor);

    HttpEntity<?> entity = new HttpEntity<>(loginSetupVisitor(visitor));

    ResponseEntity<TicketDto[]> response = client.exchange("/api/ticket/visitor/" + visitorId + "/",
        HttpMethod.GET, entity, TicketDto[].class);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(2, response.getBody().length);
    assertEquals(visitorId, response.getBody()[0].getVisitor().getMuseumUserId());
    assertTrue(VISIT_DATE_1.equals(response.getBody()[0].getVisitDate()));
    assertTrue(VISIT_DATE_2.equals(response.getBody()[1].getVisitDate()));



  }

  /**
   * Test to successfully create tickets
   *
   * @author Zahra
   */
  @Test
  public void testCreateTickets() {
    int numOfTickets = 2;
    Visitor visitor =
        UserUtilities.createVisitor(VISITOR_EMAIL_1, VISITOR_NAME_1, VISITOR_PASSWORD_1);
    visitorRepository.save(visitor);
    TicketDto ticketDto = new TicketDto(VISIT_DATE_1, DtoUtility.convertToDto(visitor));

    HttpEntity<?> entity = new HttpEntity<>(ticketDto, loginSetupVisitor(visitor));
    ResponseEntity<TicketDto[]> response = client.exchange(
        "/api/ticket/purchase?number=" + numOfTickets, HttpMethod.POST, entity, TicketDto[].class);

    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(numOfTickets, response.getBody().length);
    assertEquals(VISIT_DATE_1, response.getBody()[0].getVisitDate());
    assertEquals(visitor.getMuseumUserId(), response.getBody()[0].getVisitor().getMuseumUserId());
  }

  /**
   * Test to create tickets with invalid number
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketsWithInvalidNumber() {
    int numOfTickets = 0;
    Visitor visitor =
        UserUtilities.createVisitor(VISITOR_EMAIL_1, VISITOR_NAME_1, VISITOR_PASSWORD_1);
    visitorRepository.save(visitor);
    TicketDto ticketDto = new TicketDto(VISIT_DATE_1, DtoUtility.convertToDto(visitor));

    HttpEntity<?> entity = new HttpEntity<>(ticketDto, loginSetupVisitor(visitor));
    ResponseEntity<String> response = client.exchange("/api/ticket/purchase?number=" + numOfTickets,
        HttpMethod.POST, entity, String.class);

    assertNotNull(response, "Response has body");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Number of tickets must be at least 1", response.getBody());

  }

  // Create ticket
  public Ticket createTicket(String visitDate, Visitor visitor) {
    Ticket ticket = new Ticket();
    ticket.setVisitDate(Date.valueOf(visitDate));
    ticket.setVisitor(visitorRepository.findVisitorByMuseumUserId(visitor.getMuseumUserId()));
    ticketRepository.save(ticket);
    return ticket;
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
