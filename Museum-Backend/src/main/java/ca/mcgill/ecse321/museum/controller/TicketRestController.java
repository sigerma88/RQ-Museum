package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ticket")
public class TicketRestController {

  @Autowired
  private TicketService ticketService;

  /**
   * RESTful API to purchase tickets
   *
   * @param ticketDto a TicketDto with the attributes needed to create the tickets
   * @param numberOfTickets number of tickets to purchase
   * @return boughtTickets list of created tickets
   */
  @PostMapping(value = {"/purchase", "/purchase/"})
  public ResponseEntity<?> createTickets(HttpServletRequest request,
      @RequestBody TicketDto ticketDto, @RequestParam(name = "number") int numberOfTickets) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isVisitor(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a visitor");
      }
      List<TicketDto> boughtTickets = new ArrayList<>();
      for (Ticket ticket : ticketService.createTickets(ticketDto.getVisitor().getMuseumUserId(),
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
  public ResponseEntity<?> getTicketsByVisitor(HttpServletRequest request,
      @PathVariable("visitorId") long visitorId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isVisitor(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a visitor");
      } else if(!AuthenticationUtility.checkUserId(session, visitorId)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to view this page");
      }

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
