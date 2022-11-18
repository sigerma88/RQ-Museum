package ca.mcgill.ecse321.museum.dao;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.RoomType;

/**
 * Testing the persistence layer for the ArtworkRepository. Testing the items stored in the database
 * are the same as the items created.
 *
 * @author KL
 */

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

  /**
   * Function that compares the artwork stored in the database with the artwork created.
   *
   * @param artwork - The saved artwork from the repository
   * @param artworkName - The expected name of the artwork
   * @param artist - The expected name of the artist
   * @param isAvailableForLoan - The expected loan availability of the artwork
   * @param loanFee - The expected loan fee of the artwork
   * @param image - The expected url of the image of the artwork
   * @param room - The expected room of the artwork
   * @param isOnLoan - The expected loan status of the artwork
   */
  public void compareUniqueArtwork(Artwork artwork, long artworkId, String artworkName,
      String artist, boolean isAvailableForLoan, double loanFee, String image, Room room,
      boolean isOnLoan) {
    assertNotNull(artwork);
    assertEquals(artworkId, artwork.getArtworkId());
    assertEquals(artworkName, artwork.getName());
    assertEquals(artist, artwork.getArtist());
    assertEquals(isAvailableForLoan, artwork.getIsAvailableForLoan());
    assertEquals(loanFee, artwork.getLoanFee());
    assertEquals(image, artwork.getImage());
    assertEquals(room.getRoomId(), artwork.getRoom().getRoomId());
    assertEquals(isOnLoan, artwork.getIsOnLoan());
  }

  /**
   * Function that compares if an artwork is stored correctly in the database.
   */
  @Test
  public void testPersistenceWithOneArtwork() {

    // Expected values for the artwork
    String artworkName = "The Art";
    String artist = "Kevin";
    boolean isAvailableForLoan = true;
    boolean isOnLoan = false;
    double loanFee = 12.5;
    String image = "https://source.unsplash.com/C54OKB99iuw";
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

    // Creating an artwork containing the expected values
    Artwork artwork = new Artwork();
    artwork.setName(artworkName);
    artwork.setArtist(artist);
    artwork.setIsAvailableForLoan(isAvailableForLoan);
    artwork.setIsOnLoan(isOnLoan);
    artwork.setLoanFee(loanFee);
    artwork.setImage(image);
    artwork.setRoom(room);
    Artwork savedArtwork = artworkRepository.save(artwork);
    long artworkId = artwork.getArtworkId();

    // Comparing the artwork stored in the database with the artwork created

    // Test by retrieving with the artwork id
    artwork = null;
    artwork = artworkRepository.findArtworkByArtworkId(savedArtwork.getArtworkId());
    compareUniqueArtwork(artwork, artworkId, artworkName, artist, isAvailableForLoan, loanFee,
        image, room, isOnLoan);

    // Test by retrieving with the artwork name
    artwork = null;
    artwork = artworkRepository.findArtworkByName(savedArtwork.getName()).get(0);
    compareUniqueArtwork(artwork, artworkId, artworkName, artist, isAvailableForLoan, loanFee,
        image, room, isOnLoan);

    // Test by retrieving by its artist
    artwork = null;
    artwork = artworkRepository.findArtworkByArtist(savedArtwork.getArtist()).get(0);
    compareUniqueArtwork(artwork, artworkId, artworkName, artist, isAvailableForLoan, loanFee,
        image, room, isOnLoan);
  }

  /**
   * Function that compares if the repository retrieves all artworks that share the same artwork
   * name or artist.
   */
  @Test
  public void testPersistenceWithMultipleArtwork() {
    // Expected values for the artwork
    String artworkName = "The Art";
    String artist = "Kevin";
    boolean isAvailableForLoan = true;
    boolean isOnLoan = false;
    double loanFee = 12.5;
    String image = "https://source.unsplash.com/C54OKB99iuw";

    // Creating first artwork
    Artwork artwork = new Artwork();
    artwork.setName(artworkName);
    artwork.setArtist(artist);
    artwork.setIsAvailableForLoan(isAvailableForLoan);
    artwork.setIsOnLoan(isOnLoan);
    artwork.setLoanFee(loanFee);
    artwork.setImage(image);

    // Creating second artwork with same property as first artwork
    Artwork commonPropertyArtwork = new Artwork();
    commonPropertyArtwork.setName(artworkName);
    commonPropertyArtwork.setArtist(artist);
    commonPropertyArtwork.setIsAvailableForLoan(isAvailableForLoan);
    commonPropertyArtwork.setIsOnLoan(isOnLoan);
    commonPropertyArtwork.setLoanFee(loanFee);
    commonPropertyArtwork.setImage(image);

    // Check if retrieved artwork have same property (Artist) as created artwork
    List<Artwork> artworks = artworkRepository.findArtworkByArtist(artist);
    for (Artwork artistArtwork : artworks) {
      assertEquals(artistArtwork.getArtist(), artist);
    }

    // Check if retrieved artwork have same property (Artwork name) as created artwork
    artworks = artworkRepository.findArtworkByName(artworkName);
    for (Artwork titleArtwork : artworks) {
      assertEquals(titleArtwork.getName(), artworkName);
    }
  }
}
