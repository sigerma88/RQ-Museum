package ca.mcgill.ecse321.museum.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
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
    artwork.setName("La Joconde");
    artwork.setArtist("Leonardo Da Vinci");
    artwork.setIsAvailableForLoan(true);
    artwork.setLoanFee(110.99);
    artwork.setImage("https://upload.wikimedia.org/wikipedia/commons/thumb/6/6b/La_Joconde.jpg/800px-La_Joconde.jpg");
    artwork.setIsOnLoan(false);
    artworkRepository.save(artwork);

	// Creating a visitor
	Visitor visitor = new Visitor();
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

    @Test
	public void testCreateAndGetLoan() {
		Long id = testCreateLoan();
		testGetPerson(id);
	}

	@Test
	public void testCreateLoan() {
		Artwork artwork = artworkService.getAllArtworks().get(0);

		Visitor visitor = visitorService.getAllVisitors().get(0);

		Loan loan = new Loan();
		loan.setRequestAccepted(null);
		loan.setArtwork(artwork);
		loan.setVisitor(visitor);
		LoanDto loanDto = DtoUtility.convertToDto(loan);
		

		ResponseEntity<LoanDto> response = client.postForEntity("/postLoan", loanDto, LoanDto.class);

		// Check status and body of response are correct
		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals(loan.getRequestAccepted(), response.getBody().getRequestAccepted(), "Response has correct requestAccepted");
		assertEquals(visitor.getMuseumUserId(), response.getBody().getVisitorDto(), "Response has correct visitorDto");
		assertEquals(artwork.getArtworkId(), response.getBody().getArtworkDto(), "Response has correct artworkDto");
		assertTrue(response.getBody().getLoanId() > 0, "Response has valid ID");
	}

	private void testGetLoan(Long LoanId) {
		ResponseEntity<LoanDto> response = client.getForEntity("/loan/" + LoanId, LoanDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
    
	}
}
