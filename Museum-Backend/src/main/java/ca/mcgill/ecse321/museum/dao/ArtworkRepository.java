package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Artwork;

/**
 * @Author ZL
 *         Repository for Artwork class
 */
public interface ArtworkRepository extends CrudRepository<Artwork, Long> {

    Artwork findArtworkByArtworkId(Long artworkId);

}
