package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;

/**
 * Service class for Museum
 * @author
 */
@Service
public class MuseumService {

  @Autowired
  private MuseumRepository museumRepository;
  @Autowired
  private ScheduleRepository scheduleRepository;
  @Autowired
  private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;
  @Autowired
  private TimePeriodRepository timePeriodRepository;

  /**
   * Method to view a museum
   * 
   * @author VZ
   * @param museumId - museum id
   * @return museum
   */

  @Transactional
  public Museum getMuseum(Long museumId) {
    if (museumId == null) {
      throw new IllegalArgumentException("Museum id cannot be null");
    }
    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    if (museum == null) {
      throw new IllegalArgumentException("Museum does not exist");
    }
    return museum;
  }

  /**
   * Method to create a museum
   * 
   * @author VZ
   * @param name - name of the museum
   * @param visitFee - visit fee of the museum
   * @param schedule - schedule of the museum
   * @return museum
   */
  @Transactional
  public Museum createMuseum(String name, double visitFee, Schedule schedule) {
    if (name == null || name.trim().length() == 0) {
      throw new IllegalArgumentException("Name cannot be empty!");
    }
    if (visitFee < 0) {
      throw new IllegalArgumentException("Visit fee cannot be negative!");
    }
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule cannot be null!");
    }

    Museum museum = new Museum();
    museum.setName(name);
    museum.setVisitFee(visitFee);
    museum.setSchedule(schedule);

    return museumRepository.save(museum);
  }

  /**
   * Method to edit a museum's name, visit fee or schedule given the museum's id.
   * 
   * @author VZ
   * @param museumId - museum id
   * @param name - new name of the museum
   * @param visitFee - new visit fee of the museum
   * @param schedule - new schedule of the museum
   * @return
   */
  @Transactional
  public Museum editMuseum(long museumId, String name, Double visitFee, Schedule schedule) {
    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    if (museum == null) {
      throw new IllegalArgumentException("Museum does not exist");
    }

    if ((name == null || name.trim().length() == 0) && visitFee == null && schedule == null) {
      throw new IllegalArgumentException("Nothing to edit, all fields are empty");
    }
    if (name != null) {
      if(name.trim().length() == 0) {
        throw new IllegalArgumentException("Name cannot be empty!");
      }
      museum.setName(name);
    }
    if (visitFee != null) {
      if (visitFee < 0) {
        throw new IllegalArgumentException("Visit fee cannot be negative!");
      }
      museum.setVisitFee(visitFee);
    }
    if (schedule != null) {
      museum.setSchedule(schedule);
    }
    return museumRepository.save(museum);
  }

  /**
   * Method to get a museum's schedule
   * 
   * @author VZ
   * @param museumId - museum id
   * @return schedule of museum
   */
  @Transactional
  public Schedule getMuseumSchedule(long museumId) {
    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    if (museum == null) {
      throw new IllegalArgumentException("Museum doesn't exist!");
    }
    Schedule schedule = museum.getSchedule();
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule doesn't exist!");
    }
    return schedule;
  }

  /**
   * Method to get all of museum's shifts
   * 
   * @author VZ
   * @param museumId - museum id
   * @return list of shifts of museum
   */
  @Transactional
  public List<TimePeriod> getMuseumTimePeriods(long museumId) {
    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    if (museum == null) {
      throw new IllegalArgumentException("Museum doesn't exist!");
    }
    Schedule schedule = museum.getSchedule();
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule doesn't exist!");
    }
    List<TimePeriod> timePeriods = getTimePeriodsOfSchedule(schedule);

    return timePeriods;
  }

  /**
   * Method to add a time period to a museum's schedule
   * 
   * @author VZ 
   * @param museumId - museum id
   * @param timePeriodId - time period id
   * @return Museum with added time period
   */
  @Transactional
  public Museum addMuseumTimePeriodAssociation(long museumId, long timePeriodId) {
    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    if (museum == null) {
      throw new IllegalArgumentException("Museum doesn't exist!");
    }
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
    if (timePeriod == null) {
      throw new IllegalArgumentException("Time period doesn't exist!");
    }
    Schedule schedule = museum.getSchedule();
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule doesn't exist!");
    }
    ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
    scheduleOfTimePeriod.setTimePeriod(timePeriod);
    scheduleOfTimePeriod.setSchedule(schedule);
    scheduleOfTimePeriodRepository.save(scheduleOfTimePeriod);
    scheduleRepository.save(schedule);
    return museumRepository.save(museum);
  }

  /**
   * Method to remove a time period from a museum's schedule
   * 
   * @author VZ
   * @param museumId - museum id
   * @param timePeriodId - time period id
   * @return Museum with removed time period
   */
  @Transactional
  public Museum deleteMuseumTimePeriodAssociation(long museumId, long timePeriodId) {

    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    if (museum == null) {
      throw new IllegalArgumentException("Museum doesn't exist!");
    }
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
    if (timePeriod == null) {
      throw new IllegalArgumentException("Time period doesn't exist!");
    }
    Schedule schedule = museum.getSchedule();
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule doesn't exist!");
    }

    if (scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, timePeriod) == null) {
      throw new IllegalArgumentException("Time period doesn't exist in museum's schedule!");
    }
    scheduleOfTimePeriodRepository.deleteScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, timePeriod);

    return museumRepository.save(museum);

  }

  /**
   * Method to convert an Iterable to a List
   * 
   * @param iterable - Iterable
   * @return List
   * @author From tutorial notes
   */
  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }

  /**
   * Method to get all the museums in the database
   * 
   * @return List of all museums
   * @author Siger
   */
  @Transactional
  public List<Museum> getAllMuseums() {
    return toList(museumRepository.findAll());
  }

  /**
   * Helper method to get the time periods of a schedule
   * 
   * @author VZ
   * @param schedule - schedule
   * @return list of time periods of schedule
   */

  public List<TimePeriod> getTimePeriodsOfSchedule(Schedule schedule) {

    List<ScheduleOfTimePeriod> scheduleOfTimePeriods = scheduleOfTimePeriodRepository
        .findScheduleOfTimePeriodBySchedule(schedule);
    if (scheduleOfTimePeriods == null || scheduleOfTimePeriods.isEmpty()) {
      return null;
    }
    List<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
    for (ScheduleOfTimePeriod scheduleOfTimePeriod : scheduleOfTimePeriods) {
      timePeriods.add(scheduleOfTimePeriod.getTimePeriod());
    }

    return timePeriods;
  }

}
