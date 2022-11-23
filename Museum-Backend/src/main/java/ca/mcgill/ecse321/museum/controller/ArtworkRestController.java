package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import ca.mcgill.ecse321.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;

/**
 * RESTful api for Artwork to expose the business logic in the service layer to the frontend
 *
 * @author Siger
 * @author Zahra
 * @author kieyanmamiche
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/artwork")
public class ArtworkRestController {

  @Autowired
  private ArtworkService artworkService;

  @Autowired
  private RoomService roomService;

  /**
   * RESTful API to create an artwork
   *
   * @param name - name of the artwork
   * @param artist - artist of the artwork
   * @param isAvailableForLoan - availability of the artwork for loans
   * @param loanFee - loan fee of the artwork
   * @param image - image of the artwork
   * @param isOnLoan - loan status of the artwork
   * @param roomId - id of the room of the artwork
   * @return created artwork
   * @author Siger
   */

  @PostMapping(value = {"/", ""}, produces = "application/json")
  public ResponseEntity<?> createArtwork(HttpServletRequest request,
      @RequestParam(name = "name") String name, @RequestParam(name = "artist") String artist,
      @RequestParam(name = "isAvailableForLoan") Boolean isAvailableForLoan,
      @RequestParam(name = "loanFee", required = false) Double loanFee,
      @RequestParam(name = "image") String image, @RequestParam(name = "isOnLoan") Boolean isOnLoan,
      @RequestParam(name = "roomId", required = false) Long roomId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a staff member");
      }
      // Get room
      Room room = null;
      if (roomId != null) {
        room = roomService.getRoomById(roomId);
        if (room == null) {
          return ResponseEntity.badRequest().body("Room with id " + roomId + " does not exist");
        }
      }

      // Create artwork
      Artwork result = artworkService.createArtwork(name, artist, isAvailableForLoan, loanFee,
          image, isOnLoan, room);
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

  @GetMapping(value = {"/{artworkId}", "/{artworkId}/"})
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

  @GetMapping(value = {"/", ""})
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
   * @author kieyanmamiche
   */

  @GetMapping(value = {"/room/{roomId}", "/room/{roomId}/"})
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
   * RESTful API to get all artworks by if they are available for loan
   *
   * @param isAvailableForLoan - boolean
   * @return List of all artworks that are available for loan or not available for loan depending on
   *         the given boolean value
   * @author Siger
   */

  @GetMapping(
      value = {"/availableForLoan/{isAvailableForLoan}", "/availableForLoan/{isAvailableForLoan}/"})
  public ResponseEntity<?> getAllArtworksByAvailabilityForLoan(
      @PathVariable("isAvailableForLoan") Boolean isAvailableForLoan) {
    try {
      List<ArtworkDto> artworkDtos = new ArrayList<ArtworkDto>();
      for (Artwork artwork : artworkService
          .getAllArtworksByAvailabilityForLoan(isAvailableForLoan)) {
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
   * @param name - new name of artwork
   * @param artist - new artist of artwork
   * @param image - new image of artwork
   * @return edited artwork
   * @author Siger
   */

  @PutMapping(value = {"/info/{artworkId}", "/info/{artworkId}/"}, produces = "application/json")
  public ResponseEntity<?> editArtworkInfo(HttpServletRequest request,
      @PathVariable("artworkId") Long artworkId,
      @RequestParam(name = "name", required = false) String name,
      @RequestParam(name = "artist", required = false) String artist,
      @RequestParam(name = "image", required = false) String image) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a staff member");
      }

      Artwork result = artworkService.editArtworkInfo(artworkId, name, artist, image);
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to edit an artwork's loan availability and loan fee
   *
   * @param artworkId - id of artwork to be edited
   * @param isAvailableForLoan - new availability of artwork
   * @param loanFee - new loan fee of artwork
   * @return edited artwork
   * @author Siger
   */

  @PutMapping(value = {"/loanInfo/{artworkId}", "/loanInfo/{artworkId}/"},
      produces = "application/json")
  public ResponseEntity<?> editArtworkLoanInfo(HttpServletRequest request,
      @PathVariable("artworkId") Long artworkId,
      @RequestParam(name = "isAvailableForLoan") boolean isAvailableForLoan,
      @RequestParam(name = "loanFee", required = false) Double loanFee) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not a staff member");
      }

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

  @DeleteMapping(value = {"/{artworkId}", "/{artworkId}/"})
  public ResponseEntity<?> deleteArtwork(HttpServletRequest request,
      @PathVariable("artworkId") Long artworkId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a staff member");
      }
      // Delete the artwork
      Artwork artwork = artworkService.deleteArtwork(artworkId);
      return ResponseEntity.ok(DtoUtility.convertToDto(artwork));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }


  /**
   * RESTful API to get the artwork status
   * 
   * Note: Returns a status string of 4 options: loan/on display/in storage 1. "loan" -> The artwork
   * is on loan 2. "display" -> The artwork is on Display 3. "storage" -> The artwork is in storage
   * 
   * Getting artwork status - FR7
   *
   * @param artworkId - The id of the artwork we want to get the status of
   * @return The status of the artwork
   * @author kieyanmamiche
   */

  @GetMapping(value = {"/getArtworkStatus/{artworkId}", "/getArtworkStatus/{artworkId}/"})
  public ResponseEntity<?> getArtworkStatus(@PathVariable("artworkId") long artworkId) {
    try {
      String status = artworkService.getArtworkStatus(artworkId);
      return ResponseEntity.ok(status);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get number of artworks in a room
   *
   * @param roomId - The id of a room we want to get the number artworks in
   * @return Number of artworks in specific room
   * @author kieyanmamiche
   */

  @GetMapping(
      value = {"/getNumberOfArtworksInRoom/{roomId}", "/getNumberOfArtworksInRoom/{roomId}/"})
  public ResponseEntity<?> getNumberOfArtworksInRoom(@PathVariable("roomId") long roomId) {
    try {
      int numberOfArtworksInRoom = artworkService.getNumberOfArtworksInRoom(roomId);
      return ResponseEntity.ok(numberOfArtworksInRoom);
    } catch (Exception e) {
      // return error message
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to move artwork to different room
   *
   * @param artworkId - The id of the artwork we want to move
   * @param roomId - The id of a room we want to move the artwork to
   * @return The artwork which has been moved
   * @author kieyanmamiche
   */

  @PostMapping(value = {"/moveArtworkToRoom/{artworkId}/{roomId}",
      "/moveArtworkToRoom/{artworkId}/{roomId}/"})
  public ResponseEntity<?> moveArtworkToRoom(HttpServletRequest request,
      @PathVariable("artworkId") long artworkId, @PathVariable("roomId") long roomId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Your are not a staff member");
      }

      Artwork artwork = artworkService.moveArtworkToRoom(artworkId, roomId);
      ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);
      return ResponseEntity.ok(artworkDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
