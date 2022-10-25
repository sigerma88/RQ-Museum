package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for ScheduleOfTimePeriod Model
 * 
 * @author Kieyan
 * @author Siger
 */
public interface ScheduleOfTimePeriodRepository extends CrudRepository<ScheduleOfTimePeriod, Long> {

  ScheduleOfTimePeriod findScheduleOfTimePeriodByScheduleOfTimePeriodId(Long scheduleOfTimePeriodId);

}
