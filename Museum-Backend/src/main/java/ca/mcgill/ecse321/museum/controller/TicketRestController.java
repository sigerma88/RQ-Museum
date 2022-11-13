package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class TicketRestController {

  @Autowired
  private TicketService ticketService;

  /**
   * TODO javadoc
   */
  @PostMapping(value = {"/tickets/purchase", "/tickets/purchase/"})
  public ResponseEntity<?> buyTickets(@RequestParam(name = "visitor") Long visitorID, @RequestParam Date date, @RequestParam(name = "number") int numberOfTickets) {
    try {
      List<TicketDto> boughtTickets = new ArrayList<>();
      for (Ticket ticket : ticketService.createTickets(visitorID, date, numberOfTickets)) {
        boughtTickets.add(DtoUtility.convertToDto(ticket));
      }
      return new ResponseEntity<>(boughtTickets, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * TODO javadoc
   */
  @GetMapping(value = {"/tickets/{visitor}", "/tickets/{visitor}/"})
  public ResponseEntity<?> getAllTicketsByVisitor(@PathVariable("visitor") Visitor visitor) {
    try {
      List<TicketDto> allTicketsOfVisitor = new ArrayList<>();
      for (Ticket ticket : ticketService.getAllTicketsByVisitor(visitor)) {
        allTicketsOfVisitor.add(DtoUtility.convertToDto(ticket));

      }
      return new ResponseEntity<>(allTicketsOfVisitor, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


  }

}