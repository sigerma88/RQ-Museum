package ca.mcgill.ecse321.museum.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.service.EmployeeService;


@CrossOrigin(origins = "*")
@RestController
public class EmployeeRestController {

	@Autowired
	private EmployeeService service;


@GetMapping(value = { "/schedule/{id}"})
public ScheduleDto getSchedule(@PathVariable("id") long id) {

	return DtoUtility.convertToDto(service.getEmployeeSchedule(id));

}


  /**
   * RESTful API to get all employees
   * 
   * @return List of all employees
   * @author Siger
   */
  @GetMapping(value = { "/employees", "/employees/" })
  public ResponseEntity<?> getAllEmployees() {
    try {
      List<EmployeeDto> employeeDtos = new ArrayList<>();
      for (Employee employee : service.getAllEmployees()) {
        employeeDtos.add(DtoUtility.convertToDto(employee));
      }
      return ResponseEntity.ok(employeeDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
    
  }

  /**
   * RESTful API to delete an employee by their id
   * 
   * @param id - long
   * @return if the employee was deleted (success)
   * @author Siger
   */
  @DeleteMapping(value = { "/employee/{id}", "/employee/{id}/" })
  public ResponseEntity<?> deleteEmployee(@PathVariable("id") long id) {
    try {
      // Check if employee exists
      if (service.getEmployee(id) == null) {
        return ResponseEntity.badRequest().body("Employee does not exist");
      }

      // Delete employee
      service.deleteEmployee(id);
      return ResponseEntity.ok("Employee deleted");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}