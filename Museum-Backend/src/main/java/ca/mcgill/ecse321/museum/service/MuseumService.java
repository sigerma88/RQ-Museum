package ca.mcgill.ecse321.museum.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;

@Service
public class MuseumService {

    @Autowired
    private MuseumRepository museumRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired 
    
    /**
     * Method to view a museum
     * 
     * @author VZ
     * @param museumId - museum id
     * @return museum
     */
    @Transactional
    public Museum getMuseum(long museumId) {
        return museumRepository.findMuseumByMuseumId(museumId);
    }

    /**
     * Method to create a museum
     * 
     * @author VZ
     * @return
     */
    @Transactional
    public Museum createMuseum(String name, double visitFee, Schedule schedule){
        if(name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Museum name cannot be empty!");
        }
        if(visitFee < 0) {
            throw new IllegalArgumentException("Visit fee cannot be negative!");
        }
        if(schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null!");
        }

        Museum museum = new Museum();
        museum.setName(name);
        museum.setVisitFee(visitFee);
        museum.setSchedule(schedule);
        museumRepository.save(museum);
        return museum;
    }
    
    /**
     * Method to edit a museum's name, visit fee or schedule given the museum's id.
     * @param museumId
     * @param name
     * @param visitFee
     * @param schedule
     * @return
     */
    @Transactional
    public Museum editMuseum(long museumId, String name, double visitFee, Schedule schedule){
        if(name == null || name.trim().length() == 0) {
            throw new IllegalArgumentException("Museum name cannot be empty!");
        }
        if(visitFee < 0) {
            throw new IllegalArgumentException("Visit fee cannot be negative!");
        }
        if(schedule == null) {
            throw new IllegalArgumentException("Schedule cannot be null!");
        }

        Museum museum = museumRepository.findMuseumByMuseumId(museumId);
        museum.setName(name);
        museum.setVisitFee(visitFee);
        museum.setSchedule(schedule);
        museumRepository.save(museum);
        return museum;
    }


    public Schedule getMuseumSchedule(long museumId) {
        Museum museum = museumRepository.findMuseumByMuseumId(museumId);
        if (museum == null) {
            throw new IllegalArgumentException("There is no such museum");
        }
        return museum.getSchedule();
    }

    
}
