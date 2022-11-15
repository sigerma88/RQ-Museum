package ca.mcgill.ecse321.museum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;

/**
 * This is the test class for the ArtworkService class
 * 
 * @author Siger
 */
@ExtendWith(MockitoExtension.class)
public class TestArtworkService {

  @Mock
  private ArtworkRepository artworkRepository;

  @Mock
  private RoomRepository roomRepository;

  @Mock
  private LoanRepository loanRepository;

  @InjectMocks
  private ArtworkService artworkService;

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

    lenient().when(artworkRepository.findArtworkByArtworkId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
      if (invocation.getArgument(0).equals(FIRST_ARTWORK_ID)) {
        // Create an artwork
        Artwork artwork = new Artwork();
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
        artwork.setName(SECOND_ARTWORK_NAME);
        artwork.setArtist(SECOND_ARTWORK_ARTIST);
        artwork.setIsAvailableForLoan(SECOND_ARTWORK_IS_AVAILABLE_FOR_LOAN);
        artwork.setLoanFee(SECOND_ARTWORK_LOAN_FEE);
        artwork.setImage(SECOND_ARTWORK_IMAGE);
        artwork.setIsOnLoan(SECOND_ARTWORK_IS_ON_LOAN);
        artwork.setRoom(room);
        return artwork;
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
}
