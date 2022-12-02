package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for loanController
 * 
 * @author Eric
 */

@Service
public class LoanService {

  @Autowired
  private LoanRepository loanRepository;

  @Autowired
  private ArtworkRepository artworkRepository;

  @Autowired
  private VisitorRepository visitorRepository;

  @Autowired
  private ArtworkService artworkService;

  /**
   * Method to get loan from the database by their loanId
   *
   * @param loanId - Long primary key of loan
   * @return loan identified by loanId
   * @author Eric
   */

  @Transactional
  public Loan getLoanById(Long loanId) {
    Loan loan = loanRepository.findLoanByLoanId(loanId);
    if (loan == null) {
      throw new IllegalArgumentException("Loan does not exist");
    }
    return loan;
  }

  /**
   * Method to get all loans from the database
   *
   * @return list<Loan> of all loans from the database
   * @author Eric
   */

  @Transactional
  public List<Loan> getAllLoans() {
    return toList(loanRepository.findAll());
  }

  /**
   * Method to get all loans from the database by the userId
   *
   * @return list<Loan> of all loans from the database
   * @author Eric
   */

  @Transactional
  public List<Loan> getAllLoansByUserId(Long userId) {
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(userId);
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor does not exist");
    }
    return toList(loanRepository.findLoanByVisitor(visitor));
  }

  /**
   * Method to create loan
   *
   * @param loanDto - loanDto
   * @return loan created using the attributes of loanDto
   * @author Eric
   */

  @Transactional
  public Loan createLoan(LoanDto loanDto) {
    // Error handling associations
    Artwork artwork = artworkRepository.findArtworkByArtworkId(loanDto.getArtworkDto().getArtworkId());
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(loanDto.getVisitorDto().getMuseumUserId());
    if (artwork == null) {
      throw new IllegalArgumentException("Artwork does not exist");
    }
    if (artwork.getIsAvailableForLoan() == false) {
      throw new IllegalArgumentException("Artwork is not available for loan");
    }
    if (artwork.getIsOnLoan() == true) {
      throw new IllegalArgumentException("Cannot create a loan request for an artwork that is on loan");
    }
    if (visitor == null) {
      throw new IllegalArgumentException("Visitor does not exist");
    }

    Loan loan = new Loan();
    loan.setRequestAccepted(loanDto.getRequestAccepted());
    loan.setArtwork(artwork);
    loan.setVisitor(visitor);

    // Error handling loan attributes
    if (loanDto.getRequestAccepted() != null) {
      throw new IllegalArgumentException("Loan getRequestAccepted must be null because only an employee can define");
    }
    if (loanRepository.findLoanByArtworkAndVisitor(artwork, visitor) != null) {
      throw new IllegalArgumentException("Cannot create a duplicate loan request");
    }

    Loan persistedLoan = loanRepository.save(loan);

    return persistedLoan;
  }

  /**
   * Method to update loan given loanId and requestAccepted
   *
   * @param loanId          - Long primary key of loan
   * @param requestAccepted - Boolean loan request
   * @return updated loan
   * @author Eric
   */

  @Transactional
  public Loan putLoanById(Long loanId, Boolean requestAccepted) {
    Loan loan = loanRepository.findLoanByLoanId(loanId);
    if (loan == null) {
      throw new IllegalArgumentException("Loan does not exist");
    }

    // If the patch is to accept the loan request, the artwork must be removed from
    // the room it's in
    loan.setRequestAccepted(requestAccepted);
    if (requestAccepted) {
      loan.setArtwork(artworkService.removeArtworkFromRoom(loan.getArtwork().getArtworkId()));
    }
    return loanRepository.save(loan);
  }

  /**
   * Method to delete loan given their loanId
   *
   * @param loanId - Long primary key of loan
   * @author Eric
   */

  @Transactional
  public void deleteLoanByLoanId(Long loanId) {
    Loan loan = loanRepository.findLoanByLoanId(loanId);
    if (loan == null) {
      throw new IllegalArgumentException("Loan does not exist");
    }

    loanRepository.deleteLoanByLoanId(loanId);
  }

  /**
   * Method to convert an Iterable to a List
   *
   * @param iterable -Iterable
   * @return List
   * @author Tutorial notes
   */

  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }

}
