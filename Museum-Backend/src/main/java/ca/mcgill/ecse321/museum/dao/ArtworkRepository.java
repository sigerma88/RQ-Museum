package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Artwork;

import java.util.List;

/**
 * Repository for Artwork class
 * 
 * @author Zahra
 */
public interface ArtworkRepository extends CrudRepository<Artwork, Long> {

  Artwork findArtworkByArtworkId(Long artworkId);
  List<Artwork> findArtworkByName(String name);
  List<Artwork> findArtworkByArtist(String artist);



}
