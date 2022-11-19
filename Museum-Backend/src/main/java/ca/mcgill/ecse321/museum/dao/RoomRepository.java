package ca.mcgill.ecse321.museum.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;


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
