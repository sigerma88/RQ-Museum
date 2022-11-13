package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ca.mcgill.ecse321.museum.model.RoomType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * @author kianmamicheafara
 * Artwork service is a class where we can perform services on the artwork class
 */

@Service
public class ArtworkService {

    @Autowired
    ArtworkRepository artworkRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomService roomService;

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

    // FR3 Function which gets all artworks for a given room
    public List<Artwork> getAllArtworksInRoom(long roomId){
        List<Artwork> artworkList = new ArrayList<Artwork>();
        Iterable<Artwork> artworkIterable = artworkRepository.findAll();
        Iterator<Artwork> artworkIterator = artworkIterable.iterator();

        while(artworkIterator.hasNext()){
            Artwork currentArtwork = artworkIterator.next();
            if (currentArtwork.getRoom() != null) {
                if (currentArtwork.getRoom().getRoomId() == roomId) {
                    // add artwork to list if it has the room id we want to query for
                    artworkList.add(currentArtwork);
                }
            }
        }
        return artworkList;
    }

    // FR3 Function which checks how many artworks in single room
    public int getNumberOfArtworksInRoom(long roomId){
        int count = 0;
        Iterable<Artwork> artworkIterable = artworkRepository.findAll();
        Iterator<Artwork> artworkIterator = artworkIterable.iterator();
        while(artworkIterator.hasNext()){
            Artwork currentArtwork = artworkIterator.next();
            if (currentArtwork.getRoom() != null){
                if(currentArtwork.getRoom().getRoomId() == roomId){
                    // increment counter to se how many artworks in room
                    count++;
                }
            }
        }
        return count;
    }

    // FR3 Function which moves artwork to corresponding room
    public int moveArtworkToRoom(long artworkId,long roomId){
        // Find artwork
        Optional<Artwork> artworkOptional = artworkRepository.findById(artworkId);
        if (artworkOptional.isPresent() == false){
            // -1 corresponds to error
            return -1;
        }
        Artwork artwork = artworkOptional.get();

        // Find room
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (roomOptional.isPresent() == false){
            // -1 corresponds to error
            return -1;
        }
        Room room = roomOptional.get();

        // Update room of artwork if the room has capacity
        if (roomService.getRoomCapacity(roomId) > 0){
            // Reduce amount of artwork in old room
            Room oldRoom = artwork.getRoom();
            if (oldRoom != null){
                oldRoom.setCurrentNumberOfArtwork(oldRoom.getCurrentNumberOfArtwork()-1);
                roomRepository.save(oldRoom);
            }

            // increase amount of artwork in new room
            room.setCurrentNumberOfArtwork(room.getCurrentNumberOfArtwork()+1);
            artwork.setRoom(room);
            artworkRepository.save(artwork);

            // 0 corresponds to no error
            return 0;
        }else{
            // -2 corresponds to room being full
            return -2;
        }
    }


    // FR3 Function which removes artwork from room
    public int removeArtworkFromRoom(long artworkId){
        // Find artwork
        Optional<Artwork> artworkOptional = artworkRepository.findById(artworkId);
        if (artworkOptional.isPresent() == false){
            // -1 corresponds to error
            return -1;
        }
        Artwork artwork = artworkOptional.get();

        // decrement number of artworks in room by 1
        Room room = artwork.getRoom();
        if (room != null){
            room.setCurrentNumberOfArtwork(room.getCurrentNumberOfArtwork() - 1);
            roomRepository.save(room);
        }

        // Update room
        artwork.setRoom(null);
        artworkRepository.save(artwork);

        // 0 corresponds to no error
        return 0;
    }


}
