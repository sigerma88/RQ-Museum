package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * This is the repository for the Employee class
 *
 * @author Victor
 * @author Siger
 */

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

  Employee findEmployeeByMuseumUserId(Long museumUserId);

  Employee findEmployeeByEmail(String email);

  Employee findEmployeeByName(String name);

  void deleteEmployeeByMuseumUserId(Long museumUserId);

  Object findVisitorByName(String name);
}
