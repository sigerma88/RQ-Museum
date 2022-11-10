package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

  @Autowired
  TicketRepository ticketRepository;

  @Autowired
  VisitorRepository visitorRepository;

  /**
   * Feature 4
   * TODO Generate tickets
   */
  public Ticket createTicket(Date visitDate, Long visitorID) {

    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorID);
    Ticket ticket = new Ticket();
    ticket.setVisitDate(visitDate);
    ticket.setVisitor(visitor);
    return ticket;
  }


  /**
   * Feature 6
   * TODO get all tickets by visitor
   */
  public List<Ticket> getAllTicketsByVisitor(Visitor visitor) {

    if (visitor == null) {
      throw new IllegalArgumentException("Visitor doesn't exist");
    }
    List<Ticket> allTicketsOfVisitor = new ArrayList<>();
    List<Ticket> allTickets = toList(ticketRepository.findAll());
    for (Ticket ticket : allTickets) {
      if (ticket.getVisitor().equals(visitor)) {
        allTicketsOfVisitor.add(ticket);
      }

    }

    return allTicketsOfVisitor;
  }

  /**
   * Method to convert an Iterable to a List
   *
   * @param iterable - Iterable
   * @return List
   * @author From tutorial notes
   */
  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }

}
