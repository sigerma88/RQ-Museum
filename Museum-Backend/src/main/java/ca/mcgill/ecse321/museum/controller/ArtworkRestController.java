package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import ca.mcgill.ecse321.museum.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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
   * @param artworkDto - Artwork
   * @return created artwork
   * @author Siger
   */
  @PostMapping(value = { "/artwork", "/artwork/" }, produces = "application/json")
  public ResponseEntity<?> createArtwork(@RequestParam(name = "name") String name, @RequestParam(name = "artist") String artist, @RequestParam(name = "isAvailableForLoan") Boolean isAvailableForLoan, @RequestParam(name = "loanFee", required = false) Double loanFee, @RequestParam(name = "image") String image, @RequestParam(name = "isOnLoan") Boolean isOnLoan, @RequestParam(name = "roomId", required = false) Long roomId) {
    try {
      // Get room
      Room room = null;
      if (roomId != null) {
        room = roomService.getRoomById(roomId);
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

  @GetMapping(value = {"/artworks", "/artworks/"})
  public List<ArtworkDto> getAllArtworks() {
    List<ArtworkDto> artworkDtos = new ArrayList<ArtworkDto>();

    for (Artwork artwork : artworkService.getAllArtworks()) {
      try {
        artworkDtos.add(DtoUtility.convertToDto(artwork));

      } catch (Exception e) {
      }
    }
    return artworkDtos;
  }

  // @GetMapping(value = {"/artworks/{room}", "/artworks/{room}/"})
  // public List<ArtworkDto> getAllArtworksByRoom(@PathVariable("room") Room room) {
  //   List<ArtworkDto> artworkInRoomDtos = new ArrayList<ArtworkDto>();
  //   List<Artwork> artworksInRoom = artworkService.getAllArtworksByRoom(room);
  //   for (Artwork artwork : artworksInRoom) {
  //     try {
  //       artworkInRoomDtos.add(DtoUtility.convertToDto(artwork));
  //     } catch (Exception e) {
  //       e.printStackTrace();
  //     }

  //   }
  //   return artworkInRoomDtos;
  // }

  /**
   * RESTful API to get all artworks in a room
   *
   * @param roomId - The id of a room we want to get all the artworks in
   * @return A list of all the artworks in a specific room
   * @author kieyanmamiche
   */
  @GetMapping(value = "/getAllArtworksInRoom/{roomId}")
  public ResponseEntity<?> getAllArtworksInRoom(@PathVariable("roomId") long roomId) {
    try {
      List<Artwork> listOfArtworksThatBelongToRoom = artworkService.getAllArtworksInRoom(roomId);
      List<ArtworkDto> artworkDtos = new ArrayList<>();
      for (Artwork artwork: listOfArtworksThatBelongToRoom){
        artworkDtos.add(DtoUtility.convertToDto(artwork));
      }
      return new ResponseEntity<>(artworkDtos, HttpStatus.OK);
    }catch (Exception e){
      // return error message
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
  @PutMapping(value = { "/artwork/{artworkId}", "/artwork/{artworkId}/" }, produces = "application/json")
  public ResponseEntity<?> editArtworkInfo(@PathVariable("artworkId") Long artworkId, @RequestParam(name = "name") String name, @RequestParam(name = "artist") String artist, @RequestParam(name = "image") String image) {
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
   * @param artworkId - id of artwork to be edited
   * @param isAvailableForLoan - new availability of artwork
   * @param loanFee - new loan fee of artwork
   * @return edited artwork
   * @author Siger
   */
  @PutMapping(value = { "/artwork/loanInfo/{artworkId}", "/artwork/loanInfo/{artworkId}/" }, produces = "application/json")
  public ResponseEntity<?> editArtworkLoanInfo(@PathVariable("artworkId") Long artworkId, @RequestParam(name = "isAvailableForLoan") boolean isAvailableForLoan, @RequestParam(name = "loanFee", required = false) Double loanFee) {
    try {
      Artwork result = artworkService.editArtworkLoan(artworkId, isAvailableForLoan, loanFee);
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
      artworkService.deleteArtwork(artworkId);
      return ResponseEntity.ok("Artwork deleted");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  // Getting artwork status - FR7
  /**
   * RESTful API to get the artwork status
   *
   * Note:
   *    Returns a status string of 4 options: loan/on display/in storage
   *    1. "loan" -> The artwork is on loan
   *    2. "display" -> The artwork is on Display
   *    3. "storage" -> The artwork is in storage
   *
   * @param id - The id of the artwork we want to get the status of
   * @return The status of the artwork
   * @author kieyanmamiche
   */
  @GetMapping(value = "/getArtworkStatus/{id}")
  public ResponseEntity<?> getArtworkStatus(@PathVariable("id") long id) {
    try {
      String status = artworkService.getArtworkStatus(id);
      return new ResponseEntity<>(status, HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful API to get number of artworks in a room
   *
   * @param roomId - The id of a room we want to get the number artworks in
   * @return Number of artworks in specific room
   * @author kieyanmamiche
   */
  @GetMapping(value = "/getNumberOfArtworksInRoom/{roomId}")
  public ResponseEntity<?> getNumberOfArtworksInRoom(@PathVariable("roomId") long roomId) {
    try {
      int numberOfArtworksInRoom = artworkService.getNumberOfArtworksInRoom(roomId);
      return new ResponseEntity<>(numberOfArtworksInRoom, HttpStatus.OK);
    }catch (Exception e){
      // return error message
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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
  @PostMapping(value = "/moveArtworkToRoom/{artworkId}/{roomId}")
  public ResponseEntity<?> moveArtworkToRoom(@PathVariable("artworkId") long artworkId, @PathVariable("roomId") long roomId){
    try{
      Artwork artwork = artworkService.moveArtworkToRoom(artworkId, roomId);
      ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);
      return new ResponseEntity<>(artworkDto, HttpStatus.OK);
    }catch (Exception e){
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }
}