package dao;


import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.model.Ticket;

@SpringBootTest
public class TicketRepositoryTests {

    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    public void clearDatabase() {
        ticketRepository.deleteAll();
    }
    
    @Test
    public void testPersistandLoadTicket() {
        //create object

        Date visitDate = Date.valueOf("2020-01-01")
        Ticket ticket = new Ticket();
        ticket.setVisitDate(visitDate);

        //save object
        ticket = ticketRepository.save(ticket);
        long id = ticket.getTicketId();

        //read object from database
        ticket = ticketRepository.findTicketByTicketId(id);

        //assert that object has correct attributes
        assertNotNull(ticket);
        assertEquals(id, ticket.getTicketId());

/**
 * HAVING TROUBLE IMPORTING JUNIT FOR ASSERTIONS, maybe spring 2.7.4 
 * isn't compatible with our JUnit4?
 */

        


        
    }
    
}
