package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MuseumRestController class is used as a controller where we call
 * our API for our web application
 *
 * @author Siger
 * @author Victor
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/museum")
public class MuseumRestController {

  @Autowired
  private MuseumService museumService;

  @Autowired
  private ScheduleService scheduleService;

  /**
   * API to get a museum by id
   *
   * @param id
   * @return
   * @author VZ
   */
  @GetMapping(value = {"/museum/{id}", "/museum/{id}/"})
  public MuseumDto getMuseum(@PathVariable("id") long id) {
    return DtoUtility.convertToDto(museumService.getMuseum(id));
  }

  /**
   * API to create a museum
   *
   * @param name
   * @param visitFee
   * @param scheduleDto
   * @return
   * @author VZ
   */
  @PostMapping(value = {"/museum/app", "/museum/app/"})
  public ResponseEntity<?> createMuseum(
      @RequestParam(name = "name") String name,
      @RequestParam(name = "visitFee") double visitFee,
      @RequestBody ScheduleDto scheduleDto) {

    try {
      Schedule schedule = scheduleService.getSchedule(scheduleDto.getScheduleId());
      return new ResponseEntity<>(DtoUtility.convertToDto(museumService.createMuseum(name, visitFee, schedule)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * API to edit the museum
   *
   * @param id
   * @param name
   * @param visitFee
   * @param scheduleDto
   * @return
   */
  @PostMapping(value = {"/museum/{id}/app", "/museum/{id}/app/"})
  public ResponseEntity<?> editMuseum(
      @PathVariable("id") long id,
      @RequestParam(name = "name") String name,
      @RequestParam(name = "visitFee") double visitFee,
      @RequestBody ScheduleDto scheduleDto) {
    try {
      Schedule schedule = scheduleService.getSchedule(scheduleDto.getScheduleId());
      return new ResponseEntity<>(DtoUtility.convertToDto(museumService.editMuseum(id, name, visitFee, schedule)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
