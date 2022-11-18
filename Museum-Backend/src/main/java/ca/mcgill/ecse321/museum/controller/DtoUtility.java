package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;

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
  public static EmployeeDto convertToDto(Employee employee) {
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
  public static VisitorDto convertToDto(Visitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    VisitorDto visitorDto = new VisitorDto();
    visitorDto.setEmail(visitor.getEmail());
    visitorDto.setName(visitor.getName());
    visitorDto.setPassword(visitor.getPassword());
    visitorDto.setMuseumUserId(visitor.getMuseumUserId());
    return visitorDto;
  }

  /**
   * Method to convert an visitor to a DTO
   * 
   * @param museumUser - Visitor
   * @return visitor DTO
   * @author Kevin
   */
  public static ManagerDto convertToDto(Manager manager) {
    if (manager == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    ManagerDto managerDto = new ManagerDto();
    managerDto.setEmail(manager.getEmail());
    managerDto.setName(manager.getName());
    managerDto.setPassword(manager.getPassword());
    managerDto.setUserId(manager.getMuseumUserId());
    return managerDto;
  }
}
