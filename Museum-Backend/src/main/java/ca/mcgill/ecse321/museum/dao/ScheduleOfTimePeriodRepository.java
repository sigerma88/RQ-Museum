package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.Schedule;

/**
 * Crud Repository functionality given by Spring for ScheduleOfTimePeriod Model
 * 
 * @author Kieyan
 * @author Siger
 */
public interface ScheduleOfTimePeriodRepository extends CrudRepository<ScheduleOfTimePeriod, Long> {

  ScheduleOfTimePeriod findScheduleOfTimePeriodByScheduleOfTimePeriodId(Long scheduleOfTimePeriodId);

  ScheduleOfTimePeriod findScheduleOfTimePeriodBySchedule(Schedule schedule);

  void deleteScheduleOfTimePeriodBySchedule(Schedule schedule);

}
