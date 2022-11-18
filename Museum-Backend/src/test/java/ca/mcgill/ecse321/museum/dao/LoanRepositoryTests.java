package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;

/**
 * Test the persistence layer for the LoanRepository. Testing reading and writing of
 * objects, attributes and references to the database.
 * LoanRepository is associated with Artwork and Visitor.
 *
 * @author Zahra
 */
@SpringBootTest
public class LoanRepositoryTests {
  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private ArtworkRepository artworkRepository;

  @Autowired
  private VisitorRepository visitorRepository;

  @AfterEach
  public void clearDatabase() {
    loanRepository.deleteAll();
    artworkRepository.deleteAll();
    visitorRepository.deleteAll();
  }

  @Test
  public void testPersistAndLoadLoan() {
    // create loan
    Loan loan = new Loan();
    Boolean isRequestAccepted = true;
    loan.setRequestAccepted(isRequestAccepted);

    //create artwork
    Artwork monalisa = new Artwork();
    String artworkName = "Mona";
    String artistName = "Léo";
    Double loanFee = 1200.00;
    String image = "smile";
    Boolean isAvailableForLoan = true;
    Boolean isOnLoan = false;

    //set attributes for artwork
    monalisa.setArtist(artistName);
    monalisa.setName(artworkName);
    monalisa.setLoanFee(loanFee);
    monalisa.setIsAvailableForLoan(isAvailableForLoan);
    monalisa.setImage(image);
    monalisa.setIsOnLoan(isOnLoan);

    //create visitor
    String visitorName = "Bob";
    String visitorEmail = "bob@mail.com";
    String visitorPassword = "LaJoconde";
    Visitor visitor = new Visitor();

    //set attributes for visitor
    visitor.setName(visitorName);
    visitor.setEmail(visitorEmail);
    visitor.setPassword(visitorPassword);

    //set associations
    loan.setArtwork(monalisa);
    loan.setVisitor(visitor);

    //save artwork, visitor and loan
    monalisa = artworkRepository.save(monalisa);
    visitor = visitorRepository.save(visitor);
    loan = loanRepository.save(loan);

    //get loan, artwork and visitor Id
    long loanId = loan.getLoanId();
    long artworkId = monalisa.getArtworkId();
    long visitorId = visitor.getMuseumUserId();

    //reset
    loan = null;
    visitor = null;
    monalisa = null;

    //read loan, artwork and visitor from database
    loan = loanRepository.findLoanByLoanId(loanId);
    monalisa = artworkRepository.findArtworkByArtworkId(artworkId);
    visitor = visitorRepository.findVisitorByMuseumUserId(visitorId);

    // assert that loan exist in database and has correct attributes
    assertNotNull(loan);
    assertEquals(loanId, loan.getLoanId());
    assertEquals(isRequestAccepted, loan.getRequestAccepted());

    //assert that loan has correct associations
    assertNotNull(loan.getVisitor());
    assertEquals(visitorId, loan.getVisitor().getMuseumUserId());

    //assert that loan has correct associations
    assertNotNull(loan.getArtwork());
    assertEquals(artworkId, loan.getArtwork().getArtworkId());

    //reset
    loan = null;

    //get loan from artworkId and visitorId
    loan = loanRepository.findLoanByArtworkIdAndVisitorId(artworkId, visitorId);

    assertNotNull(loan);
  }
}
