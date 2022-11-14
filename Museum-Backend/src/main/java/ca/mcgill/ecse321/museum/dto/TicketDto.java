package ca.mcgill.ecse321.museum.dto;

import java.sql.Date;

/**
 * Ticket DTO
 *
 * @author Zahra
 */
public class TicketDto {

  private long ticketId;
  private Date visitDate;
  private VisitorDto visitor;

  public TicketDto() {

  }

  public TicketDto(long ticketId, Date visitDate, VisitorDto visitor) {
    this.ticketId = ticketId;
    this.visitDate = visitDate;
    this.visitor = visitor;

  }

  public TicketDto(Date visitSDate, VisitorDto visitor) {
    this.visitor = visitor;
    this.visitDate = visitDate;
  }

  public VisitorDto getVisitor() {
    return visitor;
  }

  public void setVisitor(VisitorDto visitor) {
    this.visitor = visitor;
  }

  public long getTicketId() {
    return ticketId;
  }

  public Date getVisitDate() {
    return visitDate;
  }

  public void setTicketId(long ticketId) {
    this.ticketId = ticketId;
  }

  public void setVisitDate(Date visitSDate) {
    this.visitDate = visitDate;
  }
}
