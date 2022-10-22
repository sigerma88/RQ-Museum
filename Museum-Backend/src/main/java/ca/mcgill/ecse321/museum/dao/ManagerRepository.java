package ca.mcgill.ecse321.museum.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Manager;

public interface ManagerRepository extends CrudRepository<Manager, Long>{

	Manager findTicketByManagerId(Long managerId);

}