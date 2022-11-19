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


    /**
     * RESTful API to view the capacity of a certain room
     *
     * FR3 -> View room capacity
     * @param roomId - The id of a room we want to get the capacity of
     * @return The capacity of the room
     * @author kieyanmamiche
     */
    @GetMapping(value = {"/getRoomCapacity/{roomId}", "/getRoomCapacity/{roomId}/"})
    public ResponseEntity<?> getRoomCapacity(@PathVariable("roomId") long roomId) {
        try{
            int capacity = roomService.getRoomCapacity(roomId);
            return new ResponseEntity<>(capacity, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
