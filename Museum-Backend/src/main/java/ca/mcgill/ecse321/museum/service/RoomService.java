package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Transactional
    public int getRoomCapacity(long id) {
        Optional<Room> roomOptional = roomRepository.findById(id);

        // The room is not in the DB so capacity is 0
        if (roomOptional.isPresent() == false){
            return 0;
        }
        Room room = roomOptional.get();
        RoomType roomType = room.getRoomType();

        // Large room has a capacity of 300
        if (roomType == RoomType.Large){
            int numberOfArtwork = room.getCurrentNumberOfArtwork();
            int capacity = 300 - numberOfArtwork;
            if (capacity < 0){
                return 0;
            } else if (capacity > 300) {
                return 300;
            } else{
                return capacity;
            }
        }

        // Small room has a capacity of 200
        if (roomType == RoomType.Small){
            int numberOfArtwork = room.getCurrentNumberOfArtwork();
            int capacity = 200 - numberOfArtwork;
            if (capacity < 0){
                return 0;
            } else if (capacity > 200) {
                return 200;
            } else{
                return capacity;
            }
        }

        // Storage has an almost unlimited capacity
        if (roomType == RoomType.Storage){
            return 999999;
        }

        // Error returns -1
        return -1;

    }
}
