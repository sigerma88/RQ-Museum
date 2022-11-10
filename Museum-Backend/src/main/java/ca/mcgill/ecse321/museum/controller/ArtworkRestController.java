package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.service.ArtworkService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class ArtworkRestController {

  @Autowired
  private ArtworkService artworkService;

  /**
   * RESTful API to create an artwork
   * 
   * @param artwork - Artwork
   * @return created artwork
   * @author Siger
   */
  @PostMapping(value = { "/artwork", "/artwork/" }, produces = "application/json")
  public ResponseEntity<?> createArtwork(@RequestBody Artwork artwork) {
    try {
      Artwork result = artworkService.createArtwork(artwork.getName(), artwork.getArtist(), artwork.getIsAvailableForLoan(), artwork.getLoanFee(), artwork.getImage(), artwork.getIsOnLoan(), artwork.getRoom());
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get an artwork by its id
   * 
   * @param id - long
   * @return artwork with the given id
   * @author Siger
   */
  @GetMapping(value = { "/artwork/{id}", "/artwork/{id}/" })
  public ResponseEntity<?> getArtworkById(@PathVariable("id") long id) {
    try {
      Artwork artwork = artworkService.getArtwork(id);
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

  @GetMapping(value = {"/artworks/{room}", "/artworks/{room}/"})
  public List<ArtworkDto> getAllArtworksByRoom(@PathVariable("room") Room room) {
    List<ArtworkDto> artworkInRoomDtos = new ArrayList<ArtworkDto>();
    List<Artwork> artworksInRoom = artworkService.getAllArtworksByRoom(room);
    for (Artwork artwork : artworksInRoom) {
      try {
        artworkInRoomDtos.add(DtoUtility.convertToDto(artwork));
      } catch (Exception e) {
        e.printStackTrace();
      }

    }
    return artworkInRoomDtos;
  }

  /**
   * RESTful API to edit an artwork's information
   * 
   * @param artworkDto - ArtworkDto
   * @return edited artwork
   * @author Siger
   */
  @PutMapping(value = { "/artwork", "/artwork/" }, produces = "application/json")
  public ResponseEntity<?> editArtworkInfo(@RequestBody ArtworkDto artworkDto) {
    try {
      Artwork result = artworkService.editArtworkInfo(artworkDto.getArtworkId(), artworkDto.getName(), artworkDto.getArtist(), artworkDto.getImage());
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to edit an artwork's loan availability and loan fee
   * 
   * @param artworkDto - ArtworkDto
   * @return edited artwork
   * @author Siger
   */
  @PutMapping(value = { "/artwork/loanInfo", "/artwork/loanInfo/" }, produces = "application/json")
  public ResponseEntity<?> editArtworkLoanInfo(@RequestBody ArtworkDto artworkDto) {
    try {
      Artwork result = artworkService.editArtworkLoan(artworkDto.getArtworkId(), artworkDto.getIsAvailableForLoan(), artworkDto.getLoanFee());
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to delete an artwork
   * 
   * @param id - long
   * @return if the artwork was deleted (success)
   * @author Siger
   */
  @DeleteMapping(value = { "/artwork/{id}", "/artwork/{id}/" })
  public ResponseEntity<?> deleteArtwork(@PathVariable("id") long id) {
    try {
      // Delete the artwork
      artworkService.deleteArtwork(id);
      return ResponseEntity.ok("Artwork deleted");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
