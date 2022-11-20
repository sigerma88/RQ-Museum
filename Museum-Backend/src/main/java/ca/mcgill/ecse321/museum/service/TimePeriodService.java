package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for timePeriodController
 *
 * @author Victor
 */

@Service
public class TimePeriodService {
  @Autowired
  private TimePeriodRepository timePeriodRepository;

  // GET

  /**
   * Method to get a time period from database
   *
   * @param timePeriodId - id of time period
   * @return time period
   * @author VZ
   */
  @Transactional
  public TimePeriod getTimePeriod(long timePeriodId) {
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
    if (timePeriod == null) {
      throw new IllegalArgumentException("Time period does not exist");
    }
    return timePeriod;
  }

  // CREATE

  /**
   * Create a TimePeriod and save to database
   *
   * @param startDate - start date of time period
   * @param endDate   - end date of time period
   * @return time period
   * @author VZ
   */
  @Transactional
  public TimePeriod createTimePeriod(Timestamp startDate, Timestamp endDate) {

    // input validation
    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Start date and end date cannot be null");
    }
    if (startDate.after(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
    // create TimePeriod
    TimePeriod timePeriod = new TimePeriod();

    timePeriod.setStartDate(startDate);
    timePeriod.setEndDate(endDate);
    timePeriodRepository.save(timePeriod);
    return timePeriod;
  }

  // DELETE

  /**
   * Delete a TimePeriod from database by ID
   *
   * @param timePeriodId - id of time period
   * @author VZ
   */
  @Transactional
  public void deleteTimePeriod(long timePeriodId) {
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
    if (timePeriod == null) {
      throw new IllegalArgumentException("Time period does not exist");
    }
    timePeriodRepository.deleteById(timePeriodId);
  }

  // EDIT

  /**
   * Edit a TimePeriod by ID and save to database
   *
   * @param timePeriodId - id of time period
   * @param startDate    - start date of time period
   * @param endDate      - end date of time period
   * @return edited time period
   * @author VZ
   */
  @Transactional
  public TimePeriod editTimePeriod(long timePeriodId, Timestamp startDate, Timestamp endDate) {

    if (startDate.after(endDate)) {
      throw new IllegalArgumentException("Start date cannot be after end date");
    }
    // find TimePeriod
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
    if (timePeriod == null) {
      throw new IllegalArgumentException("Time period does not exist");
    }

    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("Dates cannot be null");
    }
    // edit TimePeriod
    timePeriod.setStartDate(startDate);
    timePeriod.setEndDate(endDate);

    timePeriodRepository.save(timePeriod);
    return timePeriod;
  }

  /**
   * Get all time periods from database
   *
   * @return list of time periods
   * @author VZ
   */
  @Transactional
  public List<TimePeriod> getAllTimePeriods() {
    return toList(timePeriodRepository.findAll());
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
