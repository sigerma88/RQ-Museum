package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketIntegrationTests {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private TicketRepository ticketRepository;

  //@Autowired
  private VisitorRepository visitorRepository;

  @BeforeEach
  @AfterEach
  public void clearDatabase() {

    ticketRepository.deleteAll();
    visitorRepository.deleteAll();
  }

  @Test
  public void testBuyTickets() {


    ResponseEntity<?> response = client.postForEntity("purchase", new ArrayList<TicketDto>(), TicketDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }


  @Test
  public void testBuyTicketsWithInvalidDate() {
    ResponseEntity<?> response = client.postForEntity("tickets/purchase", new ArrayList<TicketDto>(), TicketDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void testBuyTicketsWithInvalidNumber() {
    ResponseEntity<?> response = client.postForEntity("tickets/purchase", new ArrayList<TicketDto>(), TicketDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

  }


  @Test
  public void testGetTicketsByVisitor(Visitor visitor) {
    ResponseEntity<?> response = client.getForEntity("/tickets/" + visitor, TicketDto.class);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());


  }

  @Test
  public void testGetTicketByInvalidVisitor() {
    ResponseEntity<?> response = client.getForEntity("tickets/purchase", TicketDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  class TicketDto {
    private Date visitDate;

    public TicketDto() {
    }

    public TicketDto(Date date) {

      this.visitDate = date;
    }

    public Date getVisitDate() {
      return visitDate;
    }

    public void setVisitDate(Date visitDate) {
      this.visitDate = visitDate;
    }
  }

}
