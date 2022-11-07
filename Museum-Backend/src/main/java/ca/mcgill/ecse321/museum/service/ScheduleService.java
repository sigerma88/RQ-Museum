package ca.mcgill.ecse321.museum.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;

public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private ScheduleOfTimePeriod scheduleOfTimePeriod;

    
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

    public Schedule editSchedule(long scheduleId) {
        Schedule schedule = scheduleRepository.findScheduleByScheduleId(scheduleId);
        ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
        
        
        return schedule;
    }
    //DELETE
    @Transactional
    public void deleteSchedule(long scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    
}
