package ca.mcgill.ecse321.museum.controller;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.service.EmployeeService;

@CrossOrigin(origins = "*")
@RequestMapping("/api/employee")
@RestController
public class EmployeeRestController {

@Autowired
private EmployeeService employeeService;


@GetMapping(value = { "/schedule/{id}"})
public ScheduleDto getSchedule(@PathVariable("id") long id) {

	return convertToDto(employeeService.getEmployeeSchedule(id));

}


  /**
   * Method to convert a schedule to a DTO
   * 
   * @param schedule - Schedule
   * @return schedule DTO
   * @author Siger
   */
  private  ScheduleDto convertToDto(Schedule schedule) {
    if (schedule == null) {
      throw new IllegalArgumentException("There is no such schedule");
    }
    return new ScheduleDto();
  }

  /**
   * Method to convert an employee to a DTO
   * 
   * @param employee - Employee
   * @return employee DTO
   * @author Siger
   */
  private EmployeeDto convertToDto(Employee employee) {
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }
    ScheduleDto scheduleDto = convertToDto(employee.getSchedule());
    return new EmployeeDto(employee.getEmail(), employee.getName(), employee.getPassword());
  }
    
}
