package ca.mcgill.ecse321.museum.dto;

import ca.mcgill.ecse321.museum.model.Visitor;

import java.sql.Date;

/**
 * Ticket DTO
 *
 * @author Zahra
 */
public class TicketDto {

  private long ticketId;
  private Date visitSDate;
  private Visitor visitor;

  public TicketDto() {

  }

  public TicketDto(long ticketId, Date visitSDate, Visitor visitor) {
    this.ticketId = ticketId;
    this.visitSDate = visitSDate;
    this.visitor = visitor;

  }

  public Visitor getVisitor() {
    return visitor;
  }

  public void setVisitor(Visitor visitor) {
    this.visitor = visitor;
  }

  public long getTicketId() {
    return ticketId;
  }

  public Date getVisitSDate() {
    return visitSDate;
  }

  public void setTicketId(long ticketId) {
    this.ticketId = ticketId;
  }

  public void setVisitSDate(Date visitSDate) {
    this.visitSDate = visitSDate;
  }
}
