package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * Repository for Room class
 *
 * @author Zahra
 * @author Siger
 */

public interface RoomRepository extends CrudRepository<Room, Long> {

  Room findRoomByRoomId(Long roomId);

  List<Room> findRoomByMuseum(Museum museum);
}
