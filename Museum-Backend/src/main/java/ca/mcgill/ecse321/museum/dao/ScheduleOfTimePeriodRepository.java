package ca.mcgill.ecse321.museum.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;

/**
 * Crud Repository functionality given by Spring for ScheduleOfTimePeriod Model
 * 
 * @author Kieyan
 * @author Siger
 * @author VZ
 */

public interface ScheduleOfTimePeriodRepository extends CrudRepository<ScheduleOfTimePeriod, Long> {

  ScheduleOfTimePeriod findScheduleOfTimePeriodByScheduleOfTimePeriodId(Long scheduleOfTimePeriodId);

  void deleteScheduleOfTimePeriodBySchedule(Schedule schedule);

  List<ScheduleOfTimePeriod> findScheduleOfTimePeriodByTimePeriod(TimePeriod timePeriod);
  
  List<ScheduleOfTimePeriod> findScheduleOfTimePeriodBySchedule(Schedule schedule);

  ScheduleOfTimePeriod findScheduleOfTimePeriodByScheduleAndTimePeriod(Schedule schedule, TimePeriod timePeriod);

  void deleteScheduleOfTimePeriodByScheduleAndTimePeriod(Schedule schedule, TimePeriod timePeriod);
}
