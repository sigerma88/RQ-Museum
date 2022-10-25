package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.Loan;
import org.springframework.data.repository.CrudRepository;

/**
 * Crud Repository functionality given by Spring for Loan Model
 * 
 * @author Kieyan
 * @author Siger
 */
public interface LoanRepository extends CrudRepository<Loan, Long> {

  Loan findLoanByLoanId(Long loanId);

}
