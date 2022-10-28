package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Employee;

/**
 * This is the repository for the Employee class
 * 
 * @author Victor
 */

public interface EmployeeRepository extends CrudRepository<Employee, Long>{

  Employee findEmployeeByMuseumUserId(Long museumUserId);

}