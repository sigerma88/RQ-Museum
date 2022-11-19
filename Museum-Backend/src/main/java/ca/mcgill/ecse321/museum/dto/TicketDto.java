package ca.mcgill.ecse321.museum.dto;

import java.sql.Date;

/**
 * Ticket DTO
 *
 * @author Zahra
 */
public class TicketDto {

  private long ticketId;
  private String visitDate;
  private long visitorId;

  public TicketDto() {

  }

  public TicketDto(long ticketId, String visitDate, long visitor) {
    this.ticketId = ticketId;
    this.visitDate = visitDate;
    this.visitorId = visitor;

  }

  public TicketDto(String visitDate, long visitor) {
    this.visitorId = visitor;
    this.visitDate = visitDate;
  }

  public long getVisitor() {
    return visitorId;
  }

  public void setVisitor(long visitorId) {
    this.visitorId = visitorId;
  }

  public long getTicketId() {
    return ticketId;
  }

  public String getVisitDate() {
    return visitDate;
  }

  public void setTicketId(long ticketId) {
    this.ticketId = ticketId;
  }

  public void setVisitDate(Date visitSDate) {
    this.visitDate = visitDate;
  }
}
