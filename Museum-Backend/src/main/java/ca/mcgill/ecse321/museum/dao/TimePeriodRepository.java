package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.TimePeriod;

/**
 * Crud Repository functionality given by Spring for TimePeriod Model
 * 
 * @author Kieyan
 * @author Siger
 */

public interface TimePeriodRepository extends CrudRepository<TimePeriod, Long> {

  TimePeriod findTimePeriodByTimePeriodId(Long timePeriodId);

}
