package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Room;

/**
 * Repository for Room class
 * 
 * @author Zahra
 */
public interface RoomRepository extends CrudRepository<Room, Long> {

  Room findRoomByRoomId(Long roomId);

}
