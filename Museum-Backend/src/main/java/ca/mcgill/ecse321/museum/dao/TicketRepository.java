package ca.mcgill.ecse321.museum.dao;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;

/**
 * This is the repository for the Ticket class
 * 
 * @author Victor
 */
public interface TicketRepository extends CrudRepository<Ticket, Long> {

  Ticket findTicketByTicketId(Long ticketId);

  List<Ticket> findTicketByVisitor(Visitor visitor);


}
