package ca.mcgill.ecse321.museum.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.ScheduleService;

@CrossOrigin(origins = "*")
@RestController
public class MuseumRestController {

  @Autowired
  private MuseumService museumService;
  @Autowired
  private ScheduleService scheduleService;

  /**
   * API to get a museum by id
   * 
   * @author VZ
   * @param id
   * @return
   */
  @GetMapping(value = { "/museum/{id}", "/museum/{id}/" })
  public MuseumDto getMuseum(@PathVariable("id") long id) {
    return DtoUtility.convertToDto(museumService.getMuseum(id));
  }

  /**
   * API to create a museum
   * 
   * @author VZ
   * @param name
   * @param visitFee
   * @return
   */
  @PostMapping(value = { "/museum/app", "/museum/app/" })
  public ResponseEntity<?> createMuseum(
      @RequestParam(name = "name") String name,
      @RequestParam(name = "visitFee") double visitFee) {

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
   * @param id
   * @param name
   * @param visitFee
   * @return
   */
  @PostMapping(value = { "/museum/app/edit/{id}/", "/museum/app/edit/{id}" })
  public ResponseEntity<?> editMuseum(
      @PathVariable("id") long id,
      @RequestParam(name = "name") String name,
      @RequestParam(name = "visitFee") double visitFee) {
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
   * @author VZ
   * @return
   */
  @GetMapping(value = {"/museums", "/museums/"})
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
