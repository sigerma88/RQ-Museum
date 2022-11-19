package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;
import com.sun.xml.bind.v2.schemagen.xmlschema.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;

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

  private static final LocalDate LOCAL_VISIT_DATE_1 = LocalDate.of(2023, 10, 05); //valueOf("2023-10-05") ;
  private static final String VISIT_DATE_2 = "2023-04-20";
  private static final String VISIT_DATE_1 = "2023-10-05";
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

    ticketRepository.deleteAll();
    visitorRepository.deleteAll();

    Visitor visitor1 = new Visitor();
    visitor1.setName(VISITOR_NAME_1);
    visitor1.setPassword(VISITOR_PASSWORD_1);
    visitor1.setEmail(VISITOR_EMAIL_1);
    // visitor1.setMuseumUserId(VISITOR_ID_1);
    visitorRepository.save(visitor1);


    Visitor visitor2 = new Visitor();
    visitor2.setEmail(VISITOR_EMAIL_2);
    visitor2.setName(VISITOR_NAME_2);
    visitor2.setPassword(VISITOR_PASSWORD_2);
    //visitor2.setMuseumUserId(VISITOR_ID_2);
    visitorRepository.save(visitor2);

    Ticket ticket1 = new Ticket();
    //ticket1.setTicketId(TICKET_ID_1);
    ticket1.setVisitor(visitorRepository.findVisitorByName(VISITOR_NAME_1));
    ticket1.setVisitDate(Date.valueOf(VISIT_DATE_1));
    ticketRepository.save(ticket1);


    Ticket ticket2 = new Ticket();
    //ticket2.setTicketId(TICKET_ID_2);
    ticket2.setVisitDate(Date.valueOf(VISIT_DATE_2));
    ticket2.setVisitor(visitorRepository.findVisitorByName(VISITOR_NAME_2));
    ticketRepository.save(ticket2);


  }

  //done

  /**
   * Test for getTicketsByVisitor
   * Fail scenario : invalid user Id given
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketByInvalidVisitor() {
    ResponseEntity<String> response = client.getForEntity("/tickets/" + -1 + "/", String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Visitor doesn't exist", response.getBody(), "Correct response body message");
  }

  /**
   * Test for getTicketsByVisitor
   * Success scenario
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketsByVisitor() {

    long visitorId = visitorRepository.findVisitorByName(VISITOR_NAME_1).getMuseumUserId();
    ResponseEntity<TicketDto[]> response = client.getForEntity("/tickets/" + visitorId + "/", TicketDto[].class);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().length);
    assertEquals(VISITOR_NAME_1, response.getBody()[0].getVisitor().getName());
    //assertEquals(Date.valueOf(VISIT_DATE_1), response.getBody()[0].getVisitDate());

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
    // ArrayList<TicketDto> ticketDtos =

    ResponseEntity<TicketDto[]> response = client.postForEntity("/tickets/purchase/?number=" + numOfTickets + "/", new ArrayList<TicketDto>(), TicketDto[].class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(numOfTickets, response.getBody().length);
    //assertEquals(VISIT_DATE_2, Arrays.stream(response.getBody()).toList().get(1).getVisitDate().toString());
    //assertEquals(VISITOR_NAME_2, response.getBody().get(1).getVisitor().getName());
  }

  /**
   * Test for createTickets
   * Fail scenario : given number of Tickets is invalid
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketsWithInvalidNumber() {
    ResponseEntity<String> response = client.postForEntity("/tickets/purchase/?visitor=" + VISITOR_ID_2 + "&date=" + LOCAL_VISIT_DATE_1 + "&number=" + 0 + "/", new ArrayList<TicketDto>(), String.class);
    assertNotNull(response, "Response has body");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertTrue(response.getBody().contains("Number of tickets must be at least 1"));

  }


}
