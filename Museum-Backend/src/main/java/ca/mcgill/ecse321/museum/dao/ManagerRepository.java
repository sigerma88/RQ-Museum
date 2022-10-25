package ca.mcgill.ecse321.museum.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Manager;

/**
 * Author : VZ
 * This is the repository for the Manager class
 */
public interface ManagerRepository extends CrudRepository<Manager, Long>{

	Manager findManagerByMuseumUserId(Long museumUserId);

}