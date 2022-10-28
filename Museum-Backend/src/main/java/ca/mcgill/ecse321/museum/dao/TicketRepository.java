package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Ticket;

/**
 * This is the repository for the Ticket class
 * 
 * @author Victor
 */
public interface TicketRepository extends CrudRepository<Ticket, Long>{

  Ticket findTicketByTicketId(Long ticketId);

}