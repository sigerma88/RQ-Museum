package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Date;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;

/**
 * Test class for the TicketRepository
 * @author VZ
 * 
 * Since the Ticket class has a unidirectional association with the Visitor class, we must create
 * both a ticket object and a visitor object in order to test the TicketRepository.
 */
@SpringBootTest
public class TicketRepositoryTests {
  @Autowired
  private TicketRepository ticketRepository;

  @Autowired
  private VisitorRepository visitorRepository;

  @AfterEach
  public void clearDatabase() {
      ticketRepository.deleteAll();
      visitorRepository.deleteAll();
  }

  @Test
  public void testPersistandLoadTicket() {
    //1.create object

    //create ticket and attribute
    Ticket ticket = new Ticket();
    Date visitDate = Date.valueOf("2020-01-01");
    //set attribute of ticket
    ticket.setVisitDate(visitDate);

    //create visitor and attribute
    Visitor visitor = new Visitor();
    String visitorName = "jdsilv2";
    String visitorEmail = "sigmamale@pog.com";
    String visitorPassword = "123";

    //set attribute of visitor
    visitor.setName(visitorName);
    visitor.setEmail(visitorEmail);
    visitor.setPassword(visitorPassword);

    //2.save object
    //save visitor object to database
    visitor = visitorRepository.save(visitor);
    //set association between ticket and visitor object then save ticket object to database
    ticket.setVisitor(visitor);
    ticket = ticketRepository.save(ticket);
    long ticketId = ticket.getTicketId();
    long visitorId = ticket.getVisitor().getMuseumUserId();

    //3.read object from database
    ticket = ticketRepository.findTicketByTicketId(ticketId);

    //4.assert that object has correct attributes
    assertNotNull(ticket);
    assertEquals(ticketId, ticket.getTicketId());
    assertEquals(visitDate, ticket.getVisitDate());

    assertNotNull(ticket.getVisitor());
    assertEquals(visitorId, ticket.getVisitor().getMuseumUserId());
    assertEquals(visitorName, ticket.getVisitor().getName());
    assertEquals(visitorEmail, ticket.getVisitor().getEmail());
    assertEquals(visitorPassword, ticket.getVisitor().getPassword());
  }
}
