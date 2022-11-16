package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TestRoomService {

    @Mock
    private RoomRepository roomRepository;

    @InjectMocks
    private RoomService roomService;

    // Room 1 - STORAGE ROOM
    private static final long ROOM_ID_1 = 1;
    private static final String ROOM_NAME_1 = "Clark Room";
    private static final int CURRENT_NUMBER_OF_ARTWORK_1 = 123;
    private static final RoomType ROOM_TYPE_1 = RoomType.Storage;

    // Room 2 - Small room
    private static final long ROOM_ID_2 = 2;
    private static final String ROOM_NAME_2 = "Bob Room";
    private static final int CURRENT_NUMBER_OF_ARTWORK_2 = 193;
    private static final RoomType ROOM_TYPE_2 = RoomType.Small;

    // Room 3 - Large room
    private static final long ROOM_ID_3 = 3;
    private static final String ROOM_NAME_3 = "Brian Room";
    private static final int CURRENT_NUMBER_OF_ARTWORK_3 = 293;
    private static final RoomType ROOM_TYPE_3 = RoomType.Large;

    // Room 4 - Full room
    private static final long ROOM_ID_4 = 4;
    private static final String ROOM_NAME_4 = "Billy Room";
    private static final int CURRENT_NUMBER_OF_ARTWORK_4 = 200;
    private static final RoomType ROOM_TYPE_4 = RoomType.Small;

    @BeforeEach
    public void setMockOutput() {

        // Museum Creation
        Schedule schedule = new Schedule();
        Museum museum = new Museum();
        museum.setName("Rougon-Macquart");
        museum.setVisitFee(12.5);
        museum.setSchedule(schedule);


        lenient().when(roomRepository.findById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ROOM_ID_1)) {
                Room room = new Room();
                room.setRoomType(ROOM_TYPE_1);
                room.setRoomName(ROOM_NAME_1);
                room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_1);
                room.setRoomId(ROOM_ID_1);
                room.setMuseum(museum);
                Optional<Room> roomOpt = Optional.of(room);
                return roomOpt;
            } else if (invocation.getArgument(0).equals(ROOM_ID_2)) {
                Room room = new Room();
                room.setRoomType(ROOM_TYPE_2);
                room.setRoomName(ROOM_NAME_2);
                room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_2);
                room.setRoomId(ROOM_ID_2);
                room.setMuseum(museum);
                Optional<Room> roomOpt = Optional.of(room);
                return roomOpt;
            }else if (invocation.getArgument(0).equals(ROOM_ID_3)) {
                Room room = new Room();
                room.setRoomType(ROOM_TYPE_3);
                room.setRoomName(ROOM_NAME_3);
                room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_3);
                room.setRoomId(ROOM_ID_3);
                room.setMuseum(museum);
                Optional<Room> roomOpt = Optional.of(room);
                return roomOpt;
            }else if (invocation.getArgument(0).equals(ROOM_ID_4)) {
                Room room = new Room();
                room.setRoomType(ROOM_TYPE_4);
                room.setRoomName(ROOM_NAME_4);
                room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_4);
                room.setRoomId(ROOM_ID_4);
                room.setMuseum(museum);
                Optional<Room> roomOpt = Optional.of(room);
                return roomOpt;
            } else {
                return null;
            }
        });

    }

    /**
     * Test method for getting the room capacity of a room given its id
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetRoomCapacity() {

        // Assert that storage room has high capacity aka greater than 10000
        int capacityStorage = 10000;
        assertTrue(roomService.getRoomCapacity(ROOM_ID_1) > capacityStorage);

        // Assert that small room has small capacity, 193 artworks out of total 200 so => 7 capacity
        assertEquals(7, roomService.getRoomCapacity(ROOM_ID_2));

        // Assert that large room has large capacity, 293 artworks out of total 300 so => 7 capacity
        assertEquals(7, roomService.getRoomCapacity(ROOM_ID_3));

        // Assert that full room has no capacity => capacity == 0
        assertEquals(0, roomService.getRoomCapacity(ROOM_ID_4));

    }

    /**
     * Test method for getting the room capacity of a room given an id which doesn't exist
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetRoomCapacityNonExisting() {
        String error = null;
        try {
            long NONE_EXISTING_ROOM_ID = 1234;
            roomService.getRoomCapacity(NONE_EXISTING_ROOM_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Room does not exist", error);
    }


}
