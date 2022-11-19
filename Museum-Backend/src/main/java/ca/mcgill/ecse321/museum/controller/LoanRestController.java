package ca.mcgill.ecse321.museum.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.service.ArtworkService;
import ca.mcgill.ecse321.museum.service.LoanService;

@CrossOrigin(origins = "*")
@RestController
public class LoanRestController {

    @Autowired
    private LoanService loanService;

    /**
     * This method is called when we need the information of a specific loan
     */
    @GetMapping(value = { "/loan/{loanId}", "/loan/{loanId}/" })
    public ResponseEntity<?> getLoanById(@PathVariable("loanId") Long loanId) {
        try {
            // Check if loan exists
            Loan loan = loanService.getLoanById(loanId);
            if (loan == null) {
            return ResponseEntity.badRequest().body("Loan does not exist");
            }
            LoanDto loanDto = DtoUtility.convertToDto(loan);
            return ResponseEntity.ok(loanDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * This method is called when employees want to see all loan requests
     */
    @GetMapping(value = { "/loans", "/loans/" }) 
    public ResponseEntity<?> getLoans() {
        try {
            List<LoanDto> loanDtos = new ArrayList<LoanDto>();
            for (Loan loan : loanService.getAllLoans()) {
              loanDtos.add(DtoUtility.convertToDto(loan));
            }
            return new ResponseEntity<>(loanDtos, HttpStatus.FOUND);
          } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
          }
    }

    /**
     * This method is called when an employee approves or denies a loan request
     */
    @PutMapping(value = { "/putLoan", "/putLoan/" })
    public ResponseEntity<?> putLoan(@RequestBody LoanDto loanDto) {
        try {
            return new ResponseEntity<>(((DtoUtility.convertToDto(loanService.putLoanById(loanDto.getLoanId(), loanDto.getRequestAccepted())))), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * postLoan method is called when a visitor wants to loan an Artwork (available for loan)
     * 
     * @param loan
     * @return
     */
    @PostMapping(value = { "/postLoan", "/postLoan/" })
    public ResponseEntity<?> postLoan(@RequestBody LoanDto loanDto) {
        try {

            Loan persistedLoan = loanService.createLoan(loanDto);

            LoanDto persistedLoanDto = DtoUtility.convertToDto(persistedLoan);

            return new ResponseEntity<>(persistedLoanDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
