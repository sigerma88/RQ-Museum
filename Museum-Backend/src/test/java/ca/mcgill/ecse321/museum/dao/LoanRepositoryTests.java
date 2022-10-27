package ca.mcgill.ecse321.museum.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;

/**
 * Test class for the LoanRepository
 * Loan class has an unidirectional association 
 * with Artwork and Visitor classes.  Hence, we must create an 
 * instance of Artwork and Visitor to test LoanRepository.
 *  @author Zahra Landou
 * 
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
      loan.setRequestAccepted(true);
    
      //create artwork and attributes
      Artwork monalisa = new Artwork();
      String artworkName = "Mona";
      String artistName = "Léo";
      Double loanfee = 1200.00;
      String image = "sourire";


      //set attributes for artwork
      monalisa.setArtist(artistName);
      monalisa.setName(artworkName);
      monalisa.setLoanFee(loanfee);
      monalisa.setIsAvailableForLoan(true);
      monalisa.setImage(image);

      //create visitor and attributes
       String visitorName = "Bob";
       String visitorEmail = "bob@mail.com";
       String visitorPassword = "LaJoconde";
       Visitor visitor1 = new Visitor();

       //set attributes for visitor
       visitor1.setName(visitorName);
       visitor1.setEmail(visitorEmail);
       visitor1.setPassword(visitorPassword);

       //set associations
       loan.setArtwork(monalisa);
      loan.setVisitor(visitor1);

      //save objects
      monalisa = artworkRepository.save(monalisa);
      visitor1 = visitorRepository.save(visitor1);
      loan = loanRepository.save(loan);

      //get objects' ID
      long loanId = loan.getLoanId();
      long artworkId = monalisa.getArtworkId();
      long visitorId = visitor1.getMuseumUserId();
      
      //reset
      loan = null;
      visitor1 = null;
       monalisa = null;
      

      // read loan from database
      loan = loanRepository.findLoanByLoanId(loanId);
      visitor1 = visitorRepository.findVisitorByMuseumUserId(visitorId);
      monalisa = artworkRepository.findArtworkByArtworkId(artworkId);

     // assert that  loan has  correct attributes
      assertNotNull(loan);
      assertEquals(loanId, loan.getLoanId());
      assertEquals(true, loan.getRequestAccepted());
       
      //verify attributes for visitor
      assertNotNull(loan.getVisitor());
      assertEquals(visitorId,loan.getVisitor().getMuseumUserId());
      assertEquals(visitorName, loan.getVisitor().getName());
      assertEquals(visitorEmail, loan.getVisitor().getEmail());

      //verify attributes for artwork
      assertNotNull(loan.getArtwork());
      assertEquals(artworkId, loan.getArtwork().getArtworkId());
      assertEquals(artworkName, loan.getArtwork().getName());
      assertEquals(true,loan.getArtwork().getIsAvailableForLoan());



    }

}
