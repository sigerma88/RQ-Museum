package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
   * @param visitorName
   * @return boughtTickets
   */
  @PostMapping(value = {"/tickets/purchase", "/tickets/purchase/"})
  public ResponseEntity<?> createTickets(@RequestParam(name = "visitorName") String visitorName, @RequestParam Date visitDate, @RequestParam(name = "number") int numberOfTickets) {
    try {
      List<TicketDto> boughtTickets = new ArrayList<>();
      for (Ticket ticket : ticketService.createTickets(visitorName, visitDate, numberOfTickets)) {
        boughtTickets.add(DtoUtility.convertToDto(ticket));
      }
      return new ResponseEntity<>(boughtTickets, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * RESTful API to get all ticktets by visitor
   *
   * @param visitorName
   * @return allTicketsOfVisitor
   * @author Zahra
   */
  @GetMapping(value = {"/tickets/{visitor}", "/tickets/{visitor}/"})
  public ResponseEntity<?> getTicketsByVisitor(@PathVariable("visitor") String visitorName) {
    try {
      List<TicketDto> allTicketsOfVisitor = new ArrayList<>();
      for (Ticket ticket : ticketService.getTicketsByVisitor(visitorName)) {
        allTicketsOfVisitor.add(DtoUtility.convertToDto(ticket));
      }
      return new ResponseEntity<>(allTicketsOfVisitor, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


  }

}
