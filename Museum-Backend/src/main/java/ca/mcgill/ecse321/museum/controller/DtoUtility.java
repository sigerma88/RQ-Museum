package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.dto.TimePeriodDto;

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
    return new EmployeeDto(employee.getMuseumUserId(), employee.getEmail(), employee.getName(), employee.getPassword(), scheduleDto);
  }

  /**
   * Method to convert a museum to a DTO
   * @author VZ
   * @param museum
   * @return
   */
  public static MuseumDto convertToDto(Museum museum) {
    if(museum == null) {
      throw new IllegalArgumentException("There is no such museum");
    }

    ScheduleDto scheduleDto = convertToDto(museum.getSchedule());
    return new MuseumDto(museum.getMuseumId(), museum.getName(), museum.getVisitFee(), scheduleDto);
  
  }

  static TimePeriodDto convertToDto(TimePeriod timePeriod) {
    if(timePeriod == null) {
      throw new IllegalArgumentException("There is no such time period");
    }
    return new TimePeriodDto(timePeriod.getTimePeriodId(), timePeriod.getStartDate(), timePeriod.getEndDate());
  }
  
}
