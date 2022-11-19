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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
// import java.util.Date;

@CrossOrigin(origins = "*")
@RestController
public class TicketRestController {

  @Autowired
  private TicketService ticketService;

  /**
   * RESTful API to purchase tickets
   *
   * @param
   * @param numberOfTickets
   * @param
   * @return boughtTickets
   */
  @PostMapping(value = {"/tickets/purchase", "/tickets/purchase/"})
  // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date visitDat
  public ResponseEntity<?> createTickets(@RequestBody TicketDto ticketDto,
      @RequestParam(name = "number") int numberOfTickets) {

    System.out.println("TicketDto: " + ticketDto.getVisitDate());
    try {
      List<TicketDto> boughtTickets = new ArrayList<>();
      System.out.println(ticketDto.getVisitDate());
      System.out.println(ticketDto.getVisitor());
      for (Ticket ticket : ticketService.createTickets(ticketDto.getVisitor(),
          Date.valueOf(ticketDto.getVisitDate()), numberOfTickets)) {
        boughtTickets.add(DtoUtility.convertToDto(ticket));
      }
      System.out.println(boughtTickets);
      return new ResponseEntity<>(boughtTickets, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful API to get all ticktets by visitor
   *
   * @param visitorId
   * @return allTicketsOfVisitor
   * @author Zahra
   */


  @GetMapping(value = {"/tickets/{visitor}", "/tickets/{visitor}/"})
  public ResponseEntity<?> getTicketsByVisitor(@PathVariable("visitor") long visitorId) {
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
