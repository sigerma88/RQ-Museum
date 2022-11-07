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
        ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod(); //create a new scheduleOfTimePeriod
        //Create add in the new timePeriod and associate it to the Employee's schedule
        scheduleOfTimePeriod.setTimePeriod(timePeriod); 
        scheduleOfTimePeriod.setSchedule(employeeSchedule);
        
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
         * 1. Find the employee's schedule first and then find all timePeriods associated with it.
         * 2. Then loop through the list of timePeriods and find the one that matches the 
         * timePeriod passed in.
         * 3. Then delete that timePeriod from the employee's schedule by
         * deleting the scheduleOfTimePeriod object that associates the timePeriod to the schedule.
         */

        Schedule employeeSchedule = employee.getSchedule(); 
        List <TimePeriod> employeeTimePeriods = getTimePeriodsBySchedule(employeeSchedule);
        
        for (TimePeriod tp : employeeTimePeriods) {
            if (tp.equals(timePeriod)) {
                List<ScheduleOfTimePeriod> employeeScheduleOfTimePeriods = getScheduleOfTimePeriodsByTimePeriod(timePeriod);
                
                for (ScheduleOfTimePeriod sotp: employeeScheduleOfTimePeriods) {
                    if (timePeriod == sotp.getTimePeriod() && employeeSchedule == sotp.getSchedule()) {
                        scheduleOfTimePeriodRepository.delete(sotp);
                        return employeeRepository.save(employee);
                    }
                }
            }
        } 

        throw new IllegalArgumentException("Time period does not exist in employee's schedule");
    }

    /**
     * Helper method that gets the list of time periods from a schedule that uses 
     * the helper method getScheduleOfTimePeriodsBySchedule
     * @param schedule
     * @return
     */
    public List<TimePeriod> getTimePeriodsBySchedule(Schedule schedule) {
        //get all ScheduleOfTimePeriods with schedule
        List<ScheduleOfTimePeriod> listOfScheduleOfTimePeriods = getScheduleOfTimePeriodsBySchedule(schedule);
        List<TimePeriod> listOfTimePeriods = new ArrayList<TimePeriod>();

        for (ScheduleOfTimePeriod scheduleOfTimePeriod : listOfScheduleOfTimePeriods) {
            listOfTimePeriods.add(scheduleOfTimePeriod.getTimePeriod());
        }
        return listOfTimePeriods;
        }

    /**
     * Helper method to find all SOTP with a given schedule. 
     * @author VZ
     * @param schedule
     * @return
     */
    public List<ScheduleOfTimePeriod> getScheduleOfTimePeriodsBySchedule (Schedule schedule) {
        List<ScheduleOfTimePeriod> listOfScheduleOfTimePeriods = new ArrayList<ScheduleOfTimePeriod>();
        
        for(ScheduleOfTimePeriod scheduleOfTimePeriod : scheduleOfTimePeriodRepository.findAll()) {
            if(scheduleOfTimePeriod.getSchedule().equals(schedule)) {
                listOfScheduleOfTimePeriods.add(scheduleOfTimePeriod);
            }
        }

        return listOfScheduleOfTimePeriods;
    }

    /**
     * Method to get all SOTP with given time period
     * @author VZ
     * @param timePeriod
     * @return
     */
    public List<ScheduleOfTimePeriod> getScheduleOfTimePeriodsByTimePeriod (TimePeriod timePeriod) {
        List<ScheduleOfTimePeriod> listOfScheduleOfTimePeriods = new ArrayList<ScheduleOfTimePeriod>();
        for(ScheduleOfTimePeriod scheduleOfTimePeriod : scheduleOfTimePeriodRepository.findAll()) {
            if(scheduleOfTimePeriod.getTimePeriod().equals(timePeriod)) {
                listOfScheduleOfTimePeriods.add(scheduleOfTimePeriod);
            }
        }
        return listOfScheduleOfTimePeriods;
    }

    

}
