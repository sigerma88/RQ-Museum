package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketIntegrationTests {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private VisitorRepository visitorRepository;

  //visitor 1 attributes
  private static final String VISITOR_EMAIL_1 = "bob@mail.com";
  private static final String VISITOR_NAME_1 = "Bob Barbier";
  private static final String VISITOR_PASSWORD_1 = "MyPassword";

  //visitor 2 attributes
  private static final String VISITOR_EMAIL_2 = "marie@mail.com";
  private static final String VISITOR_NAME_2 = "Marie B";
  private static final String VISITOR_PASSWORD_2 = "password";
  private static final String VISIT_DATE_2 = "2023-04-20";
  private static final String VISIT_DATE_1 = "2023-10-05";

  @BeforeEach
  public void setUp() {
    //clear all repositories
    ticketRepository.deleteAll();
    visitorRepository.deleteAll();

    //create visitors
    Visitor visitor1 = new Visitor();
    visitor1.setName(VISITOR_NAME_1);
    visitor1.setPassword(VISITOR_PASSWORD_1);
    visitor1.setEmail(VISITOR_EMAIL_1);
    visitorRepository.save(visitor1);

    Visitor visitor2 = new Visitor();
    visitor2.setEmail(VISITOR_EMAIL_2);
    visitor2.setName(VISITOR_NAME_2);
    visitor2.setPassword(VISITOR_PASSWORD_2);
    visitorRepository.save(visitor2);

    //create tickets
    Ticket ticket1 = new Ticket();
    ticket1.setVisitor(visitorRepository.findVisitorByName(VISITOR_NAME_1));
    ticket1.setVisitDate(Date.valueOf(VISIT_DATE_1));
    ticketRepository.save(ticket1);

    Ticket ticket2 = new Ticket();
    ticket2.setVisitDate(Date.valueOf(VISIT_DATE_2));
    ticket2.setVisitor(visitorRepository.findVisitorByName(VISITOR_NAME_2));
    ticketRepository.save(ticket2);

  }

  @AfterEach
  public void clearDatabase() {
    ticketRepository.deleteAll();
    visitorRepository.deleteAll();
  }

  /**
   * Test to get all tickets possessed by an  invalid visitor
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketByInvalidVisitor() {
    ResponseEntity<String> response = client.getForEntity("/api/ticket/visitor/" + -1 + "/", String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Visitor doesn't exist", response.getBody(), "Correct response body message");
  }

  /**
   * Test to get all tickets possessed by an existing visitor
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketsByVisitor() {

    long visitorId = visitorRepository.findVisitorByName(VISITOR_NAME_1).getMuseumUserId();
    ResponseEntity<TicketDto[]> response =
        client.getForEntity("/api/ticket/visitor/" + visitorId + "/", TicketDto[].class);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, response.getBody().length);
    assertEquals(visitorId, response.getBody()[0].getVisitor().getMuseumUserId());
    assertTrue(VISIT_DATE_1.equals(response.getBody()[0].getVisitDate()));

  }

  /**
   * Test to successfully create tickets
   *
   * @author Zahra
   */
  @Test
  public void testCreateTickets() {
    int numOfTickets = 2;
    Visitor visitor = visitorRepository.findVisitorByName(VISITOR_NAME_2);
    TicketDto ticketDto = new TicketDto(VISIT_DATE_1, DtoUtility.convertToDto(visitor));

    ResponseEntity<TicketDto[]> response = client
        .postForEntity("/api/ticket/purchase?number=" + numOfTickets, ticketDto, TicketDto[].class);
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
    TicketDto ticketDto = new TicketDto(VISIT_DATE_1, DtoUtility.convertToDto(visitorRepository.findVisitorByName(VISITOR_NAME_1)));

    ResponseEntity<String> response =
        client.postForEntity("/api/ticket/purchase?number=" + numOfTickets, ticketDto, String.class);
    assertNotNull(response, "Response has body");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Number of tickets must be at least 1", response.getBody());

  }

}
