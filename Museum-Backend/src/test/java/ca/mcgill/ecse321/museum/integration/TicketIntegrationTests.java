package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketIntegrationTests {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private VisitorRepository visitorRepository;


  private static final long VISITOR_ID_1 = 777;
  private static final String VISITOR_EMAIL_1 = "bob@mail.com";
  private static final String VISITOR_NAME_1 = "Bob Barbier";
  private static final String VISITOR_PASSWORD_1 = "MyPassword";

  private static final long VISITOR_ID_2 = 768;
  private static final String VISITOR_EMAIL_2 = "marie@mail.com";
  private static final String VISITOR_NAME_2 = "Marie B";
  private static final String VISITOR_PASSWORD_2 = "password";

  private static final String VISIT_DATE_1 = "2023-10-05";
  private static final String VISIT_DATE_2 = "2023-04-20";
  private static final String INVALID_DATE = "2020-10-03";
  private static final int TICKETS_VISITOR_2 = 2;
  private static final long TICKET_ID_2 = 666;
  private static final long TICKET_ID_1 = 555;
  private static final long TICKET_ID_3 = 444;


  @AfterEach
  public void clearDatabase() {
    ticketRepository.deleteAll();
    visitorRepository.deleteAll();
  }

  @BeforeEach
  public void setUp() {

    Visitor visitor1 = new Visitor();
    visitor1.setName(VISITOR_NAME_1);
    visitor1.setPassword(VISITOR_PASSWORD_1);
    visitor1.setEmail(VISITOR_EMAIL_1);
    visitor1.setMuseumUserId(VISITOR_ID_1);
    visitorRepository.save(visitor1);

    Visitor visitor2 = new Visitor();
    visitor2.setEmail(VISITOR_EMAIL_2);
    visitor2.setName(VISITOR_NAME_2);
    visitor2.setPassword(VISITOR_PASSWORD_2);
    visitor2.setMuseumUserId(VISITOR_ID_2);
    visitorRepository.save(visitor2);

    Ticket ticket1 = new Ticket();
    ticket1.setTicketId(TICKET_ID_1);
    ticket1.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID_1));
    ticket1.setVisitDate(Date.valueOf(VISIT_DATE_1));
    ticketRepository.save(ticket1);

    Ticket ticket2 = new Ticket();
    ticket2.setTicketId(TICKET_ID_2);
    ticket2.setVisitDate(Date.valueOf(VISIT_DATE_2));
    ticket2.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID_2));
    ticketRepository.save(ticket2);


  }


  /**
   * Test for getTicketsByVisitor
   * Fail scenario : invalid user Id given
   *
   * @author Zahra
   */

  @Test
  public void testGetTicketByInvalidVisitor() {
    ResponseEntity<String> response = client.getForEntity("/visitor/tickets/" + -1, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    //assertTrue(response.getBody().contains("Visitor doesn't exist"));
    // assertEquals("Visitor doesn't exist", response.getBody());
  }

  /**
   * Test for getTicketsByVisitor
   * Success scenario
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketsByVisitor() {
    // List<TicketDto> ticketDtoList = createTicketDto();

    ResponseEntity<TicketDto[]> response = client.getForEntity("/tickets/" + VISITOR_ID_1, TicketDto[].class);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(VISITOR_ID_1, Arrays.stream(response.getBody()).toList().get(1).getVisitor().getUserId());
  }

  /**
   * Test for createTickets
   * Success scenario
   *
   * @author Zahra
   */
  @Test
  public void testCreateTickets() {
    int numOfTickets = 2;
    ResponseEntity<TicketDto[]> response = client.postForEntity("ticket/purchase/?visitorId=" + VISITOR_ID_2 + "&visitDate=" + Date.valueOf(VISIT_DATE_2) + "&number=" + numOfTickets, new ArrayList<TicketDto>(), TicketDto[].class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(numOfTickets, response.getBody().length);
    assertEquals(VISIT_DATE_2, Arrays.stream(response.getBody()).toList().get(1).getVisitDate().toString());
    assertEquals(VISITOR_ID_2, Arrays.stream(response.getBody()).toList().get(1).getVisitor().getUserId());
  }

  /**
   * Test for createTickets
   * Fail scenario : given number of Tickets is invalid
   *
   * @author Zahra
   */
  @Test
  public void testCreateicketsWithInvalidNumber() {
    ResponseEntity<String> response = client.postForEntity("ticket/purchase/?visitorId=" + VISITOR_ID_2 + "&visitDate=" + Date.valueOf(VISIT_DATE_2) + "&number=" + 0, new ArrayList<TicketDto>(), String.class);
    assertNotNull(response, "Response has body");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("Number of tickets must be at least 1"));
  }

  /**
   * Test for createTickets
   * Fail scenario : given date is invalid
   *
   * @author Zahra
   */
  /*@Test

  public void testBuyTicketsWithInvalidDate() {
    int numberOfArtworks = 3;
    ResponseEntity<String> response = client.postForEntity("ticket/purchase/?visitorId=" + VISITOR_ID_2 + "&visitDate=" + Date.valueOf(INVALID_DATE) + "&number=" + numberOfArtworks, new ArrayList<TicketDto>(), String.class);
    assertNotNull(response, "Response has body");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("Number of tickets must be at least 1"));

  }
  */


}
