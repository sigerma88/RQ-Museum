package ca.mcgill.ecse321.museum.dto;

/**
 * Employee DTO
 *
 * @author Siger, Kevin
 */

public class EmployeeDto extends MuseumUserDto {

  private ScheduleDto schedule;

  public EmployeeDto() {
  }

  public EmployeeDto(Long museumUserId, String email, String name, String password,
                     ScheduleDto schedule) {
    super(museumUserId, email, name, password);
    this.schedule = schedule;
  }

  public ScheduleDto getSchedule() {
    return schedule;
  }

  public void setSchedule(ScheduleDto schedule) {
    this.schedule = schedule;
  }
}
