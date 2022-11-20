package ca.mcgill.ecse321.museum.dto;

/**
 * Museum DTO
 *
 * @author Siger
 * @author Victor
 */

public class MuseumDto {

  private Long museumId;
  private String name;
  private Double visitFee;
  private ScheduleDto schedule;

  public MuseumDto() {
  }

  public MuseumDto(Long museumId, String name, Double visitFee, ScheduleDto schedule) {
    this.museumId = museumId;
    this.name = name;
    this.visitFee = visitFee;
    this.schedule = schedule;
  }

  public Long getMuseumId() {
    return museumId;
  }

  public void setMuseumId(Long museumId) {
    this.museumId = museumId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Double getVisitFee() {
    return visitFee;
  }

  public void setVisitFee(Double visitFee) {
    this.visitFee = visitFee;
  }

  public ScheduleDto getSchedule() {
    return schedule;
  }

  public void setSchedule(ScheduleDto schedule) {
    this.schedule = schedule;
  }
}
