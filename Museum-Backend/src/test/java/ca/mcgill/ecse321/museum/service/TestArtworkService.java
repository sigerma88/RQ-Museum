package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


    @BeforeEach
    public void setMockOutput() {
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        // Functions called
        // 1. artworkRepository.findById(id) - done
        // 2. artworkRepository.findAll(); - done
        // 3. roomRepository.findById(roomId); - done
        // 4. roomService.getRoomCapacity(roomId) -- NOTE SERVICE HERE - done

        // 5. roomRepository.save -- done
        // 5. artworkRepository.save -- done


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
            }else {
                return null;
            }
        });

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
            } else {
                return null;
            }
        });

        lenient().when(roomService.getRoomCapacity(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(ROOM_ID_1)) {
                // Small room
                return 200 - CURRENT_NUMBER_OF_ARTWORK_1;
            } else if (invocation.getArgument(0).equals(ROOM_ID_2)) {
                // Large room
                return 300 - CURRENT_NUMBER_OF_ARTWORK_1;
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

            return artworks;
        });

        lenient().when(artworkRepository.save(any(Artwork.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(roomRepository.save(any(Room.class))).thenAnswer(returnParameterAsAnswer);

    }

    @Test
    public void testGetArtworkStatus() {
        assertEquals("display", artworkService.getArtworkStatus(ARTWORK_ID_1));
        assertEquals("storage", artworkService.getArtworkStatus(ARTWORK_ID_2));
    }

    @Test
    public void testGetAllArtworksInRoom() {
        List<Artwork> artworkList = artworkService.getAllArtworksInRoom(ROOM_ID_1);
        assertEquals(ARTWORK_ID_1, artworkList.get(0).getArtworkId());
        List<Artwork> artworkList2 = artworkService.getAllArtworksInRoom(ROOM_ID_2);
        assertEquals(ARTWORK_ID_2, artworkList2.get(0).getArtworkId());
    }

    @Test
    public void testGetNumberOfArtworksInRoom() {
        int numberOfArtworksInRoom = artworkService.getNumberOfArtworksInRoom(ROOM_ID_1);
        int numberOfArtworksInRoom2 = artworkService.getNumberOfArtworksInRoom(ROOM_ID_2);
        assertEquals(1, numberOfArtworksInRoom);
        assertEquals(1,  numberOfArtworksInRoom2);
    }

    @Test
    public void testMoveArtworkToRoom() {
        artworkService.moveArtworkToRoom(ARTWORK_ID_1, ROOM_ID_2);
        List<Artwork> artworkList = artworkService.getAllArtworksInRoom(ROOM_ID_2);
        System.out.println(artworkList.size());

        // Check that artworks in room two are both artworks.
        assertEquals(ARTWORK_ID_2, artworkList.get(0).getArtworkId());
        assertEquals(ARTWORK_ID_1, artworkList.get(1).getArtworkId());

    }

    @Test
    public void testRemoveArtworkFromRoom() {
        artworkService.removeArtworkFromRoom(ARTWORK_ID_2);
        List<Artwork> artworkList = artworkService.getAllArtworksInRoom(ROOM_ID_2);

        // Check that artwork in room is only artwork 1
        assertEquals(ARTWORK_ID_1, artworkList.get(0).getArtworkId());

    }
}
