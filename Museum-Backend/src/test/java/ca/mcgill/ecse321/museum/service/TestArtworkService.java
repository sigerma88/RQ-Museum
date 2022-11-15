package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;


/**
 * @author Zahra
 */
@ExtendWith(MockitoExtension.class)
public class TestArtworkService {

  @Mock
  private ArtworkRepository artworkRepository;

  @Mock
  private RoomRepository roomRepository;


  @InjectMocks
  private ArtworkService artworkService;

  // Artwork
  private static final long ARTWORK_ID = 678;
  private static final String ARTWORK_NAME = "La Peur";
  private static final String ARTWORK_ARTIST = "Ernest";
  private static final Boolean IS_AVAILABLE_FOR_LOAN = true;
  private static final double LOAN_FEE_1 = 100.00;
  private static final String IMAGE_1 = "ooo";
  private static final Boolean IS_ON_LOAN_1 = false;
  private static final String AVAILABILITY = "Available";


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

  private static final int NUMBER_OF_ARTWORKS = 2;


  @BeforeEach
  public void setMockOutput() {

    lenient().when(artworkRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
      List<Artwork> allArtworks = new ArrayList<Artwork>();

      Room room1 = new Room();
      room1.setRoomId(ROOM_ID_1);
      room1.setRoomName(ROOM_NAME_1);

      room1.setCurrentNumberOfArtwork(ROOM1_NUMBER_OF_ARTWORKS);

      Room room2 = new Room();
      room2.setRoomId(ROOM_ID_2);
      room2.setRoomName(ROOM_NAME_2);
      room2.setCurrentNumberOfArtwork(ROOM2_NUMBER_OF_ARTWORKS);


      Artwork artwork1 = new Artwork();
      artwork1.setArtworkId(ARTWORK_ID);
      artwork1.setArtist(ARTWORK_ARTIST);
      artwork1.setName(ARTWORK_NAME);
      artwork1.setRoom(room1);
      artwork1.setImage(IMAGE_1);
      artwork1.setLoanFee(LOAN_FEE_1);
      artwork1.setIsOnLoan(IS_ON_LOAN_1);
      artwork1.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN);

      allArtworks.add(artwork1);

      Artwork artwork2 = new Artwork();
      artwork2.setArtworkId(ARTWORK_ID_2);
      artwork2.setArtist(ARTWORK_ARTIST_2);
      artwork2.setName(ARTWORK_NAME_2);
      artwork2.setRoom(room2);
      artwork2.setImage(IMAGE_2);
      artwork1.setLoanFee(LOAN_FEE_2);
      artwork1.setIsOnLoan(IS_ON_LOAN_2);
      artwork1.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_2);
      allArtworks.add(artwork2);
      return allArtworks;

    });

    lenient().when(roomRepository.findRoomByRoomId(anyLong())).thenAnswer((InvocationOnMock invocation) ->
    {
      if (invocation.getArgument(0).equals(ROOM_ID_1)) {

        Room room1 = new Room();
        room1.setRoomId(ROOM_ID_1);
        room1.setRoomName(ROOM_NAME_1);
        room1.setCurrentNumberOfArtwork(ROOM1_NUMBER_OF_ARTWORKS);
        return room1;

      } else {
        return null;
      }

    });

  }


  @Test
  public void testGetAllArtworks() {

    List<Artwork> foundArtworks = null;
    try {
      foundArtworks = artworkService.getAllArtworks();

    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(foundArtworks);
    assertEquals(NUMBER_OF_ARTWORKS, foundArtworks.size());
  }


  @Test
  public void testGetArtworksByRooom() {
    List<Artwork> artworksInRoom = null;

    artworksInRoom = artworkService.getAllArtworksByRoom(roomRepository.findRoomByRoomId(ROOM_ID_1));
    assertNotNull(artworksInRoom);
    assertEquals(ROOM1_NUMBER_OF_ARTWORKS, artworksInRoom.size());


  }

  //DONE
  @Test
  public void testGetArtworksWithNonExistingRoom() {

    List<Artwork> artworksInRoom = null;
    Room room = null;
    String error = "";
    try {
      artworksInRoom = artworkService.getAllArtworksByRoom(room);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertNull(artworksInRoom);
    assertEquals("Room doesn't exist", error);

  }

  @Test
  public void testDisplayArtworkWithAvailabilityForLoan() {

    Map<String, String> artworkWithAvailability = new HashMap<>();
    artworkWithAvailability = artworkService.getArtworkwithAvailabilityForLoan();
    assertEquals(NUMBER_OF_ARTWORKS, artworkWithAvailability.size());
    assertEquals(AVAILABILITY, artworkWithAvailability.get(ARTWORK_NAME));

  }

  @Test
  public void testGetArtworkWithLoanFee() {
    Map<String, Double> artworkWithLoanFee = new HashMap<>();
    artworkWithLoanFee = artworkService.getArtworkWithLoanFee();
    assertEquals(NUMBER_OF_ARTWORKS, artworkWithLoanFee.size());
    assertEquals(LOAN_FEE_1, artworkWithLoanFee.get(ARTWORK_NAME));

  }


}
