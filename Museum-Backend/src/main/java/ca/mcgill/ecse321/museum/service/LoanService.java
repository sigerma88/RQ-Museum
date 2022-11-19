package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.ldap.LdapRepositoriesAutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;

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

    @Transactional
    public Loan getLoanById(Long loanId) {
        Loan loan = loanRepository.findLoanByLoanId(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan does not exist");
          }
        return loan;
    }

    @Transactional 
    public List<Loan> getAllLoans() {
        List<Loan> loans = toList(loanRepository.findAll());
        return loans;
    }

    @Transactional
    public Loan createLoan(Boolean aRequestAccepted, Long aArtworkId, Long aVisitorId) {
        // Error handling associations
        Artwork artwork = artworkRepository.findArtworkByArtworkId(aArtworkId);
        Visitor visitor = visitorRepository.findVisitorByMuseumUserId(aVisitorId);
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
        loan.setRequestAccepted(aRequestAccepted);
        loan.setArtwork(artwork);
        loan.setVisitor(visitor);

        // Error handling loan attributes
        if (aRequestAccepted != null) {
            throw new IllegalArgumentException("Loan getRequestAccepted must be null because only an employee can define");
        }
        // TODO: find out how findBySingleColumn works to determine if this implementation would work
        // loanRepository.findLoanByArtworkAndVisitor does not seem to work but this does 
        if (loanRepository.findLoanByArtworkAndVisitor(artwork, visitor).getLoanId() == loan.getLoanId()) {
            throw new IllegalArgumentException("Cannot create a duplicate loan request");
        }
        
        Loan persistedLoan = loanRepository.save(loan);

        return persistedLoan;
    }

    @Transactional
    public Loan patchLoanById(Long loanId, Boolean requestAccepted) {
        Loan loan = loanRepository.findLoanByLoanId(loanId);
        if (loan == null) {
            throw new IllegalArgumentException("Loan does not exist");
        }

        // If the patch is to accept the loan request, the artwork must be removed from the room it's in
        loan.setRequestAccepted(requestAccepted);
        if (requestAccepted) {
            loan.setArtwork(artworkService.removeArtworkFromRoom(loan.getArtwork().getArtworkId()));
        }
        return loanRepository.save(loan);
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
