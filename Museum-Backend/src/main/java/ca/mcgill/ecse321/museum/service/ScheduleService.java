package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Schedule;

/**
 * This is the service class for schedule
 * @author VZ
 * 
 */
@Service
public class ScheduleService {
  @Autowired
  private ScheduleRepository scheduleRepository;

    /**
     * Method to create a schedule
     * @author VZ
     * @return created schedule
     */
    @Transactional
    public Schedule createSchedule() {
        Schedule schedule = new Schedule();
        scheduleRepository.save(schedule);
        return schedule;

    }
    
}
