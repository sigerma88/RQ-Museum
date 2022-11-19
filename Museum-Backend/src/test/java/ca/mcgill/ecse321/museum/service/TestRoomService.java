package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;

/**
 * This is the test class for the RoomService class
 * 
 * @author Siger
 */
@ExtendWith(MockitoExtension.class)
public class TestRoomService {

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private ArtworkRepository artworkRepository;

  @Mock
  private MuseumRepository museumRepository;

  @InjectMocks
  private MuseumService museumService;

  @InjectMocks
  private RoomService roomService;

  private static final Long FIRST_ROOM_ID = 0L;
  private static final String FIRST_ROOM_NAME = "Room 1";
  private static final RoomType FIRST_ROOM_TYPE = RoomType.Small;

  private static final Long SECOND_ROOM_ID = 1L;
  private static final String SECOND_ROOM_NAME = "Room 2";
  private static final RoomType SECOND_ROOM_TYPE = RoomType.Large;

  private static final Long THIRD_ROOM_ID = 2L;
  private static final String THIRD_ROOM_NAME = "Room 3";
  private static final RoomType THIRD_ROOM_TYPE = RoomType.Storage;

  private static final Long MUSEUM_ID = 0L;
  private static final String MUSEUM_NAME = "Museum";
  private static final Double MUSEUM_VISIT_FEE = 10.0;

  private static final Long SCHEDULE_ID = 0L;

  private static final Long INVALID_ID = -1L;

  /**
   * This method sets up the mock objects
   * There are three rooms in the museum, one is a small room, one is a large room
   * and one is a storage room
   * 
   * @author Siger
   */
  @BeforeEach
  public void setMockOutput() {
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    lenient().when(roomRepository.findRoomByRoomId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(FIRST_ROOM_ID)) {
        // Create museum and its schedule
        Museum museum = createMuseumStub();

        // Create room
        Room room = new Room();
        room.setRoomId(FIRST_ROOM_ID);
        room.setRoomName(FIRST_ROOM_NAME);
        room.setRoomType(FIRST_ROOM_TYPE);
        room.setMuseum(museum);
        return room;
      } else if (invocation.getArgument(0).equals(SECOND_ROOM_ID)) {
        // Create museum and its schedule
        Museum museum = createMuseumStub();

        // Create room
        Room room = new Room();
        room.setRoomId(SECOND_ROOM_ID);
        room.setRoomName(SECOND_ROOM_NAME);
        room.setRoomType(SECOND_ROOM_TYPE);
        room.setMuseum(museum);
        return room;
      } else if (invocation.getArgument(0).equals(THIRD_ROOM_ID)) {
        // Create museum and its schedule
        Museum museum = createMuseumStub();

        // Create room
        Room room = new Room();
        room.setRoomId(THIRD_ROOM_ID);
        room.setRoomName(THIRD_ROOM_NAME);
        room.setRoomType(THIRD_ROOM_TYPE);
        room.setMuseum(museum);
        return room;
      } else {
        return null;
      }
    });

    lenient().when(roomRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
      // Create museum and its schedule
      Museum museum = createMuseumStub();

      // Create rooms
      Room room1 = new Room();
      room1.setRoomId(FIRST_ROOM_ID);
      room1.setRoomName(FIRST_ROOM_NAME);
      room1.setRoomType(FIRST_ROOM_TYPE);
      room1.setMuseum(museum);

      Room room2 = new Room();
      room2.setRoomId(SECOND_ROOM_ID);
      room2.setRoomName(SECOND_ROOM_NAME);
      room2.setRoomType(SECOND_ROOM_TYPE);
      room2.setMuseum(museum);

      Room room3 = new Room();
      room3.setRoomId(THIRD_ROOM_ID);
      room3.setRoomName(THIRD_ROOM_NAME);
      room3.setRoomType(THIRD_ROOM_TYPE);
      room3.setMuseum(museum);

      List<Room> rooms = new ArrayList<Room>();
      rooms.add(room1);
      rooms.add(room2);
      rooms.add(room3);

      return rooms;
    });

    lenient().when(roomRepository.findRoomByMuseum(any(Museum.class))).thenAnswer((InvocationOnMock invocation) -> {
      // Create museum and its schedule
      Museum museum = createMuseumStub();

      // Create rooms
      Room room1 = new Room();
      room1.setRoomId(FIRST_ROOM_ID);
      room1.setRoomName(FIRST_ROOM_NAME);
      room1.setRoomType(FIRST_ROOM_TYPE);
      room1.setMuseum(museum);

      Room room2 = new Room();
      room2.setRoomId(SECOND_ROOM_ID);
      room2.setRoomName(SECOND_ROOM_NAME);
      room2.setRoomType(SECOND_ROOM_TYPE);
      room2.setMuseum(museum);

      Room room3 = new Room();
      room3.setRoomId(THIRD_ROOM_ID);
      room3.setRoomName(THIRD_ROOM_NAME);
      room3.setRoomType(THIRD_ROOM_TYPE);
      room3.setMuseum(museum);

      List<Room> rooms = new ArrayList<Room>();
      rooms.add(room1);
      rooms.add(room2);
      rooms.add(room3);

      return rooms;
    });

    lenient().when(museumRepository.findMuseumByMuseumId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(MUSEUM_ID)) {
        // Create museum and its schedule
        return createMuseumStub();
      } else {
        return null;
      }
    });

    lenient().when(roomRepository.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);
  }

  /**
   * This method tests the creation of a small room and checks its max capacity
   * 
   * @author Siger
   */
  @Test
  public void testCreateSmallRoom() {
    // Create the museum and its schedule
    Museum museum = createMuseumStub();

    // Create the small room
    Room room = null;
    try {
      room = roomService.createRoom(FIRST_ROOM_NAME, FIRST_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room's attributes are correct
    assertNotNull(room);
    assertEquals(FIRST_ROOM_NAME, room.getRoomName());
    assertEquals(FIRST_ROOM_TYPE, room.getRoomType());
    assertEquals(0, room.getCurrentNumberOfArtwork());
    assertEquals(museum, room.getMuseum());

    // Get the room's max capacity
    try {
      assertEquals(200, roomService.getMaxNumberOfArtwork(room.getRoomType()));
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  /**
   * This method tests the creation of a large room and checks its max capacity
   * 
   * @author Siger
   */
  @Test
  public void testCreateLargeRoom() {
    // Create the museum and its schedule
    Museum museum = createMuseumStub();

    // Create the large room
    Room room = null;
    try {
      room = roomService.createRoom(SECOND_ROOM_NAME, SECOND_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room's attributes are correct
    assertNotNull(room);
    assertEquals(SECOND_ROOM_NAME, room.getRoomName());
    assertEquals(SECOND_ROOM_TYPE, room.getRoomType());
    assertEquals(0, room.getCurrentNumberOfArtwork());
    assertEquals(museum, room.getMuseum());

    // Get the room's max capacity
    try {
      assertEquals(300, roomService.getMaxNumberOfArtwork(room.getRoomType()));
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  /**
   * This method tests the creation of a storage room and checks its max capacity
   * 
   * @author Siger
   */
  @Test
  public void testCreateStorageRoom() {
    // Create the museum and its schedule
    Museum museum = createMuseumStub();

    // Create the storage room
    Room room = null;
    try {
      room = roomService.createRoom(THIRD_ROOM_NAME, THIRD_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room's attributes are correct
    assertNotNull(room);
    assertEquals(THIRD_ROOM_NAME, room.getRoomName());
    assertEquals(THIRD_ROOM_TYPE, room.getRoomType());
    assertEquals(0, room.getCurrentNumberOfArtwork());
    assertEquals(museum, room.getMuseum());

    // Get the room's max capacity
    try {
      assertEquals(-1, roomService.getMaxNumberOfArtwork(room.getRoomType()));
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  /**
   * This method tests the creation of a room with an null room name
   * 
   * @author Siger
   */
  @Test
  public void testCreateRoomNullRoomName() {
    // Create the museum and its schedule
    Museum museum = createMuseumStub();

    // Create the room with a null room name
    Room room = null;
    try {
      room = roomService.createRoom(null, FIRST_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Room name cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of a room with a null room type
   * 
   * @author Siger
   */
  @Test
  public void testCreateRoomWithNullRoomType() {
    // Create the museum and its schedule
    Museum museum = createMuseumStub();

    // Create the room with a null room type
    Room room = null;
    try {
      room = roomService.createRoom(FIRST_ROOM_NAME, null, museum);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Room type cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of a room with a null museum
   * 
   * @author Siger
   */
  @Test
  public void testCreateRoomWithNullMuseum() {
    // Create the room with an invalid museum
    Room room = null;
    try {
      room = roomService.createRoom(FIRST_ROOM_NAME, FIRST_ROOM_TYPE, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Museum cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests getting a room by its id
   * 
   * @author Siger
   */
  @Test
  public void testGetRoomById() {
    // Get the room by its id
    Room firstRoom = null;
    Room secondRoom = null;
    Room thirdRoom = null;
    try {
      firstRoom = roomService.getRoomById(FIRST_ROOM_ID);
      secondRoom = roomService.getRoomById(SECOND_ROOM_ID);
      thirdRoom = roomService.getRoomById(THIRD_ROOM_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the rooms are correct
    assertNotNull(firstRoom);
    assertEquals(FIRST_ROOM_ID, firstRoom.getRoomId());
    assertNotNull(secondRoom);
    assertEquals(SECOND_ROOM_ID, secondRoom.getRoomId());
    assertNotNull(thirdRoom);
    assertEquals(THIRD_ROOM_ID, thirdRoom.getRoomId());
  }

  /**
   * This method tests getting a room by its id with a null id
   * 
   * @author Siger
   */
  @Test
  public void testGetRoomByIdNullId() {
    Room room = null;
    try {
      room = roomService.getRoomById(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Room id cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests getting a room by its id with an invalid id
   * 
   * @author Siger
   */
  @Test
  public void testGetRoomByIdInvalidId() {
    Room room = null;
    try {
      room = roomService.getRoomById(INVALID_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room is null
    assertNull(room);
  }

  /**
   * This method tests getting all the rooms
   * 
   * @author Siger
   */
  @Test
  public void testGetAllRooms() {
    // Get all the rooms
    List<Room> rooms = null;
    try {
      rooms = roomService.getAllRooms();
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the rooms are correct
    assertNotNull(rooms);
    assertEquals(3, rooms.size());
    assertEquals(FIRST_ROOM_ID, rooms.get(0).getRoomId());
    assertEquals(SECOND_ROOM_ID, rooms.get(1).getRoomId());
    assertEquals(THIRD_ROOM_ID, rooms.get(2).getRoomId());
  }

  /**
   * This method tests getting all the rooms in a museum
   * 
   * @author Siger
   */
  @Test
  public void testGetAllRoomsInMuseum() {
    // Get the museum
    Museum museum = null;
    try {
      museum = museumService.getMuseum(MUSEUM_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Get all the rooms in the museum
    List<Room> rooms = null;
    try {
      rooms = roomService.getAllRoomsByMuseum(museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the rooms are correct
    assertNotNull(rooms);
    assertEquals(3, rooms.size());
    assertEquals(FIRST_ROOM_ID, rooms.get(0).getRoomId());
    assertEquals(SECOND_ROOM_ID, rooms.get(1).getRoomId());
    assertEquals(THIRD_ROOM_ID, rooms.get(2).getRoomId());
  }

  /**
   * This method tests getting all the rooms in a museum with a null museum
   * 
   * @author Siger
   */
  @Test
  public void testGetAllRoomsInMuseumNullMuseum() {
    // Get all the rooms in the museum
    List<Room> rooms = null;
    try {
      rooms = roomService.getAllRoomsByMuseum(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(rooms);
      assertEquals("Museum cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests changing the current number of artworks in a room
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtwork() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(FIRST_ROOM_ID, 5);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room was changed
    assertNotNull(room);
    assertEquals(5, room.getCurrentNumberOfArtwork());
  }

  /**
   * This method tests changing the current number of artworks in a room with a
   * null id
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtworkNullId() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(null, 5);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Room does not exist", e.getMessage());
    }
  }

  /**
   * This method tests changing the current number of artworks in a room with an
   * invalid id
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtworkInvalidId() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(INVALID_ID, 5);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Room does not exist", e.getMessage());
    }
  }

  /**
   * This method tests changing the current number of artworks in a room with a
   * null number
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtworkNullNumber() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(FIRST_ROOM_ID, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Current number of artworks cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests changing the current number of artworks in a room with a
   * negative number
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtworkNegativeNumber() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(FIRST_ROOM_ID, -5);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Current number of artworks cannot be negative", e.getMessage());
    }
  }

  /**
   * This method tests changing the current number of artworks in a room with a
   * number greater than the maximum number of artworks for a small room
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtworkGreaterThanMaxSmallRoom() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(FIRST_ROOM_ID, 50000);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Current number of artworks 50000 cannot be greater than the maximum number of artworks 200",
          e.getMessage());
    }
  }

  /**
   * This method tests changing the current number of artworks in a room with a
   * number greater than the maximum number of artworks for a large room
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtworkGreaterThanMaxLargeRoom() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(SECOND_ROOM_ID, 50000);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Current number of artworks 50000 cannot be greater than the maximum number of artworks 300",
          e.getMessage());
    }
  }

  /**
   * This method tests changing the current number of artworks in a room with a
   * very large number of artworks for a storage room
   * 
   * @author Siger
   */
  @Test
  public void testChangeCurrentNumberOfArtworkVeryLargeNumberStorageRoom() {
    // Change the current number of artworks in the room
    Room room = null;
    try {
      room = roomService.changeCurrentNumberOfArtwork(THIRD_ROOM_ID, 50000);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room was changed
    assertNotNull(room);
    assertEquals(50000, room.getCurrentNumberOfArtwork());
  }

  /**
   * This method tests getting the maximum number of artworks for a room with a
   * null room type
   * 
   * @author Siger
   */
  @Test
  public void testGetMaxNumberOfArtworkNullRoomType() {
    // Get the maximum number of artworks for a room
    Integer maxNumberOfArtwork = null;
    try {
      maxNumberOfArtwork = roomService.getMaxNumberOfArtwork(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(maxNumberOfArtwork);
      assertEquals("Room type cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests deleting a room
   * 
   * @author Siger
   */
  @Test
  public void testDeleteRoom() {
    try {
      roomService.deleteRoom(FIRST_ROOM_ID);
    } catch (IllegalArgumentException e) {
      fail();
    }
  }

  /**
   * This method tests deleting a room with a null id
   * 
   * @author Siger
   */
  @Test
  public void testDeleteRoomNullId() {
    try {
      roomService.deleteRoom(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertEquals("Room does not exist", e.getMessage());
    }
  }

  /**
   * This method tests editing a room
   * 
   * @author Siger
   */
  @Test
  public void testEditRoom() {
    // Edit the room
    Room room = null;
    try {
      room = roomService.editRoom(FIRST_ROOM_ID, SECOND_ROOM_NAME, SECOND_ROOM_TYPE, null);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room was edited
    assertNotNull(room);
    assertEquals(FIRST_ROOM_ID, room.getRoomId());
    assertEquals(SECOND_ROOM_NAME, room.getRoomName());
    assertEquals(SECOND_ROOM_TYPE, room.getRoomType());
  }

  /**
   * This method tests editing a room with a null id
   * 
   * @author Siger
   */
  @Test
  public void testEditRoomNullId() {
    // Edit the room
    Room room = null;
    try {
      room = roomService.editRoom(null, SECOND_ROOM_NAME, SECOND_ROOM_TYPE, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Room does not exist", e.getMessage());
    }
  }

  /**
   * This method tests editing a room with a null name
   * 
   * @author Siger
   */
  @Test
  public void testEditRoomNullName() {
    // Edit the room
    Room room = null;
    try {
      room = roomService.editRoom(FIRST_ROOM_ID, null, SECOND_ROOM_TYPE, null);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room was edited
    assertNotNull(room);
    assertEquals(FIRST_ROOM_ID, room.getRoomId());
    assertEquals(FIRST_ROOM_NAME, room.getRoomName());
    assertEquals(SECOND_ROOM_TYPE, room.getRoomType());
  }

  /**
   * This method tests editing a room with a null type
   * 
   * @author Siger
   */
  @Test
  public void testEditRoomNullType() {
    // Edit the room
    Room room = null;
    try {
      room = roomService.editRoom(FIRST_ROOM_ID, SECOND_ROOM_NAME, null, null);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room was edited
    assertNotNull(room);
    assertEquals(FIRST_ROOM_ID, room.getRoomId());
    assertEquals(SECOND_ROOM_NAME, room.getRoomName());
    assertEquals(FIRST_ROOM_TYPE, room.getRoomType());
  }

  /**
   * This method tests editing a room with a null name and type
   * 
   * @author Siger
   */
  @Test
  public void testEditRoomNullNameAndType() {
    // Edit the room
    Room room = null;
    try {
      room = roomService.editRoom(FIRST_ROOM_ID, null, null, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(room);
      assertEquals("Nothing to edit, all fields are empty", e.getMessage());
    }
  }

  /**
   * This is a helper method creating a museum stub
   * 
   * @author Siger
   */
  public Museum createMuseumStub() {
    // Create the museum and its schedule
    Schedule schedule = new Schedule();
    schedule.setScheduleId(SCHEDULE_ID);
    Museum museum = new Museum();
    museum.setMuseumId(MUSEUM_ID);
    museum.setName(MUSEUM_NAME);
    museum.setVisitFee(MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);

    return museum;
  }
}
