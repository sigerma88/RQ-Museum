package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;

/**
 * This is the test class for the ArtworkService class
 *
 * @author Siger
 * @author kieyanmamiche
 */

@ExtendWith(MockitoExtension.class)
public class TestArtworkService {

  @Mock
  private ArtworkRepository artworkRepository;

  @Mock
  private LoanRepository loanRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private RoomService roomService;

  @InjectMocks
  private ArtworkService artworkService;

  // Constants for Siger's tests
  private static final Long FIRST_ARTWORK_ID = 0L;
  private static final String FIRST_ARTWORK_NAME = "Artwork 1 Name";
  private static final String FIRST_ARTWORK_ARTIST = "Artist 1";
  private static final Boolean FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN = true;
  private static final Double FIRST_ARTWORK_LOAN_FEE = 80.0;
  private static final String FIRST_ARTWORK_IMAGE = "Image1";
  private static final Boolean FIRST_ARTWORK_IS_ON_LOAN = true;

  private static final Long SECOND_ARTWORK_ID = 1L;
  private static final String SECOND_ARTWORK_NAME = "Arwtork 2 Name";
  private static final String SECOND_ARTWORK_ARTIST = "Artist 2";
  private static final Boolean SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN = false;
  private static final Double SECOND_ARTWORK_LOAN_FEE = null;
  private static final String SECOND_ARTWORK_IMAGE = "Image2";
  private static final Boolean SECOND_ARTWORK_IS_ON_LOAN = false;

  private static final Long FIRST_ROOM_ID = 0L;
  private static final String FIRST_ROOM_NAME = "Room Name";
  private static final int FIRST_CURRENT_NUMEBR_OF_ARTWORK = 1;
  private static final RoomType FIRST_ROOM_TYPE = RoomType.Small;

  private static final Long FIRST_MUSEUM_ID = 0L;
  private static final String FIRST_MUSEUM_NAME = "Museum Name";
  private static final double FIRST_MUSEUM_VISIT_FEE = 10.0;

  private static final Long FIRST_SCHEDULE_ID = 0L;

  private static final Long FIRST_VISITOR_ID = 0L;
  private static final String FIRST_VISITOR_NAME = "Visitor Name";
  private static final String FIRST_VISITOR_EMAIL = "Visitor Email";
  private static final String FIRST_VISITOR_PASSWORD = "Visitor Password";

  private static final Long FIRST_LOAN_ID = 0L;
  private static final Boolean FIRST_LOAN_REQUEST_ACCEPTED = false;

  private static final Double SECOND_LOAN_FEE = 100.0;

  // Constants for Kieyan's tests
  // Artwork 1
  private static final Long ARTWORK_ID_1 = 2L;
  private static final String NAME_1 = "Mona Lisa";
  private static final String ARTIST_1 = "Kian";
  private static final Boolean IS_AVAILABLE_FOR_LOAN_1 = true;
  private static final double LOAN_FEE_1 = 10;
  private static final String IMAGE_1 = "https://source.unsplash.com/C54OKB99iuw";
  private static final Boolean IS_ON_LOAN_1 = false;

  // Artwork 2
  private static final Long ARTWORK_ID_2 = 3L;
  private static final String NAME_2 = "Mona Lisa 2";
  private static final String ARTIST_2 = "Kian 2";
  private static final Boolean IS_AVAILABLE_FOR_LOAN_2 = false;
  private static final double LOAN_FEE_2 = 1000000000;
  private static final String IMAGE_2 = "https://source.unsplash.com/C54OKBaasdad99iuw";
  private static final Boolean IS_ON_LOAN_2 = false;

  // Artwork 3
  private static final Long ARTWORK_ID_3 = 4L;
  private static final String NAME_3 = "Mona Lisa 3";
  private static final String ARTIST_3 = "Kian 3";
  private static final Boolean IS_AVAILABLE_FOR_LOAN_3 = false;
  private static final double LOAN_FEE_3 = 12345;
  private static final String IMAGE_3 = "https://source.unsplash.com/C54OKBaasdad99iuw";
  private static final Boolean IS_ON_LOAN_3 = true;

  // Artwork 4 - NULL ROOM
  private static final Long ARTWORK_ID_4 = 5L;
  private static final String NAME_4 = "Mona Lisa 3";
  private static final String ARTIST_4 = "Kian 3";
  private static final Boolean IS_AVAILABLE_FOR_LOAN_4 = false;
  private static final double LOAN_FEE_4 = 12345;
  private static final String IMAGE_4 = "https://source.unsplash.com/C54OKBaasdad99iuw";
  private static final Boolean IS_ON_LOAN_4 = false;

  // Room 1
  private static final Long ROOM_ID_1 = 1L;
  private static final String ROOM_NAME_1 = "Clark Room";
  private static final int CURRENT_NUMBER_OF_ARTWORK_1 = 123;
  private static final RoomType ROOM_TYPE_1 = RoomType.Small;

  // Room 2
  private static final Long ROOM_ID_2 = 2L;
  private static final String ROOM_NAME_2 = "Bob Room";
  private static final int CURRENT_NUMBER_OF_ARTWORK_2 = 293;
  private static final RoomType ROOM_TYPE_2 = RoomType.Storage;

  // Room 3 - THE FULL ROOM
  private static final Long ROOM_ID_3 = 3L;
  private static final String ROOM_NAME_3 = "Full Room";
  private static final int CURRENT_NUMBER_OF_ARTWORK_3 = 300;
  private static final RoomType ROOM_TYPE_3 = RoomType.Large;

  /**
   * This method sets up the mock objects before each test
   * There is one loaned artwork not in a room and one non-loanable artwork in a
   * room
   *
   * @author Siger
   */
  @BeforeEach
  public void setMockOutput() {
    Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    };

    // Museum Creation
    Schedule schedule = new Schedule();
    Museum museum = new Museum();
    museum.setName("Rougon-Macquart");
    museum.setVisitFee(12.5);
    museum.setSchedule(schedule);

    lenient().when(artworkRepository.findArtworkByArtworkId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(FIRST_ARTWORK_ID)) {
        // Create an artwork
        Artwork artwork = new Artwork();
        artwork.setArtworkId(FIRST_ARTWORK_ID);
        artwork.setName(FIRST_ARTWORK_NAME);
        artwork.setArtist(FIRST_ARTWORK_ARTIST);
        artwork.setIsAvailableForLoan(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN);
        artwork.setLoanFee(FIRST_ARTWORK_LOAN_FEE);
        artwork.setImage(FIRST_ARTWORK_IMAGE);
        artwork.setIsOnLoan(FIRST_ARTWORK_IS_ON_LOAN);
        return artwork;
      } else if (invocation.getArgument(0).equals(SECOND_ARTWORK_ID)) {
        // Create a room
        Room room = createRoomStub();

        // Create an artwork
        Artwork artwork = new Artwork();
        artwork.setArtworkId(SECOND_ARTWORK_ID);
        artwork.setName(SECOND_ARTWORK_NAME);
        artwork.setArtist(SECOND_ARTWORK_ARTIST);
        artwork.setIsAvailableForLoan(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN);
        artwork.setLoanFee(SECOND_ARTWORK_LOAN_FEE);
        artwork.setImage(SECOND_ARTWORK_IMAGE);
        artwork.setIsOnLoan(SECOND_ARTWORK_IS_ON_LOAN);
        artwork.setRoom(room);
        return artwork;
      } else if (invocation.getArgument(0).equals(ARTWORK_ID_1)) {
        Artwork artwork = new Artwork();
        artwork.setArtworkId(ARTWORK_ID_1);
        artwork.setName(NAME_1);
        artwork.setImage(IMAGE_1);
        artwork.setLoanFee(LOAN_FEE_1);
        artwork.setArtist(ARTIST_1);
        artwork.setIsOnLoan(IS_ON_LOAN_1);
        artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_1);
        Room room = roomRepository.findRoomByRoomId(ROOM_ID_1);
        artwork.setRoom(room);
        return artwork;
      } else if (invocation.getArgument(0).equals(ARTWORK_ID_2)) {
        Artwork artwork = new Artwork();
        artwork.setArtworkId(ARTWORK_ID_2);
        artwork.setImage(IMAGE_2);
        artwork.setLoanFee(LOAN_FEE_2);
        artwork.setArtist(ARTIST_2);
        artwork.setName(NAME_2);
        artwork.setIsOnLoan(IS_ON_LOAN_2);
        artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_2);
        Room room = roomRepository.findRoomByRoomId(ROOM_ID_2);
        artwork.setRoom(room);
        return artwork;
      } else if (invocation.getArgument(0).equals(ARTWORK_ID_3)) {
        Artwork artwork = new Artwork();
        artwork.setArtworkId(ARTWORK_ID_3);
        artwork.setImage(IMAGE_3);
        artwork.setLoanFee(LOAN_FEE_3);
        artwork.setArtist(ARTIST_3);
        artwork.setName(NAME_3);
        artwork.setIsOnLoan(IS_ON_LOAN_3);
        artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_3);
        Room room = roomRepository.findRoomByRoomId(ROOM_ID_3);
        artwork.setRoom(room);
        return artwork;
      } else if (invocation.getArgument(0).equals(ARTWORK_ID_4)) { // Artwork with null room
        Artwork artwork = new Artwork();
        artwork.setArtworkId(ARTWORK_ID_4);
        artwork.setImage(IMAGE_4);
        artwork.setLoanFee(LOAN_FEE_4);
        artwork.setArtist(ARTIST_4);
        artwork.setName(NAME_4);
        artwork.setIsOnLoan(IS_ON_LOAN_4);
        artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_4);
        artwork.setRoom(null);
        return artwork;
      } else {
        return null;
      }
    });

    lenient().when(artworkRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
      // Create a room
      Room room = createRoomStub();

      // Create an artworks
      Artwork firstArtwork = new Artwork();
      firstArtwork.setArtworkId(FIRST_ARTWORK_ID);
      firstArtwork.setName(FIRST_ARTWORK_NAME);
      firstArtwork.setArtist(FIRST_ARTWORK_ARTIST);
      firstArtwork.setIsAvailableForLoan(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN);
      firstArtwork.setLoanFee(FIRST_ARTWORK_LOAN_FEE);
      firstArtwork.setImage(FIRST_ARTWORK_IMAGE);
      firstArtwork.setIsOnLoan(FIRST_ARTWORK_IS_ON_LOAN);

      Artwork secondArtwork = new Artwork();
      secondArtwork.setArtworkId(SECOND_ARTWORK_ID);
      secondArtwork.setName(SECOND_ARTWORK_NAME);
      secondArtwork.setArtist(SECOND_ARTWORK_ARTIST);
      secondArtwork.setIsAvailableForLoan(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN);
      secondArtwork.setLoanFee(SECOND_ARTWORK_LOAN_FEE);
      secondArtwork.setImage(SECOND_ARTWORK_IMAGE);
      secondArtwork.setIsOnLoan(SECOND_ARTWORK_IS_ON_LOAN);
      secondArtwork.setRoom(room);

      // Create and return a list of artworks
      List<Artwork> artworks = new ArrayList<>();
      artworks.add(firstArtwork);
      artworks.add(secondArtwork);
      return artworks;
    });

    lenient().when(artworkRepository.findArtworkByRoom(any(Room.class))).thenAnswer((InvocationOnMock invocation) -> {
      Room room = invocation.getArgument(0);
      if (room.getRoomId() == FIRST_ROOM_ID) {
        // Create an artwork
        Artwork artwork = new Artwork();
        artwork.setArtworkId(SECOND_ARTWORK_ID);
        artwork.setName(SECOND_ARTWORK_NAME);
        artwork.setArtist(SECOND_ARTWORK_ARTIST);
        artwork.setIsAvailableForLoan(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN);
        artwork.setLoanFee(SECOND_ARTWORK_LOAN_FEE);
        artwork.setImage(SECOND_ARTWORK_IMAGE);
        artwork.setIsOnLoan(SECOND_ARTWORK_IS_ON_LOAN);
        artwork.setRoom(room);

        // Create and return a list of artworks
        List<Artwork> artworks = new ArrayList<>();
        artworks.add(artwork);
        return artworks;
      } else if (room.getRoomId() == ROOM_ID_1) {
        Artwork artwork = new Artwork();
        artwork.setArtworkId(ARTWORK_ID_1);
        artwork.setName(NAME_1);
        artwork.setImage(IMAGE_1);
        artwork.setLoanFee(LOAN_FEE_1);
        artwork.setArtist(ARTIST_1);
        artwork.setIsOnLoan(IS_ON_LOAN_1);
        artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_1);
        artwork.setRoom(room);
        List<Artwork> artworks = new ArrayList<Artwork>();
        artworks.add(artwork);
        return artworks;
      } else if (room.getRoomId() == ROOM_ID_2) {
        Artwork artwork = new Artwork();
        artwork.setArtworkId(ARTWORK_ID_2);
        artwork.setImage(IMAGE_2);
        artwork.setLoanFee(LOAN_FEE_2);
        artwork.setArtist(ARTIST_2);
        artwork.setName(NAME_2);
        artwork.setIsOnLoan(IS_ON_LOAN_2);
        artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_2);
        artwork.setRoom(room);
        List<Artwork> artworks = new ArrayList<Artwork>();
        artworks.add(artwork);
        return artworks;
      } else if (room.getRoomId() == ROOM_ID_3) {
        Artwork artwork = new Artwork();
        artwork.setArtworkId(ARTWORK_ID_3);
        artwork.setImage(IMAGE_3);
        artwork.setLoanFee(LOAN_FEE_3);
        artwork.setArtist(ARTIST_3);
        artwork.setName(NAME_3);
        artwork.setIsOnLoan(IS_ON_LOAN_3);
        artwork.setIsAvailableForLoan(IS_AVAILABLE_FOR_LOAN_3);
        artwork.setRoom(room);
        List<Artwork> artworks = new ArrayList<Artwork>();
        artworks.add(artwork);
        return artworks;
      } else {
        return null;
      }
    });

    lenient().when(artworkRepository.findArtworkByIsAvailableForLoan(anyBoolean())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(true)) {
        // Create an artwork
        Artwork artwork = new Artwork();
        artwork.setArtworkId(FIRST_ARTWORK_ID);
        artwork.setName(FIRST_ARTWORK_NAME);
        artwork.setArtist(FIRST_ARTWORK_ARTIST);
        artwork.setIsAvailableForLoan(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN);
        artwork.setLoanFee(FIRST_ARTWORK_LOAN_FEE);
        artwork.setImage(FIRST_ARTWORK_IMAGE);
        artwork.setIsOnLoan(FIRST_ARTWORK_IS_ON_LOAN);

        // Create and return a list of artworks
        List<Artwork> artworks = new ArrayList<>();
        artworks.add(artwork);
        return artworks;
      } else if (invocation.getArgument(0).equals(false)) {
        // Create a room
        Room room = createRoomStub();

        // Create an artwork
        Artwork artwork = new Artwork();
        artwork.setArtworkId(SECOND_ARTWORK_ID);
        artwork.setName(SECOND_ARTWORK_NAME);
        artwork.setArtist(SECOND_ARTWORK_ARTIST);
        artwork.setIsAvailableForLoan(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN);
        artwork.setLoanFee(SECOND_ARTWORK_LOAN_FEE);
        artwork.setImage(SECOND_ARTWORK_IMAGE);
        artwork.setIsOnLoan(SECOND_ARTWORK_IS_ON_LOAN);
        artwork.setRoom(room);

        // Create and return a list of artworks
        List<Artwork> artworks = new ArrayList<>();
        artworks.add(artwork);
        return artworks;
      } else {
        return null;
      }
    });

    lenient().when(roomService.getRoomById(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(FIRST_ROOM_ID)) {
        return createRoomStub();
      } else {
        return null;
      }
    });

    lenient().when(loanRepository.findLoanByArtwork(any(Artwork.class))).thenAnswer((InvocationOnMock invocation) -> {
      Artwork artwork = invocation.getArgument(0);
      if (artwork.getArtworkId().equals(FIRST_ARTWORK_ID)) {
        // Create a loan stub
        List<Loan> loans = new ArrayList<>();
        Loan loan = createLoanStub(artwork);
        loans.add(loan);
        return loans;
      } else {
        return null;
      }
    });

    lenient().when(roomRepository.findRoomByRoomId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(ROOM_ID_1)) {
        Room room = new Room();
        room.setRoomId(ROOM_ID_1);
        room.setRoomType(ROOM_TYPE_1);
        room.setRoomName(ROOM_NAME_1);
        room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_1);
        room.setMuseum(museum);
        return room;
      } else if (invocation.getArgument(0).equals(ROOM_ID_2)) {
        Room room = new Room();
        room.setRoomId(ROOM_ID_2);
        room.setRoomType(ROOM_TYPE_2);
        room.setRoomName(ROOM_NAME_2);
        room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_2);
        room.setMuseum(museum);
        return room;
      } else if (invocation.getArgument(0).equals(ROOM_ID_3)) {
        Room room = new Room();
        room.setRoomId(ROOM_ID_3);
        room.setRoomType(ROOM_TYPE_3);
        room.setRoomName(ROOM_NAME_3);
        room.setCurrentNumberOfArtwork(CURRENT_NUMBER_OF_ARTWORK_3);
        room.setMuseum(museum);
        return room;
      } else {
        return null;
      }
    });

    lenient().when(roomService.getRoomCapacity(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(ROOM_ID_1)) {
        // Small room
        return 200 - CURRENT_NUMBER_OF_ARTWORK_1;
      } else if (invocation.getArgument(0).equals(ROOM_ID_2)) {
        // Storage room
        return Integer.MAX_VALUE;
      } else if (invocation.getArgument(0).equals(ROOM_ID_3)) {
        // Large room
        return 300 - CURRENT_NUMBER_OF_ARTWORK_3;
      } else {
        return null;
      }
    });

    lenient().when(artworkRepository.save(any(Artwork.class))).thenAnswer(returnParameterAsAnswer);
  }

  /**
   * This method tests the creation of an artwork
   *
   * @author Siger
   */
  @Test
  public void testCreateArtwork() {
    // Create an artwork
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN,
          null);
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }

    // Check that the artwork was created
    assertNotNull(artwork);
    assertEquals(FIRST_ARTWORK_NAME, artwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, artwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
        artwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, artwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, artwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, artwork.getIsOnLoan());
  }

  /**
   * This method tests the creation of an artwork with a null name
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkNullName() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(null, FIRST_ARTWORK_ARTIST, FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork name cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork with an empty name
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkEmptyName() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork("", FIRST_ARTWORK_ARTIST, FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork name cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork with a null artist
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkNullArtist() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, null, FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artist name cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork with an empty artist
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkEmptyArtist() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, "", FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artist name cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork with a null image
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkNullImage() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE, null, FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Image cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork with an empty image
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkEmptyImage() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE, "", FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Image cannot be empty", e.getMessage());
    }
  }

  /**
   * This method tests the creation of a loanable artwork with a null loan fee
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkNullLoanFee() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          null, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Loan fee cannot be null if artwork is available for loan", e.getMessage());
    }
  }

  /**
   * This method tests the creation of a non-loanable artwork with a loan fee
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkLoanFee() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          !FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Loan fee must be null if artwork is not available for loan", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork on loan with a room
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkRoom() {
    Artwork artwork = null;
    try {
      Room room = createRoomStub();
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, FIRST_ARTWORK_IS_ON_LOAN,
          room);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Room must be null if artwork is on loan", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork not on loan with no room
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkNoRoom() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, !FIRST_ARTWORK_IS_ON_LOAN,
          null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Room cannot be null if artwork is not on loan", e.getMessage());
    }
  }

  /**
   * This method tests the creation of an artwork with a null isOnLoan
   *
   * @author Siger
   */
  @Test
  public void testCreateArtworkNullIsOnLoan() {
    Artwork artwork = null;
    try {
      artwork = artworkService.createArtwork(FIRST_ARTWORK_NAME, FIRST_ARTWORK_ARTIST,
          FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, FIRST_ARTWORK_LOAN_FEE, FIRST_ARTWORK_IMAGE, null, null);
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork must be either on loan or not on loan", e.getMessage());
    }
  }

  /**
   * This method tests getting an artwork by its id
   *
   * @author Siger
   */
  @Test
  public void testGetArtworkById() {
    Artwork firstArtwork = null;
    Artwork secondArtwork = null;
    try {
      firstArtwork = artworkService.getArtwork(FIRST_ARTWORK_ID);
      secondArtwork = artworkService.getArtwork(SECOND_ARTWORK_ID);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }

    // Check that the artwork was correctly retrieved
    assertNotNull(firstArtwork);
    assertEquals(FIRST_ARTWORK_ID, firstArtwork.getArtworkId());
    assertEquals(FIRST_ARTWORK_NAME, firstArtwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, firstArtwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, firstArtwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, firstArtwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, firstArtwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, firstArtwork.getIsOnLoan());
    assertNull(firstArtwork.getRoom());

    assertNotNull(secondArtwork);
    assertEquals(SECOND_ARTWORK_ID, secondArtwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, secondArtwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, secondArtwork.getArtist());
    assertEquals(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN, secondArtwork.getIsAvailableForLoan());
    assertEquals(SECOND_ARTWORK_LOAN_FEE, secondArtwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, secondArtwork.getImage());
    assertEquals(SECOND_ARTWORK_IS_ON_LOAN, secondArtwork.getIsOnLoan());
    assertNotNull(secondArtwork.getRoom());
  }

  /**
   * This method tests getting an artwork by a non-existent id
   *
   * @author Siger
   */
  @Test
  public void testGetArtworkByNonExistentId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.getArtwork(-1L);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }

    // Check that the artwork was correctly retrieved
    assertNull(artwork);
  }

  /**
   * This method tests getting an artwork by a null id
   *
   * @author Siger
   */
  @Test
  public void testGetArtworkByNullId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.getArtwork(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork id cannot be null", e.getMessage());
    }
  }

  /**
   * This method tests getting all artworks
   *
   * @author Siger
   */
  @Test
  public void testGetAllArtworks() {
    List<Artwork> artworks = null;
    try {
      artworks = artworkService.getAllArtworks();
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }

    // Check that the artworks were correctly retrieved
    assertNotNull(artworks);
    assertEquals(2, artworks.size());

    Artwork firstArtwork = artworks.get(0);
    assertEquals(FIRST_ARTWORK_ID, firstArtwork.getArtworkId());
    assertEquals(FIRST_ARTWORK_NAME, firstArtwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, firstArtwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, firstArtwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, firstArtwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, firstArtwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, firstArtwork.getIsOnLoan());
    assertNull(firstArtwork.getRoom());

    Artwork secondArtwork = artworks.get(1);
    assertEquals(SECOND_ARTWORK_ID, secondArtwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, secondArtwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, secondArtwork.getArtist());
    assertEquals(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN, secondArtwork.getIsAvailableForLoan());
    assertEquals(SECOND_ARTWORK_LOAN_FEE, secondArtwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, secondArtwork.getImage());
    assertEquals(SECOND_ARTWORK_IS_ON_LOAN, secondArtwork.getIsOnLoan());
    assertEquals(FIRST_ROOM_ID, secondArtwork.getRoom().getRoomId());
  }

  /**
   * This method tests getting all artworks by a room
   *
   * @author Siger
   */
  @Test
  public void testGetAllArtworksByRoom() {
    List<Artwork> artworks = null;
    try {
      artworks = artworkService.getAllArtworksByRoom(FIRST_ROOM_ID);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }

    // Check that the artworks were correctly retrieved
    assertNotNull(artworks);
    assertEquals(1, artworks.size());

    Artwork secondArtwork = artworks.get(0);
    assertEquals(SECOND_ARTWORK_ID, secondArtwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, secondArtwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, secondArtwork.getArtist());
    assertEquals(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN, secondArtwork.getIsAvailableForLoan());
    assertEquals(SECOND_ARTWORK_LOAN_FEE, secondArtwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, secondArtwork.getImage());
    assertEquals(SECOND_ARTWORK_IS_ON_LOAN, secondArtwork.getIsOnLoan());
    assertEquals(FIRST_ROOM_ID, secondArtwork.getRoom().getRoomId());
  }

  /**
   * This method tests getting all artworks by a non-existent room
   *
   * @author Siger
   */
  @Test
  public void testGetAllArtworksByNonExistentRoom() {
    List<Artwork> artworks = null;
    try {
      artworks = artworkService.getAllArtworksByRoom(-1L);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artworks);
      assertEquals("Room does not exist", e.getMessage());
    }
  }

  /**
   * This method tests getting all artworks by a null room
   *
   * @author Siger
   */
  @Test
  public void testGetAllArtworksByNullRoom() {
    List<Artwork> artworks = null;
    try {
      artworks = artworkService.getAllArtworksByRoom(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artworks);
      assertEquals("Room does not exist", e.getMessage());
    }
  }

  /**
   * This method tests getting all artworks by if they are available for loan
   *
   * @author Siger
   */
  @Test
  public void testGetAllArtworksByIsAvailableForLoan() {
    List<Artwork> artworks = null;
    try {
      artworks = artworkService.getAllArtworksByAvailabilityForLoan(true);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }

    // Check that the artworks were correctly retrieved
    assertNotNull(artworks);
    assertEquals(1, artworks.size());

    Artwork firstArtwork = artworks.get(0);
    assertEquals(FIRST_ARTWORK_ID, firstArtwork.getArtworkId());
    assertEquals(FIRST_ARTWORK_NAME, firstArtwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, firstArtwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, firstArtwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, firstArtwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, firstArtwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, firstArtwork.getIsOnLoan());
    assertNull(firstArtwork.getRoom());
  }

  /**
   * This method tests getting all artworks by if they are not available for loan
   *
   * @author Siger
   */
  @Test
  public void testGetAllArtworksByIsNotAvailableForLoan() {
    List<Artwork> artworks = null;
    try {
      artworks = artworkService.getAllArtworksByAvailabilityForLoan(false);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }

    // Check that the artworks were correctly retrieved
    assertNotNull(artworks);
    assertEquals(1, artworks.size());

    Artwork secondArtwork = artworks.get(0);
    assertEquals(SECOND_ARTWORK_ID, secondArtwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, secondArtwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, secondArtwork.getArtist());
    assertEquals(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN, secondArtwork.getIsAvailableForLoan());
    assertEquals(SECOND_ARTWORK_LOAN_FEE, secondArtwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, secondArtwork.getImage());
    assertEquals(SECOND_ARTWORK_IS_ON_LOAN, secondArtwork.getIsOnLoan());
    assertEquals(FIRST_ROOM_ID, secondArtwork.getRoom().getRoomId());
  }

  /**
   * This method tests getting all artworks by if they are available for loan but the argument is null
   *
   * @author Siger
   */
  @Test
  public void testGetAllArtworksByNullIsAvailableForLoan() {
    List<Artwork> artworks = null;
    try {
      artworks = artworkService.getAllArtworksByAvailabilityForLoan(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artworks);
      assertEquals("Artwork availability cannot be null", e.getMessage());
    }
  }

  /**
   * This method tests editing an artwork's information
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkInfo() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkInfo(FIRST_ARTWORK_ID, SECOND_ARTWORK_NAME, SECOND_ARTWORK_ARTIST,
          SECOND_ARTWORK_IMAGE);
    } catch (IllegalArgumentException e) {
      // Check that no error occurred
      fail();
    }

    // Check that the artwork was correctly edited
    assertNotNull(artwork);
    assertEquals(FIRST_ARTWORK_ID, artwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, artwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, artwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, artwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, artwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, artwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, artwork.getIsOnLoan());
    assertNull(artwork.getRoom());
  }

  /**
   * This method tests editing an artwork's information with a null id
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoNullId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkInfo(null, SECOND_ARTWORK_NAME, SECOND_ARTWORK_ARTIST,
          SECOND_ARTWORK_IMAGE);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork does not exist", e.getMessage());
    }
  }

  /**
   * This method tests editing an artwork's information with a non-existent id
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoNonExistentId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkInfo(-1L, SECOND_ARTWORK_NAME, SECOND_ARTWORK_ARTIST,
          SECOND_ARTWORK_IMAGE);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork does not exist", e.getMessage());
    }
  }

  /**
   * This method tests editing an artwork's information with a null name
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoNullName() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkInfo(FIRST_ARTWORK_ID, null, SECOND_ARTWORK_ARTIST,
          SECOND_ARTWORK_IMAGE);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the artwork was correctly edited
    assertNotNull(artwork);
    assertEquals(FIRST_ARTWORK_ID, artwork.getArtworkId());
    assertEquals(FIRST_ARTWORK_NAME, artwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, artwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, artwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, artwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, artwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, artwork.getIsOnLoan());
    assertNull(artwork.getRoom());
  }

  /**
   * This method tests editing an artwork's information with a null artist
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoNullArtist() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkInfo(FIRST_ARTWORK_ID, SECOND_ARTWORK_NAME, null,
          SECOND_ARTWORK_IMAGE);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the artwork was correctly edited
    assertNotNull(artwork);
    assertEquals(FIRST_ARTWORK_ID, artwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, artwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, artwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, artwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, artwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, artwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, artwork.getIsOnLoan());
    assertNull(artwork.getRoom());
  }

  /**
   * This method tests editing an artwork's information with a null image
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoNullImage() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkInfo(FIRST_ARTWORK_ID, SECOND_ARTWORK_NAME, SECOND_ARTWORK_ARTIST,
          null);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the artwork was correctly edited
    assertNotNull(artwork);
    assertEquals(FIRST_ARTWORK_ID, artwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, artwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, artwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, artwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, artwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, artwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, artwork.getIsOnLoan());
    assertNull(artwork.getRoom());
  }

  /**
   * This method tests editing an artwork's information with everything null
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkInfoEverythingNull() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkInfo(FIRST_ARTWORK_ID, null, null, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Nothing to edit, all fields are empty", e.getMessage());
    }
  }

  /**
   * This method tests editing an artwork's loan information
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfo() {
    Artwork firstArtwork = null;
    Artwork secondArtwork = null;
    try {
      firstArtwork = artworkService.editArtworkLoanInfo(FIRST_ARTWORK_ID, SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          SECOND_ARTWORK_LOAN_FEE);
      secondArtwork = artworkService.editArtworkLoanInfo(SECOND_ARTWORK_ID, FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          FIRST_ARTWORK_LOAN_FEE);
    } catch (IllegalArgumentException e) {
      fail();
    }

    // Check that the artwork was correctly edited
    assertNotNull(firstArtwork);
    assertEquals(FIRST_ARTWORK_ID, firstArtwork.getArtworkId());
    assertEquals(FIRST_ARTWORK_NAME, firstArtwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, firstArtwork.getArtist());
    assertEquals(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN, firstArtwork.getIsAvailableForLoan());
    assertEquals(SECOND_ARTWORK_LOAN_FEE, firstArtwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, firstArtwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, firstArtwork.getIsOnLoan());
    assertNull(firstArtwork.getRoom());

    assertNotNull(secondArtwork);
    assertEquals(SECOND_ARTWORK_ID, secondArtwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, secondArtwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, secondArtwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, secondArtwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, secondArtwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, secondArtwork.getImage());
    assertEquals(SECOND_ARTWORK_IS_ON_LOAN, secondArtwork.getIsOnLoan());
    assertNotNull(secondArtwork.getRoom());
  }

  /**
   * This method tests editing an artwork's loan information with a null artwork
   * id
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoNullArtworkId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkLoanInfo(null, SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          SECOND_ARTWORK_LOAN_FEE);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork does not exist", e.getMessage());
    }
  }

  /**
   * This method tests editing an artwork's loan information with an invalid
   * artwork id
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoInvalidArtworkId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkLoanInfo(-1L, SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN,
          SECOND_ARTWORK_LOAN_FEE);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork does not exist", e.getMessage());
    }
  }

  /**
   * This method tests editing an artwork's loan information with a null
   * isAvailableForLoan
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoNullIsAvailableForLoan() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkLoanInfo(FIRST_ARTWORK_ID, null, SECOND_LOAN_FEE);
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }

    // Check that the artwork was correctly edited
    assertNotNull(artwork);
    assertEquals(FIRST_ARTWORK_ID, artwork.getArtworkId());
    assertEquals(FIRST_ARTWORK_NAME, artwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, artwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, artwork.getIsAvailableForLoan());
    assertEquals(SECOND_LOAN_FEE, artwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, artwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, artwork.getIsOnLoan());
    assertNull(artwork.getRoom());
  }

  /**
   * This method tests editing an artwork's loan information with a null loanFee
   * when it is available for loan
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoNullLoanFee() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkLoanInfo(FIRST_ARTWORK_ID, true, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Loan fee cannot be null if artwork is available for loan", e.getMessage());
    }
  }

  /**
   * This method tests editing an artwork's loan information with a non null
   * loanFee when it is not available for loan
   *
   * @author Siger
   */
  @Test
  public void testEditArtworkLoanInfoNotNullLoanFee() {
    Artwork artwork = null;
    try {
      artwork = artworkService.editArtworkLoanInfo(FIRST_ARTWORK_ID, false, SECOND_LOAN_FEE);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Loan fee must be null if artwork is not available for loan", e.getMessage());
    }
  }

  /**
   * This method tests deleting an artwork with a valid id
   *
   * @author Siger
   */
  @Test
  public void testDeleteArtwork() {
    Artwork firstArtwork = null;
    Artwork secondArtwork = null;
    try {
      firstArtwork = artworkService.deleteArtwork(FIRST_ARTWORK_ID);
      secondArtwork = artworkService.deleteArtwork(SECOND_ARTWORK_ID);
    } catch (IllegalArgumentException e) {
      fail(e.getMessage());
    }

    // Check that the artworks were correctly deleted
    assertNotNull(firstArtwork);
    assertEquals(FIRST_ARTWORK_ID, firstArtwork.getArtworkId());
    assertEquals(FIRST_ARTWORK_NAME, firstArtwork.getName());
    assertEquals(FIRST_ARTWORK_ARTIST, firstArtwork.getArtist());
    assertEquals(FIRST_ARTWORK_IS_AVAILABLE_FOR_LOAN, firstArtwork.getIsAvailableForLoan());
    assertEquals(FIRST_ARTWORK_LOAN_FEE, firstArtwork.getLoanFee());
    assertEquals(FIRST_ARTWORK_IMAGE, firstArtwork.getImage());
    assertEquals(FIRST_ARTWORK_IS_ON_LOAN, firstArtwork.getIsOnLoan());
    assertNull(firstArtwork.getRoom());

    assertNotNull(secondArtwork);
    assertEquals(SECOND_ARTWORK_ID, secondArtwork.getArtworkId());
    assertEquals(SECOND_ARTWORK_NAME, secondArtwork.getName());
    assertEquals(SECOND_ARTWORK_ARTIST, secondArtwork.getArtist());
    assertEquals(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN, secondArtwork.getIsAvailableForLoan());
    assertEquals(SECOND_ARTWORK_LOAN_FEE, secondArtwork.getLoanFee());
    assertEquals(SECOND_ARTWORK_IMAGE, secondArtwork.getImage());
    assertEquals(SECOND_ARTWORK_IS_ON_LOAN, secondArtwork.getIsOnLoan());
    assertNotNull(secondArtwork.getRoom());
  }

  /**
   * This method tests deleting an artwork with a null id
   *
   * @author Siger
   */
  @Test
  public void testDeleteArtworkNullId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.deleteArtwork(null);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork does not exist", e.getMessage());
    }
  }

  /**
   * This method tests deleting an artwork with an invalid id
   *
   * @author Siger
   */
  @Test
  public void testDeleteArtworkInvalidId() {
    Artwork artwork = null;
    try {
      artwork = artworkService.deleteArtwork(-1L);
      fail();
    } catch (IllegalArgumentException e) {
      // Check that an error occurred
      assertNull(artwork);
      assertEquals("Artwork does not exist", e.getMessage());
    }
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
   * Test method for getting the status of an artwork when the artwork doesn't exist
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetArtworkStatus_RoomNonExisting() {
    String error = null;
    try {
      artworkService.getArtworkStatus(ARTWORK_ID_4);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals("Room does not exist", error);
  }

  /**
   * Test method for getting the number of artworks in a room
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetNumberOfArtworksInRoom() {
    try {
      int numberOfArtworksInRoom = artworkService.getNumberOfArtworksInRoom(ROOM_ID_1);
      int numberOfArtworksInRoom2 = artworkService.getNumberOfArtworksInRoom(ROOM_ID_2);
      assertEquals(1, numberOfArtworksInRoom);
      assertEquals(1, numberOfArtworksInRoom2);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  /**
   * Test method for moving an artwork to a different room
   *
   * @author kieyanmamiche
   */
  @Test
  public void testMoveArtworkToRoom() {
    Artwork artworkBeforeMove = artworkRepository.findArtworkByArtworkId(ARTWORK_ID_1);

    // Check that artwork before move is in room 1
    assertEquals(ROOM_ID_1, artworkBeforeMove.getRoom().getRoomId(), "Artwork before move is in room 1");

    // Move artwork to other room
    Artwork artwork = artworkService.moveArtworkToRoom(ARTWORK_ID_1, ROOM_ID_2);

    // Check that artwork after move is in room 2
    assertEquals(ROOM_ID_2, artwork.getRoom().getRoomId(), "Artwork after move is in room 2");
  }

  /**
   * Test method for getting the number of artworks in a room when room doesn't exist
   *
   * @author kieyanmamiche
   */
  @Test
  public void testGetNumberOfArtworksInRoom_NoRoom() {
    String error = null;
    try {
      int numberOfArtworksInRoom = artworkService.getNumberOfArtworksInRoom(123456);
    } catch (IllegalArgumentException e) {
      error = e.getMessage();
    }
    assertEquals("Room does not exist", error);
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
    assertEquals("Room is at full capacity", error);
  }

  /**
   * Test method for removing an artwork from its room
   *
   * @author kieyanmamiche
   */
  @Test
  public void testRemoveArtworkFromRoom() {
    Artwork artworkBeforeRemove = artworkRepository.findArtworkByArtworkId(ARTWORK_ID_1);

    // Check that artwork before move is in room 1
    assertEquals(ROOM_ID_1, artworkBeforeRemove.getRoom().getRoomId(), "Artwork before move is in room 1");
    Artwork artwork = artworkService.removeArtworkFromRoom(ARTWORK_ID_1);

    // Check that artwork's room is now null
    assertNull(artwork.getRoom(), "Artwork's room is now null");
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

  /**
   * This is a helper method that creates a room stub
   *
   * @author Siger
   */
  public Room createRoomStub() {
    // Create a schedule of museum
    Schedule schedule = new Schedule();
    schedule.setScheduleId(FIRST_SCHEDULE_ID);

    // Create a museum
    Museum museum = new Museum();
    museum.setMuseumId(FIRST_MUSEUM_ID);
    museum.setName(FIRST_MUSEUM_NAME);
    museum.setVisitFee(FIRST_MUSEUM_VISIT_FEE);
    museum.setSchedule(schedule);

    // Create a room
    Room room = new Room();
    room.setRoomId(FIRST_ROOM_ID);
    room.setRoomName(FIRST_ROOM_NAME);
    room.setCurrentNumberOfArtwork(FIRST_CURRENT_NUMEBR_OF_ARTWORK);
    room.setRoomType(FIRST_ROOM_TYPE);
    room.setMuseum(museum);

    return room;
  }

  /**
   * This is a helper method that creates a loan stub
   *
   * @author Siger
   */
  public Loan createLoanStub(Artwork artwork) {
    // Create a visitor
    Visitor visitor = new Visitor();
    visitor.setMuseumUserId(FIRST_VISITOR_ID);
    visitor.setName(FIRST_VISITOR_NAME);
    visitor.setEmail(FIRST_VISITOR_EMAIL);
    visitor.setPassword(FIRST_VISITOR_PASSWORD);

    // Create a loan
    Loan loan = new Loan();
    loan.setLoanId(FIRST_LOAN_ID);
    loan.setRequestAccepted(FIRST_LOAN_REQUEST_ACCEPTED);
    loan.setVisitor(visitor);
    loan.setArtwork(artwork);

    return loan;
  }
}
