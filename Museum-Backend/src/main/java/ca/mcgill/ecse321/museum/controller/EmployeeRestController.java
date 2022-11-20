package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * EmployeeRestController class is used as a controller where we call
 * our API for our web application
 *
 * @author Siger
 * @author Kevin
 */

@CrossOrigin(origins = "*")
@RequestMapping("/api/employee")
@RestController
public class EmployeeRestController {

  @Autowired
  private EmployeeService service;

  /**
   * RESTful API to get all employees
   *
   * @return List of all employees
   * @author Siger, Kevin
   */

  @GetMapping(value = {"/", ""})
  public ResponseEntity<?> getAllEmployees(HttpServletRequest request) {
    try {
      if (!AuthenticationUtility.isLoggedIn(request.getSession())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(request.getSession())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not the manager");
      }

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
   * @param employeeId - long
   * @return if the employee was deleted (success)
   * @author Siger, Kevin
   */

  @DeleteMapping(value = {"/{employeeId}", "/{employeeId}/"})
  public ResponseEntity<?> deleteEmployee(@PathVariable("employeeId") long employeeId, HttpServletRequest request) {
    try {

      // Check if logged in
      if (!AuthenticationUtility.isLoggedIn(request.getSession())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(request.getSession())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not the manager");
      }

      // Delete employee
      service.deleteEmployee(employeeId);
      return ResponseEntity.ok("Employee deleted");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
