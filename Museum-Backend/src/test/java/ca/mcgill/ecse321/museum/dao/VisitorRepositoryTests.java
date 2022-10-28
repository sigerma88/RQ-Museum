package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.museum.model.Visitor;

/**
 * Visitor Repository test class
 * Here we test the visitor repository interface by saving a visitor into the database, querying for it,
 * and then checking if the results are consistent
 * 
 * @author Kieyan
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

    // Assert that visitorFromDB is not NULL
    assertNotNull(visitorFromDB);

    // Assert that object has correct attributes
    assertEquals(name, visitorFromDB.getName()); // test name
    assertEquals(email, visitorFromDB.getEmail()); // test email
    assertEquals(password, visitorFromDB.getPassword()); // test password
    assertEquals(visitor.getMuseumUserId(), visitorFromDB.getMuseumUserId()); // test visitorId
  }
}
