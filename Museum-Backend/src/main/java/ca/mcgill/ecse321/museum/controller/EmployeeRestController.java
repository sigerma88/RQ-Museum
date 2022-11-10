package ca.mcgill.ecse321.museum.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.MuseumUserDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.service.EmployeeService;
import ca.mcgill.ecse321.museum.service.RegistrationService;

@CrossOrigin(origins = "*")
@RequestMapping("api/employee")
@RestController
public class EmployeeRestController {

  @Autowired
  private EmployeeService service;

  @Autowired
  private RegistrationService registrationService;

  /**
   * RESTful API to get all employees
   * 
   * @return List of all employees
   * @author Siger
   */
  @GetMapping(value = {"/", "/"})
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
  @DeleteMapping(value = {"/{id}", "/{id}/"})
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

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity<?> register(@RequestBody Employee employee) {
    try {
      EmployeeDto employeeDto =
          DtoUtility.convertToDto(registrationService.registerEmployee(employee.getName()));
      return new ResponseEntity<>(employeeDto, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
