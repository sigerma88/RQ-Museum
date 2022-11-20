package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;

/**
 * Crud Repository functionality given by Spring for Loan Model
 * 
 * @author Kieyan
 * @author Siger
 */

public interface LoanRepository extends CrudRepository<Loan, Long> {

  Loan findLoanByLoanId(Long loanId);

  Loan findLoanByVisitor(Visitor visitor);

  Loan findLoanByArtwork(Artwork artwork);

  void deleteLoanByLoanId(Long loanId);

  void deleteLoanByVisitor(Visitor visitor);

  void deleteLoanByArtwork(Artwork artwork);
}
