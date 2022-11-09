package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class ArtworkRestController {

  @Autowired
  private ArtworkService artworkService;

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


}
