package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.RoomDto;
import ca.mcgill.ecse321.museum.dto.RoomDtoNoIdRequest;
import ca.mcgill.ecse321.museum.integration.utilities.UserUtilities;
import ca.mcgill.ecse321.museum.model.*;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.RoomService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomIntegrationTests {

  private static final String FIRST_VALID_MANAGER_NAME = "admin";
  private static final String FIRST_VALID_MANAGER_EMAIL = "admin@mail.ca";

  private static final String VALID_PASSWORD = "#BrazilGp2022";

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private ArtworkRepository artworkRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private MuseumRepository museumRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private ManagerRepository managerRepository;

  @Autowired
  private RoomService roomService;

  @Autowired
  private MuseumService museumService;

  @BeforeEach
  public void setup() {
    // clear all repositories
    artworkRepository.deleteAll();
    roomRepository.deleteAll();
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();
    managerRepository.deleteAll();

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
  }

  @AfterEach
  public void clearDatabase() {
    // clear all repositories
    artworkRepository.deleteAll();
    roomRepository.deleteAll();
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();
    managerRepository.deleteAll();
  }

  /**
   * Test to create a room
   *
   * @author Siger
   */
  @Test
  public void testCreateRoom() {
    RoomDtoNoIdRequest roomDtoNoIdRequest = new RoomDtoNoIdRequest();

    // Get museum id
    roomDtoNoIdRequest.setMuseumId(museumService.getAllMuseums().get(0).getMuseumId());

    // Params
    roomDtoNoIdRequest.setRoomName("Room 2");
    roomDtoNoIdRequest.setRoomType(RoomType.Large);

    HttpEntity<?> entity = new HttpEntity<>(roomDtoNoIdRequest, loginSetupManager());

    // Test controller POST RESTful API
    ResponseEntity<RoomDto> response = client.exchange("/api/room", HttpMethod.POST, entity, RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(roomDtoNoIdRequest.getRoomName(), response.getBody().getRoomName(), "Room name is correct");
    assertEquals(roomDtoNoIdRequest.getRoomType(), response.getBody().getRoomType(), "Room type is correct");
    assertEquals(roomDtoNoIdRequest.getMuseumId(), response.getBody().getMuseum().getMuseumId(),
        "Museum id is correct");
    assertTrue(response.getBody().getRoomId() > 0, "Room id is correct");
  }

  /**
   * Test to create a room with no room name
   *
   * @author Siger
   */
  @Test
  public void testCreateRoomNoRoomName() {
    RoomDtoNoIdRequest roomDtoNoIdRequest = new RoomDtoNoIdRequest();

    // Get museum id
    roomDtoNoIdRequest.setMuseumId(museumService.getAllMuseums().get(0).getMuseumId());

    // Params
    roomDtoNoIdRequest.setRoomName("");
    roomDtoNoIdRequest.setRoomType(RoomType.Large);

    HttpEntity<?> entity = new HttpEntity<>(roomDtoNoIdRequest, loginSetupManager());

    // Test controller POST RESTful API
    ResponseEntity<String> response = client.postForEntity("/api/room", entity, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room name cannot be empty", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to get a room by id
   *
   * @author Siger
   */
  @Test
  public void testGetRoomById() {
    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    // Test controller GET RESTful API
    ResponseEntity<RoomDto> response =
        client.getForEntity("/api/room/" + room.getRoomId(), RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(room.getRoomId(), response.getBody().getRoomId(), "Room id is correct");
    assertEquals(room.getRoomName(), response.getBody().getRoomName(), "Room name is correct");
    assertEquals(room.getRoomType(), response.getBody().getRoomType(), "Room type is correct");
    assertEquals(room.getMuseum().getMuseumId(), response.getBody().getMuseum().getMuseumId(),
        "Museum id is correct");
  }

  /**
   * Test to get a room by invalid id
   *
   * @author Siger
   */
  @Test
  public void testGetRoomByInvalidId() {
    // Test controller GET RESTful API
    ResponseEntity<String> response = client.getForEntity("/api/room/-1", String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("There is no such room", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to get all rooms There is no failure case for this test
   *
   * @author Siger
   */
  @Test
  public void testGetAllRooms() {
    // Test controller GET RESTful API
    ResponseEntity<RoomDto[]> response = client.getForEntity("/api/room", RoomDto[].class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(1, response.getBody().length, "Response has correct number of rooms");
  }

  /**
   * Test to get all rooms by museum id
   *
   * @author Siger
   */
  @Test
  public void testGetAllRoomsByMuseumId() {
    // Get museum id
    Long museumId = museumService.getAllMuseums().get(0).getMuseumId();

    // Test controller GET RESTful API
    ResponseEntity<RoomDto[]> response =
        client.getForEntity("/api/room/museum/" + museumId, RoomDto[].class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(1, response.getBody().length, "Response has correct number of rooms");
    assertEquals(museumId, response.getBody()[0].getMuseum().getMuseumId(), "Museum id is correct");
  }

  /**
   * Test to get all rooms by invalid museum id
   *
   * @author Siger
   */
  @Test
  public void testGetAllRoomsByInvalidMuseumId() {
    // Test controller GET RESTful API
    ResponseEntity<String> response = client.getForEntity("/api/room/museum/-1", String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Museum with id -1 does not exist", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to update a room
   *
   * @author Siger
   */
  @Test
  public void testUpdateRoom() {
    RoomDtoNoIdRequest roomDtoNoIdRequest = new RoomDtoNoIdRequest();

    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    // Create another schedule
    Schedule schedule = new Schedule();

    // Creating another museum
    Museum museum = new Museum();
    museum.setName("Les Travailleurs de la mer");
    museum.setVisitFee(7.99);
    museum.setSchedule(schedule);
    museumRepository.save(museum);

    // Params
    roomDtoNoIdRequest.setRoomName("New room name");
    roomDtoNoIdRequest.setRoomType(RoomType.Storage);
    roomDtoNoIdRequest.setMuseumId(museum.getMuseumId());

    HttpEntity<?> entity = new HttpEntity<>(roomDtoNoIdRequest, loginSetupManager());

    // Test controller PUT RESTful API
    ResponseEntity<RoomDto> response = client.exchange("/api/room/" + room.getRoomId(),
        HttpMethod.PUT, entity, RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(room.getRoomId(), response.getBody().getRoomId(), "Room id is correct");
    assertEquals(roomDtoNoIdRequest.getRoomName(), response.getBody().getRoomName(), "Room name is correct");
    assertEquals(roomDtoNoIdRequest.getRoomType(), response.getBody().getRoomType(), "Room type is correct");
    assertEquals(museum.getMuseumId(), response.getBody().getMuseum().getMuseumId(), "Museum id is correct");
  }

  /**
   * Test to update a room with invalid room id
   *
   * @author Siger
   */
  @Test
  public void testUpdateRoomWithInvalidRoomId() {
    RoomDtoNoIdRequest roomDtoNoIdRequest = new RoomDtoNoIdRequest();

    // Params
    roomDtoNoIdRequest.setRoomName("New room name");
    roomDtoNoIdRequest.setRoomType(RoomType.Storage);

    HttpEntity<?> entity = new HttpEntity<>(roomDtoNoIdRequest, loginSetupManager());

    // Test controller PUT RESTful API
    ResponseEntity<String> response =
        client.exchange("/api/room/-1", HttpMethod.PUT, entity, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room does not exist", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to update a room with invalid museum id
   *
   * @author Siger
   */
  @Test
  public void testUpdateRoomWithInvalidMuseumId() {
    RoomDtoNoIdRequest roomDtoNoIdRequest = new RoomDtoNoIdRequest();

    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    // Params
    roomDtoNoIdRequest.setRoomName("New room name");
    roomDtoNoIdRequest.setRoomType(RoomType.Storage);
    roomDtoNoIdRequest.setMuseumId(-1L);

    HttpEntity<?> entity = new HttpEntity<>(roomDtoNoIdRequest, loginSetupManager());

    // Test controller PUT RESTful API
    ResponseEntity<String> response =
        client.exchange("/api/room/" + room.getRoomId(), HttpMethod.PUT, entity, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Museum with id -1 does not exist", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to edit a room with no params
   *
   * @author Siger
   */
  @Test
  public void testEditRoomWithNoParams() {
    RoomDtoNoIdRequest roomDtoNoIdRequest = new RoomDtoNoIdRequest();

    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    HttpEntity<?> entity = new HttpEntity<>(roomDtoNoIdRequest, loginSetupManager());

    // Test controller PUT RESTful API
    ResponseEntity<String> response =
        client.exchange("/api/room/" + room.getRoomId(), HttpMethod.PUT, entity, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Nothing to edit, all fields are empty", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to delete a room
   *
   * @author Siger
   */
  @Test
  public void testDeleteRoom() {
    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());

    // Test controller DELETE RESTful API
    ResponseEntity<RoomDto> response =
        client.exchange("/api/room/" + room.getRoomId(), HttpMethod.DELETE, entity, RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(room.getRoomId(), response.getBody().getRoomId(), "Room id is correct");
    assertEquals(room.getRoomName(), response.getBody().getRoomName(), "Room name is correct");
    assertEquals(room.getRoomType(), response.getBody().getRoomType(), "Room type is correct");
    assertEquals(room.getMuseum().getMuseumId(), response.getBody().getMuseum().getMuseumId(),
        "Museum id is correct");
  }

  /**
   * Test to delete a room with invalid room id
   *
   * @author Siger
   */
  @Test
  public void testDeleteRoomWithInvalidRoomId() {
    // Test controller DELETE RESTful API

    HttpEntity<?> entity = new HttpEntity<>(loginSetupManager());

    ResponseEntity<String> response =
        client.exchange("/api/room/-1", HttpMethod.DELETE, entity, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room does not exist", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Test to get the maximum number of artworks for a room
   *
   * @author Siger
   */
  @Test
  public void testGetMaxArtworks() {
    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    // Test controller GET RESTful API
    ResponseEntity<Integer> response = client.exchange("/api/room/maxArtworks/" + room.getRoomId(),
        HttpMethod.GET, null, Integer.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(200, response.getBody(), "Max artworks is correct");
  }

  /**
   * Test to get the maximum number of artworks for a room with invalid room id
   *
   * @author Siger
   */
  @Test
  public void testGetMaxArtworksWithInvalidRoomId() {
    // Test controller GET RESTful API
    ResponseEntity<String> response =
        client.exchange("/api/room/maxArtworks/-1", HttpMethod.GET, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room with id -1 does not exist", response.getBody(),
        "Response has correct body error message");
  }

  /**
   * Integration test method for getting the room capacity by using TEST REST
   * TEMPLATE
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetRoomCapacity() {
    List<Artwork> artworkList = createArtworks();
    Long roomId1 = artworkList.get(0).getRoom().getRoomId(); // Small room
    Long roomId2 = artworkList.get(1).getRoom().getRoomId(); // Large room
    Long roomId3 = artworkList.get(2).getRoom().getRoomId(); // Storage room

    // Get the capacity of room 1 using get request -- SMALL ROOM
    ResponseEntity<Integer> response =
        client.getForEntity("/api/room/getRoomCapacity/" + roomId1, Integer.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    // There is one artwork in a small room so the capacity should be 199
    assertEquals(199, response.getBody(), "Response correctly said that the capacity is 199");

    // Get the capacity of room 2 using get request -- LARGE ROOM
    ResponseEntity<Integer> response2 =
        client.getForEntity("/api/room/getRoomCapacity/" + roomId2, Integer.class);
    assertNotNull(response2);
    assertEquals(HttpStatus.OK, response2.getStatusCode());
    assertNotNull(response2.getBody(), "Response has body");
    // There is one artwork in a large room so the capacity should be 299
    assertEquals(299, response2.getBody(), "Response correctly said that the capacity is 299");

    // Get the capacity of room 3 using get request -- STORAGE ROOM
    ResponseEntity<Integer> response3 =
        client.getForEntity("/api/room/getRoomCapacity/" + roomId3, Integer.class);
    assertNotNull(response3);
    assertEquals(HttpStatus.OK, response3.getStatusCode());
    assertNotNull(response3.getBody(), "Response has body");
    // There is one artwork in a storage room so the capacity should be -1
    // (infinite)
    assertEquals(-1, response3.getBody(), "Response correctly said that the capacity is infinite");
  }

  /**
   * Integration test method for getting the room capacity by using TEST REST
   * TEMPLATE
   * - When room doesn't exist
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetRoomCapacity_NonExistingRoom() {

    String roomIdBad = "-1";

    // We do a get request to see if our controller handles bad request well
    ResponseEntity<String> response =
        client.getForEntity("/api/room/getRoomCapacity/" + roomIdBad, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertNotNull(response.getBody(), "Response has body");
    assertEquals("Room does not exist", response.getBody(), "Response has correct error message");
  }

  /**
   * An initialization method which helps populate the database so that the
   * integration tests work properly
   *
   * @author kieyanmamiche
   */
  public List<Artwork> createArtworks() {
    List<Artwork> artworks = new ArrayList<Artwork>();

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

    // Creating room 1 - SMALL ROOM
    Room room = new Room();
    room.setRoomName("Room 1");
    room.setRoomType(RoomType.Small);
    room.setCurrentNumberOfArtwork(1);
    room.setMuseum(museum);
    roomRepository.save(room);

    // Creating room 2 - LARGE ROOM
    Room room2 = new Room();
    room2.setRoomName("Room 2");
    room2.setRoomType(RoomType.Large);
    room2.setCurrentNumberOfArtwork(1);
    room2.setMuseum(museum);
    roomRepository.save(room2);

    // Creating room 3 - STORAGE ROOM
    Room room3 = new Room();
    room3.setRoomName("Room 3");
    room3.setRoomType(RoomType.Storage);
    room3.setCurrentNumberOfArtwork(1);
    room3.setMuseum(museum);
    roomRepository.save(room3);

    // Initialize artwork 1
    Artwork artwork1 = new Artwork();
    artwork1.setName(artworkName);
    artwork1.setArtist(artist);
    artwork1.setIsAvailableForLoan(isAvailableForLoan);
    artwork1.setIsOnLoan(isOnLoan);
    artwork1.setLoanFee(loanFee);
    artwork1.setImage(image);
    artwork1.setRoom(room);
    artworkRepository.save(artwork1);

    // Initialize artwork 2
    Artwork artwork2 = new Artwork();
    artwork2.setName(artworkName2);
    artwork2.setArtist(artist2);
    artwork2.setIsAvailableForLoan(isAvailableForLoan2);
    artwork2.setIsOnLoan(isOnLoan2);
    artwork2.setLoanFee(loanFee2);
    artwork2.setImage(image2);
    artwork2.setRoom(room2);
    artworkRepository.save(artwork2);

    // Initialize artwork 3
    Artwork artwork3 = new Artwork();
    artwork3.setName(artworkName3);
    artwork3.setArtist(artist3);
    artwork3.setIsAvailableForLoan(isAvailableForLoan3);
    artwork3.setIsOnLoan(isOnLoan3);
    artwork3.setLoanFee(loanFee3);
    artwork3.setImage(image3);
    artwork3.setRoom(room3);
    artworkRepository.save(artwork3);

    artworks.add(artwork1);
    artworks.add(artwork2);
    artworks.add(artwork3);

    return artworks;
  }

  /**
   * Create a manager and login
   *
   * @param newManager - the manager to login
   * @return managerDto - the logged in manager
   * @author Kevin
   */

  public ManagerDto createManagerAndLogin(Manager newManager) {
    managerRepository.save(newManager);
    ManagerDto manager = UserUtilities.createManagerDto(newManager);
    ResponseEntity<String> response =
        client.postForEntity("/api/auth/login", manager, String.class);
    List<String> session = response.getHeaders().get("Set-Cookie");

    String sessionId = session.get(0);
    manager.setSessionId(sessionId);

    return manager;
  }

  /**
   * Create a museum and login
   *
   * @param newMuseum - the museum to login
   * @return museumDto - the logged in museum
   * @author Kevin
   */
  public HttpHeaders loginSetupManager() {
    ManagerDto manager = createManagerAndLogin(UserUtilities.createManager(FIRST_VALID_MANAGER_NAME,
        FIRST_VALID_MANAGER_EMAIL, VALID_PASSWORD));

    HttpHeaders headers = new HttpHeaders();
    headers.set("Cookie", manager.getSessionId());
    return headers;
  }

}
