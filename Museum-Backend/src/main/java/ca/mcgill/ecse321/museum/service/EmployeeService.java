package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * Method to view an employee's schedule
     * @author VZ
     * @param employee
     * @return
     */
    public Schedule getEmployeeSchedule(Employee employee) {
        Schedule schedule = employee.getSchedule();
        return schedule;
    }

    /**
     * Method to add a time period to employee's schedule, in other words 
     * give more work to an employee.
     * @param employee
     * @param timeperiod
     * @return
     */
    public Employee addEmployeeTimePeriod(Employee employee, TimePeriod timePeriod) {

        Schedule employeeSchedule = employee.getSchedule(); //get the schedule of the employee
        ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod(); //create a new schedule of time period
        scheduleOfTimePeriod.setTimePeriod(timePeriod); //set the time period of the schedule of time period
        scheduleOfTimePeriod.setSchedule(employeeSchedule); //set the schedule of the schedule of time period

        scheduleOfTimePeriodRepository.save(scheduleOfTimePeriod); //save the schedule of time period
        scheduleRepository.save(employeeSchedule); //save the schedule

        return employeeRepository.save(employee);
    }

    /**
     * Method to delete a time period from an employee's schedule, in other words
     * lessen an employee's workload.
     * @param schedule
     * @return
     */
    public Employee deleteEmployeeTimePeriod(Employee employee, TimePeriod timePeriod) { 
        /**
         * 1. Find the employee's schedule first and then delete the ScheduleOfTimePeriod that associates with
         * the TimePeriod that we want to delete.
         */
        Schedule employeeSchedule = employee.getSchedule(); 
        if(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByTimePeriod(timePeriod) == null) {
            throw new IllegalArgumentException("Time period does not exist in employee's schedule");
        }
        
        scheduleOfTimePeriodRepository.deleteScheduleOfTimePeriodByScheduleAndTimePeriod(employeeSchedule, timePeriod);
        return employeeRepository.save(employee);
    }
    
}
