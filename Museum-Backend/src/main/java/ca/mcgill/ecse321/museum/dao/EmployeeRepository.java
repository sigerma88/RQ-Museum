package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Employee Model
 */
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
}
