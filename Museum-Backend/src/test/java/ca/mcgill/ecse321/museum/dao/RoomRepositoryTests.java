package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Ticket;

/**
 * Test class for the RoomRepository
 * @author VZ
 * 
 * Since the Room class has a unidirectional association with the Museum class, and the 
 * Museum class has a unidirectional association with the Schedule class, we must create a Room, 
 * a Museum and a Schedule object in order to test the RoomRepository.
 */

@SpringBootTest
public class RoomRepositoryTests {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private MuseumRepository museumRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @AfterEach
    public void clearDatabase() {
        roomRepository.deleteAll();
        museumRepository.deleteAll();
        ticketRepository.deleteAll();
    }

    @Test
    public void testPersistandLoadRoom() {
        
        //1. create object
        //create room and attributes
        Room room = new Room();
        String roomName = "Room 1";
        int currentNumberOfArtwork = 0;
        RoomType roomType = RoomType.Large;

        //set attributes of room
        room.setRoomName(roomName);
        room.setCurrentNumberOfArtwork(currentNumberOfArtwork);
        room.setRoomType(roomType);

        //create museum and attributes
        Museum museum = new Museum();
        String museumName = "Museum 1";
        Double visitFee = 9.99;

        //set attributes of museum
        museum.setName(museumName);
        museum.setVisitFee(visitFee);
    
        //create schedule
        Schedule schedule = new Schedule();

        //2. save object

        //set association between museum and schedule, then save museum object to database
        museum.setSchedule(schedule);
        museum = museumRepository.save(museum);
        // set association between room and museum, then save room object to database
        room.setMuseum(museum);
        room = roomRepository.save(room);
        long id = room.getRoomId();

        //3. read object from database
        room = roomRepository.findRoomByRoomId(id);

        //4. assert that room object has correct attributes
        assertNotNull(room);
        assertEquals(id, room.getRoomId());
        assertEquals(roomName, room.getRoomName());
        assertEquals(currentNumberOfArtwork, room.getCurrentNumberOfArtwork());
        assertEquals(roomType, room.getRoomType());
        assertNotNull(room.getMuseum());
        assertEquals(museumName, room.getMuseum().getName());
        assertEquals(visitFee, room.getMuseum().getVisitFee());
        assertNotNull(room.getMuseum().getSchedule());


    }
}
