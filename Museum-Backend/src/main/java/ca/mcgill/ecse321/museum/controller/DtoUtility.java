package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.MuseumUser;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.dto.MuseumUserDto;

public class DtoUtility {
  /**
   * Method to convert a schedule to a DTO
   * 
   * @param schedule - Schedule
   * @return schedule DTO
   * @author Siger
   */
  static ScheduleDto convertToDto(Schedule schedule) {
    if (schedule == null) {
      throw new IllegalArgumentException("There is no such schedule");
    }
    return new ScheduleDto(schedule.getScheduleId());
  }

  /**
   * Method to convert an employee to a DTO
   * 
   * @param employee - Employee
   * @return employee DTO
   * @author Siger
   */
  static EmployeeDto convertToDto(Employee employee) {
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }
    ScheduleDto scheduleDto = convertToDto(employee.getSchedule());
    return new EmployeeDto(employee.getMuseumUserId(), employee.getEmail(), employee.getName(),
        employee.getPassword(), scheduleDto);
  }

  /**
   * Method to convert an visitor to a DTO
   * 
   * @param museumUser - Visitor
   * @return visitor DTO
   * @author Kevin
   */
  static MuseumUserDto convertToDto(MuseumUser museumUser) {
    if (museumUser == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    MuseumUserDto museumUserDto = new MuseumUserDto();
    museumUserDto.setEmail(museumUser.getEmail());
    museumUserDto.setName(museumUser.getName());
    museumUserDto.setPassword(museumUser.getPassword());
    museumUserDto.setUserId(museumUser.getMuseumUserId());
    return museumUserDto;
  }
}
