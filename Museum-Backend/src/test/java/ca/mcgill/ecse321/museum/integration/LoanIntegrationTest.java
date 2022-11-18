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

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.LoanDto;


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
	
	private static final long LOAN_ID = 1;
    private static final Boolean LOAN_REQUESTACCEPTED = null;

    private static final long SECOND_LOAN_ID = 2;
    private static final Boolean SECOND_LOAN_REQUESTACCEPTED = null;

    // Loan that should fail because associations with bad artwork or visitor
    private static final long THIRD_LOAN_ID = 3;
    private static final boolean THIRD_LOAN_REQUESTACCEPTED = true; 

    private static final long NONE_EXISTING_LOAN_ID = 4;

    private static final long VISITOR_ID = 1;
    private static final String VISITOR_EMAIL = "IAMAVISITOR@email.com";
    private static final String VISITOR_NAME = "Steve";
    private static final String VISITOR_PASSWORD = "Password123";

    private static final long NONE_EXISTING_VISITOR_ID = 2;

    // Artwork that can't be loaned
    private static final long ARTWORK_ID =1;
    private static final String ARTWORK_NAME = "George";
    private static final String ARTWORK_ARTIST = "Curious";
    private static final boolean ARTWORK_ISAVAILABLEFORLOAN = false;
    private static final boolean ARTWORK_ISONLOAN = false; 
    private static final double ARTWORK_LOANFEE = 54.2;
    private static final String ARTWORK_IMAGE = "bruh";

    // Artwork that can be loaned and isn't on loan
    private static final long SECOND_ARTWORK_ID =2;
    private static final String SECOND_ARTWORK_NAME = "HELLO";
    private static final String SECOND_ARTWORK_ARTIST = "WORLD";
    private static final boolean SECOND_ARTWORK_ISAVAILABLEFORLOAN = true;
    private static final boolean SECOND_ARTWORK_ISONLOAN = false; 
    private static final double SECOND_ARTWORK_LOANFEE = 63.5;
    private static final String SECOND_ARTWORK_IMAGE = "bruuuuh";

    // Artwork that can be loaned but is on loan
    private static final long THIRD_ARTWORK_ID =3;
    private static final String THIRD_ARTWORK_NAME = "dna";
    private static final String THIRD_ARTWORK_ARTIST = "code";
    private static final boolean THIRD_ARTWORK_ISAVAILABLEFORLOAN = true;
    private static final boolean THIRD_ARTWORK_ISONLOAN = true; 
    private static final double THIRD_ARTWORK_LOANFEE = 69.69;
    private static final String THIRD_ARTWORK_IMAGE = "bruuuuuuuuuuuuuuuuuh";

    private static final long NONE_EXISTING_ARTWORK_ID = 4;
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loanRepository.deleteAll();
        artworkRepository.deleteAll();
        visitorRepository.deleteAll();
    }
    @Test
	public void testCreateAndGetLoan() {
		int id = testCreateLoan();
		testGetPerson(id);
	}

	private int testCreateLoan() {
		

		ResponseEntity<LoanDto> response = client.postForEntity("/postLoan", new LoanDto(), LoanDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals("Obi-Wan Kenobi", response.getBody().getName(), "Response has correct name");
		assertTrue(response.getBody().getId() > 0, "Response has valid ID");

		return response.getBody().getId();
	}

	private void testGetPerson(int id) {
		ResponseEntity<PersonDto> response = client.getForEntity("/person/" + id, PersonDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.OK, response.getStatusCode(), "Response has correct status");
		assertNotNull(response.getBody(), "Response has body");
		assertEquals("Obi-Wan Kenobi", response.getBody().getName(), "Response has correct name");
    
}
