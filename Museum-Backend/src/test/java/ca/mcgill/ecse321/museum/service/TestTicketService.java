package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;


/**
 * Test class for TicketService class
 *
 * @author Zahra
 */
@ExtendWith(MockitoExtension.class)
public class TestTicketService {

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private VisitorRepository visitorRepository;

  @InjectMocks
  private TicketService ticketService;

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
  private static final int TICKETS_VISITOR_2 = 3;
  private static final long TICKET_ID_2 = 666;
  private static final long TICKET_ID_1 = 555;
  private static final long TICKET_ID_3 = 444;


  /**
   * Method to set up mock objects (tickets and visitors)
   */
  @BeforeEach
  public void setMockOutput() {

    lenient().when(visitorRepository.findVisitorByMuseumUserId(anyLong()))
            .thenAnswer((InvocationOnMock invocation) -> {
              if (invocation.getArgument(0).equals(VISITOR_ID_1)) {
                Visitor visitor = new Visitor();
                visitor.setEmail(VISITOR_EMAIL_1);
                visitor.setName(VISITOR_NAME_1);
                visitor.setPassword(VISITOR_PASSWORD_1);
                visitor.setMuseumUserId(VISITOR_ID_1);
                return visitor;
              } else if (invocation.getArgument(0).equals(VISITOR_ID_2)) {
                Visitor visitor2 = new Visitor();
                visitor2.setEmail(VISITOR_EMAIL_2);
                visitor2.setName(VISITOR_NAME_2);
                visitor2.setPassword(VISITOR_PASSWORD_2);
                visitor2.setMuseumUserId(VISITOR_ID_2);

                return visitor2;
              } else {
                return null;
              }

            });

    lenient().when(ticketRepository.findTicketByVisitor(any()))
            .thenAnswer((InvocationOnMock invocation) -> {
              Visitor visitor = invocation.getArgument(0);

              List<Ticket> allTickets = new ArrayList<>();
              Ticket ticket1 = new Ticket();
              ticket1.setTicketId(TICKET_ID_1);
              ticket1
                      .setVisitor(visitorRepository.findVisitorByMuseumUserId(visitor.getMuseumUserId()));
              ticket1.setVisitDate(Date.valueOf(VISIT_DATE_1));
              allTickets.add(ticket1);

              Ticket ticket2 = new Ticket();
              ticket2.setTicketId(TICKET_ID_2);
              ticket2.setVisitDate(Date.valueOf(VISIT_DATE_2));
              ticket2
                      .setVisitor(visitorRepository.findVisitorByMuseumUserId(visitor.getMuseumUserId()));
              allTickets.add(ticket2);

              Ticket ticket3 = new Ticket();
              ticket3.setTicketId(TICKET_ID_3);
              ticket3
                      .setVisitor(visitorRepository.findVisitorByMuseumUserId(visitor.getMuseumUserId()));
              ticket3.setVisitDate(Date.valueOf(VISIT_DATE_2));
              allTickets.add(ticket3);
              return allTickets;
            });
    
  }

  /**
   * Test that a ticket can be created successfully.
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicket() {

    Date visitDate = Date.valueOf("2023-10-10");
    Ticket ticket = null;
    try {
      ticket = ticketService.createTicket(VISITOR_ID_1, visitDate);
    } catch (Exception exception) {

      fail(exception.getMessage());

    }
    assertNotNull(ticket);
    assertEquals(VISITOR_NAME_1, ticket.getVisitor().getName());

  }

  /**
   * Test that a ticket cannot be created without a date
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketWithNoDate() {
    Ticket ticket = null;
    String error = "";
    Date visitDate = null;
    long visitorID = VISITOR_ID_1;
    try {
      ticket = ticketService.createTicket(visitorID, visitDate);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(ticket);
    assertEquals("Please insert a date", error);

  }

  /**
   * Test that a visitor cannot be created with an invalid date.
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketWithInvalidDate() {
    Ticket ticket = null;
    String error = "";
    String visitDate = INVALID_DATE;
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(VISITOR_ID_1);
    long visitorID = VISITOR_ID_1;
    try {
      ticket = ticketService.createTicket(visitorID, Date.valueOf(visitDate));
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNotNull(visitor);
    assertNull(ticket);
    assertEquals("Cannot pick a date in the past.", error);

  }


  /**
   * Test that a ticket cannot be created with an invalid visitor ID
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketWithNonExistingVisitor() {
    Ticket ticket = null;
    String visitDate = "2023-04-21";
    long visitorId = 0;
    String error = "";
    try {
      ticket = ticketService.createTicket(visitorId, Date.valueOf(visitDate));
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(ticket);
    assertEquals("Visitor doesn't exist", error);


  }

  /**
   * Test that a list of tickets can be successfully created
   *
   * @author Zahra
   */
  @Test
  public void testCreateTickets() {
    List<Ticket> createdTickets = null;
    String visitDate = VISIT_DATE_2;
    int number = 4;
    long visitorID = VISITOR_ID_1;
    try {
      createdTickets = ticketService.createTickets(visitorID, Date.valueOf(visitDate), number);

    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(createdTickets);
    assertEquals(number, createdTickets.size());
    assertEquals(Date.valueOf(visitDate), createdTickets.get(1).getVisitDate());
    assertEquals(VISITOR_NAME_1, createdTickets.get(1).getVisitor().getName());

  }

  /**
   * Test that a list of tickets cannot be created with an invalid visitor ID.
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketsWithNonExistingVisitor() {

    List<Ticket> createdTickets = null;
    String visitDate = "2023-04-10";
    int number = 5;
    long visitorId = 0;
    String error = "";
    try {
      createdTickets = ticketService.createTickets(visitorId, Date.valueOf(visitDate), number);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(createdTickets);
    assertEquals("Visitor doesn't exist", error);
  }

  /**
   * Test that a list of tickets cannot be create with an invalid number
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketsWithInvalidNumber() {
    List<Ticket> createdTickets = null;
    String visitDate = "2023-04-22";
    String error = "";
    long visitorID = VISITOR_ID_1;
    int number = 0;
    try {
      createdTickets = ticketService.createTickets(visitorID, Date.valueOf(visitDate), number);
    } catch (IllegalArgumentException e) {

      error = e.getMessage();
    }

    assertNull(createdTickets);
    assertEquals("Number of tickets must be at least 1", error);

  }

  /**
   * Test that a list of tickets cannot be create with an invalid date
   *
   * @author Zahra
   */
  @Test
  public void testCreateTicketsWithInvalidDate() {
    List<Ticket> createdTickets = null;
    String visitDate = INVALID_DATE;
    String error = "";
    long visitorID = VISITOR_ID_1;
    int number = 7;
    try {
      createdTickets = ticketService.createTickets(visitorID, Date.valueOf(visitDate), number);
    } catch (IllegalArgumentException e) {

      error = e.getMessage();
    }

    assertNull(createdTickets);
    assertEquals("Cannot pick a date in the past.", error);

  }

  /**
   * Test that getTicketsByVisitor can be successfully done
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketsByVisitor() {
    List<Ticket> allTicketsOfVisitor = null;

    try {
      allTicketsOfVisitor = ticketService.getTicketsByVisitor(VISITOR_ID_2);
      assertNotNull(allTicketsOfVisitor);

    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }
    assertEquals(TICKETS_VISITOR_2, allTicketsOfVisitor.size());
    assertEquals(VISIT_DATE_2, allTicketsOfVisitor.get(1).getVisitDate().toString());
    assertEquals(VISITOR_ID_2, allTicketsOfVisitor.get(1).getVisitor().getMuseumUserId());
  }

  /**
   * Test that tickets cannot be got with an invalid visitor's ID
   *
   * @author Zahra
   */
  @Test
  public void testGetTicketsWithNonExistingVisitor() {
    Visitor visitor = null;
    String error = null;
    List<Ticket> allTicketsOfVisitor = new ArrayList<>();
    try {
      allTicketsOfVisitor = ticketService.getTicketsByVisitor(0);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNotNull(error);
    assertEquals(error, "Visitor doesn't exist");
  }

}
