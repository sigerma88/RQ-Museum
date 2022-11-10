package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class TicketRestController {

  @Autowired
  private TicketService ticketService;

  /**
   * TODO write postmapping for buying tickets
   *
   */


  /**
   * TODO write getmapping to see tickets in account
   */
  @GetMapping(value = {"/visitor/tickets", "visitor/tickets/"})
  public List<TicketDto> getAllTicketsByVisitor(Visitor visitor) {

    return null;
  }

}
