package ca.mcgill.ecse321.museum.dto;

/**
 * Schedule DTO
 * 
 * @author Siger
 */
public class ScheduleDto {

  private Long scheduleId;

  public ScheduleDto() {
  }

  public ScheduleDto(Long scheduleId) {
    this.scheduleId = scheduleId;
  }

  public void setSheduleId(Long scheduleId) {
    this.scheduleId = scheduleId;
  }

  public Long getScheduleId() {
    return scheduleId;
  }
}
