package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
public class ArtworkRestController {

    @Autowired
    ArtworkService artworkService;

    // Getting artwork status - FR7
    // Returns a status string of 4 options: loan/on display/in storage
        // 1. "none" -> The artwork doesn't exist
        // 2. "loan" -> The artwork is on loan
        // 3. "display" -> The artwork is on Display
        // 4. "storage" -> The artwork is in storage
    @GetMapping(value = "/getArtworkStatus/{id}")
    public String getArtworkStatus(@PathVariable("id") long id) {
        String status = artworkService.getArtworkStatus(id);
        return status;
    }


}
