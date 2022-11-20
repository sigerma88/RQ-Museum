package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for Artwork class
 *
 * @author Zahra
 * @author Kevin
 * @author Siger
 */

public interface ArtworkRepository extends CrudRepository<Artwork, Long> {

  Artwork findArtworkByArtworkId(Long artworkId);

  List<Artwork> findArtworkByName(String name);

  List<Artwork> findArtworkByArtist(String artist);

  List<Artwork> findArtworkByRoom(Room room);

  List<Artwork> findArtworkByIsAvailableForLoan(Boolean isAvailableForLoan);

  void deleteArtworkByArtworkId(Long artworkId);
}
