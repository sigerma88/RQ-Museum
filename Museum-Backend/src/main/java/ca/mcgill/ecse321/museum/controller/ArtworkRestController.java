package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.model.*;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kianmamicheafara
 * ArtworkRestController class is used as a controller where we call
 * our API for our web application
 */

@CrossOrigin(origins = "*")
@RestController
public class ArtworkRestController {

    @Autowired
    ArtworkService artworkService;

    @Autowired
    ArtworkRepository artworkRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    MuseumRepository museumRepository;

    // Getting artwork status - FR7
    // Returns a status string of 4 options: loan/on display/in storage
        // 1. "none" -> The artwork doesn't exist
        // 2. "loan" -> The artwork is on loan
        // 3. "display" -> The artwork is on Display
        // 4. "storage" -> The artwork is in storage
    @GetMapping(value = "/getArtworkStatus/{id}")
    public ResponseEntity<?> getArtworkStatus(@PathVariable("id") long id) {
        try {
            String status = artworkService.getArtworkStatus(id);
            if (status.equals("none")){
                return new ResponseEntity<>("Error getting status", HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(status, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/getAllArtworksInRoom/{roomId}")
    public ResponseEntity<?> getAllArtworksInRoom(@PathVariable("roomId") long roomId) {
        try {
            List<Artwork> listOfArtworksThatBelongToRoom = artworkService.getAllArtworksInRoom(roomId);
            List<ArtworkDto> artworkDtos = new ArrayList<>();
            for (Artwork artwork: listOfArtworksThatBelongToRoom){
                artworkDtos.add(new ArtworkDto(artwork.getArtworkId(), artwork.getName(), artwork.getArtist(), artwork.getIsAvailableForLoan(), artwork.getLoanFee(), artwork.getImage(), artwork.getIsOnLoan(), artwork.getRoom()));
            }
            return new ResponseEntity<>(artworkDtos, HttpStatus.OK);
        }catch (Exception e){
            // return error message
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

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



    @PutMapping(value = "/moveArtworkToRoom/{artworkId}/{roomId}")
    public ResponseEntity<?> moveArtworkToRoom(@PathVariable("artworkId") long artworkId, @PathVariable("roomId") long roomId){
        try{
            int result = artworkService.moveArtworkToRoom(artworkId, roomId);
            if (result == 0){
                return new ResponseEntity<>("Successfully moved artwork to room", HttpStatus.OK);
            } else if (result ==- 2) {
                return new ResponseEntity<>("Room has no capacity left", HttpStatus.BAD_REQUEST);
            }else {
                return new ResponseEntity<>("There was an error", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
