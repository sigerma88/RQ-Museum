package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.museum.model.RoomType;

import java.util.Optional;

@Service
public class ArtworkService {

    @Autowired
    ArtworkRepository artworkRepository;

    @Transactional
    public String getArtworkStatus(long id) {
        Optional<Artwork> artworkOptional = artworkRepository.findById(id);
        if (artworkOptional.isPresent() == false){
            return "none";
        }
        Artwork artwork = artworkOptional.get();
        // Find out the status of the artwork OPTIONS: loan / on display / in storage
        // 1. Check if it is on loan:
        if (artwork.getIsOnLoan() == true){
            return "loan";
        }

        Room roomOfArtwork = artwork.getRoom();
        RoomType roomType = roomOfArtwork.getRoomType();
        if (roomType == RoomType.Storage){
            return "storage";
        }

        if (roomType == RoomType.Small || roomType == RoomType.Large){
            return "display";
        }

        return "none";

    }



}
