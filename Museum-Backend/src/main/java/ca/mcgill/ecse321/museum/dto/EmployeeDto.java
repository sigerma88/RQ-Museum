package ca.mcgill.ecse321.museum.dto;

/**
 * Employee DTO
 * 
 * @author Siger
 */
public class EmployeeDto {

  private Long museumUserId;
  private String email;
  private String name;
  private String password;
  private ScheduleDto schedule;

  public EmployeeDto() {
  }

  public EmployeeDto(String email, String name, String password, ScheduleDto schedule) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.schedule = schedule;
  }

  public Long getMuseumUserId() {
    return museumUserId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public ScheduleDto getSchedule() {
    return schedule;
  }

  public void setSchedule(ScheduleDto schedule) {
    this.schedule = schedule;
  }
}