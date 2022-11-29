package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
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
   * Method to create a list of tickets
   *
   * @param visitorID       visitor's ID
   * @param date            chosen date of visit
   * @param numberOfTickets number of tickets to create
   * @return ticketsBought  list of created tickets
   * @author Zahra
   */
  @Transactional
  public List<Ticket> createTickets(long visitorID, Date date, int numberOfTickets) {
    List<Ticket> ticketsBought = new ArrayList<>();

    if (numberOfTickets < 1) {
      throw new IllegalArgumentException("Number of tickets must be at least 1");
    }
    if (visitorRepository.findVisitorByMuseumUserId(visitorID) == null) {
      throw new IllegalArgumentException("Visitor doesn't exist");
    } else if (date == null) {
      throw new IllegalArgumentException("Please insert a date");
    } else if (date.toLocalDate().isBefore(java.time.LocalDate.now())) {
      throw new IllegalArgumentException("Cannot pick a date in the past.");
    }

    for (int i = 0; i < numberOfTickets; i++) {
      Ticket ticket = createTicket(visitorID, date);
      ticketsBought.add(ticket);
    }

    return ticketsBought;
  }

  /**
   * Method to create a ticket
   *
   * @param visitorID visitor's ID
   * @param visitDate chosen date of visit
   * @return ticket   a ticket
   * @author Zahra
   */
  @Transactional
  public Ticket createTicket(Long visitorID, Date visitDate) {
    Ticket ticket = new Ticket();
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorID);
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor doesn't exist");
    } else if (visitDate == null) {
      throw new IllegalArgumentException("Please insert a date");
    } else if (visitDate.toLocalDate().isBefore(java.time.LocalDate.now())) {
      throw new IllegalArgumentException("Cannot pick a date in the past.");
    }
    ticket.setVisitDate(visitDate);
    ticket.setVisitor(visitor);
    return ticketRepository.save(ticket);
  }

  /**
   * Method to get a list of all tickets possessed by a visitor
   *
   * @param visitorID - id of the visitor
   * @return allTicketsOfVisitor
   * @author Zahra
   */
  @Transactional
  public List<Ticket> getTicketsByVisitor(long visitorID) {
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorID);
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor doesn't exist");
    }
    return ticketRepository.findTicketByVisitorOrderByVisitDate(visitor);
  }

  /**
   * Method to convert an Iterable to a List
   *
   * @param iterable - Iterable
   * @return List
   * @author From tutorial notes
   */
  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}
