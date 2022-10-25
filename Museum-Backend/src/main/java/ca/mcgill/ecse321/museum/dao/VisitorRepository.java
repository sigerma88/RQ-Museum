package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Visitor;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Visitor Model
 */
public interface VisitorRepository extends CrudRepository<Visitor, Long> {
}
