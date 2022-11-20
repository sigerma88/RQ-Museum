package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
   * @param id the id of the museum
   * @return museum with the given id
   * @author VZ
   */
  @GetMapping(value = {"/{id}", "/{id}/"})
  public ResponseEntity<?> getMuseum(@PathVariable("id") long id) {
    try {
      return new ResponseEntity<>(DtoUtility.convertToDto(museumService.getMuseum(id)), HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
  }

  /**
   * API to create a museum
   *
   * @param name     the name of the museum
   * @param visitFee the visit fee of the museum
   * @return the created museum
   * @author VZ
   */
  @PostMapping(value = {"/app", "/app/"})
  public ResponseEntity<?> createMuseum(
      @RequestParam(name = "name") String name,
      @RequestParam(name = "visitFee") Double visitFee) {

    try {
      Schedule schedule = scheduleService.createSchedule();
      return new ResponseEntity<>(DtoUtility.convertToDto(museumService.createMuseum(name, visitFee, schedule)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * API to edit the museum's name or visit fee
   *
   * @param id       the id of the museum
   * @param name     the new name of the museum
   * @param visitFee the new visit fee of the museum
   * @return the edited museum
   * @author VZ
   */
  @PostMapping(value = {"/app/edit/{id}/", "/app/edit/{id}"})
  public ResponseEntity<?> editMuseum(
      @PathVariable("id") long id,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "visitFee", required = false) Double visitFee) {
    try {
      Schedule schedule = museumService.getMuseum(id).getSchedule();
      return new ResponseEntity<>(DtoUtility.convertToDto(museumService.editMuseum(id, name, visitFee, schedule)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * API to get all museums
   *
   * @return all museums
   * @author VZ
   */
  @GetMapping(value = {"", "/"})
  public ResponseEntity<?> getAllMuseums() {
    try {
      List<MuseumDto> museums = new ArrayList<>();
      for (Museum museum : museumService.getAllMuseums()) {
        museums.add(DtoUtility.convertToDto(museum));
      }
      return new ResponseEntity<>(museums, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
