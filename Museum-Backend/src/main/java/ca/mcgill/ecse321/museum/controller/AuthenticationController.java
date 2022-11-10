package ca.mcgill.ecse321.museum.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.museum.dto.MuseumUserDto;
import ca.mcgill.ecse321.museum.model.MuseumUser;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.AuthenticationService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(HttpServletRequest request,
            @RequestBody MuseumUserDto museumUser) {
        try {
            authenticationService.authenticateUser(request, museumUser.getEmail(),
                    museumUser.getPassword());
            return new ResponseEntity<>("logged in", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            authenticationService.logout(request);
            return new ResponseEntity<>("logged out", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
