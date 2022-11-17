package ca.mcgill.ecse321.museum.integration;

import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomIntegrationTests {

    @Autowired
    private TestRestTemplate client;

    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MuseumRepository museumRepository;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        artworkRepository.deleteAll();
        roomRepository.deleteAll();
        museumRepository.deleteAll();
        scheduleRepository.deleteAll();

    }

    @Test
    public void testGetRoomCapacity() {
        List<ArtworkDto> artworkDtoList = createArtworkDtos();
        Long roomId1 = artworkDtoList.get(0).getRoom().getRoomId(); // Small room
        Long roomId2 = artworkDtoList.get(1).getRoom().getRoomId(); // Large room
        Long roomId3 = artworkDtoList.get(2).getRoom().getRoomId(); // Storage room

        // Get the capacity of room 1 using get request -- SMALL ROOM
        ResponseEntity<Integer> response = client.getForEntity("/getRoomCapacity/" + roomId1.toString(), Integer.class);
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody(), "Response has body");
        // There is one artwork in a small room so the capacity should be 199
        assertEquals(199, response.getBody(), "Response correctly said that the capacity is 199");

        // Get the capacity of room 2 using get request -- LARGE ROOM
        ResponseEntity<Integer> response2 = client.getForEntity("/getRoomCapacity/" + roomId2.toString(), Integer.class);
        assertNotNull(response2);
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertNotNull(response2.getBody(), "Response has body");
        // There is one artwork in a large room so the capacity should be 299
        assertEquals(299, response2.getBody(), "Response correctly said that the capacity is 299");

        // Get the capacity of room 3 using get request -- STORAGE ROOM
        ResponseEntity<Integer> response3 = client.getForEntity("/getRoomCapacity/" + roomId3.toString(), Integer.class);
        assertNotNull(response3);
        assertEquals(HttpStatus.OK, response3.getStatusCode());
        assertNotNull(response3.getBody(), "Response has body");
        // There is one artwork in a storage room so the capacity should be very large -> 99998
        assertTrue(90000 < response3.getBody(), "Response correctly said that the capacity is arbitrarily large");
    }


    public List<ArtworkDto> createArtworkDtos(){
        List<ArtworkDto> artworkDtos = new ArrayList<ArtworkDto>();

        // Expected values for the artwork 1
        String artworkName = "The Art";
        String artist = "Kian";
        boolean isAvailableForLoan = false;
        boolean isOnLoan = true;
        double loanFee = 12.5;
        String image = "https://source.unsplash.com/C54OKB99iuw";

        // Expected values for the artwork 2
        String artworkName2 = "The Art2";
        String artist2 = "Bob";
        boolean isAvailableForLoan2 = true;
        boolean isOnLoan2 = false;
        double loanFee2 = 1000000;
        String image2 = "https://source.unsplash.com/C54OKB9922iuw";

        // Expected values for the artwork 3
        String artworkName3 = "The Art3";
        String artist3 = "Billy";
        boolean isAvailableForLoan3 = false;
        boolean isOnLoan3 = false;
        double loanFee3 = 999999999;
        String image3 = "https://source.unsplash.com/C54OKB9922iuw";

        Schedule schedule = new Schedule();

        // Creating a museum
        Museum museum = new Museum();
        museum.setName("Rougon-Macquart");
        museum.setVisitFee(12.5);
        museum.setSchedule(schedule);
        museumRepository.save(museum);

        // Creating room 1 - SMALL ROOM
        Room room = new Room();
        room.setRoomName("Room 1");
        room.setRoomType(RoomType.Small);
        room.setCurrentNumberOfArtwork(1);
        room.setMuseum(museum);
        roomRepository.save(room);

        // Creating room 2 - LARGE ROOM
        Room room2 = new Room();
        room2.setRoomName("Room 2");
        room2.setRoomType(RoomType.Large);
        room2.setCurrentNumberOfArtwork(1);
        room2.setMuseum(museum);
        roomRepository.save(room2);

        // Creating room 3 - STORAGE ROOM
        Room room3 = new Room();
        room3.setRoomName("Room 3");
        room3.setRoomType(RoomType.Storage);
        room3.setCurrentNumberOfArtwork(1);
        room3.setMuseum(museum);
        roomRepository.save(room3);

        // Initialize artwork 1
        Artwork artwork = new Artwork();
        artwork.setName(artworkName);
        artwork.setArtist(artist);
        artwork.setIsAvailableForLoan(isAvailableForLoan);
        artwork.setIsOnLoan(isOnLoan);
        artwork.setLoanFee(loanFee);
        artwork.setImage(image);
        artwork.setRoom(room);
        artworkRepository.save(artwork);

        // Initialize artwork 2
        Artwork artwork2 = new Artwork();
        artwork2.setName(artworkName2);
        artwork2.setArtist(artist2);
        artwork2.setIsAvailableForLoan(isAvailableForLoan2);
        artwork2.setIsOnLoan(isOnLoan2);
        artwork2.setLoanFee(loanFee2);
        artwork2.setImage(image2);
        artwork2.setRoom(room2);
        artworkRepository.save(artwork2);

        // Initialize artwork 3
        Artwork artwork3 = new Artwork();
        artwork3.setName(artworkName3);
        artwork3.setArtist(artist3);
        artwork3.setIsAvailableForLoan(isAvailableForLoan3);
        artwork3.setIsOnLoan(isOnLoan3);
        artwork3.setLoanFee(loanFee3);
        artwork3.setImage(image3);
        artwork3.setRoom(room3);
        artworkRepository.save(artwork3);

        ArtworkDto artworkDto1 = DtoUtility.convertToDto(artwork);
        ArtworkDto artworkDto2 = DtoUtility.convertToDto(artwork2);
        ArtworkDto artworkDto3 = DtoUtility.convertToDto(artwork3);
        artworkDtos.add(artworkDto1);
        artworkDtos.add(artworkDto2);
        artworkDtos.add(artworkDto3);

        return artworkDtos;
    }
}
