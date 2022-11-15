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

import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dto.MuseumDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class MuseumIntegrationTests {

    @Autowired
    private TestRestTemplate client;
    @Autowired 
    private MuseumRepository museumRepository;
    @Autowired 
    private ScheduleRepository scheduleRepository;
    
    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        museumRepository.deleteAll();
        scheduleRepository.deleteAll();

    }

    /**
     * Test to successfully create and get a museum
     * 
     * @author VZ
     */
    @Test
    public void testCreateGetAndEditMuseum() {
         Long id = testCreateMuseum();
         testGetMuseum(id);
         testEditMuseum(id);
    }

    public Long testCreateMuseum() {
        ResponseEntity<MuseumDto> response = 
        client.postForEntity("/museum/app?name=Museum&visitFee=10", new MuseumDto() , MuseumDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Museum", response.getBody().getMuseumName(), "Response has correct name");
        assertTrue(response.getBody().getMuseumId() > 0, "Response has valid ID");

        return response.getBody().getMuseumId();
    }

    public void testGetMuseum(Long id) {
        ResponseEntity<MuseumDto> response = 
        client.getForEntity("/museum/" + id, MuseumDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("Museum", response.getBody().getMuseumName(), "Response has correct name");    
        assertEquals(10, response.getBody().getVisitFee(), "Response has correct visit fee");
        assertEquals(id, response.getBody().getMuseumId());
    }

    public void testEditMuseum(Long id) {
        MuseumDto museumDto = new MuseumDto();
        museumDto.setMuseumId(id);
        ResponseEntity<MuseumDto> response = 
        client.postForEntity("/museum/app/edit/"+ id+"/?name=RQ&visitFee=20", museumDto , MuseumDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals("RQ", response.getBody().getMuseumName(), "Response has correct name");
        assertEquals(20, response.getBody().getVisitFee(), "Response has correct visit fee");
        assertEquals(id, response.getBody().getMuseumId());
    }
 
    /**
     * Test to unsuccessfully create an invalid museum without a name
     * 
     * @author VZ
     */
    @Test
    public void testCreateInvalidMuseumWithoutName(){
        ResponseEntity<MuseumDto> response = 
        client.postForEntity("/museum/app?visitFee=10", new MuseumDto() , MuseumDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(null, response.getBody().getMuseumName());
    }

    /**
     * Test to unsuccessfully edit an invalid museum without a name
     * 
     * @author VZ
     */
    @Test
    public void testEditInvalidMuseumWithoutName(){
        Long id = testCreateMuseum();
        MuseumDto museumDto = new MuseumDto();
        museumDto.setMuseumId(id);
        ResponseEntity<MuseumDto> response = 
        client.postForEntity("/museum/app/edit/"+ id+"/?visitFee=20", museumDto , MuseumDto.class);
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        assertEquals(null, response.getBody().getMuseumName());
    }


    



    
}
