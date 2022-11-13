package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class ArtworkRestController {

  @Autowired
  private ArtworkService artworkService;


  /**
   * RESTful API to get all artworks
   *
   * @return List of all artworks
   * @author Zahra
   */
  @GetMapping(value = {"/artworks", "/artworks/"})
  public ResponseEntity<?> getAllArtworks() {
    try {
      List<ArtworkDto> artworkDtos = new ArrayList<ArtworkDto>();
      for (Artwork artwork : artworkService.getAllArtworks()) {
        artworkDtos.add(DtoUtility.convertToDto(artwork));
      }
      return ResponseEntity.ok(artworkDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }


  /**
   * RESTful API to get all artworks in a room
   *
   * @param room - Room
   * @return list of artwork in room
   * @author Zahra
   */
  @GetMapping(value = {"/artworks/{room}", "/artworks/{room}/"})
  public ResponseEntity<?> getAllArtworksByRoom(@PathVariable("room") Room room) {
    try {
      List<ArtworkDto> artworkInRoomDtos = new ArrayList<ArtworkDto>();
      for (Artwork artwork : artworkService.getAllArtworksByRoom(room)) {
        artworkInRoomDtos.add(DtoUtility.convertToDto(artwork));
      }
      return ResponseEntity.ok(artworkInRoomDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }

  }

  @GetMapping(value = {"/artworks/availabilty", "/artworks/availabilty/"})
  public ResponseEntity<?> displayArtworkWithAvailabilityForLoan() {
    try {
      return ResponseEntity.ok(artworkService.getArtworkwithAvailabilityForLoan());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @GetMapping(value = {"/artworks/loanFee", "artworks/loanFee/"})
  public ResponseEntity<?> displayArtworkWithLoanFee() {
    try {
      return ResponseEntity.ok(artworkService.getArtworkWithLoanFee());
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }


  }


}
