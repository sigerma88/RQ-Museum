package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Business logic for scheduleController
 *
 * @author Victor
 */

@Service
public class ScheduleService {
  @Autowired
  private ScheduleRepository scheduleRepository;

  // GET

  /**
   * Method to get a schedule
   *
   * @param scheduleId
   * @return
   * @author VZ
   */

  @Transactional
  public Schedule getSchedule(long scheduleId) {
    return scheduleRepository.findScheduleByScheduleId(scheduleId);
  }

  // CREATE

  /**
   * @return
   * @author VZ
   */

  @Transactional
  public Schedule createSchedule() {
    Schedule schedule = new Schedule();
    scheduleRepository.save(schedule);
    return schedule;

  }

  // DELETE
  @Transactional
  public void deleteSchedule(long scheduleId) {
    scheduleRepository.deleteById(scheduleId);
  }

}
