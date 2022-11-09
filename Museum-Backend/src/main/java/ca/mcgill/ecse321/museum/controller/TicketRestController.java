package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
public class TicketRestController {

  @Autowired
  private TicketService ticketService;

  
}
