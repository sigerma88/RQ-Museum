package ca.mcgill.ecse321.museum.dao;
import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Employee;

/**
 * Author : VZ
 * This is the repository for the Employee class
 */

public interface EmployeeRepository extends CrudRepository<Employee, Long>{

	Employee findEmployeebyEmployeeId(Long employeeId);

}