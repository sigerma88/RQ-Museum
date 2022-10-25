package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.MuseumSystem;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for MuseumSystem Model
 */
public interface MuseumSystemRepository extends CrudRepository<MuseumSystem, Long> {

}
