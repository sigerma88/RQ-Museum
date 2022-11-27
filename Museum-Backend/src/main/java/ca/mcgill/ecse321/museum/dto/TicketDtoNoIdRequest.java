package ca.mcgill.ecse321.museum.dto;

/**
 * Ticket DTO without id for request
 *
 * @author Siger
 */
public class TicketDtoNoIdRequest {

  private String visitDate;
  private Long visitorId;

  public TicketDtoNoIdRequest() {

  }

  public TicketDtoNoIdRequest(String visitDate, Long visitorId) {
    this.visitDate = visitDate;
    this.visitorId = visitorId;
  }

  public Long getVisitorId() {
    return visitorId;
  }

  public void setVisitorId(Long visitorId) {
    this.visitorId = visitorId;
  }

  public String getVisitDate() {
    return visitDate;
  }

  public void setVisitDate(String visitDate) {
    this.visitDate = visitDate;
  }
}
