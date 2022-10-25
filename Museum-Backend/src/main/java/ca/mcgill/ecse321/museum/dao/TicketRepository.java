package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Ticket;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Ticket Model
 */
public interface TicketRepository extends CrudRepository<Ticket, Long> {
}
