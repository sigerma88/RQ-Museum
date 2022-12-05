package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.MuseumUserDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.MuseumUser;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * RESTful API to handle authentication
 *
 * @author Kevin
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

  @Autowired
  private AuthenticationService authenticationService;

  /**
   * POST to login a user
   *
   * @param museumUser - MuseumUserDto
   * @return if the user was logged in (success)
   * @author Kevin
   */

  @PostMapping("/login")
  public ResponseEntity<?> login(HttpServletRequest request,
                                 @RequestBody MuseumUserDto museumUser) {
    try {
      if (AuthenticationUtility.isLoggedIn(request.getSession())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot login while logged in");
      }

      MuseumUser userAuthentication =
          authenticationService.authenticateUser(museumUser.getEmail(), museumUser.getPassword());
      HttpSession session = request.getSession(true);

      if (userAuthentication.getClass().equals(Visitor.class)) {
        session.setAttribute("user_id", userAuthentication.getMuseumUserId());
        session.setAttribute("role", "visitor");
        session.setMaxInactiveInterval(60 * 60 * 24);
        MuseumUserDto user = DtoUtility.convertToDto(userAuthentication, "visitor");
        return ResponseEntity.ok(user);
      } else if (userAuthentication.getClass().equals(Manager.class)) {
        ManagerDto managerDto = DtoUtility.convertToDto((Manager) userAuthentication);
        session.setAttribute("user_id", userAuthentication.getMuseumUserId());
        session.setAttribute("role", "manager");
        managerDto.setRole("manager");
        session.setMaxInactiveInterval(60 * 60 * 24);
        MuseumUserDto user = DtoUtility.convertToDto(userAuthentication, "manager");
        return ResponseEntity.ok(user);
      } else if (userAuthentication.getClass().equals(Employee.class)) {
        EmployeeDto employeeDto = DtoUtility.convertToDto((Employee) userAuthentication);
        session.setAttribute("user_id", userAuthentication.getMuseumUserId());
        session.setAttribute("role", "employee");
        session.setMaxInactiveInterval(60 * 60 * 24);
        employeeDto.setRole("employee");
        MuseumUserDto user = DtoUtility.convertToDto(userAuthentication, "employee");
        return ResponseEntity.ok(user);
      }

      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credentials");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * POST to logout a user
   *
   * @return if the user was logged out (success)
   * @author Kevin
   */

  @PostMapping("/logout")
  public ResponseEntity<?> logout(HttpServletRequest request) {
    try {
      HttpSession session = request.getSession();
      session.invalidate();
      return ResponseEntity.ok("logged out");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
