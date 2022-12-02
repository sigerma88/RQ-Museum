package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.AuthenticationUtility;
import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/loan")
public class LoanRestController {

  @Autowired
  private LoanService loanService;

  /**
   * RESTful API to get loan by their loan id
   *
   * @param loanId - Long Id loan
   * @return loanDto with loanId
   * @author Eric
   */
  @GetMapping(value = { "/{loanId}", "/{loanId}/" })
  public ResponseEntity<?> getLoanById(HttpServletRequest request,
      @PathVariable("loanId") Long loanId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Hello are not logged in");
      } else if (!AuthenticationUtility.isMuseumUser(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Your need to be a museum user to access this");
      }

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
   * RESTful API to get all loans by userId
   *
   * @return List of all loans
   * @author Eric
   */
  @GetMapping(value = { "/view/{userId}", "/view/{userId}/" })
  public ResponseEntity<?> getLoansByUserId(HttpServletRequest request, @PathVariable("userId") Long userId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isMuseumUser(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You need to be a museum user to access this");
      }
      List<LoanDto> loanDtos = new ArrayList<LoanDto>();
      for (Loan loan : loanService.getAllLoansByUserId(userId)) {
        loanDtos.add(DtoUtility.convertToDto(loan));
      }
      return new ResponseEntity<>(loanDtos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful API to get all loans
   *
   * @return List of all loans
   * @author Eric
   */
  @GetMapping(value = { "", "/" })
  public ResponseEntity<?> getLoans(HttpServletRequest request) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You need to be a staff member to access this");
      }
      List<LoanDto> loanDtos = new ArrayList<LoanDto>();
      for (Loan loan : loanService.getAllLoans()) {
        loanDtos.add(DtoUtility.convertToDto(loan));
      }
      return new ResponseEntity<>(loanDtos, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful API to put loan
   *
   * @param loanDto - RequestBody to be converted to a loanDto object
   * @return loanDto of patched loan if successful
   * @author Eric
   */
  @PutMapping(value = { "/edit", "/edit/" })
  public ResponseEntity<?> putLoan(HttpServletRequest request, @RequestBody LoanDto loanDto) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body("You need to be a staff member to access this");
      }

      return new ResponseEntity<>(
          ((DtoUtility.convertToDto(
              loanService.putLoanById(loanDto.getLoanId(), loanDto.getRequestAccepted())))),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful API to create a loan
   *
   * @param loanDto - RequestBody to be converted to a loanDto object
   * @return List of all
   * @author Eric
   */
  @PostMapping(value = { "/create", "/create/" })
  public ResponseEntity<?> postLoan(HttpServletRequest request, @RequestBody LoanDto loanDto) {
    try {

      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isMuseumUser(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("Your need to be a museum user to access this");
      }

      Loan persistedLoan = loanService.createLoan(loanDto);

      LoanDto persistedLoanDto = DtoUtility.convertToDto(persistedLoan);

      return new ResponseEntity<>(persistedLoanDto, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  /**
   * RESTful API to delete loan by their loanId
   *
   * @param loanId - PathVariable loanId of the loan to be deleted
   * @return String "Loan deleted" if successful
   * @author Eric
   */
  @DeleteMapping(value = { "/delete/{loanId}", "/delete/{loanId}/" })
  public ResponseEntity<?> deleteLoan(HttpServletRequest request,
      @PathVariable("loanId") Long loanId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isMuseumUser(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You need to be a museum user to access this");
      }

      loanService.deleteLoanByLoanId(loanId);

      return new ResponseEntity<>("Loan deleted", HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

}
