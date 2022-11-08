package ca.mcgill.ecse321.museum.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.VisitorService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/visitor")
public class VisitorRestController {
    @Autowired
    private VisitorService visitorService;

    @Autowired
    VisitorRepository visitorRepository;


    /**
     * POST method to create a new visitor account
     * 
     * @param visitorDto
     * @return visitor parameter
     */

    @PostMapping(value = "/register", produces = "application/json")
    public ResponseEntity<?> register(@RequestBody Visitor visitor) {
        try {
            VisitorDto visitorDto = DtoUtility.convertToDto(visitorService
                    .createVisitor(visitor.getEmail(), visitor.getPassword(), visitor.getName()));
            return new ResponseEntity<>(visitorDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<?> viewInformation(@PathVariable long id) {
        try {
            VisitorDto visitorDto =
                    DtoUtility.convertToDto(visitorService.getVisitorPersonalInformation(id));
            return new ResponseEntity<>(visitorDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping(value = "/edit/{id}", produces = "application/json")
    public ResponseEntity<?> editInformation(@PathVariable long id,
            @RequestBody Visitor updatedCredential) {
        try {
            VisitorDto visitor = DtoUtility
                    .convertToDto(visitorService.editInformation(id, updatedCredential.getEmail(),
                            updatedCredential.getPassword(), updatedCredential.getName()));
            return new ResponseEntity<>(visitor, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // @GetMapping(value = "/login", produces = "application/json")
    // public ResponseEntity<?> loginVisitor(@ResponseBody )
}
