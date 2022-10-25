package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.TimePeriod;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for TimePeriod Model
 */
public interface TimePeriodRepository extends CrudRepository<TimePeriod, Long> {
}
