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
   * RESTful API to purchase tickets
   *
   * @param visitDate
   * @param numberOfTickets
   * @param visitorID
   * @return boughtTickets
   */
  @PostMapping(value = {"/tickets/purchase", "/tickets/purchase/"})
  public ResponseEntity<?> buyTickets(@RequestParam(name = "visitor") Long visitorID, @RequestParam Date visitDate, @RequestParam(name = "number") int numberOfTickets) {
    try {
      List<TicketDto> boughtTickets = new ArrayList<>();
      for (Ticket ticket : ticketService.createTickets(visitorID, visitDate, numberOfTickets)) {
        boughtTickets.add(DtoUtility.convertToDto(ticket));
      }
      return new ResponseEntity<>(boughtTickets, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * RESTful API to get all ticktets by visitor
   *
   * @param visitor Visitor
   * @return allTicketsOfVisitor
   * @author Zahra
   */
  @GetMapping(value = {"/tickets/{visitor}", "/tickets/{visitor}/"})
  public ResponseEntity<?> getTicketsByVisitor(@PathVariable("visitor") Visitor visitor) {
    try {
      List<TicketDto> allTicketsOfVisitor = new ArrayList<>();
      for (Ticket ticket : ticketService.getTicketsByVisitor(visitor)) {
        allTicketsOfVisitor.add(DtoUtility.convertToDto(ticket));

      }
      return new ResponseEntity<>(allTicketsOfVisitor, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


  }

}
