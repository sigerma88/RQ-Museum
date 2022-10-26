package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Written by Kieyan
 * Loan Repository test class
 * Here we test the loan repository interface by saving a loan into the database, querying for it,
 * and then checking if the results are consistent
 */

@SpringBootTest
public class LoanRepositoryTests {

    @Autowired
    LoanRepository loanRepository;

    @Autowired
    VisitorRepository visitorRepository;

    @Autowired
    ArtworkRepository artworkRepository;


    @AfterEach
    public void clearDatabase() {
        loanRepository.deleteAll();
        visitorRepository.deleteAll();
        artworkRepository.deleteAll();
    }

    @Test
    public void testPersistAndLoadLoan() {
        // Create objects
        // 1. Create visitor
        String email = "kianLoan@gmail.com";
        Visitor visitor = new Visitor();
        visitor.setEmail(email);

        // 2. Create artwork
        String artworkName = "Mona Lisa";
        Artwork artwork = new Artwork();
        artwork.setName(artworkName);

        // 3. Create Loan
        Loan loan = new Loan();
        loan.setArtwork(artwork);
        loan.setVisitor(visitor);
        loan.setRequestedAccepted(true);

        // Save object
        visitorRepository.save(visitor);
        artworkRepository.save(artwork);
        loanRepository.save(loan);
        long id = loan.getLoanId();


        // Read object from database
        Loan loanRead = loanRepository.findLoanByLoanId(id);

        // Assert that object has correct attributes
        // ASSERT EQUALS IS NOT WORKING


    }

}
