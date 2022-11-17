package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;


@ExtendWith(MockitoExtension.class)
public class TestArtworkService {

    @Mock
    private ArtworkRepository artworkRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomService roomService;

    @InjectMocks
    private ArtworkService artworkService;


    // Artwork 1
    private static final long ARTWORK_ID_1 = 1;
    private static final String NAME_1 = "Mona Lisa";
    private static final String ARTIST_1 = "Kian";
    private static final Boolean IS_AVAILABLE_FOR_LOAN_1 = true;
    private static final double LOAN_FEE_1 = 10;
    private static final String IMAGE_1 = "https://source.unsplash.com/C54OKB99iuw";
    private static final Boolean IS_ON_LOAN_1 = false;

    // Artwork 2
    private static final long ARTWORK_ID_2 = 2;
    private static final String NAME_2 = "Mona Lisa 2";
    private static final String ARTIST_2 = "Kian 2";
    private static final Boolean IS_AVAILABLE_FOR_LOAN_2 = false;
    private static final double LOAN_FEE_2 = 1000000000;
    private static final String IMAGE_2 = "https://source.unsplash.com/C54OKBaasdad99iuw";
    private static final Boolean IS_ON_LOAN_2 = false;

    // Artwork 3
    private static final long ARTWORK_ID_3 = 8;
    private static final String NAME_3 = "Mona Lisa 3";
    private static final String ARTIST_3 = "Kian 3";
    private static final Boolean IS_AVAILABLE_FOR_LOAN_3 = false;
    private static final double LOAN_FEE_3 = 12345;
    private static final String IMAGE_3 = "https://source.unsplash.com/C54OKBaasdad99iuw";
    private static final Boolean IS_ON_LOAN_3 = true;

    // Room 1
    private static final long ROOM_ID_1 = 3;
    private static final String ROOM_NAME_1 = "Clark Room";
    private static final int CURRENT_NUMBER_OF_ARTWORK_1 = 123;
    private static final RoomType ROOM_TYPE_1 = RoomType.Small;

    // Room 2
    private static final long ROOM_ID_2 = 4;
    private static final String ROOM_NAME_2 = "Bob Room";
    private static final int CURRENT_NUMBER_OF_ARTWORK_2 = 293;
    private static final RoomType ROOM_TYPE_2 = RoomType.Storage;

    // Room 3 - THE FULL ROOM
    private static final long ROOM_ID_3 = 5;
    private static final String ROOM_NAME_3 = "Full Room";
    private static final int CURRENT_NUMBER_OF_ARTWORK_3 = 300;
    private static final RoomType ROOM_TYPE_3 = RoomType.Large;


    @BeforeEach
    public void setMockOutput() {

        // Museum Creation
        Schedule schedule = new Schedule();
        Museum museum = new Museum();
        museum.setName("Rougon-Macquart");
        museum.setVisitFee(12.5);
        museum.setSchedule(schedule);

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        lenient().when(artworkRepository.findById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ARTWORK_ID_1)) {
                Artwork artwork = new Artwork();
                artwork.setArtworkId(ARTWORK_ID_1);
                artwork.setName(NAME_1);
                artwork.setImage(IMAGE_1);
                artwork.setLoanFee(LOAN_FEE_1);
                artwork.setArtist(ARTIST_1);
                artwork.setIsOnLoan(IS_ON_LOAN_1);
                artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_1);
                Optional<Room> roomOptional = roomRepository.findById(ROOM_ID_1);
                artwork.setRoom(roomOptional.get());
                return Optional.of(artwork);
            } else if(invocation.getArgument(0).equals(ARTWORK_ID_2)){
                Artwork artwork = new Artwork();
                artwork.setArtworkId(ARTWORK_ID_2);
                artwork.setImage(IMAGE_2);
                artwork.setLoanFee(LOAN_FEE_2);
                artwork.setArtist(ARTIST_2);
                artwork.setName(NAME_2);
                artwork.setIsOnLoan(IS_ON_LOAN_2);
                artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_2);
                Optional<Room> roomOptional = roomRepository.findById(ROOM_ID_2);
                artwork.setRoom(roomOptional.get());
                return Optional.of(artwork);
            }else if(invocation.getArgument(0).equals(ARTWORK_ID_3)){
                Artwork artwork = new Artwork();
                artwork.setArtworkId(ARTWORK_ID_3);
                artwork.setImage(IMAGE_3);
                artwork.setLoanFee(LOAN_FEE_3);
                artwork.setArtist(ARTIST_3);
                artwork.setName(NAME_3);
                artwork.setIsOnLoan(IS_ON_LOAN_3);
                artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_3);
                Optional<Room> roomOptional = roomRepository.findById(ROOM_ID_3);
                artwork.setRoom(roomOptional.get());
                return Optional.of(artwork);
            }
            else {
                return null;
            }
        });

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
            } else if (invocation.getArgument(0).equals(ROOM_ID_3)) {
                Room room = new Room();
                room.setRoomType(ROOM_TYPE_3);
                room.setRoomName(ROOM_NAME_3);
                room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_3);
                room.setRoomId(ROOM_ID_3);
                room.setMuseum(museum);
                Optional<Room> roomOpt = Optional.of(room);
                return roomOpt;
            }else {
                return null;
            }
        });

        lenient().when(roomService.getRoomCapacity(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ROOM_ID_1)) {
                // Small room
                return 200 - CURRENT_NUMBER_OF_ARTWORK_1;
            } else if (invocation.getArgument(0).equals(ROOM_ID_2)) {
                // Large room
                return 300 - CURRENT_NUMBER_OF_ARTWORK_2;
            }else if (invocation.getArgument(0).equals(ROOM_ID_3)) {
                // Large room
                return 300 - CURRENT_NUMBER_OF_ARTWORK_3;
            } else {
                return null;
            }
        });

        lenient().when(artworkRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            Iterable<Artwork> artworks = new ArrayList<Artwork>();
            Artwork artwork1 = new Artwork();
            artwork1.setArtworkId(ARTWORK_ID_1);
            artwork1.setName(NAME_1);
            artwork1.setImage(IMAGE_1);
            artwork1.setLoanFee(LOAN_FEE_1);
            artwork1.setArtist(ARTIST_1);
            artwork1.setIsOnLoan(IS_ON_LOAN_1);
            artwork1.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_1);
            Room room1 = new Room();
            room1.setRoomName(ROOM_NAME_1);
            room1.setRoomType(ROOM_TYPE_1);
            room1.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_1);
            room1.setRoomId(ROOM_ID_1);
            artwork1.setRoom(room1);
            ((ArrayList<Artwork>) artworks).add(artwork1);

            Artwork artwork2 = new Artwork();
            artwork2.setArtworkId(ARTWORK_ID_2);
            artwork2.setImage(IMAGE_2);
            artwork2.setLoanFee(LOAN_FEE_2);
            artwork2.setName(NAME_2);
            artwork2.setArtist(ARTIST_2);
            artwork2.setIsOnLoan(IS_ON_LOAN_2);
            artwork2.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_2);
            Room room2 = new Room();
            room2.setRoomName(ROOM_NAME_2);
            room2.setRoomType(ROOM_TYPE_2);
            room2.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_2);
            room2.setRoomId(ROOM_ID_2);
            artwork2.setRoom(room2);
            ((ArrayList<Artwork>) artworks).add(artwork2);

            Artwork artwork3 = new Artwork();
            artwork3.setArtworkId(ARTWORK_ID_3);
            artwork3.setImage(IMAGE_3);
            artwork3.setLoanFee(LOAN_FEE_3);
            artwork3.setName(NAME_3);
            artwork3.setArtist(ARTIST_3);
            artwork3.setIsOnLoan(IS_ON_LOAN_3);
            artwork3.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_3);
            Room room3 = new Room();
            room3.setRoomName(ROOM_NAME_3);
            room3.setRoomType(ROOM_TYPE_3);
            room3.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_3);
            room3.setRoomId(ROOM_ID_3);
            artwork3.setRoom(room3);
            ((ArrayList<Artwork>) artworks).add(artwork3);

            return artworks;
        });

        lenient().when(artworkRepository.save(any(Artwork.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(roomRepository.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);

    }

    /**
     * Test method for getting the status of an artwork
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetArtworkStatus() {
        assertEquals("display", artworkService.getArtworkStatus(ARTWORK_ID_1));
        assertEquals("storage", artworkService.getArtworkStatus(ARTWORK_ID_2));
        assertEquals("loan", artworkService.getArtworkStatus(ARTWORK_ID_3));
    }

    /**
     * Test method for getting the status of an artwork when the artwork doesn't exist
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetArtworkStatusNonExisting() {
        String error = null;
        try {
            long NONE_EXISTING_ARTWORK_ID = 1234;
            artworkService.getArtworkStatus(NONE_EXISTING_ARTWORK_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Artwork does not exist", error);
    }

    /**
     * Test method for getting all artworks in a room
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetAllArtworksInRoom() {
        try{
            List<Artwork> artworkList = artworkService.getAllArtworksInRoom(ROOM_ID_1);
            assertEquals(ARTWORK_ID_1, artworkList.get(0).getArtworkId());
            List<Artwork> artworkList2 = artworkService.getAllArtworksInRoom(ROOM_ID_2);
            assertEquals(ARTWORK_ID_2, artworkList2.get(0).getArtworkId());
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Test method for getting the number of artworks in a room
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetNumberOfArtworksInRoom() {
        try{
            int numberOfArtworksInRoom = artworkService.getNumberOfArtworksInRoom(ROOM_ID_1);
            int numberOfArtworksInRoom2 = artworkService.getNumberOfArtworksInRoom(ROOM_ID_2);
            assertEquals(1, numberOfArtworksInRoom);
            assertEquals(1,  numberOfArtworksInRoom2);
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Test method for moving an artwork to a different room
     *
     * @author kieyanmamiche
     */
    @Test
    public void testMoveArtworkToRoom() {
        Optional<Artwork> artworkBeforeMoveOptional = artworkRepository.findById(ARTWORK_ID_1);
        Artwork artworkBeforeMove = artworkBeforeMoveOptional.get();

        // Check that artwork before move is in room 1
        assertEquals(artworkBeforeMove.getRoom().getRoomId(), ROOM_ID_1);

        // Move artwork to other room
        Artwork artwork = artworkService.moveArtworkToRoom(ARTWORK_ID_1, ROOM_ID_2);

        // Check that artwork after move is in room 2
        assertEquals(artwork.getRoom().getRoomId(), ROOM_ID_2);
    }

    /**
     * Test method for getting all artworks in a room when room doesn't exist
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetAllArtworksInRoom_NoRoom() {
        try{
            List<Artwork> artworkList = artworkService.getAllArtworksInRoom(12345);
            assertEquals(0, artworkList.size());
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Test method for getting the number of artworks in a room when room doesn't exist
     *
     * @author kieyanmamiche
     */
    @Test
    public void testGetNumberOfArtworksInRoom_NoRoom() {
        try{
            int numberOfArtworksInRoom = artworkService.getNumberOfArtworksInRoom(123456);
            assertEquals(0, numberOfArtworksInRoom);
        }catch (Exception e){
            fail();
        }
    }

    /**
     * Test method for moving an artwork to a room where the artwork doesn't exist
     *
     * @author kieyanmamiche
     */

    @Test
    public void testMoveArtworkToRoom_ArtworkNonExisting() {
        String error = null;
        try {
            long NON_EXISTING_ARTWORK_ID = 1234;
            artworkService.moveArtworkToRoom(NON_EXISTING_ARTWORK_ID, ROOM_ID_2);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Artwork does not exist", error);
    }

    /**
     * Test method for moving an artwork to a room when the room doesn't exist
     *
     * @author kieyanmamiche
     */
    @Test
    public void testMoveArtworkToRoom_RoomNonExisting() {
        String error = null;
        try {
            long NON_EXISTING_ROOM_ID = 1234;
            artworkService.moveArtworkToRoom(ARTWORK_ID_1, NON_EXISTING_ROOM_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Room does not exist", error);
    }

    /**
     * Test method for moving an artwork to a room when the room is at full capacity
     *
     * @author kieyanmamiche
     */
    @Test
    public void testMoveArtworkToRoom_FullCapacity() {
        String error = null;
        try {
            // ROOM 3 is FULL
            artworkService.moveArtworkToRoom(ARTWORK_ID_1, ROOM_ID_3);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Room is full capacity", error);
    }

    /**
     * Test method for removing an artwork from its room
     *
     * @author kieyanmamiche
     */
    @Test
    public void testRemoveArtworkFromRoom() {
        Optional<Artwork> artworkBeforeRemoveOptional = artworkRepository.findById(ARTWORK_ID_1);
        Artwork artworkBeforeRemove = artworkBeforeRemoveOptional.get();

        // Check that artwork before move is in room 1
        assertEquals(artworkBeforeRemove.getRoom().getRoomId(), ROOM_ID_1);
        Artwork artwork = artworkService.removeArtworkFromRoom(ARTWORK_ID_1);

        // Check that artwork's room is now null
        assertNull(artwork.getRoom());
    }

    /**
     * Test method for removing artwork from its room but the artwork doesn't exist
     *
     * @author kieyanmamiche
     */
    @Test
    public void testRemoveArtworkFromRoomNonExisting() {
        String error = null;
        try {
            long NON_EXISTING_ARTWORK_ID = 1234;
            artworkService.removeArtworkFromRoom(NON_EXISTING_ARTWORK_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Artwork does not exist", error);
    }

}
