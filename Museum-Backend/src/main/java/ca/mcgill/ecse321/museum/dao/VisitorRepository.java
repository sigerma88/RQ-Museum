package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Visitor;

/**
 * This is the repository for the Visitor class
 * 
 * @author Victor
 */
public interface VisitorRepository extends CrudRepository<Visitor, Long> {

  Visitor findVisitorByMuseumUserId(Long museumUserId);

  Visitor findVisitorByEmail(String email);

  Visitor findVisitorByName(String name);
}
