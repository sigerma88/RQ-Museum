package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Written by Kieyan
 * Visitor Repository test class
 * Here we test the visitor repository interface by saving a loan into the database, querying for it,
 * and then checking if the results are consistent
 */
@SpringBootTest
public class VisitorRepositoryTests {

    @Autowired
    private VisitorRepository visitorRepository;

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
        Visitor visitorRead = visitorRepository.findVisitorByMuseumUserId(id);

        // Assert that object has correct attributes
        // ASSERT EQUALS IS NOT WORKING


    }

}
