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
import ca.mcgill.ecse321.museum.dto.MuseumUserDto;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.RegistrationService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/visitor")
public class VisitorRestController {
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

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<?> register(HttpServletRequest request, @RequestBody Visitor visitor) {
        try {
            HttpSession session = request.getSession();
            System.out.println(session.getAttribute("user_id"));
            System.out.println(AuthenticationUtility.isLoggedIn(session));
            if (AuthenticationUtility.isLoggedIn(session)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Cannot register a visitor while logged in");
            }

            MuseumUserDto visitorDto = DtoUtility.convertToDto(registrationService
                    .createVisitor(visitor.getEmail(), visitor.getPassword(), visitor.getName()));

            if (visitorDto != null) {
                session.setAttribute("user_id", visitorDto.getUserId());
                session.setAttribute("user_type", "visitor");
            }
            return ResponseEntity.ok(visitorDto);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> viewInformation(HttpServletRequest request, @PathVariable long id) {
        try {
            HttpSession session = request.getSession();
            if (!AuthenticationUtility.isLoggedIn(session)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You are not logged in.");

            } else if (!AuthenticationUtility.checkUserId(session, id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("You can only view your own information");

            }

            MuseumUserDto visitorDto =
                    DtoUtility.convertToDto(registrationService.getVisitorPersonalInformation(id));
            return ResponseEntity.ok(visitorDto);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/edit/{id}", produces = "application/json")
    public ResponseEntity<?> editVisitorInformation(HttpServletRequest request,
            @PathVariable long id, @RequestBody Map<String, String> updatedCredential) {
        try {
            HttpSession session = request.getSession();
            if (!AuthenticationUtility.isLoggedIn(session)) {
                return new ResponseEntity<>("You are not logged in", HttpStatus.BAD_REQUEST);
            } else if (!AuthenticationUtility.checkUserId(session, id)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Not allowed to edit this account");
            }

            MuseumUserDto visitorDto =
                    DtoUtility.convertToDto(registrationService.editVisitorInformation(id,
                            updatedCredential.get("email"), updatedCredential.get("oldPassword"),
                            updatedCredential.get("newPassword"), updatedCredential.get("name")));
            return new ResponseEntity<>(visitorDto, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
