package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Room;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtworkIntegrationTest {


  @Autowired
  private TestRestTemplate client;

  @Autowired
  private ArtworkRepository artworkRepository;

  @Autowired
  private RoomRepository roomRepository;


  @BeforeEach
  @AfterEach
  public void clearDatabase() {
    roomRepository.deleteAll();
    artworkRepository.deleteAll();
  }


  @Test
  public void testGetAllArtworks() {
    ResponseEntity<ArtworkDto> response = client.postForEntity("/artworks", new ArrayList<ArtworkDto>(), ArtworkDto.class);
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());

  }


  @Test
  public void testGetArtworksWithRoom(Room room) {
    ResponseEntity<ArtworkDto> response = client.getForEntity("/artworks"+ room, new ArrayList<ArtworkDto>(), ArtworkDto.class);
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());


  }

  @Test
  public void testGetArtworksWithInvalidRoom(Room room) {
    ResponseEntity<ArtworkDto> response = client.getForEntity("/artworks"+ room, new ArrayList<ArtworkDto>(), ArtworkDto.class);
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());


  }


  @Test
  public void testDisplayArtworkWithAvailabilityForLoan() {
ResponseEntity<Map<String,String>> response = client.getForEntity("/artworks/availabilty", );
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");



  }

  @Test
  public void testDisplayArtworkWithLoanFee() {

  }





}
