package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dto.TimePeriodDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import ca.mcgill.ecse321.museum.service.EmployeeService;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.TimePeriodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * SchedulingRestController class is used as a controller where we call our API for our web
 * application
 *
 * @author Victor
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/scheduling")
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
   * @param id the id of the employee
   * @return the schedule of the employee
   * @author VZ
   */
  @GetMapping(value = { "/employee/schedule/{id}", "/employee/schedule/{id}/" })
  public ResponseEntity<?> getScheduleByEmployee(HttpServletRequest request,
      @PathVariable("id") long id) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a staff member");
      } else if (!AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You are not authorized to view this schedule");
      }

      return new ResponseEntity<>(DtoUtility.convertToDto(employeeService.getEmployeeSchedule(id)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  /**
   * RESTful api to get all employee's shifts
   *
   * @param id the id of the employee
   * @return all shifts of the employee
   * @author VZ
   */
  @GetMapping(value = { "/employee/shifts/{id}", "/employee/shifts/{id}/" })
  public ResponseEntity<?> getAllShiftsByEmployee(HttpServletRequest request,
      @PathVariable("id") long id) {

    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a staff member");
      }

      if (!AuthenticationUtility.isManager(session)
          && !AuthenticationUtility.checkUserId(session, id)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You are not authorized to view this schedule");
      }

      List<TimePeriodDto> timePeriodDtos = new ArrayList<TimePeriodDto>();

      List<TimePeriod> employeeTimePeriods = employeeService.getEmployeeTimePeriods(id);
      if (employeeTimePeriods == null) {
        return new ResponseEntity<>(timePeriodDtos, HttpStatus.OK);
      }

      for (TimePeriod tp : employeeTimePeriods) {
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
   * @param id the id of the museum
   * @return the schedule of the museum
   * @author VZ
   */
  @GetMapping(value = { "/museum/schedule/{id}", "/museum/schedule/{id}/" })
  public ResponseEntity<?> getScheduleByMuseum(@PathVariable("id") long id) {
    try {
      return new ResponseEntity<>(DtoUtility.convertToDto(museumService.getMuseumSchedule(id)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

  }

  /**
   * RESTful api to get all museum's shifts
   *
   * @param id the id of the museum
   * @return all shifts of the museum
   * @author VZ
   */
  @GetMapping(value = { "/museum/shifts/{id}", "/museum/shifts/{id}/" })
  public ResponseEntity<?> getAllShiftsByMuseum(HttpServletRequest request,
      @PathVariable("id") long id) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }
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
   * RESTful api to create a new time period in the database We pass in a Dto object, we could also
   * pass in strings to represent start and enddates, but that requires object mapper to parse into
   * json.
   *
   * @param timePeriodDto the time period dto
   * @return the time period dto
   * @author VZ
   */

  @PostMapping(value = { "/shift/create", "/shift/create/" })
  public ResponseEntity<?> createTimePeriod(HttpServletRequest request,
      @RequestBody TimePeriodDto timePeriodDto) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }

      TimePeriod timePeriod =
          timePeriodService.createTimePeriod(Timestamp.valueOf(timePeriodDto.getStartDate()),
              Timestamp.valueOf(timePeriodDto.getEndDate()));
      return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * RESTful api to get a timeperiod from the database
   *
   * @param id the id of the timeperiod
   * @return the timeperiod dto
   * @author VZ
   */

  @GetMapping(value = { "/shift/{id}", "/shift/{id}/" })
  public ResponseEntity<?> getTimePeriod(HttpServletRequest request, @PathVariable("id") long id) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a staff member");
      }
      TimePeriod timePeriod = timePeriodService.getTimePeriod(id);
      return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

  }

  /**
   * RESTful api to delete a time period from the database
   *
   * @param id the id of the time period
   * @return the time period dto
   * @author VZ
   */
  @DeleteMapping(value = { "/shift/delete/{id}", "/shift/delete/{id}/" })
  public ResponseEntity<?> deleteTimePeriod(HttpServletRequest request,
      @PathVariable("id") long id) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }

      timePeriodService.deleteTimePeriod(id);
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  /**
   * RESTful api to edit a timeperiod in the database
   *
   * @param id the id of the timeperiod
   * @param timePeriodDto the new timeperiod Dto
   * @return the edited timeperiod dto
   * @author VZ
   */
  @PostMapping(value = { "/shift/edit/{id}", "/shift/edit/{id}/" })
  public ResponseEntity<?> editTimePeriod(HttpServletRequest request, @PathVariable("id") long id,
      @RequestBody TimePeriodDto timePeriodDto) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }
      TimePeriod timePeriod =
          timePeriodService.editTimePeriod(id, Timestamp.valueOf(timePeriodDto.getStartDate()),
              Timestamp.valueOf(timePeriodDto.getEndDate()));
      return new ResponseEntity<>(DtoUtility.convertToDto(timePeriod), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  /**
   * RESTful api to add a time period to an employee
   *
   * @param employeeId the employee id
   * @param tpId the time period id
   * @return the employeedto with an added timeperiod
   * @author VZ
   */
  @PostMapping(value = { "/employee/{employeeId}/add/shift/{tpId}",
      "/employee/{employeeId}/add/shift/{tpId}/" })
  public ResponseEntity<?> addTimePeriodToEmployeeSchedule(HttpServletRequest request,
      @PathVariable("employeeId") long employeeId, @PathVariable("tpId") long tpId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }
      Employee employee = employeeService.addEmployeeTimePeriodAssociation(employeeId, tpId);
      return new ResponseEntity<>(DtoUtility.convertToDto(employee), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  /**
   * RESTful api to remove a time period to an employee's schedule
   *
   * @param employeeId the employee id
   * @param tpId the time period id
   * @return the employeedto with a removed timeperiod
   * @author VZ
   */

  @DeleteMapping(value = { "/employee/{employeeId}/remove/shift/{tpId}",
      "/employee/{employeeId}/remove/shift/{tpId}/" })
  public ResponseEntity<?> deleteTimePeriodFromEmployeeSchedule(HttpServletRequest request,
      @PathVariable("employeeId") long employeeId, @PathVariable("tpId") long tpId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }

      employeeService.deleteEmployeeTimePeriodAssociation(employeeId, tpId);
      return new ResponseEntity<>("Shift deleted", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful api to add a time period to a museum
   *
   * @param museumId the museum id
   * @param tpId the timeperiod id
   * @return the museum dto with an added timeperiod
   * @author VZ
   */
  @PostMapping(
      value = { "/museum/{museumId}/add/shift/{tpId}", "/museum/{museumId}/add/shift/{tpId}/" })
  public ResponseEntity<?> addTimePeriodToMuseumSchedule(HttpServletRequest request,
      @PathVariable("museumId") long museumId, @PathVariable("tpId") long tpId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }
      Museum museum = museumService.addMuseumTimePeriodAssociation(museumId, tpId);
      return new ResponseEntity<>(DtoUtility.convertToDto(museum), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful api to remove a time period from a museum
   *
   * @param museumId the museum id
   * @param tpId the timeperiod id
   * @return the museum dto with a removed timeperiod
   * @author VZ
   */
  @DeleteMapping(
      value = { "/museum/{museumId}/remove/shift/{tpId}", "/museum/{museumId}/remove/shift/{tpId}/" })
  public ResponseEntity<?> removeTimePeriodFromMuseumSchedule(HttpServletRequest request,
      @PathVariable("museumId") long museumId, @PathVariable("tpId") long tpId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isManager(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a manager");
      }
      museumService.deleteMuseumTimePeriodAssociation(museumId, tpId);
      return new ResponseEntity<>("Shift deleted", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}
