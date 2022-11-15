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
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;

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

  private static final Long FIRST_VISITOR_ID = 0L;
  private static final String FIRST_VISITOR_NAME = "Visitor Name";
  private static final String FIRST_VISITOR_EMAIL = "Visitor Email";
  private static final String FIRST_VISITOR_PASSWORD = "Visitor Password";

  private static final Long FIRST_LOAN_ID = 0L;
  private static final Boolean FIRST_LOAN_REQUEST_ACCEPTED = false;

  private static final Double SECOND_LOAN_FEE = 100.0;

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
      } else {
        return null;
      }
    });

    lenient().when(loanRepository.findLoanByArtwork(any(Artwork.class))).thenAnswer((InvocationOnMock invocation) -> {
      Artwork artwork = (Artwork) invocation.getArgument(0);
      if (artwork.getArtworkId().equals(FIRST_ARTWORK_ID)) {
        // Create a loan stub
        Loan loan = createLoanStub(artwork);
        return loan;
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
