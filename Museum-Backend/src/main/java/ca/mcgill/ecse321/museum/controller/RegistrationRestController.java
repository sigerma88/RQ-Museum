package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * RESTful API to handle registration
 *
 * @author Kevin
 */

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/profile")
public class RegistrationRestController {

  @Autowired
  private RegistrationService registrationService;

  @Autowired
  VisitorRepository visitorRepository;

  /**
   * POST method to create a new visitor account
   *
   * @param visitor - VisitorDto object
   * @return created visitor
   * @author Kevin
   */

  @PostMapping(value = "/visitor/register", produces = "application/json")
  public ResponseEntity<?> registerVisitor(HttpServletRequest request,
                                           @RequestBody VisitorDto visitor) {
    try {
      HttpSession session = request.getSession();
      if (AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Cannot register a visitor while logged in");
      }

      VisitorDto visitorDto = DtoUtility.convertToDto(registrationService
          .createVisitor(visitor.getEmail(), visitor.getPassword(), visitor.getName()));

      if (visitorDto != null) {
        session.setAttribute("user_id", visitorDto.getMuseumUserId());
        session.setAttribute("role", "visitor");
        visitorDto.setRole("visitor");
      }

      return ResponseEntity.ok(visitorDto);

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * GET method to get a visitor account
   *
   * @param id - long (visitor id)
   * @return visitorDto
   * @author Kevin
   */

  @GetMapping(value = "/visitor/{id}", produces = "application/json")
  public ResponseEntity<?> viewVisitorInformation(HttpServletRequest request,
                                                  @PathVariable long id) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in.");

      } else if (!AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You are not authorized to view this profile.");

      }

      VisitorDto visitorDto =
          DtoUtility.convertToDto(registrationService.getVisitorPersonalInformation(id));
      return ResponseEntity.ok(visitorDto);

    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * PUT method to update visitor information
   *
   * @param id                - long (visitor id)
   * @param updatedCredential - map containing oldPassword, newPassword, email and name
   * @return visitorDto
   * @author Kevin
   */

  @PutMapping(value = "/visitor/edit/{id}", produces = "application/json")
  public ResponseEntity<?> editVisitorInformation(HttpServletRequest request, @PathVariable long id,
                                                  @RequestBody Map<String, String> updatedCredential) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not logged in.");
      } else if (!AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Not allowed to edit this account");
      }

      VisitorDto visitorDto = DtoUtility.convertToDto(registrationService.editVisitorInformation(id,
          updatedCredential.get("email"), updatedCredential.get("oldPassword"),
          updatedCredential.get("newPassword"), updatedCredential.get("name")));
      return new ResponseEntity<>(visitorDto, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


  /**
   * GET method to view employee information
   *
   * @param id - long (employee id)
   * @return employeeDto
   * @author Kevin
   */

  @GetMapping(value = {"/employee/{id}", "/{id}/"}, produces = "application/json")
  public ResponseEntity<?> getEmployee(HttpServletRequest request, @PathVariable("id") long id) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You are not authorized to view this page");
      }

      if (!AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You can only view your own information");
      }

      return ResponseEntity
          .ok(DtoUtility.convertToDto(registrationService.getEmployeePersonalInformation(id)));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * POST method to create a new employee account
   *
   * @param employeeName - Name of new employee
   * @return employeeDto
   * @author Kevin
   */

  @PostMapping(value = "employee/register", produces = "application/json")
  public ResponseEntity<?> register(HttpServletRequest request,
                                    @RequestBody Map<String, String> employee) {
    try {
      HttpSession session = request.getSession();

      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You must be logged in to register an employee");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You must be a manager to register an employee");
      }

      EmployeeDto employeeDto =
          DtoUtility.convertToDto(registrationService.createEmployee(employee.get("name")));

      return ResponseEntity.ok(employeeDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * PUT method to update employee information
   *
   * @param id                        - long (employee id)
   * @param updatedEmployeeCredential - map containing oldPassword, newPassword
   * @return employeeDto
   * @author Kevin
   */

  @PutMapping(value = "/employee/edit/{id}", produces = "application/json")
  public ResponseEntity<?> editEmployeeInformation(HttpServletRequest request,
                                                   @PathVariable long id, @RequestBody Map<String, String> updatedEmployeeCredential) {
    try {
      HttpSession session = request.getSession();

      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You must be logged in to edit an employee");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You must be a staff member to edit an employee");
      } else if (!AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You can only edit your own information");
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

  /**
   * PUT method to update manager information
   *
   * @param id                       - long (manager id)
   * @param updatedManagerCredential - map containing oldPassword, newPassword, managerId
   * @return managerDto
   * @author Kevin
   */

  @PutMapping(value = "/manager/edit/{id}", produces = "application/json")
  public ResponseEntity<?> editManagerInformation(HttpServletRequest request, @PathVariable long id,
                                                  @RequestBody Map<String, String> updatedManagerCredential) {
    try {
      HttpSession session = request.getSession();

      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You must be logged in to edit a manager");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You must be a manager to edit a manager");
      }
      ManagerDto manager = DtoUtility.convertToDto(registrationService.editManagerInformation(id,
          updatedManagerCredential.get("oldPassword"),
          updatedManagerCredential.get("newPassword")));

      return ResponseEntity.ok(manager);
    } catch (

        Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
