package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TestTicketService {

  @Mock
  private TicketRepository ticketRepository;

  @Mock
  private VisitorRepository visitorRepository;

  @InjectMocks
  private TicketService ticketService;


  private static final long VISITOR_ID = 777;
  private static final String VISITOR_EMAIL = "bob@mail.com";
  private static final String VISITOR_NAME = "Bob Barbier";
  private static final String VISITOR_PASSWORD = "MyPassword";

  @BeforeEach
  public void setMockOutput() {

    lenient().when(visitorRepository.findVisitorByMuseumUserId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(VISITOR_ID)) {
        Visitor visitor = new Visitor();
        visitor.setEmail(VISITOR_EMAIL);
        visitor.setName(VISITOR_NAME);
        visitor.setPassword(VISITOR_PASSWORD);
        visitor.setMuseumUserId(VISITOR_ID);
        return visitor;
      } else {
        return null;
      }
    });

  }

}
