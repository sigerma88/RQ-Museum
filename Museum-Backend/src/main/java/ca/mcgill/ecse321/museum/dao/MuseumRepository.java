package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Museum;

/**
 * @Author ZL
 *         Repository for Museum class
 */

public interface MuseumRepository extends CrudRepository<Museum, Long> {

    Museum findMuseumByMuseumId(Long museumId);
}
