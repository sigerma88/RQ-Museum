package ca.mcgill.ecse321.museum.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Visitor;
/**
 * Author : VZ
 * This is the repository for the Visitor class
 */
public interface VisitorRepository extends CrudRepository<Visitor, Long>{

	Visitor findVisitorByVisitorId(Long visitorId);

}