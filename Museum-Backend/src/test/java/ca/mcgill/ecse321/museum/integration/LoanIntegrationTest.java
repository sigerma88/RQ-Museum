package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import ca.mcgill.ecse321.museum.service.VisitorService;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

public class LoanIntegrationTest {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private ArtworkRepository artworkRepository;
    @Autowired
    private VisitorRepository visitorRepository;
	@Autowired
	private ScheduleRepository scheduleRepository;
	@Autowired
	private MuseumRepository museumRepository;
	@Autowired
	private RoomRepository roomRepository;

	@Autowired
	private ArtworkService artworkService;

	@Autowired 
	private VisitorService visitorService;

    @BeforeEach
	public void setup() {
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

    // Creating an artwork
    Artwork artwork = new Artwork();
	Long artworkId = (long) 1;
	artwork.setArtworkId(artworkId);
    artwork.setName("La Joconde");
    artwork.setArtist("Leonardo Da Vinci");
    artwork.setIsAvailableForLoan(true);
    artwork.setLoanFee(110.99);
    artwork.setImage("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/La_Joconde.jpg/800px-La_Joconde.jpg");
    artwork.setIsOnLoan(false);
	artwork.setRoom(room);
    artworkRepository.save(artwork);

	// Creating a visitor
	Visitor visitor = new Visitor();
	Long visitorId = (long) 1;
	visitor.setMuseumUserId(visitorId);
	visitor.setEmail("Please@email.com");
	visitor.setName("Please");
	visitor.setPassword("password");
	visitorRepository.save(visitor);
	}

	@AfterEach
    public void clearDatabase() {
        loanRepository.deleteAll();
        artworkRepository.deleteAll();
        visitorRepository.deleteAll();
    }

	/**
	 * Test suite that combines successfully creating a loan and getting that same loan
	 * @author Eric
	 */
    @Test
	public void testCreateAndGetLoan() {
		Long id = testCreateLoan();
		testGetLoan(id);
	}

	
	/**
	 * Test to create a loan successfully
	 * @author Eric
	 */
	private Long testCreateLoan() {

		Artwork artwork = artworkRepository.findArtworkByName("La Joconde").get(0);
		Visitor visitor = visitorRepository.findVisitorByName("Please");

		Loan loan = new Loan();
		loan.setRequestAccepted(null);
		loan.setArtwork(artwork);
		loan.setVisitor(visitor);
		LoanDto loanDto = DtoUtility.convertToDto(loan);

		ResponseEntity<LoanDto> response = client.postForEntity("/postLoan/", loanDto, LoanDto.class);

		// Check status and body of response are correct
		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(null, response.getBody().getRequestAccepted(), "Response has correct requestAccepted");
		assertEquals(visitor.getMuseumUserId(), response.getBody().getVisitorDto().getUserId(), "Response has correct visitorDto");
		assertEquals(artwork.getArtworkId(), response.getBody().getArtworkDto().getArtworkId(), "Response has correct artworkDto");
		assertTrue(response.getBody().getLoanId() > 0, "Response has valid ID");

		return response.getBody().getLoanId();
	}

	/**
	 * Test to get loan successfully
	 * @author Eric
	 */
	private void testGetLoan(Long LoanId) {
		ResponseEntity<LoanDto> response = client.getForEntity("/loan/" + LoanId, LoanDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
	}

	/**
	 * Test to create a loan unsuccessfully due to duplicate loan
	 */
	@Test
	public void testCreateLoanDuplicate() {

		Artwork artwork = artworkRepository.findArtworkByName("La Joconde").get(0);
		Visitor visitor = visitorRepository.findVisitorByName("Please");

		Loan loan = new Loan();
		loan.setRequestAccepted(null);
		loan.setArtwork(artwork);
		loan.setVisitor(visitor);
		loanRepository.save(loan);

		Loan loan2 = new Loan();
		loan2.setRequestAccepted(null);
		loan2.setArtwork(artwork);
		loan2.setVisitor(visitor);
		LoanDto loanDto = DtoUtility.convertToDto(loan);

		ResponseEntity<String> response = client.postForEntity("/postLoan/", loanDto, String.class);

		assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
		assertEquals("Cannot create a duplicate loan request", response.getBody());
	}

	/**
	 * Test to patch existing loan to requestAccepted false
	 * @author Eric
	 */
    @Test
	public void testPatchLoanSuccessfullyToFalse() {
		Artwork artwork = artworkRepository.findArtworkByName("La Joconde").get(0);
		Visitor visitor = visitorRepository.findVisitorByName("Please");

		Loan loan = new Loan();
		Boolean aRequestAccepted = false;
		loan.setRequestAccepted(aRequestAccepted);
		loan.setArtwork(artwork);
		loan.setVisitor(visitor);
		loanRepository.save(loan);
		LoanDto loanDto = DtoUtility.convertToDto(loan);

		
		ResponseEntity<LoanDto> response = client.postForEntity("/patchLoan/", loanDto, LoanDto.class);

		// Check status and body of response are correct
		assertNotNull(response);
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(false, response.getBody().getRequestAccepted(), "Response has correct requestAccepted");
		assertEquals(visitor.getMuseumUserId(), response.getBody().getVisitorDto().getUserId(), "Response has correct visitorDto");
		assertEquals(artwork.getArtworkId(), response.getBody().getArtworkDto().getArtworkId(), "Response has correct artworkDto");
		assertTrue(response.getBody().getLoanId() > 0, "Response has valid ID");

	}

	/**
	 * Test suite that combines successfully creating a loan and patching that loan requestAccepted to false
	 * @author Eric
	 */
    @Test
	public void testPatchLoanSuccessfullyToTrue() {
		Artwork artwork = artworkRepository.findArtworkByName("La Joconde").get(0);
		Visitor visitor = visitorRepository.findVisitorByName("Please");

		Loan loan = new Loan();
		loan.setRequestAccepted(true);
		loan.setArtwork(artwork);
		loan.setVisitor(visitor);
		LoanDto loanDto = DtoUtility.convertToDto(loan);

		
		ResponseEntity<LoanDto> response = client.postForEntity("/patchLoan/", loanDto, LoanDto.class);

		// Check status and body of response are correct
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(true, response.getBody().getRequestAccepted(), "Response has correct requestAccepted");
		assertEquals(visitor.getMuseumUserId(), response.getBody().getVisitorDto().getUserId(), "Response has correct visitorDto");
		assertEquals(artwork.getArtworkId(), response.getBody().getArtworkDto().getArtworkId(), "Response has correct artworkDto");
		assertTrue(response.getBody().getLoanId() > 0, "Response has valid ID");

	}

}
