package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
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

  @Mock
  private ScheduleRepository scheduleRepository;

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

  private static final Long ARTWORK_ID = 0L;
  private static final String ARTWORK_NAME = "La Joconde";
  private static final String ARTWORK_AUTHOR = "Leonardo Da Vinci";
  private static final Boolean ARTWORK_IS_AVAILABLE_FOR_LOAN = true;
  private static final Double ARTWORK_LOAN_FEE = 100.0;
  private static final String ARTWORK_IMAGE = "image";
  private static final Boolean ARTWORK_IS_ON_LOAN = false;

  private static final Long MUSEUM_ID = 0L;
  private static final String MUSEUM_NAME = "Museum";
  private static final Double MUSEUM_VISIT_FEE = 10.0;

  private static final Long SCHEDULE_ID = 0L;

  /**
   * This method sets up the mock objects
   * There are three rooms in the museum, one is a small room, one is a large room and one is a storage room
   * 
   * @author Siger
   */
  @BeforeEach
  public void setMockOutput() {
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    lenient().when(roomRepository.findRoomByRoomId(anyLong())).thenAnswer( (InvocationOnMock invocation) -> {
      if(invocation.getArgument(0).equals(FIRST_ROOM_ID)) {
        // Create museum and its schedule
        Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISIT_FEE);
        Schedule schedule = new Schedule();
        schedule.setScheduleId(SCHEDULE_ID);
        museum.setSchedule(schedule);

        // Create room
        Room room = new Room();
        room.setRoomId(FIRST_ROOM_ID);
        room.setRoomName(FIRST_ROOM_NAME);
        room.setRoomType(FIRST_ROOM_TYPE);
        room.setMuseum(museum);
        return room;
      } else if (invocation.getArgument(0).equals(SECOND_ROOM_ID)) {
        // Create museum and its schedule
        Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISIT_FEE);
        Schedule schedule = new Schedule();
        schedule.setScheduleId(SCHEDULE_ID);
        museum.setSchedule(schedule);

        // Create room
        Room room = new Room();
        room.setRoomId(SECOND_ROOM_ID);
        room.setRoomName(SECOND_ROOM_NAME);
        room.setRoomType(SECOND_ROOM_TYPE);
        room.setMuseum(museum);
        return room;
      } else if (invocation.getArgument(0).equals(THIRD_ROOM_ID)) {
        // Create museum and its schedule
        Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISIT_FEE);
        Schedule schedule = new Schedule();
        schedule.setScheduleId(SCHEDULE_ID);
        museum.setSchedule(schedule);

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

    lenient().when(roomRepository.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(roomRepository.saveAll(any(Iterable.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(artworkRepository.save(any(Artwork.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(museumRepository.save(any(Museum.class))).thenAnswer(returnParameterAsAnswer);
    lenient().when(scheduleRepository.save(any(Schedule.class))).thenAnswer(returnParameterAsAnswer);
  }

  /**
   * This method tests the creation of a small room and checks its max capacity
   * 
   * @author Siger
   */
  @Test
  public void testCreateSmallRoom() {
    // Create the museum and its schedule
    Schedule schedule = new Schedule();
    schedule.setScheduleId(SCHEDULE_ID);
    Museum museum = new Museum();
    museum.setMuseumId(MUSEUM_ID);
    museum.setName(MUSEUM_NAME);
    museum.setVisitFee(MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);
    museum = museumRepository.save(museum);

    // Create the small room
    Room room = null;
    try {
      room = roomService.createRoom(FIRST_ROOM_NAME, FIRST_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room's attributes are correct
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
    Schedule schedule = new Schedule();
    schedule.setScheduleId(SCHEDULE_ID);
    Museum museum = new Museum();
    museum.setMuseumId(MUSEUM_ID);
    museum.setName(MUSEUM_NAME);
    museum.setVisitFee(MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);
    museum = museumRepository.save(museum);

    // Create the large room
    Room room = null;
    try {
      room = roomService.createRoom(SECOND_ROOM_NAME, SECOND_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room's attributes are correct
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
    Schedule schedule = new Schedule();
    schedule.setScheduleId(SCHEDULE_ID);
    Museum museum = new Museum();
    museum.setMuseumId(MUSEUM_ID);
    museum.setName(MUSEUM_NAME);
    museum.setVisitFee(MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);
    museum = museumRepository.save(museum);

    // Create the storage room
    Room room = null;
    try {
      room = roomService.createRoom(THIRD_ROOM_NAME, THIRD_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room's attributes are correct
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
    Schedule schedule = new Schedule();
    schedule.setScheduleId(SCHEDULE_ID);
    Museum museum = new Museum();
    museum.setMuseumId(MUSEUM_ID);
    museum.setName(MUSEUM_NAME);
    museum.setVisitFee(MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);
    museum = museumRepository.save(museum);

    // Create the room with a null room name
    Room room = null;
    String error = null;
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
    Schedule schedule = new Schedule();
    schedule.setScheduleId(SCHEDULE_ID);
    Museum museum = new Museum();
    museum.setMuseumId(MUSEUM_ID);
    museum.setName(MUSEUM_NAME);
    museum.setVisitFee(MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);
    museum = museumRepository.save(museum);

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
    // Create the museum and its schedule
    Schedule schedule = new Schedule();
    schedule.setScheduleId(SCHEDULE_ID);
    Museum museum = new Museum();
    museum.setMuseumId(MUSEUM_ID);
    museum.setName(MUSEUM_NAME);
    museum.setVisitFee(MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);
    museum = museumRepository.save(museum);

    // Create the room
    Room room = null;
    try {
      room = roomService.createRoom(FIRST_ROOM_NAME, FIRST_ROOM_TYPE, museum);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Get the room by its id
    Room storedRoom = null;
    try {
      storedRoom = roomService.getRoomById(room.getRoomId());
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the room's attributes are correct
    assertEquals(room.getRoomId(), storedRoom.getRoomId());
    assertEquals(room.getRoomName(), storedRoom.getRoomName());
    assertEquals(room.getRoomType(), storedRoom.getRoomType());
    assertEquals(room.getCurrentNumberOfArtwork(), storedRoom.getCurrentNumberOfArtwork());
    assertEquals(room.getMuseum().getMuseumId(), storedRoom.getMuseum().getMuseumId());
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
}
