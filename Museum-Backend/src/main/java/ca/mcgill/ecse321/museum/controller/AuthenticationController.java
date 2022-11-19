package ca.mcgill.ecse321.museum.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.dto.MuseumUserDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.MuseumUser;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.AuthenticationService;

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
   * @param user - MuseumUserDto
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
      } else if (userAuthentication.getClass().equals(Manager.class)) {
        session.setAttribute("user_id", userAuthentication.getMuseumUserId());
        session.setAttribute("role", "manager");
        session.setMaxInactiveInterval(60 * 60 * 24);
      } else if (userAuthentication.getClass().equals(Employee.class)) {
        session.setAttribute("user_id", userAuthentication.getMuseumUserId());
        session.setAttribute("role", "employee");
        session.setMaxInactiveInterval(60 * 60 * 24);
      }

      return ResponseEntity.ok("logged in");
    } catch (

    Exception e) {
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

      if (AuthenticationUtility.isLoggedIn(session)
          && AuthenticationUtility.isMuseumUser(session)) {
        session.invalidate();
        return ResponseEntity.ok("logged out");
      }
      return new ResponseEntity<>("Cannot logout when not logged in", HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
