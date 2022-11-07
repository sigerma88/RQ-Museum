package ca.mcgill.ecse321.museum.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.LoanService;

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
        /**
         * TODO
         * Modify the arguments passed in the VisitorDto constructor to properly construct the VisitorDto
         */
        VisitorDto visitorDto = new VisitorDto();
        return visitorDto;
    }

    private ArtworkDto convertToDto(Artwork artwork) {
        if (artwork == null) {
            throw new IllegalArgumentException("There is no such Artwork!");
        }
        /**
         * TODO
         * Modify the arguments passed in the ArtworkDto constructor to properly construct the ArtworkDto
         */
        ArtworkDto artworkDto = new ArtworkDto();
        return artworkDto;
    }

}
