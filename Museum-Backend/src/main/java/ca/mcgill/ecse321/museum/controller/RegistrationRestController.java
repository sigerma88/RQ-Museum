package ca.mcgill.ecse321.museum.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.service.RegistrationService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/profile")
public class RegistrationRestController {
  @Autowired
  private RegistrationService registrationService;

  @Autowired
  VisitorRepository visitorRepository;

  /**
   * POST method to create a new visitor account
   * 
   * @param visitorDto
   * @return visitor parameter
   */

  @PostMapping(value = "/visitor/register", produces = "application/json")
  public ResponseEntity<?> registerVisitor(HttpServletRequest request,
      @RequestBody VisitorDto visitor) {
    try {
      HttpSession session = request.getSession();
      System.out.println(session.getAttribute("user_id"));
      System.out.println(AuthenticationUtility.isLoggedIn(session));
      if (AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("Cannot register a visitor while logged in");
      }

      VisitorDto visitorDto = DtoUtility.convertToDto(registrationService
          .createVisitor(visitor.getEmail(), visitor.getPassword(), visitor.getName()));

      if (visitorDto != null) {
        session.setAttribute("user_id", visitorDto.getUserId());
        session.setAttribute("role", "visitor");
      }

      return ResponseEntity.ok(visitorDto);

    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
  * 
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

  @PostMapping(value = "/register", produces = "application/json")
  public ResponseEntity<?> register(HttpServletRequest request, @RequestBody EmployeeDto employee) {
    try {
      HttpSession session = request.getSession();

      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You must be logged in to register an employee");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You must be a manager to register an employee");
      }

      EmployeeDto employeeDto =
          DtoUtility.convertToDto(registrationService.createEmployee(employee.getName()));

      return ResponseEntity.ok(employeeDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PostMapping(value = "/edit/{id}", produces = "application/json")
  public ResponseEntity<?> editEmployeeInformation(HttpServletRequest request,
      @PathVariable long id, @RequestBody Map<String, String> updatedEmployeeCredential) {
    try {
      HttpSession session = request.getSession();

      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You must be logged in to edit an employee");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You must be a staff member to edit an employee");
      }

      if (!AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
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

}
