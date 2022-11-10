package ca.mcgill.ecse321.museum.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Artwork;

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

  void deleteArtworkByArtworkId(Long artworkId);

}
