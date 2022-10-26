package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Kieyan
 * Visitor Repository test class
 * Here we test the visitor repository interface by saving a loan into the database, querying for it,
 * and then checking if the results are consistent
 */
@SpringBootTest
public class VisitorRepositoryTests {

    @Autowired
    VisitorRepository visitorRepository;

    @AfterEach
    public void clearDatabase() {
        visitorRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadVisitor() {
        // Create object
        String email = "kian@gmail.com";
        String name = "Kian";
        String password = "123Kian";
        Visitor visitor = new Visitor();
        visitor.setEmail(email);
        visitor.setName(name);
        visitor.setPassword(password);

        // Save object
        visitorRepository.save(visitor);
        long id = visitor.getMuseumUserId();

        // Read object from database
        Visitor visitorFromDB = visitorRepository.findVisitorByMuseumUserId(id);

        // Assert that object has correct attributes
        Assert.assertEquals(visitor.getName(), visitorFromDB.getName()); // test name
        Assert.assertEquals(visitor.getEmail(), visitorFromDB.getEmail()); // test name
        Assert.assertEquals(visitor.getPassword(), visitorFromDB.getPassword()); // test name
        Assert.assertEquals(visitor.getMuseumUserId(), visitorFromDB.getMuseumUserId()); // test name

    }

}
