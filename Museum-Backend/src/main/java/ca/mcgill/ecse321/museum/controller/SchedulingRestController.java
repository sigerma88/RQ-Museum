package ca.mcgill.ecse321.museum.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import ca.mcgill.ecse321.museum.dto.TimePeriodDto;
import ca.mcgill.ecse321.museum.service.EmployeeService;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.TimePeriodService;

@CrossOrigin(origins = "*")
@RestController
public class SchedulingRestController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private MuseumService museumService;
    @Autowired
    private TimePeriodService timePeriodService;

    /**
     * RESTful api get an employee's schedule
     * 
     * @author VZ
     * @param id the id of the employee
     * @return the schedule of the employee
     */
    @GetMapping(value = { "/employee/schedule/{id}", "/employee/schedule/{id}/" })
    public ResponseEntity<?> getScheduleByEmployee(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(DtoUtility.convertToDto(employeeService.getEmployeeSchedule(id)),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * RESTful api to get all employee's shifts
     * 
     * @author VZ 
     * @param id the id of the employee
     * @return all shifts of the employee
     */
    @GetMapping(value = { "/employee/shifts/{id}", "/employee/shifts/{id}/" })
    public ResponseEntity<?> getAllShiftsByEmployee(@PathVariable("id") long id) {

        try {
            List<TimePeriodDto> timePeriodDtos = new ArrayList<TimePeriodDto>();
            for (TimePeriod tp : employeeService.getEmployeeTimePeriods(id)) {
                timePeriodDtos.add(DtoUtility.convertToDto(tp));
            }
            return new ResponseEntity<>(timePeriodDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * RESTful api to get the museum's schedule
     * 
     * @author VZ
     * @param id the id of the museum
     * @return the schedule of the museum
     */
    @GetMapping(value = { "/museum/schedule/{id}", "/museum/schedule/{id}/" })
    public ResponseEntity<?> getScheduleByMuseum(@PathVariable("id") long id) {
        try {
            return new ResponseEntity<>(DtoUtility.convertToDto(museumService.getMuseumSchedule(id)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * RESTful api to get all museum's shifts
     * 
     * @author VZ
     * @param id the id of the museum
     * @return all shifts of the museum
     */
    @GetMapping(value = { "/museum/shifts/{id}", "/museum/shifts/{id}/" })
    public ResponseEntity<?> getAllShiftsByMuseum(@PathVariable("id") long id) {

        try {
            List<TimePeriodDto> timePeriodDtos = new ArrayList<TimePeriodDto>();
            for (TimePeriod tp : museumService.getMuseumTimePeriods(id)) {
                timePeriodDtos.add(DtoUtility.convertToDto(tp));
            }
            return new ResponseEntity<>(timePeriodDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * RESTful api to create a new time period in the database
     * We pass in a Dto object, we could also pass in strings to represent start and
     * enddates, but that requires object mapper to parse into json.
     * 
     * @author VZ
     * @param timePeriodDto the time period dto
     * @return the time period dto
     */

    @PostMapping(value = { "/shift/create", "/shift/create/" })
    public ResponseEntity<?> createTimePeriod(@RequestBody TimePeriodDto timePeriodDto) {
        try {
            TimePeriod timePeriod = timePeriodService.createTimePeriod(
                    Timestamp.valueOf(timePeriodDto.getStartDate()),
                    Timestamp.valueOf(timePeriodDto.getEndDate()));
            return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * RESTful api to get a timeperiod from the database
     * @author VZ
     * @param id the id of the timeperiod
     * @return the timeperiod dto
     */

    @GetMapping(value = { "/shift/{id}", "/shift/{id}/" })
    public ResponseEntity<?> getTimePeriod(
            @PathVariable("id") long id) {
        try {
            TimePeriod timePeriod = timePeriodService.getTimePeriod(id);
            return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * RESTful api to delete a time period from the database
     * 
     * @author VZ
     * @param id the id of the time period
     * @return the time period dto
     */
    @DeleteMapping(value = { "/shift/delete/{id}", "/shift/delete/{id}/" })
    public ResponseEntity<?> deleteTimePeriod(@PathVariable("id") long id) {
        try {
            timePeriodService.deleteTimePeriod(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * RESTful api to edit a timeperiod in the database
     * 
     * @author VZ
     * @param id the id of the timeperiod
     * @param startDate the new start date
     * @param endDate the new end date
     * @return the edited timeperiod dto
     */
    @PostMapping(value = { "/shift/edit/{id}", "/shift/edit/{id}/" })
    public ResponseEntity<?> editTimePeriod(@PathVariable("id") long id,
            @RequestBody TimePeriodDto timePeriodDto) {
        try {
            TimePeriod timePeriod = timePeriodService.editTimePeriod(id,
                    Timestamp.valueOf(timePeriodDto.getStartDate()),
                    Timestamp.valueOf(timePeriodDto.getEndDate()));
            return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * RESTful api to add a time period to an employee
     * 
     * @author VZ
     * @param employeeDto the employee dto
     * @param timePeriodDto the time period dto
     * @return the employeedto with an added timeperiod
     */
    @PostMapping(value = { "/employee/{employeeId}/add/shift/{tpId}", "/employee/{employeeId}/add/shift/{tpId}/" })
    public ResponseEntity<?> addTimePeriodToEmployeeSchedule(
            @PathVariable("employeeId") long employeeId,
            @PathVariable("tpId") long tpId) {
        try {
            Employee employee = employeeService.addEmployeeTimePeriodAssociation(employeeId, tpId);
            return new ResponseEntity<>(DtoUtility.convertToDto(employee), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * RESTful api to remove a time period to an employee's schedule
     * 
     * @author VZ
     * @param employeeId the employee id
     * @param tpId the time period id
     * @return the employeedto with a removed timeperiod
     */
    @DeleteMapping(value = { "/employee/{employeeId}/remove/shift/{tpId}", "/employee/{employeeId}/remove/shift/{tpId}/" })
    public ResponseEntity<?> deleteTimePeriodFromEmployeeSchedule(
            @PathVariable("employeeId") long employeeId,
            @PathVariable("tpId") long tpId) {
        try {
            employeeService.deleteEmployeeTimePeriodAssociation(employeeId, tpId);
            return new ResponseEntity<>("Shift deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * RESTful api to add a time period to a museum
     * 
     * @author VZ
     * @param museumId the museum id
     * @param tpId the timeperiod id
     * @return the museum dto with an added timeperiod
     */
    @PostMapping(value = { "/museum/{museumId}/add/shift/{tpId}", "/museum/{museumId}/add/shift/{tpId}/" })
    public ResponseEntity<?> addTimePeriodToMuseumSchedule(
            @PathVariable("museumId") long museumId,
            @PathVariable("tpId") long tpId) {
        try {
            Museum museum = museumService.addMuseumTimePeriodAssociation(museumId, tpId);
            return new ResponseEntity<>(DtoUtility.convertToDto(museum), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * RESTful api to remove a time period from a museum
     * 
     * @author VZ
     * @param museumId the museum id
     * @param tpId the timeperiod id
     * @return the museum dto with a removed timeperiod
     */
    @DeleteMapping(value = { "/museum/{museumId}/remove/shift/{tpId}", "/museum/{museumId}/remove/shift/{tpId}/" })
    public ResponseEntity<?> removeTimePeriodFromMuseumSchedule(
            @PathVariable("museumId") long museumId,
            @PathVariable("tpId") long tpId) {
        try {
            museumService.deleteMuseumTimePeriodAssociation(museumId, tpId);
            return new ResponseEntity<>("Shift deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
