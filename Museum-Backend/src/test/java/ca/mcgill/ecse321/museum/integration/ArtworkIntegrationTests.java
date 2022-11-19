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
   * Test to get all artworks
   * 
   * @author Siger
   */
  @Test
  public void testGetAllArtworks() {
    // Test controller GET RESTful API
    ResponseEntity<ArtworkDto[]> response = client.getForEntity("/artworks", ArtworkDto[].class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artworkService.getAllArtworks().size(), response.getBody().length, "Response body has correct size");
    assertEquals(artworkService.getAllArtworks().get(0).getArtworkId(), response.getBody()[0].getArtworkId(),
        "Response body has correct artwork id");
  }

  /**
   * Test to get all artworks in a room
   * 
   * @author Siger
   */
  @Test
  public void testGetAllArtworksInRoom() {
    // Get room id
    Long roomId = roomService.getAllRooms().get(0).getRoomId();

    // Create artwork in room
    Artwork artwork = new Artwork();
    artwork.setName("The Scream");
    artwork.setArtist("Edvard Munch");
    artwork.setIsAvailableForLoan(true);
    artwork.setLoanFee(100.99);
    artwork.setImage("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0a/The_Scream.jpg/1200px-The_Scream.jpg");
    artwork.setIsOnLoan(false);
    artwork.setRoom(roomService.getRoomById(roomId));
    artwork = artworkRepository.save(artwork);

    // Test controller GET RESTful API
    ResponseEntity<ArtworkDto[]> response = client.getForEntity("/artworks/room/" + roomId, ArtworkDto[].class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artworkService.getAllArtworksByRoom(roomId).size(), response.getBody().length,
        "Response body has correct size");
    assertEquals(artwork.getArtworkId(), response.getBody()[0].getArtworkId(), "Response body has correct artwork id");
  }

  /**
   * Test to get all artworks in a room that does not exist
   * 
   * @author Siger
   */
  @Test
  public void testGetAllArtworksInRoomDoesNotExist() {
    // Test controller GET RESTful API
    ResponseEntity<String> response = client.getForEntity("/artworks/room/-1", String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertEquals("Room does not exist", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to get all artworks that is available for loan
   * 
   * @author Siger
   */
  @Test
  public void testGetAllArtworksAvailableForLoan() {
    // Test controller GET RESTful API
    ResponseEntity<ArtworkDto[]> response = client.getForEntity("/artworks/availableForLoan/true", ArtworkDto[].class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artworkService.getAllArtworksByAvailabilityForLoan(true).size(), response.getBody().length,
        "Response body has correct size");
  }

  /**
   * Test to get all artworks that is not available for loan
   * 
   * @author Siger
   */
  @Test
  public void testGetAllArtworksNotAvailableForLoan() {
    // Test controller GET RESTful API
    ResponseEntity<ArtworkDto[]> response = client.getForEntity("/artworks/availableForLoan/false", ArtworkDto[].class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(artworkService.getAllArtworksByAvailabilityForLoan(false).size(), response.getBody().length,
        "Response body has correct size");
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

  /**
   * Integration test method for getting the specific artworks status by using TEST REST TEMPLATE
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetArtworkStatus() {
      // We created an artwork in the DB
      List<ArtworkDto> artworkDtos = createArtworkDtos();
      Long artworkId = artworkDtos.get(0).getArtworkId();
      Long artworkId2 = artworkDtos.get(1).getArtworkId();

      // We do a get request to see if our controller method works -- Artwork 1 should be on loan
      ResponseEntity<String> response = client.getForEntity("/getArtworkStatus/" + artworkId.toString(), String.class);
      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      assertEquals("loan", response.getBody(), "Response correctly said that artwork is on loan");

      // // We do a get request to see if our controller method works -- Artwork 2 should be on display
      ResponseEntity<String> response2 = client.getForEntity("/getArtworkStatus/" + artworkId2.toString(), String.class);
      assertNotNull(response2);
      assertEquals(HttpStatus.OK, response2.getStatusCode());
      assertNotNull(response2.getBody(), "Response has body");
      assertEquals("display", response2.getBody(), "Response correctly said that artwork is on display");
  }

  /**
   * Integration test method for getting the number of artworks in a room by using TEST REST TEMPLATE
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetNumberOfArtworksInRoom() {
      // We created an artwork in the DB
      List<ArtworkDto> artworkDtos = createArtworkDtos();
      Long roomId = artworkDtos.get(0).getRoom().getRoomId();

      // We do a get request to see if our controller method works
      ResponseEntity<Integer> response = client.getForEntity("/getNumberOfArtworksInRoom/" + roomId.toString(), Integer.class);
      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      // There are two artworks in the room so the request body should be 2
      assertEquals(2, response.getBody(), "Response correctly said that there are two artworks in room");
  }

  /**
   * Integration test method for moving artwork to particular room using TEST REST TEMPLATE
   *
   * @author kieyanmamiche
   */
  @Test
  public void testMoveArtworkToRoom() {
      List<ArtworkDto> artworkDtoList = createArtworkDtos();
      Long artworkId1 = artworkDtoList.get(0).getArtworkId();
      Long artworkId2 = artworkDtoList.get(1).getArtworkId();
      Long roomIdNew = artworkDtoList.get(2).getRoom().getRoomId();

      // We do a post request to see if our controller method works - Test for artwork1
      ResponseEntity<ArtworkDto> response = client.postForEntity("/moveArtworkToRoom/" + artworkId1.toString() + "/" + roomIdNew.toString(), null, ArtworkDto.class);
      assertNotNull(response);
      assertEquals(HttpStatus.OK, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      // There are two artworks in the room so the request body should be 2
      assertEquals(roomIdNew, response.getBody().getRoom().getRoomId(), "Response correctly showed that the artworks new room id is correct and that it has been moved");

      // We do a get request to see if our controller method works
      ResponseEntity<ArtworkDto> response2 = client.postForEntity("/moveArtworkToRoom/" + artworkId2.toString() + "/" + roomIdNew,null, ArtworkDto.class);
      assertNotNull(response2);
      assertEquals(HttpStatus.OK, response2.getStatusCode());
      assertNotNull(response2.getBody(), "Response has body");
      // There are two artworks in the room so the request body should be 2
      assertEquals(roomIdNew, response2.getBody().getRoom().getRoomId(), "Response correctly showed that the artworks new room id is correct and that it has been moved");

  }


  /**
   * Integration test method for getting the artwork status when the artwork doesn't exist
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetArtworkStatusNonExisting() {
      // We do a get request to see if our controller handles bad request well
      ResponseEntity<String> response = client.getForEntity("/getArtworkStatus/" + "1234", String.class);
      assertNotNull(response);
      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      assertEquals("Artwork does not exist", response.getBody(), "Response has correct error message");
  }

  /**
   * Integration test method for getting the number of artworks in a given room
   * when the room doesn't exist
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetNumberOfArtworksInRoom_NoRoom() {

      String roomId = "1234";

      // We do a get request to see if our controller method works
      ResponseEntity<String> response = client.getForEntity("/getNumberOfArtworksInRoom/" + roomId, String.class);
      assertNotNull(response);
      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      assertEquals("Room does not exist", response.getBody(), "Response has correct error message");
  }

  /**
   * Integration test method for moving a specific artwork to a different room
   * when the artwork doesn't exist
   *
   * @author kieyanmamiche
   */
  @Test
  public void testMoveArtworkToRoom_ArtworkNonExisting() {

      // We created an artwork in the DB
      List<ArtworkDto> artworkDtos = createArtworkDtos();
      Long roomId = artworkDtos.get(0).getRoom().getRoomId();

      // Make sure there are 2 artworks in the room before the move
      ResponseEntity<Integer> responseTester = client.getForEntity("/getNumberOfArtworksInRoom/" + roomId.toString(), Integer.class);
      assertEquals(2, responseTester.getBody(), "Response correctly said that there are two artworks in room");

      // Bad artwork id, for artwork which doesn't exist
      String artworkIdBad = "123214";

      // We do a get request to see if our controller handles bad request well
      ResponseEntity<String> response = client.postForEntity("/moveArtworkToRoom/" + artworkIdBad + "/" + roomId,null, String.class);
      assertNotNull(response);
      System.out.println(response.getBody());
      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      assertEquals("Artwork does not exist", response.getBody(), "Response has correct error message");

      // Make sure that the count of artworks in the room stays the same -- aka it stays at 2
      ResponseEntity<Integer> responseTester2 = client.getForEntity("/getNumberOfArtworksInRoom/" + roomId, Integer.class);
      assertEquals(2, responseTester2.getBody(), "Response correctly said that there are two artworks in room");

  }

  /**
   * Integration test method for moving a specific artwork to a different room
   * when the room doesn't exist
   *
   * @author kieyanmamiche
   */
  @Test
  public void testMoveArtworkToRoom_RoomNonExisting() {

      List<ArtworkDto> artworkDtoList = createArtworkDtos();
      Long artworkId = artworkDtoList.get(0).getArtworkId();
      Long roomIdOriginal = artworkDtoList.get(0).getRoom().getRoomId(); // This shouldn't change
      String roomIdBad = "123214";

      // We do a get request to see if our controller handles bad request well
      ResponseEntity<String> response = client.postForEntity("/moveArtworkToRoom/" + artworkId + "/" + roomIdBad,null, String.class);
      assertNotNull(response);
      System.out.println(response.getBody());
      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      assertEquals("Room does not exist", response.getBody(), "Response has correct error message");

      // Make sure the artwork's room hasn't changed
      Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);
      assertEquals(roomIdOriginal, artwork.getRoom().getRoomId(), "Room has not changed on Room error");

  }

  /**
   * Integration test method for moving a specific artwork to a different room
   * when the room is at full capacity
   *
   * @author kieyanmamiche
   */
  @Test
  public void testMoveArtworkToRoom_FullCapacity() {

      List<ArtworkDto> artworkDtoList = createArtworkDtos();
      Long artworkId = artworkDtoList.get(0).getArtworkId();
      Long roomIdOriginal = artworkDtoList.get(0).getRoom().getRoomId(); // This shouldn't change
      Long roomIdFull = artworkDtoList.get(3).getRoom().getRoomId();
      int roomCount1 = roomRepository.findRoomByRoomId(roomIdOriginal).getCurrentNumberOfArtwork();
      int roomCount2 = roomRepository.findRoomByRoomId(roomIdFull).getCurrentNumberOfArtwork();

      // We do a get request to see if our controller handles bad request well
      ResponseEntity<String> response = client.postForEntity("/moveArtworkToRoom/" + artworkId + "/" + roomIdFull,null, String.class);
      assertNotNull(response);
      System.out.println(response.getBody());
      assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
      assertNotNull(response.getBody(), "Response has body");
      assertEquals("Room is full capacity", response.getBody(), "Response has correct error message");


      // Make sure that the number of artworks in each of the rooms hasn't changed
      Room roomOriginal = roomRepository.findRoomByRoomId(roomIdOriginal);
      Room roomFull = roomRepository.findRoomByRoomId(roomIdFull);
      assertEquals(roomOriginal.getCurrentNumberOfArtwork(), roomCount1, "Room has not changed number of artworks");
      assertEquals(roomFull.getCurrentNumberOfArtwork(), roomCount2, "Room has not changed number of artworks");

      // Make sure the artwork didn't change rooms
      Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);
      assertEquals(roomIdOriginal, artwork.getRoom().getRoomId(), "Room has not changed on Room error");

  }


  /**
   * An initialization method which helps populate the database so that the integration tests work properly
   *
   * @author kieyanmamiche
   */
  public List<ArtworkDto> createArtworkDtos(){
      List<ArtworkDto> artworkDtos = new ArrayList<>();

      // Expected values for the artwork 1
      String artworkName = "The Art";
      String artist = "Kian";
      boolean isAvailableForLoan = false;
      boolean isOnLoan = true;
      double loanFee = 12.5;
      String image = "https://source.unsplash.com/C54OKB99iuw";

      // Expected values for the artwork 2
      String artworkName2 = "The Art2";
      String artist2 = "Bob";
      boolean isAvailableForLoan2 = true;
      boolean isOnLoan2 = false;
      double loanFee2 = 1000000;
      String image2 = "https://source.unsplash.com/C54OKB9922iuw";

      // Expected values for the artwork 3
      String artworkName3 = "The Art3";
      String artist3 = "Billy";
      boolean isAvailableForLoan3 = false;
      boolean isOnLoan3 = false;
      double loanFee3 = 999999999;
      String image3 = "https://source.unsplash.com/C54OKB9922iuw";

      Schedule schedule = new Schedule();

      // Creating a museum
      Museum museum = new Museum();
      museum.setName("Rougon-Macquart");
      museum.setVisitFee(12.5);
      museum.setSchedule(schedule);
      museumRepository.save(museum);

      // Creating room 1
      Room room = new Room();
      room.setRoomName("Room 1");
      room.setRoomType(RoomType.Small);
      room.setCurrentNumberOfArtwork(2);
      room.setMuseum(museum);
      roomRepository.save(room);


      // Creating room 2
      Room room2 = new Room();
      room2.setRoomName("Room 2");
      room2.setRoomType(RoomType.Large);
      room2.setCurrentNumberOfArtwork(1);
      room2.setMuseum(museum);
      roomRepository.save(room2);

      // Creating room 3
      Room room3 = new Room();
      room3.setRoomName("Room 3 - Full capacity");
      room3.setRoomType(RoomType.Small);
      room3.setCurrentNumberOfArtwork(200);
      room3.setMuseum(museum);
      roomRepository.save(room3);

      // Initialize artwork 1
      Artwork artwork = new Artwork();
      artwork.setName(artworkName);
      artwork.setArtist(artist);
      artwork.setIsAvailableForLoan(isAvailableForLoan);
      artwork.setIsOnLoan(isOnLoan);
      artwork.setLoanFee(loanFee);
      artwork.setImage(image);
      artwork.setRoom(room);
      artworkRepository.save(artwork);

      // Initialize artwork 2
      Artwork artwork2 = new Artwork();
      artwork2.setName(artworkName2);
      artwork2.setArtist(artist2);
      artwork2.setIsAvailableForLoan(isAvailableForLoan2);
      artwork2.setIsOnLoan(isOnLoan2);
      artwork2.setLoanFee(loanFee2);
      artwork2.setImage(image2);
      artwork2.setRoom(room);
      artworkRepository.save(artwork2);

      // Initialize artwork 3
      Artwork artwork3 = new Artwork();
      artwork3.setName(artworkName3);
      artwork3.setArtist(artist3);
      artwork3.setIsAvailableForLoan(isAvailableForLoan3);
      artwork3.setIsOnLoan(isOnLoan3);
      artwork3.setLoanFee(loanFee3);
      artwork3.setImage(image3);
      artwork3.setRoom(room2);
      artworkRepository.save(artwork3);

      // Initialize artwork 4 - duplicate of artwork 3 except different room
      Artwork artwork4 = new Artwork();
      artwork4.setName(artworkName3);
      artwork4.setArtist(artist3);
      artwork4.setIsAvailableForLoan(isAvailableForLoan3);
      artwork4.setIsOnLoan(isOnLoan3);
      artwork4.setLoanFee(loanFee3);
      artwork4.setImage(image3);
      artwork4.setRoom(room3);
      artworkRepository.save(artwork4);

      ArtworkDto artworkDto1 = DtoUtility.convertToDto(artwork);
      ArtworkDto artworkDto2 = DtoUtility.convertToDto(artwork2);
      ArtworkDto artworkDto3 = DtoUtility.convertToDto(artwork3);
      ArtworkDto artworkDto4 = DtoUtility.convertToDto(artwork4);
      artworkDtos.add(artworkDto1);
      artworkDtos.add(artworkDto2);
      artworkDtos.add(artworkDto3);
      artworkDtos.add(artworkDto4);

      return artworkDtos;
  }
}
