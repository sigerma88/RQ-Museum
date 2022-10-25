package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Museum;

/**
 * Repository for Museum class
 * 
 * @author Zahra
 */

public interface MuseumRepository extends CrudRepository<Museum, Long> {

  Museum findMuseumByMuseumId(Long museumId);

}
