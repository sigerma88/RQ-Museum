package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArtworkIntegrationTest {


  @Autowired
  private TestRestTemplate client;

  @Autowired
  private ArtworkRepository artworkRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private MuseumRepository museumRepository;


  // Artwork
  private static final long ARTWORK_ID_1 = 678;
  private static final String ARTWORK_NAME_1 = "La Peur";
  private static final String ARTWORK_ARTIST_1 = "Ernest";
  private static final Boolean IS_AVAILABLE_FOR_LOAN = true;
  private static final double LOAN_FEE_1 = 100.00;
  private static final String IMAGE_1 = "ooo";
  private static final Boolean IS_ON_LOAN_1 = false;
  private static final String AVAILABILITY_1 = "Available";

  private static final long ARTWORK_ID_2 = 456;
  private static final String ARTWORK_NAME_2 = "Bien";
  private static final String ARTWORK_ARTIST_2 = "Booba";
  private static final Boolean IS_AVAILABLE_FOR_LOAN_2 = false;
  private static final double LOAN_FEE_2 = 245.5;
  private static final String IMAGE_2 = "aah";
  private static final Boolean IS_ON_LOAN_2 = false;

  private static final long ROOM_ID_1 = 235;
  private static final String ROOM_NAME_1 = "Le loat";
  private static final int ROOM1_NUMBER_OF_ARTWORKS = 1;

  private static final long ROOM_ID_2 = 890;
  private static final String ROOM_NAME_2 = "Le Boat";
  private static final int ROOM2_NUMBER_OF_ARTWORKS = 1;

  private static final String MUSEUM_NAME = "Le Louvre";
  private static final double VISIT_FEE = 24.5;
  private static final int NUMBER_OF_ARTWORKS = 2;


  @AfterEach
  public void clearDatabase() {
    artworkRepository.deleteAll();
    roomRepository.deleteAll();
  }

  @BeforeEach
  public void createArtworks() {

    artworkRepository.deleteAll();
    roomRepository.deleteAll();

    List<ArtworkDto> artworkDtos = new ArrayList<>();

    Schedule schedule = new Schedule();

    Museum museum = new Museum();
    museum.setName(MUSEUM_NAME);
    museum.setSchedule(schedule);
    museum.setVisitFee(VISIT_FEE);
    museumRepository.save(museum);


    Room room1 = new Room();
    //  room1.setRoomId(ROOM_ID_1);
    room1.setRoomName(ROOM_NAME_1);
    room1.setCurrentNumberOfArtwork(ROOM1_NUMBER_OF_ARTWORKS);
    room1.setMuseum(museum);
    room1.setRoomType(RoomType.Small);
    roomRepository.save(room1);

    Room room2 = new Room();
    // room2.setRoomId(ROOM_ID_2);
    room2.setRoomName(ROOM_NAME_2);
    room2.setCurrentNumberOfArtwork(ROOM2_NUMBER_OF_ARTWORKS);
    room2.setMuseum(museum);
    room2.setRoomType(RoomType.Small);
    roomRepository.save(room2);

    Artwork artwork1 = new Artwork();
    artwork1.setArtworkId(ARTWORK_ID_1);
    artwork1.setName(ARTWORK_NAME_1);
    artwork1.setArtist(ARTWORK_ARTIST_1);
    artwork1.setImage(IMAGE_1);
    artwork1.setLoanFee(LOAN_FEE_1);
    artwork1.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN);
    artwork1.setIsOnLoan(IS_ON_LOAN_1);
    artwork1.setRoom(room1);
    artworkRepository.save(artwork1);
    //artworkDtos.add(DtoUtility.convertToDto(artwork1));


    Artwork artwork2 = new Artwork();
    artwork2.setArtworkId(ARTWORK_ID_2);
    artwork2.setArtist(ARTWORK_ARTIST_2);
    artwork2.setName(ARTWORK_NAME_2);
    artwork2.setImage(IMAGE_2);
    artwork2.setLoanFee(LOAN_FEE_2);
    artwork2.setIsOnLoan(IS_ON_LOAN_2);
    artwork2.setRoom(room2);
    artwork2.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_2);
    artworkRepository.save(artwork2);
    //  artworkDtos.add(DtoUtility.convertToDto(artwork2));

    //return artworkDtos;
  }

  /**
   * Test for getAllArtworks
   * Success scenario
   *
   * @author Zahra
   */

  @Test
  public void testGetAllArtworks() {
    //List<ArtworkDto> artworkDtos = createArtworkDtos();
    ResponseEntity<ArtworkDto[]> response = client.getForEntity("/artworks", ArtworkDto[].class);
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(NUMBER_OF_ARTWORKS, response.getBody().length);

  }

  /**
   * Test for getAllArtworks
   * Fail scenario
   *
   * @author Zahra
   */
  @Test
  public void testEmptyGetAllArtworks() {
    ResponseEntity<List> response = client.getForEntity("/artworks", List.class);
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

  }


  @Test
  public void testDisplayArtworkWithAvailabilityForLoan() {

    ResponseEntity<Map> response = client.getForEntity("/artworks/availability", Map.class);
    //List<ArtworkDto> artworkList = createArtworkDtos();
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    //  assertEquals("Unavailable", response.getBody().get(ARTWORK_NAME_1));
    assertEquals(new HashMap<>(), response.getBody());

    assertEquals(NUMBER_OF_ARTWORKS, response.getBody().size());

  }


  /**
   * Test for getArtworkWithLoanFee
   * Success scenario
   *
   * @author Zahra
   */
  @Test
  public void testDisplayArtworkWithLoanFee() {
    ResponseEntity<Map> response = client.getForEntity("/artworks/loanFee", Map.class);
    assertNotNull(response);
    assertNotNull(response.getBody(), "Response has body");
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertTrue(LOAN_FEE_1 == (double) response.getBody().get(ARTWORK_NAME_1));

  }
  /*

   */
}
