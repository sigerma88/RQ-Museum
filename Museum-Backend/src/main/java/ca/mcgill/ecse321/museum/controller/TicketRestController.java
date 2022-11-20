package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Ticket;
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
@RequestMapping("/api/ticket")
public class TicketRestController {

  @Autowired
  private TicketService ticketService;

  /**
   * RESTful API to purchase tickets
   *
   * @param ticketDto       a TicketDto with the attributes needed to create the tickets
   * @param numberOfTickets number of tickets to purchase
   * @return boughtTickets list of created tickets
   */
  @PostMapping(value = {"/purchase", "/purchase/"})
  public ResponseEntity<?> createTickets(@RequestBody TicketDto ticketDto, @RequestParam(name = "number") int numberOfTickets) {

    try {
      List<TicketDto> boughtTickets = new ArrayList<>();
      for (Ticket ticket : ticketService.createTickets(ticketDto.getVisitor(),
          Date.valueOf(ticketDto.getVisitDate()), numberOfTickets)) {
        boughtTickets.add(DtoUtility.convertToDto(ticket));
      }
      return new ResponseEntity<>(boughtTickets, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }


  /**
   * RESTful API to get all tickets by visitor
   *
   * @param visitorId visitor's ID
   * @return allTicketsOfVisitor list of tickets possessed by visitor
   * @author Zahra
   */
  @GetMapping(value = {"/visitor/{visitorId}", "/visitor/{visitorId}/"})
  public ResponseEntity<?> getTicketsByVisitor(@PathVariable("visitorId") long visitorId) {
    try {
      List<TicketDto> allTicketsOfVisitor = new ArrayList<>();
      for (Ticket ticket : ticketService.getTicketsByVisitor(visitorId)) {
        allTicketsOfVisitor.add(DtoUtility.convertToDto(ticket));
      }
      return new ResponseEntity<>(allTicketsOfVisitor, HttpStatus.OK);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

}
