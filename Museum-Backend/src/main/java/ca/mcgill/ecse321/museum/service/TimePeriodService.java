package ca.mcgill.ecse321.museum.service;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.TimePeriod;

@Service
public class TimePeriodService {
    @Autowired
    private TimePeriodRepository timePeriodRepository;
    
    //CREATE 
    /**
     * Create a TimePeriod and save to database
     * @author VZ
     * @param startDate
     * @param endDate
     * @return
     */
    @Transactional
    public TimePeriod createTimePeriod(Timestamp startDate, Timestamp endDate) {

        //input validation
        if(startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if(startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        //create TimePeriod
        TimePeriod timePeriod = new TimePeriod();

        timePeriod.setStartDate(startDate);
        timePeriod.setEndDate(endDate);
        timePeriodRepository.save(timePeriod);
        return timePeriod;
    }

    //DELETE 

    /**
     * Delete a TimePeriod from database by ID
     * @author VZ
     * @param timePeriodId
     */
    @Transactional
    public void deleteTimePeriod(long timePeriodId) {

        timePeriodRepository.deleteById(timePeriodId);
    }

    //EDIT
    /**
     * Edit a TimePeriod by ID and save to database
     * @param timePeriodId
     * @param startDate
     * @param endDate
     * @return
     */
    @Transactional
    public TimePeriod editTimePeriod(long timePeriodId, Timestamp startDate, Timestamp endDate) {
        //input validation
        if(startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }
        if(startDate.after(endDate)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
        //find TimePeriod
        TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
        if(timePeriod == null) {
            throw new IllegalArgumentException("TimePeriod does not exist");
        }
        //edit TimePeriod
        timePeriod.setStartDate(startDate);
        timePeriod.setEndDate(endDate);
        timePeriodRepository.save(timePeriod);
        return timePeriod;
    }

}
