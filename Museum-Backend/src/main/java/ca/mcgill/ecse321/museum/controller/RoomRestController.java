package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class RoomRestController {

    @Autowired
    RoomService roomService;



    // FR3 -> View room capacity
    @GetMapping(value = "/getRoomCapacity/{id}")
    public int getRoomCapacity(@PathVariable("id") long id) {
        int capacity = roomService.getRoomCapacity(id);
        return capacity;
    }
}
