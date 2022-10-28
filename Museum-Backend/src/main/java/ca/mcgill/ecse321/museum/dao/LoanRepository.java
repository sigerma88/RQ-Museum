package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Loan;

/**
 * Crud Repository functionality given by Spring for Loan Model
 * 
 * @author Kieyan
 * @author Siger
 */
public interface LoanRepository extends CrudRepository<Loan, Long> {

  Loan findLoanByLoanId(Long loanId);

}
