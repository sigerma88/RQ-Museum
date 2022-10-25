package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.MuseumSystem;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for MuseumSystem Model
 * 
 * @author Kieyan
 * @author Siger
 */
public interface MuseumSystemRepository extends CrudRepository<MuseumSystem, Long> {

  MuseumSystem findMuseumSystemByMuseumSystemId(Long museumSystemId);

}
