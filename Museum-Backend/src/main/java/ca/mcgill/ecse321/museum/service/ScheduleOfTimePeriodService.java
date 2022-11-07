package ca.mcgill.ecse321.museum.service;

import org.springframework.beans.factory.annotation.Autowired;

import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;

public class ScheduleOfTimePeriodService {
    
    @Autowired
    private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;
    
}
