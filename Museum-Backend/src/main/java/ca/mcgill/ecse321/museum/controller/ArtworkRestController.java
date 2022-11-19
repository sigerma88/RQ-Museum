package ca.mcgill.ecse321.museum.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import ca.mcgill.ecse321.museum.service.RoomService;

/**
 * RESTful api for Artwork to expose the business logic in the service layer to the frontend
 * 
 * @author Siger
 * @author Zahra
 */
@CrossOrigin(origins = "*")
@RestController
public class ArtworkRestController {

  @Autowired
  private ArtworkService artworkService;

  @Autowired
  private RoomService roomService;

  /**
   * RESTful API to create an artwork
   * 
   * @param name               - name of the artwork
   * @param artist             - artist of the artwork
   * @param isAvailableForLoan - availability of the artwork for loans
   * @param loanFee            - loan fee of the artwork
   * @param image              - image of the artwork
   * @param isOnLoan           - loan status of the artwork
   * @param roomId             - id of the room of the artwork
   * @return created artwork
   * @author Siger
   */
  @PostMapping(value = { "/artwork", "/artwork/" }, produces = "application/json")
  public ResponseEntity<?> createArtwork(@RequestParam(name = "name") String name,
      @RequestParam(name = "artist") String artist,
      @RequestParam(name = "isAvailableForLoan") Boolean isAvailableForLoan,
      @RequestParam(name = "loanFee", required = false) Double loanFee, @RequestParam(name = "image") String image,
      @RequestParam(name = "isOnLoan") Boolean isOnLoan, @RequestParam(name = "roomId", required = false) Long roomId) {
    try {
      // Get room
      Room room = null;
      if (roomId != null) {
        room = roomService.getRoomById(roomId);
        if (room == null) {
          return ResponseEntity.badRequest().body("Room with id " + roomId + " does not exist");
        }
      }

      // Create artwork
      Artwork result = artworkService.createArtwork(name, artist, isAvailableForLoan, loanFee, image, isOnLoan, room);
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get an artwork by its id
   * 
   * @param artworkId - long
   * @return artwork with the given id
   * @author Siger
   */
  @GetMapping(value = { "/artwork/{artworkId}", "/artwork/{artworkId}/" })
  public ResponseEntity<?> getArtworkById(@PathVariable("artworkId") long artworkId) {
    try {
      Artwork artwork = artworkService.getArtwork(artworkId);
      return ResponseEntity.ok(DtoUtility.convertToDto(artwork));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

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
   * RESTful API to get all artworks by room
   * 
   * @param roomId - long
   * @return List of all artworks in the given room
   * @author Siger
   * @author Zahra
   */
  @GetMapping(value = { "/artworks/{roomId}", "/artworks/{roomId}/" })
  public ResponseEntity<?> getAllArtworksByRoom(@PathVariable("roomId") Long roomId) {
    try {
      List<ArtworkDto> artworkDtos = new ArrayList<ArtworkDto>();
      for (Artwork artwork : artworkService.getAllArtworksByRoom(roomId)) {
        artworkDtos.add(DtoUtility.convertToDto(artwork));
      }
      return ResponseEntity.ok(artworkDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to edit an artwork's information
   * 
   * @param artworkId - id of artwork to be edited
   * @param name      - new name of artwork
   * @param artist    - new artist of artwork
   * @param image     - new image of artwork
   * @return edited artwork
   * @author Siger
   */
  @PutMapping(value = { "/artwork/info/{artworkId}", "/artwork/info/{artworkId}/" }, produces = "application/json")
  public ResponseEntity<?> editArtworkInfo(@PathVariable("artworkId") Long artworkId,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "artist", required = false) String artist,
      @RequestParam(name = "image", required = false) String image) {
    try {
      Artwork result = artworkService.editArtworkInfo(artworkId, name, artist, image);
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to edit an artwork's loan availability and loan fee
   * 
   * @param artworkId          - id of artwork to be edited
   * @param isAvailableForLoan - new availability of artwork
   * @param loanFee            - new loan fee of artwork
   * @return edited artwork
   * @author Siger
   */
  @PutMapping(value = { "/artwork/loanInfo/{artworkId}",
      "/artwork/loanInfo/{artworkId}/" }, produces = "application/json")
  public ResponseEntity<?> editArtworkLoanInfo(@PathVariable("artworkId") Long artworkId,
      @RequestParam(name = "isAvailableForLoan") boolean isAvailableForLoan,
      @RequestParam(name = "loanFee", required = false) Double loanFee) {
    try {
      Artwork result = artworkService.editArtworkLoanInfo(artworkId, isAvailableForLoan, loanFee);
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to delete an artwork
   * 
   * @param artworkId - long
   * @return if the artwork was deleted (success)
   * @author Siger
   */
  @DeleteMapping(value = { "/artwork/{artworkId}", "/artwork/{artworkId}/" })
  public ResponseEntity<?> deleteArtwork(@PathVariable("artworkId") Long artworkId) {
    try {
      // Delete the artwork
      Artwork artwork = artworkService.deleteArtwork(artworkId);
      return ResponseEntity.ok(DtoUtility.convertToDto(artwork));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
