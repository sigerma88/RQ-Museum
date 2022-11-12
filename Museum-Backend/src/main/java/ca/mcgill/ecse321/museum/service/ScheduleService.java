package ca.mcgill.ecse321.museum.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Schedule;


public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    //GET

    /**
     * Method to get a schedule
     * @author VZ
     * @param scheduleId
     * @return
     */
    @Transactional
    public Schedule getSchedule(long scheduleId) {
        return scheduleRepository.findScheduleByScheduleId(scheduleId);
    }

    //CREATE
    /**
     * @author VZ
     * @return
     */
    @Transactional
    public Schedule createSchedule() {
        Schedule schedule = new Schedule();
        scheduleRepository.save(schedule);
        return schedule;

    }

    //DELETE
    @Transactional
    public void deleteSchedule(long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    
}
