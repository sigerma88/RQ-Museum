package ca.mcgill.ecse321.museum.dto;

/**
 * Schedule of time period DTO
 * @author VZ
 */

public class ScheduleOfTimePeriodDto {

  private TimePeriodDto timePeriod;
  private ScheduleDto schedule;

  public ScheduleOfTimePeriodDto() {
  }

  public ScheduleOfTimePeriodDto(TimePeriodDto timePeriod, ScheduleDto schedule) {
    this.timePeriod = timePeriod;
    this.schedule = schedule;
  }

  public TimePeriodDto getTimePeriod() {
    return timePeriod;
  }

  public ScheduleDto getSchedule() {
    return schedule;
  }

  public void setTimePeriod(TimePeriodDto timePeriod) {
    this.timePeriod = timePeriod;
  }

  public void setSchedule(ScheduleDto schedule) {
    this.schedule = schedule;
  }

}
    