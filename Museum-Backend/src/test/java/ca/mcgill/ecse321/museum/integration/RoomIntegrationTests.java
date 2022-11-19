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

import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dto.RoomDto;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.RoomService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoomIntegrationTests {

  @Autowired
  private TestRestTemplate client;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private MuseumRepository museumRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private RoomService roomService;

  @Autowired
  private MuseumService museumService;

  @BeforeEach
  public void setup() {
    // clear all repositories
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
  }

  @AfterEach
  public void clearDatabase() {
    // clear all repositories
    roomRepository.deleteAll();
    museumRepository.deleteAll();
    scheduleRepository.deleteAll();
  }

  /**
   * Test to create a room
   * 
   * @author Siger
   */
  @Test
  public void testCreateRoom() {
    // Get museum id
    Long museumId = museumService.getAllMuseums().get(0).getMuseumId();

    // Params
    String roomName = "Room 2";
    RoomType roomType = RoomType.Large;

    // Test controller POST RESTful API
    ResponseEntity<RoomDto> response = client.postForEntity(
        "/room" + "?roomName=" + roomName + "&roomType=" + roomType + "&museumId=" + museumId, null, RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(roomName, response.getBody().getRoomName(), "Room name is correct");
    assertEquals(roomType, response.getBody().getRoomType(), "Room type is correct");
    assertEquals(museumId, response.getBody().getMuseum().getMuseumId(), "Museum id is correct");
    assertTrue(response.getBody().getRoomId() > 0, "Room id is correct");
  }

  /**
   * Test to create a room with no room name
   * 
   * @author Siger
   */
  @Test
  public void testCreateRoomNoRoomName() {
    // Get museum id
    Long museumId = museumService.getAllMuseums().get(0).getMuseumId();

    // Params
    String roomName = "";
    RoomType roomType = RoomType.Large;

    // Test controller POST RESTful API
    ResponseEntity<String> response = client.postForEntity(
        "/room" + "?roomName=" + roomName + "&roomType=" + roomType + "&museumId=" + museumId, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room name cannot be empty", response.getBody(), "Response has correct body error message");
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
    ResponseEntity<RoomDto> response = client.getForEntity("/room/" + room.getRoomId(), RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(room.getRoomId(), response.getBody().getRoomId(), "Room id is correct");
    assertEquals(room.getRoomName(), response.getBody().getRoomName(), "Room name is correct");
    assertEquals(room.getRoomType(), response.getBody().getRoomType(), "Room type is correct");
    assertEquals(room.getMuseum().getMuseumId(), response.getBody().getMuseum().getMuseumId(), "Museum id is correct");
  }

  /**
   * Test to get a room by invalid id
   * 
   * @author Siger
   */
  @Test
  public void testGetRoomByInvalidId() {
    // Test controller GET RESTful API
    ResponseEntity<String> response = client.getForEntity("/room/-1", String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("There is no such room", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to get all rooms
   * There is no failure case for this test
   * 
   * @author Siger
   */
  @Test
  public void testGetAllRooms() {
    // Test controller GET RESTful API
    ResponseEntity<RoomDto[]> response = client.getForEntity("/rooms", RoomDto[].class);

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
    ResponseEntity<RoomDto[]> response = client.getForEntity("/rooms/" + museumId, RoomDto[].class);

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
    ResponseEntity<String> response = client.getForEntity("/rooms/-1", String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Museum with id -1 does not exist", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to update a room
   * 
   * @author Siger
   */
  @Test
  public void testUpdateRoom() {
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
    String roomName = "New room name";
    RoomType roomType = RoomType.Storage;

    // Test controller PUT RESTful API
    ResponseEntity<RoomDto> response = client.exchange(
        "/room/" + room.getRoomId() + "?roomName=" + roomName + "&roomType=" + roomType + "&museumId="
            + museum.getMuseumId(),
        HttpMethod.PUT, null, RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(room.getRoomId(), response.getBody().getRoomId(), "Room id is correct");
    assertEquals(roomName, response.getBody().getRoomName(), "Room name is correct");
    assertEquals(roomType, response.getBody().getRoomType(), "Room type is correct");
    assertEquals(museum.getMuseumId(), response.getBody().getMuseum().getMuseumId(), "Museum id is correct");
  }

  /**
   * Test to update a room with invalid room id
   * 
   * @author Siger
   */
  @Test
  public void testUpdateRoomWithInvalidRoomId() {
    // Params
    String roomName = "New room name";
    RoomType roomType = RoomType.Storage;

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange(
        "/room/-1" + "?roomName=" + roomName + "&roomType=" + roomType, HttpMethod.PUT, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room does not exist", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to update a room with invalid museum id
   * 
   * @author Siger
   */
  @Test
  public void testUpdateRoomWithInvalidMuseumId() {
    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    // Params
    String roomName = "New room name";
    RoomType roomType = RoomType.Storage;

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange(
        "/room/" + room.getRoomId() + "?roomName=" + roomName + "&roomType=" + roomType + "&museumId=-1",
        HttpMethod.PUT,
        null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Museum with id -1 does not exist", response.getBody(), "Response has correct body error message");
  }

  /**
   * Test to edit a room with no params
   * 
   * @author Siger
   */
  @Test
  public void testEditRoomWithNoParams() {
    // Get room and its id
    Room room = roomService.getAllRooms().get(0);

    // Test controller PUT RESTful API
    ResponseEntity<String> response = client.exchange("/room/" + room.getRoomId(), HttpMethod.PUT, null, String.class);

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

    // Test controller DELETE RESTful API
    ResponseEntity<RoomDto> response = client.exchange("/room/" + room.getRoomId(), HttpMethod.DELETE, null,
        RoomDto.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals(room.getRoomId(), response.getBody().getRoomId(), "Room id is correct");
    assertEquals(room.getRoomName(), response.getBody().getRoomName(), "Room name is correct");
    assertEquals(room.getRoomType(), response.getBody().getRoomType(), "Room type is correct");
    assertEquals(room.getMuseum().getMuseumId(), response.getBody().getMuseum().getMuseumId(), "Museum id is correct");
  }

  /**
   * Test to delete a room with invalid room id
   * 
   * @author Siger
   */
  @Test
  public void testDeleteRoomWithInvalidRoomId() {
    // Test controller DELETE RESTful API
    ResponseEntity<String> response = client.exchange("/room/-1", HttpMethod.DELETE, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room does not exist", response.getBody(), "Response has correct body error message");
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
    ResponseEntity<Integer> response = client.exchange("/room/maxArtworks/" + room.getRoomId(),
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
    ResponseEntity<String> response = client.exchange("/room/maxArtworks/-1", HttpMethod.GET, null, String.class);

    // Check status and body of response are correct
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Response has correct status");
    assertNotNull(response.getBody(), "Response has a body");
    assertEquals("Room with id -1 does not exist", response.getBody(), "Response has correct body error message");
  }
}
