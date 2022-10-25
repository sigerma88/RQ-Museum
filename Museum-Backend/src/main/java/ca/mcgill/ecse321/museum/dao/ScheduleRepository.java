package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Schedule;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Schedule Model
 * 
 * @author Kieyan
 * @author Siger
 */
public interface ScheduleRepository extends CrudRepository<Schedule, Long> {

  Schedule findScheduleByScheduleId(Long scheduleId);

}
