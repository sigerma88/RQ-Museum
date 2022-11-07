package ca.mcgill.ecse321.museum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
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
    public ResponseEntity<?> register(@RequestBody VisitorDto visitorDto) {
        try {
            VisitorDto visitor = visitorService.createVisitor(visitorDto.getEmail(),
                    visitorDto.getPassword(), visitorDto.getName());
            return new ResponseEntity<>(visitor, HttpStatus.OK);
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
            VisitorDto visitor = visitorService.getVisitorPersonalInformation(id);
            return new ResponseEntity<>(visitor, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}

