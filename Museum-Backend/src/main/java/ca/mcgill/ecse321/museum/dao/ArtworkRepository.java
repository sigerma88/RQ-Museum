package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Artwork;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Artwork Model
 */
public interface ArtworkRepository extends CrudRepository<Artwork, Long> {

}
