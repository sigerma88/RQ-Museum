package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for employeeController
 *
 * @author Victor
 * @author Siger
 */

@Service
public class EmployeeService {

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private TimePeriodService timePeriodService;

  /**
   * Method to get all the employees in the database
   * Allows the manager to view the list of employees
   *
   * @return List of all employees
   * @author Siger
   */

  @Transactional
  public List<Employee> getAllEmployees() {
    return toList(employeeRepository.findAll());
  }

  /**
   * Method to view an employee's schedule
   * 
   * @author VZ
   * @param employeeId - id of employee
   * @return employee's schedule
   */

  @Transactional
  public Schedule getEmployeeSchedule(long employeeId) {
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    Schedule schedule = employee.getSchedule();

    return schedule;
  }

  /**
   * Method to get all of employee's shifts
   * 
   * @author VZ
   * @param employeeId - id of employee
   * @return list of employee's shifts
   */

  @Transactional
  public List<TimePeriod> getEmployeeTimePeriods(long employeeId) {

    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    Schedule schedule = employee.getSchedule();

    List<TimePeriod> timePeriods = getTimePeriodsOfSchedule(schedule);

    return timePeriods;
  }

  /**
   * Method to add a shift to an employee's schedule
   * 
   * @author VZ
   * @param employeeId - id of employee
   * @param timePeriodId - id of time period
   * @return Employee with added shift
   */

  @Transactional
  public Employee addEmployeeTimePeriodAssociation(long employeeId, long timePeriodId) {

    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }
    if (timePeriod == null) {
      throw new IllegalArgumentException("There is no such time period");
    }

    Schedule employeeSchedule = employee.getSchedule(); // get the schedule of the employee
    if (employeeSchedule == null) {
      throw new IllegalArgumentException("Employee has no schedule");
    }

    ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod(); // create a new schedule of time period
    scheduleOfTimePeriod.setTimePeriod(timePeriod); // set the time period of the schedule of time period
    scheduleOfTimePeriod.setSchedule(employeeSchedule); // set the schedule of the schedule of time period

    scheduleOfTimePeriodRepository.save(scheduleOfTimePeriod); // save the schedule of time period
    scheduleRepository.save(employeeSchedule); // save the schedule

    return employeeRepository.save(employee);
  }

  /**
   * Method to delete a time period from an employee's schedule, in other words
   * lessen an employee's workload.
   * 
   * @author VZ
   * @param employeeId - id of employee
   * @param timePeriodId - id of time period
   * @return Employee with deleted shift
   */

  @Transactional
  public Employee deleteEmployeeTimePeriodAssociation(long employeeId, long timePeriodId) {
    /*
     * 1. Find the employee's schedule first and then delete the
     * ScheduleOfTimePeriod that is associated with the TimePeriod that we
     * want to remove from the employee's schedule.
     */
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }
    TimePeriod timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);
    if (timePeriod == null) {
      throw new IllegalArgumentException("There is no such time period");
    }
    Schedule employeeSchedule = employee.getSchedule();

    if (scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(employeeSchedule,
        timePeriod) == null) {
      throw new IllegalArgumentException("Time period does not exist in employee's schedule");
    }

    scheduleOfTimePeriodRepository.deleteScheduleOfTimePeriodByScheduleAndTimePeriod(employeeSchedule, timePeriod);
    return employeeRepository.save(employee);
  }

  /**
   * Method to delete an employee from the database by their id
   * Allows the manager to delete an employee
   *
   * @param employeeId - Long
   * @return if the employee was deleted (success)
   * @author Siger
   */

  @Transactional
  public boolean deleteEmployee(Long employeeId) {
    // Check if the employee exists and error handling
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    if (employee == null) {
      throw new IllegalArgumentException("Employee does not exist");
    }

    // Delete scheduleOfTimePeriods
    List<ScheduleOfTimePeriod> scheduleOfTimePeriods = scheduleOfTimePeriodRepository
        .findScheduleOfTimePeriodBySchedule(employee.getSchedule());
    if (scheduleOfTimePeriods != null && !scheduleOfTimePeriods.isEmpty()) {
      scheduleOfTimePeriodRepository.deleteScheduleOfTimePeriodBySchedule(employee.getSchedule());
    }

    // Delete employee
    employeeRepository.deleteEmployeeByMuseumUserId(employeeId);

    // Check if employee was deleted
    return employeeRepository.findEmployeeByMuseumUserId(employeeId) == null;
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
   * Helper method to get the time periods of a schedule
   * 
   * @author VZ
   * @param schedule - schedule
   * @return list of time periods that are associated to the schedule
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