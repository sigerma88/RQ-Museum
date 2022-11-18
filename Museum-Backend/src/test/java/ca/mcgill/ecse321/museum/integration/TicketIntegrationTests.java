package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.TicketRepository;
import ca.mcgill.ecse321.museum.dto.TicketDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TicketIntegrationTests {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private TicketRepository ticketRepository;

  @BeforeEach
  @AfterEach
  public void clearDatabase() {

    ticketRepository.deleteAll();
  }

  @Test
  public void testBuyTickets() {

    ResponseEntity<?> response = client.postForEntity("tickets/purchase", new ArrayList<TicketDto>(), TicketDto.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    // assertEquals(response.getBody(), );
  }


  @Test
  public void testBuyTicketsWithInvalidDate() {


  }

  @Test
  public void testBuyTicketsWithInvalidNumber() {


  }


  @Test
  public void testGetTicketsByVisitor() {


  }

  @Test
  public void testGetTicketByInvalidVisitor() {

  }


}
