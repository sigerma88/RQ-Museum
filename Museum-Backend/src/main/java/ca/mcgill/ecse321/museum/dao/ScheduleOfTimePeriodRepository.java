package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;

/**
 * Crud Repository functionality given by Spring for ScheduleOfTimePeriod Model
 * 
 * @author Kieyan
 * @author Siger
 */
public interface ScheduleOfTimePeriodRepository extends CrudRepository<ScheduleOfTimePeriod, Long> {

  ScheduleOfTimePeriod findScheduleOfTimePeriodByScheduleOfTimePeriodId(Long scheduleOfTimePeriodId);

}
