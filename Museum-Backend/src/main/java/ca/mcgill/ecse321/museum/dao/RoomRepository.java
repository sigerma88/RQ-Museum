package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;
import ca.mcgill.ecse321.museum.model.Room;

/**
 * @Author Zahra Landou
 * Repository for Room class
 */
public interface RoomRepository extends CrudRepository<Room, Long> {

    Room findRoomByRoomId(Long roomId);

}
