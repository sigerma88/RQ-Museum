package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Manager;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Manager Model
 */
public interface ManagerRepository extends CrudRepository<Manager, Long> {
}
