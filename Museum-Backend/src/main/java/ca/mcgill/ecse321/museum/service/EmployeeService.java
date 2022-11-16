package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;

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

  /**
   * Method to view an employee and get them by their id
   * 
   * @author VZ
   * @author SM
   * @param employeeId - employee id
   * @return employee
   */
  @Transactional
  public Employee getEmployee(Long employeeId) {
    // Error handling
    if (employeeId == null) {
      throw new IllegalArgumentException("Employee id cannot be null");
    }
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    if (employee == null) {
      throw new IllegalArgumentException("Employee does not exist");
    }
    return employee;
  }

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
   * @return
   */
  @Transactional
  public Schedule getEmployeeSchedule(long employeeId) {
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    Schedule schedule = employee.getSchedule();
    if (schedule == null) {
      System.out.println("No schedule found");
    }
    return schedule;
  }

  /**
   * Method to get all of employee's shifts
   * 
   * @author VZ
   * @param employeeId
   * @return
   */

  @Transactional
  public List<TimePeriod> getEmployeeTimePeriods(long employeeId) {

    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    Schedule schedule = employee.getSchedule();

    if (schedule == null) {
      throw new IllegalArgumentException("Employee has empty schedule");
    }

    List<TimePeriod> timePeriods = getTimePeriodsOfSchedule(schedule);
    if (timePeriods == null) {
      throw new IllegalArgumentException("Employee has no shift");
    }
    return timePeriods;
  }

  /**
   * Method to add a time period to employee's schedule, in other words
   * give more work to an employee.
   * 
   * @author VZ
   * @param employee
   * @param timeperiod
   * @return
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
   * @param employeeId
   * @param timePeriodId
   * @return
   */
  @Transactional
  public Employee deleteEmployeeTimePeriodAssociation(long employeeId, long timePeriodId) {
    /**
     * 1. Find the employee's schedule first and then delete the
     * ScheduleOfTimePeriod that associates with
     * the TimePeriod that we want to remove from the employee's schedule.
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
    if (employeeSchedule == null) {
      throw new IllegalArgumentException("Employee has empty schedule");
    }
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
   * @param id - Long
   * @return if the employee was deleted (success)
   * @author Siger
   */
  @Transactional
  public boolean deleteEmployee(Long id) {
    // Check if the employee exists and error handling
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(id);
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
    employeeRepository.deleteEmployeeByMuseumUserId(id);

    // Check if employee was deleted
    return getEmployee(id) == null;
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
   * @param schedule
   * @return
   */

  public List<TimePeriod> getTimePeriodsOfSchedule(Schedule schedule) {

    List<ScheduleOfTimePeriod> scheduleOfTimePeriods = scheduleOfTimePeriodRepository
        .findScheduleOfTimePeriodBySchedule(schedule);
    if (scheduleOfTimePeriods == null) {
      return null;
    }
    List<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
    for (ScheduleOfTimePeriod scheduleOfTimePeriod : scheduleOfTimePeriods) {
      timePeriods.add(scheduleOfTimePeriod.getTimePeriod());
    }

    return timePeriods;
  }
}