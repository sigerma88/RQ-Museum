package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.MuseumSystem;
import ca.mcgill.ecse321.museum.model.Schedule;


/**
 * Test the persistence layer for the ManagerRepository. Testing reading and writing of 
 * objects, attributes and references to the database.
 * 
 * @author Eric
 */
@SpringBootTest
public class ManagerRepositoryTests {
    @Autowired
    private ManagerRepository managerRepository;

    @AfterEach
    public void clearDatabase() {
        managerRepository.deleteAll();
    }

    @Test
    public void testPersistandLoadManager() {
        
        //create manager 
        Manager manager = new Manager();
        manager.setName("Jeremy");
        manager.setEmail("aEmail");
        manager.setPassword("aPassword");

        //save manager
        manager = managerRepository.save(manager);
        long id = manager.getMuseumUserId();

        //reset manager
        manager = null;

        //read manager from database
        manager = managerRepository.findManagerByMuseumUserId(id);

        //assert that manager has correct attributes
        assertNotNull(manager);
        assertEquals(id, manager.getMuseumUserId());
        assertEquals("Jeremy", manager.getName());
        assertEquals("aEmail", manager.getEmail());
        assertEquals("aPassword", manager.getPassword());
    }
}
