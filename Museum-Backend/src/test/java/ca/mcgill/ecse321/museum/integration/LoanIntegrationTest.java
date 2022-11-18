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

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        loanRepository.deleteAll();
        artworkRepository.deleteAll();
        visitorRepository.deleteAll();
    }
    @Test
	public void testCreateAndGetLoan() {
		int id = testCreatePerson();
		testGetPerson(id);
	}

	private int testCreatePerson() {
		ResponseEntity<PersonDto> response = client.postForEntity("/person", new PersonDto("Obi-Wan Kenobi"), PersonDto.class);

		assertNotNull(response);
		assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response has correct status");
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
