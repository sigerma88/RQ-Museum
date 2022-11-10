package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author kianmamicheafara
 * RoomRestController class is used as a controller where we call
 * our API for our web application
 */

@CrossOrigin(origins = "*")
@RestController
public class RoomRestController {

    @Autowired
    RoomService roomService;


    // FR3 -> View room capacity
    @GetMapping(value = "/getRoomCapacity/{id}")
    public ResponseEntity<?> getRoomCapacity(@PathVariable("id") long id) {
        try{
            int capacity = roomService.getRoomCapacity(id);
            if (capacity == -1){
                return new ResponseEntity<>("Error getting the capacity", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(capacity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
