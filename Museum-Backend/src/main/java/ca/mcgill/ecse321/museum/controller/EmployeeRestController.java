package ca.mcgill.ecse321.museum.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
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
  public ResponseEntity<?> register(HttpServletRequest request, @RequestBody Employee employee) {
    try {
      HttpSession session = request.getSession();

      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(401).body("You must be logged in to register an employee");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(401).body("You must be a manager to register an employee");
      }

      EmployeeDto employeeDto =
          DtoUtility.convertToDto(registrationService.registerEmployee(employee.getName()));
      return ResponseEntity.ok(employeeDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/edit/{id}", produces = "application/json")
  public ResponseEntity<?> editInformation(HttpServletRequest request, @PathVariable long id,
      @RequestBody Map<String, String> updatedEmployeeCredential) {
    try {
      HttpSession session = request.getSession();

      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(401).body("You must be logged in to edit an employee");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(401).body("You must be a staff member to edit an employee");
      }

      if (!AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(401).body("You can only edit your own information");
      }

      EmployeeDto employeeDto =
          DtoUtility.convertToDto(registrationService.editEmployeeInformation(id,
              updatedEmployeeCredential.get("oldPassword"),
              updatedEmployeeCredential.get("newPassword")));
      return ResponseEntity.ok(employeeDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
