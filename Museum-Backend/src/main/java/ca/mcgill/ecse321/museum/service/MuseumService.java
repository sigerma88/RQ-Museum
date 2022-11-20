package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for museumController
 *
 * @author Victor
 * @author Siger
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

  @Autowired
  private TimePeriodService timePeriodService;

  /**
   * Method to view a museum
   *
   * @param museumId - museum id
   * @return museum
   * @author VZ
   */

  @Transactional
  public Museum getMuseum(long museumId) {
    return museumRepository.findMuseumByMuseumId(museumId);
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
   * Method to create a museum
   *
   * @return
   * @author VZ
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
   * @param museumId
   * @param name
   * @param visitFee
   * @param schedule
   * @return
   */

  @Transactional
  public Museum editMuseum(long museumId, String name, double visitFee, Schedule schedule) {
    if (name == null || name.trim().length() == 0) {
      throw new IllegalArgumentException("Name cannot be empty!");
    }
    if (visitFee < 0) {
      throw new IllegalArgumentException("Visit fee cannot be negative!");
    }
    if (schedule == null) {
      throw new IllegalArgumentException("Schedule cannot be null!");
    }

    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    museum.setName(name);
    museum.setVisitFee(visitFee);
    museum.setSchedule(schedule);

    return museumRepository.save(museum);
  }

  /**
   * Method to get a museum's schedule
   *
   * @param museumId
   * @return
   * @author VZ
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
   * @param museumId
   * @return
   * @author VZ
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
    List<TimePeriod> timePeriods = timePeriodService.getTimePeriodsOfSchedule(schedule);
    return timePeriods;
  }

  /**
   * Method to add a time period to a museum's schedule
   *
   * @param museumId
   * @param timePeriodId
   * @return
   * @author VZ
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
   * @param museumId
   * @param timePeriodId
   * @return
   * @author VZ
   */

  @Transactional
  public Museum removeMuseumTimePeriodAssociation(long museumId, long timePeriodId) {

    Museum museum = museumRepository.findMuseumByMuseumId(museumId);
    if (museum == null) {
      throw new IllegalArgumentException("Museum doesn't exist!");
    }
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
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
}
