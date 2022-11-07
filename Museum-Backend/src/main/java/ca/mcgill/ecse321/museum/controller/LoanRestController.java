package ca.mcgill.ecse321.museum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.model.Loan;

public class LoanRestController {

    @GetMapping(value = { "/loan{LoanId}", "/loan{LoanId}/" })
    public LoanDto getLoanById(@PathVariable("LoanId") Long loanId) {
	    Loan loan = LoanService.getLoanById(loanId);
        LoanDto loanDto = convertToDto(loan);
        return loanDto;
    }





    private LoanDto convertToDto(Loan loan) {
        if (loan == null) {
            throw new IllegalArgumentException("There is no such Loan!");
        }
        VisitorDto visitorDto = convertToDto(loan.getVisitor());
        ArtworkDto artworkDto = convertToDto(loan.getArtwork());
        LoanDto loanDto = new LoanDto(loan.getRequestAccepted(), visitorDto, artworkDto);
        return loanDto;
    }

    private VisitorDto convertToDto(Visitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("There is no such Visitor!");
        }
        VisitorDto visitorDto = new VisitorDto();
        return visitorDto;
    }

    private ArtworkDto convertToDto(Artwork artwork) {
        if (artwork == null) {
            throw new IllegalArgumentException("There is no such Artwork!");
        }
        ArtworkDto artworkDto = new ArtWorkDto();
        return artworkDto;
    }

}
