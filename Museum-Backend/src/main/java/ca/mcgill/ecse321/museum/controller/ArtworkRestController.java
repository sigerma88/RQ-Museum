package ca.mcgill.ecse321.museum.controller;


import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
public class ArtworkRestController {

  @Autowired
  private ArtworkService artworkService;

 /* @GetMapping(value = {"/artworks", "/artworks/"})
  public List<ArtworkDto> getAllArtworks() {

    return artworkService.getAllArtworks().stream().map(a -> convertToDto(a).collect(Collectors.toList()));

  }*/


  private ArtworkDto convertToDto(Artwork a) {

    if (a == null) throw new IllegalArgumentException("There is no such Artwork");
    ArtworkDto artworkDto = new ArtworkDto(a.getName(), a.getArtist(), a.getIsAvailableForLoan(), a.getLoanFee(), a.getImage());

    return artworkDto;

  }

}
