package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import ca.mcgill.ecse321.museum.service.RoomService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ArtworkIntegrationTests {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private ArtworkRepository artworkRepository;

  @Autowired
  private MuseumRepository museumRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private ArtworkService artworkService;

  @Autowired
  private RoomService roomService;

  @BeforeEach
  public void setup() {
    // clear all repositories
    artworkRepository.deleteAll();
    roomRepository.deleteAll();
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();

    // Create stubs

    // Create a schedule
    Schedule schedule = new Schedule();

    // Creating a museum
    Museum museum = new Museum();
    museum.setName("Rougon-Macquart");
    museum.setVisitFee(12.5);
    museum.setSchedule(schedule);
    museumRepository.save(museum);

    // Creating a room
    Room room = new Room();
    room.setRoomName("Room 1");
    room.setRoomType(RoomType.Small);
    room.setCurrentNumberOfArtwork(0);
    room.setMuseum(museum);
    roomRepository.save(room);

    // Creating an artwork
    Artwork artwork = new Artwork();
    artwork.setName("La Joconde");
    artwork.setArtist("Leonardo Da Vinci");
    artwork.setIsAvailableForLoan(true);
    artwork.setLoanFee(110.99);
    artwork.setImage("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/La_Joconde.jpg/800px-La_Joconde.jpg");
    artwork.setIsOnLoan(true);
    artworkRepository.save(artwork);
  }

  @AfterEach
  public void clearDatabase() {
    // clear all repositories
    artworkRepository.deleteAll();
    roomRepository.deleteAll();
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();
  }

  /**
   * Test to create an artwork in no room and on loan
   * 
   * @author Siger
   */
  @Test
  public void testCreateArtworkOnLoanNoRoom() {
    // Params
    String name = "The Scream";
    String artist = "Edvard Munch";
    Boolean isAvailableForLoan = true;
    Double loanFee = 100.99;
    String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg";
    Boolean isOnLoan = true;
    Long roomId = null;

    // Test controller POST RESTful API
    ResponseEntity<ArtworkDto> response = client.postForEntity("/artwork?name=" + name + "&artist=" + artist
        + "&isAvailableForLoan=" + isAvailableForLoan + "&loanFee=" + loanFee + "&image=" + image + "&isOnLoan="
        + isOnLoan, null, ArtworkDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(name, response.getBody().getName(), "Response body has correct name");
    assertEquals(artist, response.getBody().getArtist(), "Response body has correct artist");
    assertEquals(isAvailableForLoan, response.getBody().getIsAvailableForLoan(),
        "Response body has correct availability");
    assertEquals(loanFee, response.getBody().getLoanFee(), "Response body has correct loan fee");
    assertEquals(image, response.getBody().getImage(), "Response body has correct image");
    assertEquals(isOnLoan, response.getBody().getIsOnLoan(), "Response body has correct loan status");
    assertEquals(roomId, response.getBody().getRoom(), "Response body has correct room");
    assertTrue(response.getBody().getArtworkId() > 0, "Response body has valid artwork id");
  }

  /**
   * Test to create an artwork in a room and not on loan
   * 
   * @author Siger
   */
  @Test
  public void testCreateArtworkNotOnLoanInRoom() {
    // Get room id
    Long roomId = roomService.getAllRooms().get(0).getRoomId();

    // Params
    String name = "The Scream";
    String artist = "Edvard Munch";
    Boolean isAvailableForLoan = true;
    Double loanFee = 100.99;
    String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg";
    Boolean isOnLoan = false;

    // Test controller POST RESTful API
    ResponseEntity<ArtworkDto> response = client.postForEntity("/artwork?name=" + name + "&artist=" + artist
        + "&isAvailableForLoan=" + isAvailableForLoan + "&loanFee=" + loanFee + "&image=" + image + "&isOnLoan="
        + isOnLoan + "&roomId=" + roomId, null, ArtworkDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(name, response.getBody().getName(), "Response body has correct name");
    assertEquals(artist, response.getBody().getArtist(), "Response body has correct artist");
    assertEquals(isAvailableForLoan, response.getBody().getIsAvailableForLoan(),
        "Response body has correct availability");
    assertEquals(loanFee, response.getBody().getLoanFee(), "Response body has correct loan fee");
    assertEquals(image, response.getBody().getImage(), "Response body has correct image");
    assertEquals(isOnLoan, response.getBody().getIsOnLoan(), "Response body has correct loan status");
    assertEquals(roomId, response.getBody().getRoom().getRoomId(), "Response body has correct room");
    assertTrue(response.getBody().getArtworkId() > 0, "Response body has valid artwork id");
  }

  /**
   * Test to create an artwork in a room and on loan
   * 
   * @author Siger
   */
  @Test
  public void testCreateArtworkOnLoanInRoom() {
    // Get room id
    Long roomId = roomService.getAllRooms().get(0).getRoomId();

    // Params
    String name = "The Scream";
    String artist = "Edvard Munch";
    Boolean isAvailableForLoan = true;
    Double loanFee = 100.99;
    String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg";
    Boolean isOnLoan = true;

    // Test controller POST RESTful API
    ResponseEntity<String> response = client.postForEntity("/artwork?name=" + name + "&artist=" + artist
        + "&isAvailableForLoan=" + isAvailableForLoan + "&loanFee=" + loanFee + "&image=" + image + "&isOnLoan="
        + isOnLoan + "&roomId=" + roomId, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Room must be null if artwork is on loan", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to create an artwork in no room and not on loan
   * 
   * @author Siger
   */
  @Test
  public void testCreateArtworkNotOnLoanNoRoom() {
    // Params
    String name = "The Scream";
    String artist = "Edvard Munch";
    Boolean isAvailableForLoan = true;
    Double loanFee = 100.99;
    String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg";
    Boolean isOnLoan = false;

    // Test controller POST RESTful API
    ResponseEntity<String> response = client.postForEntity("/artwork?name=" + name + "&artist=" + artist
        + "&isAvailableForLoan=" + isAvailableForLoan + "&loanFee=" + loanFee + "&image=" + image + "&isOnLoan="
        + isOnLoan, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Room cannot be null if artwork is not on loan", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to create an artwork with no name
   * 
   * @author Siger
   */
  @Test
  public void testCreateArtworkNoName() {
    // Get room id
    Long roomId = roomService.getAllRooms().get(0).getRoomId();

    // Params
    String name = "";
    String artist = "Edvard Munch";
    Boolean isAvailableForLoan = true;
    Double loanFee = 100.99;
    String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg";
    Boolean isOnLoan = false;

    // Test controller POST RESTful API
    ResponseEntity<String> response = client.postForEntity("/artwork?name=" + name + "&artist=" + artist
        + "&isAvailableForLoan=" + isAvailableForLoan + "&loanFee=" + loanFee + "&image=" + image + "&isOnLoan="
        + isOnLoan + "&roomId=" + roomId, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Artwork name cannot be empty", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to get an artwork by id
   * 
   * @author Siger
   */
  @Test
  public void testGetArtworkById() {
    // Get artwork and its id
    Artwork artwork = artworkService.getAllArtworks().get(0);

    // Test controller GET RESTful API
    ResponseEntity<ArtworkDto> response = client.getForEntity("/artwork/" + artwork.getArtworkId(), ArtworkDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artwork.getArtworkId(), response.getBody().getArtworkId(), "Response body has correct artwork id");
    assertEquals(artwork.getName(), response.getBody().getName(), "Response body has correct name");
    assertEquals(artwork.getArtist(), response.getBody().getArtist(), "Response body has correct artist");
    assertEquals(artwork.getIsAvailableForLoan(), response.getBody().getIsAvailableForLoan(),
        "Response body has correct availability");
    assertEquals(artwork.getLoanFee(), response.getBody().getLoanFee(), "Response body has correct loan fee");
    assertEquals(artwork.getImage(), response.getBody().getImage(), "Response body has correct image");
    assertEquals(artwork.getIsOnLoan(), response.getBody().getIsOnLoan(), "Response body has correct loan status");
    assertEquals(artwork.getRoom(), response.getBody().getRoom(), "Response body has correct room");
  }

  /**
   * Test to get an artwork by id that does not exist
   * 
   * @author Siger
   */
  @Test
  public void testGetArtworkByIdDoesNotExist() {
    // Test controller GET RESTful API
    ResponseEntity<String> response = client.getForEntity("/artwork/-1", String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("There is no such artwork", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to edit an artwork's information
   * 
   * @author Siger
   */
  @Test
  public void testEditArtworkInfo() {
    // Get artwork id
    Artwork artwork = artworkService.getAllArtworks().get(0);

    // Params
    String name = "The Scream";
    String artist = "Edvard Munch";
    String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg";

    // Test controller PUT RESTful API
    ResponseEntity<ArtworkDto> response = client.exchange(
        "/artwork/info/" + artwork.getArtworkId() + "?name=" + name + "&artist=" + artist + "&image=" + image,
        HttpMethod.PUT, null, ArtworkDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artwork.getArtworkId(), response.getBody().getArtworkId(), "Response body has correct artwork id");
    assertEquals(name, response.getBody().getName(), "Response body has correct name");
    assertEquals(artist, response.getBody().getArtist(), "Response body has correct artist");
    assertEquals(image, response.getBody().getImage(), "Response body has correct image");
  }

  /**
   * Test to edit an artwork's information with no parameters
   * 
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoNoParams() {
    // Get artwork id
    Artwork artwork = artworkService.getAllArtworks().get(0);

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange("/artwork/info/" + artwork.getArtworkId(), HttpMethod.PUT, null,
        String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Nothing to edit, all fields are empty", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to edit an artwork's information with invalid artwork id
   * 
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoInvalidArtworkId() {
    // Params
    String name = "The Scream";
    String artist = "Edvard Munch";
    String image = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg";

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange(
        "/artwork/info/-1?name=" + name + "&artist=" + artist + "&image=" + image, HttpMethod.PUT, null,
        String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Artwork does not exist", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to edit an artwork's loan information
   * 
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfo() {
    // Get artwork id
    Artwork artwork = artworkService.getAllArtworks().get(0);

    // Params
    boolean isAvailableForLoan = true;
    double loanFee = 99.0;

    // Test controller PUT RESTful API
    ResponseEntity<ArtworkDto> response = client.exchange(
        "/artwork/loanInfo/" + artwork.getArtworkId() + "?isAvailableForLoan=" + isAvailableForLoan + "&loanFee="
            + loanFee,
        HttpMethod.PUT, null, ArtworkDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artwork.getArtworkId(), response.getBody().getArtworkId(), "Response body has correct artwork id");
    assertEquals(isAvailableForLoan, response.getBody().getIsAvailableForLoan(),
        "Response body has correct availability");
    assertEquals(loanFee, response.getBody().getLoanFee(), "Response body has correct loan fee");
  }

  /**
   * Test to edit an artwork's loan information with invalid artwork id
   * 
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoInvalidArtworkId() {
    // Params
    boolean isAvailableForLoan = true;
    double loanFee = 99.0;

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange(
        "/artwork/loanInfo/-1?isAvailableForLoan=" + isAvailableForLoan + "&loanFee=" + loanFee, HttpMethod.PUT, null,
        String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Artwork does not exist", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to edit an artwork's loan information with no loan fee when it is
   * available for loan
   * 
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoAvailableAndNoLoanFee() {
    // Get artwork id
    Artwork artwork = artworkService.getAllArtworks().get(0);

    // Params
    boolean isAvailableForLoan = true;

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange(
        "/artwork/loanInfo/" + artwork.getArtworkId() + "?isAvailableForLoan=" + isAvailableForLoan, HttpMethod.PUT,
        null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Loan fee cannot be null if artwork is available for loan", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to edit an artwork's loan information with non null loan fee when it is
   * not available for loan
   * 
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoNotAvailableAndNonNullLoanFee() {
    // Get artwork id
    Artwork artwork = artworkService.getAllArtworks().get(0);

    // Params
    boolean isAvailableForLoan = false;
    double loanFee = 99.0;

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange(
        "/artwork/loanInfo/" + artwork.getArtworkId() + "?isAvailableForLoan=" + isAvailableForLoan + "&loanFee="
            + loanFee,
        HttpMethod.PUT, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Loan fee must be null if artwork is not available for loan", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to delete an artwork
   * 
   * @author Siger
   */
  @Test
  public void testDeleteArtwork() {
    // Get artwork id
    Artwork artwork = artworkService.getAllArtworks().get(0);

    // Test controller DELETE RESTful API
    ResponseEntity<ArtworkDto> response = client.exchange("/artwork/" + artwork.getArtworkId(), HttpMethod.DELETE, null,
        ArtworkDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artwork.getArtworkId(), response.getBody().getArtworkId(), "Response body has correct artwork id");
    assertEquals(artwork.getName(), response.getBody().getName(), "Response body has correct artwork name");
    assertEquals(artwork.getArtist(), response.getBody().getArtist(), "Response body has correct artwork artist");
    assertEquals(artwork.getImage(), response.getBody().getImage(), "Response body has correct artwork image");
    assertEquals(artwork.getIsAvailableForLoan(), response.getBody().getIsAvailableForLoan(),
        "Response body has correct artwork availability");
    assertEquals(artwork.getLoanFee(), response.getBody().getLoanFee(), "Response body has correct artwork loan fee");
    assertEquals(artwork.getIsOnLoan(), response.getBody().getIsOnLoan(), "Response body has correct artwork on loan");
    assertEquals(artwork.getRoom(), response.getBody().getRoom(), "Response body has correct artwork room");
  }

  /**
   * Test to delete an artwork with invalid artwork id
   * 
   * @author Siger
   */
  @Test
  public void testDeleteArtworkInvalidArtworkId() {
    // Test controller DELETE RESTful API
    ResponseEntity<String> response = client.exchange("/artwork/-1", HttpMethod.DELETE, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Artwork does not exist", response.getBody(), "Response has correct body error message");
  }
}
