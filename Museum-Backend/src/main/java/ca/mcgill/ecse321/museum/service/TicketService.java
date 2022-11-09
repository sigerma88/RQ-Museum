package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

  @Autowired
  TicketRepository ticketRepository;

  @Autowired
  VisitorRepository visitorRepository;


}
