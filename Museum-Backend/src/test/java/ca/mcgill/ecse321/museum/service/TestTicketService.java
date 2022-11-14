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

  private static final String VISIT_DATE_1 = "10/05/2023";
  private static final String VISIT_DATE_2 = "20/04/2023";
  private static final String VISIT_DATE_3 = "20/03/2023";
  private static final int TICKETS_VISITOR_2 = 4;
  private static final long TICKET_ID_2 = 666;
  private static final long TICKET_ID_1 = 555;
  private static final long TICKET_ID_3 = 444;


  @BeforeEach
  public void setMockOutput() {

    lenient().when(visitorRepository.findVisitorByMuseumUserId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(VISITOR_ID_1)) {
        Visitor visitor = new Visitor();
        visitor.setEmail(VISITOR_EMAIL_1);
        visitor.setName(VISITOR_NAME_1);
        visitor.setPassword(VISITOR_PASSWORD_1);
        visitor.setMuseumUserId(VISITOR_ID_1);
        return visitor;
      } else {
        return null;
      }
    });

    lenient().when(visitorRepository.findVisitorByMuseumUserId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(VISITOR_ID_2)) {
        Visitor visitor = new Visitor();
        visitor.setEmail(VISITOR_EMAIL_2);
        visitor.setName(VISITOR_NAME_2);
        visitor.setPassword(VISITOR_PASSWORD_2);
        visitor.setMuseumUserId(VISITOR_ID_2);
        ticketService.createTickets(VISITOR_ID_2, Date.valueOf(VISIT_DATE_2), TICKETS_VISITOR_2);
        return visitor;
      } else {
        return null;
      }
    });

    lenient().when(ticketRepository.findTicketByTicketId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(TICKET_ID_1)) {
        Ticket ticket = new Ticket();
        ticket.setTicketId(TICKET_ID_1);
        ticket.setVisitDate(Date.valueOf(VISIT_DATE_1));
        return ticket;
      } else {
        return null;
      }

    });
    //lenient().when(ticketRepository.save(any(Ticket.class))).thenAnswer( );

  }

  //DONE
  @Test
  public void createTicket() {

    Date visitDate = Date.valueOf("2023-10-10");
    long visitorID = VISITOR_ID_1;
    Ticket ticket = null;
    try {
      ticket = ticketService.createTicket(visitorID, visitDate);
    } catch (Exception exception) {

      fail(exception.getMessage());

    }
    assertNotNull(ticket);
    assertEquals(ticket.getVisitor().getName(), VISITOR_NAME_1);

  }

  //done
  @Test
  public void createTicketWithNoDate() {
    Ticket ticket = null;
    String error = "";
    Date visitDate = null;
    long visitorID = VISITOR_ID_1;
    try {
      ticket = ticketService.createTicket(visitorID, visitDate);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNotNull(error);
    assertNull(ticket);
    assertEquals("Please insert a date", error);

  }


  //done
  @Test
  public void createTicketWithInvalidDate() {
    Ticket ticket = null;
    String error = "";
    String visitDate = "2021-11-11";
    long visitorID = VISITOR_ID_1;
    try {
      ticket = ticketService.createTicket(visitorID, Date.valueOf(visitDate));
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(ticket);
    assertEquals("Cannot pick a date in the past.", error);

  }


  @Test
  public void createTicketWithNonExistingVisitor() {
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


  @Test
  public void createTickets() {
    List<Ticket> createdTickets = new ArrayList<>();

  }

  //done
  @Test
  public void createTicketsWithNonExistingVisitor() {

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

  //done
  @Test
  public void createTicketsWithInvalidNumber() {
    List<Ticket> createdTickets = null;
    String visitDate = "2023-04-22";
    String error = "";
    long visitorID = VISITOR_ID_1;
    int number = 0;
    try {
      ticketService.createTickets(visitorID, Date.valueOf(visitDate), number);
    } catch (IllegalArgumentException e) {

      error = e.getMessage();
    }

    assertNull(createdTickets);
    assertEquals("Number of tickets must be at least 1", error);

  }

  @Test
  public void createTicketsWithInvalidDate() {
    List<Ticket> createdTickets = null;
    String visitDate = "2020-04-22";
    String error = "";
    long visitorID = VISITOR_ID_1;
    int number = 7;
    try {
      ticketService.createTickets(visitorID, Date.valueOf(visitDate), number);
    } catch (IllegalArgumentException e) {

      error = e.getMessage();
    }

    assertNull(createdTickets);
    assertEquals("Cannot pick a date in the past.", error);

  }

  /**
   * Success scenario : getting all tickets
   */

  @Test
  public void getTicketsByVisitor() {
    List<Ticket> allTicketsOfVisitor = new ArrayList<>();
    long visitorId = VISITOR_ID_2;
    try {
      allTicketsOfVisitor = ticketService.getTicketsByVisitor(visitorRepository.findVisitorByMuseumUserId(visitorId));

    } catch (Exception e) {

    }

    assertNotNull(allTicketsOfVisitor);
    assertEquals(TICKETS_VISITOR_2, allTicketsOfVisitor.size());
    for (int i = 0; i <= allTicketsOfVisitor.size(); i++)
      assertEquals(VISIT_DATE_2, allTicketsOfVisitor.get(i));

  }

  /**
   * Getting all tickets
   * Fail scenario 1 : visitor doesn't exist
   */

  @Test
  public void getAllTicketsWithNonExistingVisitor() {
    Visitor visitor = null;
    String error = null;
    List<Ticket> allTicketsOfVisitor = new ArrayList<>();
    try {
      allTicketsOfVisitor = ticketService.getTicketsByVisitor(visitor);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNotNull(error);
    assertEquals(error, "Visitor doesn't exist");

  }


}
