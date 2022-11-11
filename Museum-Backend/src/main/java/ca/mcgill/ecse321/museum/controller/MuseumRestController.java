package ca.mcgill.ecse321.museum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.service.MuseumService;

@CrossOrigin(origins = "*")
@RestController
public class MuseumRestController {

    @Autowired
    private MuseumService museumService;

}
