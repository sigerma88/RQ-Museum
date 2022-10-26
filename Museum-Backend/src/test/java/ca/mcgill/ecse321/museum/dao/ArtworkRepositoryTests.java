package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ArtworkRepositoryTests {
    @Autowired
    private ArtworkRepository artworkRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MuseumRepository museumRepository;


    @AfterEach
    public void clearDatabase() {
        artworkRepository.deleteAll();
        roomRepository.deleteAll();
        museumRepository.deleteAll();
    }

    public void compareUniqueArtwork(Artwork artwork, String artworkName, String artist,
                                     boolean isAvailableForLoan, double loanFee, String image, Room room) {
        assertNotNull(artwork);
        assertEquals(artworkName, artwork.getName());
        assertEquals(artist, artwork.getArtist());
        assertEquals(isAvailableForLoan, artwork.getIsAvailableForLoan());
        assertEquals(loanFee, artwork.getLoanFee());
        assertEquals(image, artwork.getImage());
        assertEquals(room.getRoomId(), artwork.getRoom().getRoomId());
    }

    @Test
    public void testPersistenceWithOneArtwork() {

        Schedule schedule = new Schedule();

        Museum museum = new Museum();
        museum.setName("Rougon-Macquart");
        museum.setVisitFee(12.5);
        museum.setSchedule(schedule);

        museumRepository.save(museum);

        Room room = new Room();
        room.setRoomName("Room 1");
        room.setRoomType(RoomType.Small);
        room.setCurrentNumberOfArtwork(0);
        room.setMuseum(museum);
        roomRepository.save(room);

        String artworkName = "The Art";
        String artist = "Kevin";
        boolean isAvailableForLoan = true;
        double loanFee = 12.5;
        String image = "https://source.unsplash.com/C54OKB99iuw";

        Artwork artwork = new Artwork();
        artwork.setName(artworkName);
        artwork.setArtist(artist);
        artwork.setIsAvailableForLoan(isAvailableForLoan);
        artwork.setLoanFee(loanFee);
        artwork.setImage(image);
        artwork.setRoom(room);

        // Searching when artworks have distinct properties

        Artwork savedArtwork = artworkRepository.save(artwork);

        artwork = null;
        artwork = artworkRepository.findArtworkByArtworkId(savedArtwork.getArtworkId());

        artwork = null;
        artwork = artworkRepository.findArtworkByName(savedArtwork.getName()).get(0);
        compareUniqueArtwork(artwork, artworkName, artist, isAvailableForLoan, loanFee, image,
                room);

        artwork = null;
        artwork = artworkRepository.findArtworkByArtist(savedArtwork.getArtist()).get(0);
        compareUniqueArtwork(artwork, artworkName, artist, isAvailableForLoan, loanFee, image,
                room);
    }

    @Test
    public void testPersistenceWithMultipleArtwork() {

        String artworkName = "The Art";
        String artist = "Kevin";
        boolean isAvailableForLoan = true;
        double loanFee = 12.5;
        String image = "https://source.unsplash.com/C54OKB99iuw";

        Artwork artwork = new Artwork();
        artwork.setName(artworkName);
        artwork.setArtist(artist);
        artwork.setIsAvailableForLoan(isAvailableForLoan);
        artwork.setLoanFee(loanFee);
        artwork.setImage(image);

        Artwork commonPropertyArtwork = new Artwork();
        commonPropertyArtwork.setName(artworkName);
        commonPropertyArtwork.setArtist(artist);
        commonPropertyArtwork.setIsAvailableForLoan(isAvailableForLoan);
        commonPropertyArtwork.setLoanFee(loanFee);
        commonPropertyArtwork.setImage(image);

        List<Artwork> artworks = artworkRepository.findArtworkByArtist(artist);
        for (Artwork artistArtwork : artworks) {
            assertEquals(artistArtwork.getArtist(), artist);
        }

        artworks = artworkRepository.findArtworkByName(artworkName);
        for (Artwork titleArtwork : artworks) {
            assertEquals(titleArtwork.getName(), artworkName);
        }
    }
}
