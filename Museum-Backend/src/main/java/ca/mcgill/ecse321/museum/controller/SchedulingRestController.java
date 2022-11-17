package ca.mcgill.ecse321.museum.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
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
     * @param id
     * @return
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
     * @param id
     * @return
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
     * @param id
     * @return
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
     * @param id
     * @return
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
     * 
     * @author VZ
     * @param timePeriodDto
     * @return
     */

    @PostMapping(value = { "/shift/create", "/shift/create/" })
    public ResponseEntity<?> createTimePeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd'T'HH:mm") Timestamp startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy-MM-dd'T'HH:mm") Timestamp endDate) {
        try {
            TimePeriod timePeriod = timePeriodService.createTimePeriod(startDate, endDate);
            return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * RESTful api to get a timeperiod from the database
     * 
     * @param id
     * @return
     * @author VZ
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
     * @param id
     * @return
     */
    @DeleteMapping(value = { "/shift/{id}", "/shift/{id}/" })
    public ResponseEntity<?> deleteTimePeriod(@PathVariable("id") long id) {
        try {
            timePeriodService.deleteTimePeriod(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * RESTful api to assign edit a timeperiod in the database
     * 
     * @author VZ
     * @param id
     * @param startDate
     * @param endDate
     * @return
     */
    @PostMapping(value = { "/shift/edit", "/shift/edit/" })
    public ResponseEntity<?> editTimePeriod(@PathVariable("id") long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy/MM/dd hh:mm") Timestamp startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME, pattern = "yyyy/MM/dd hh:mm") Timestamp endDate) {
        try {
            TimePeriod timePeriod = timePeriodService.editTimePeriod(id, startDate, endDate);
            return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * RESTful api to add a time period to an employee
     * 
     * @author VZ
     * @param employeeDto
     * @param timePeriodDto
     * @return
     */
    @PostMapping(value = { "/employee/{employeeId}/shift/{tpId}", "/employee/{employeeId}/shift/{tpId}/" })
    public ResponseEntity<?> addTimePeriodToEmployeeSchedule(
            @PathVariable("employeeId") long employeeId,
            @PathVariable("tpId") long tpId) {
        try {
            Employee employee = employeeService.addEmployeeTimePeriodAssociation(employeeId, tpId);
            return new ResponseEntity<>(DtoUtility.convertToDto(employee), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * RESTful api to remove a time period to an employee's schedule
     * 
     * @author VZ
     * @param employeeId
     * @param tpId
     * @return
     */
    @DeleteMapping(value = { "/employee/{employeeId}/shift/{tpId}", "/employee/{employeeId}/shift/{tpId}/" })
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
     * @param museumId
     * @param tpId
     * @return
     */
    @PostMapping(value = { "/museum/{museumId}/shift/{tpId}", "/museum/{museumId}/shift/{tpId}/" })
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
     * @param museumId
     * @param tpId
     * @return
     */
    @DeleteMapping(value = { "/museum/{museumId}/shift/{tpId}", "/museum/{museumId}/shift/{tpId}/" })
    public ResponseEntity<?> removeTimePeriodFromMuseumSchedule(
            @PathVariable("museumId") long museumId,
            @PathVariable("tpId") long tpId) {
        try {
            museumService.removeMuseumTimePeriodAssociation(museumId, tpId);
            return new ResponseEntity<>("Shift deleted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
