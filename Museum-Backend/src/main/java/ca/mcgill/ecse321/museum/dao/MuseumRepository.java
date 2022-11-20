package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Museum;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for Museum class
 *
 * @author Zahra
 */

public interface MuseumRepository extends CrudRepository<Museum, Long> {

  Museum findMuseumByMuseumId(Long museumId);

}
