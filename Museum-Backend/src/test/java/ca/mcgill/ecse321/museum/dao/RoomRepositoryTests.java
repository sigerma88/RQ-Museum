package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;

@SpringBootTest
public class RoomRepositoryTests {
    @Autowired
    private RoomRepository roomRepository;

    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
    }

    @Test
    public void testPersistandLoadRoom() {
        
        //create object
        Room room = new Room();
        room.setRoomName("Room 1");
        room.setCurrentNumberOfArtwork(0);
        room.setRoomType(RoomType.Large);

        //save object
        room = roomRepository.save(room);
        long id = room.getRoomId();

        //read object from database
        room = roomRepository.findRoomByRoomId(id);

        //assert that object has correct attributes
        assertNotNull(room);
        assertEquals(id, room.getRoomId());
        assertEquals("Room 1", room.getRoomName());
    }
}
