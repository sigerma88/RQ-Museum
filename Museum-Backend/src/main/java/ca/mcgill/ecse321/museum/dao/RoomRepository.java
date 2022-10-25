package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Room Model
 */
public interface RoomRepository extends CrudRepository<Room, Long>{

}
