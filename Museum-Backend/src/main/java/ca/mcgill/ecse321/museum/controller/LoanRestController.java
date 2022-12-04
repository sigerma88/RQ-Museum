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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

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
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
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

      // Check if loan is associated with the logged in visitor
      if (!AuthenticationUtility.isStaffMember(session)) {
        if (!AuthenticationUtility.checkUserId(session, loanDto.getVisitorDto().getMuseumUserId())) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to view this loan");
        }
      }

      // Return loanDto response
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
  @GetMapping(value = { "/user/{userId}", "/user/{userId}/" })
  public ResponseEntity<?> getLoansByUserId(HttpServletRequest request, @PathVariable("userId") Long userId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isMuseumUser(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You need to be a museum user to access this");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        if (!AuthenticationUtility.checkUserId(session, userId)) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to view this page");
        }
      }

      List<LoanDto> loanDtos = new ArrayList<LoanDto>();
      for (Loan loan : loanService.getAllLoansByUserId(userId)) {
        loanDtos.add(DtoUtility.convertToDto(loan));
      }
      return ResponseEntity.ok(loanDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get all loans by artworkId
   *
   * @param artworkId - Long Id artwork
   * @return List of all loans
   * @author Siger
   */
  @GetMapping(value = { "/artwork/{artworkId}", "/artwork/{artworkId}/" })
  public ResponseEntity<?> getLoansByArtworkId(HttpServletRequest request, @PathVariable("artworkId") Long artworkId) {
    try {
      HttpSession session = request.getSession();
      if (!AuthenticationUtility.isLoggedIn(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not logged in");
      } else if (!AuthenticationUtility.isStaffMember(session)) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body("You are not authorized to access this");
      }

      List<LoanDto> loanDtos = new ArrayList<LoanDto>();
      for (Loan loan : loanService.getAllLoansByArtworkId(artworkId)) {
        loanDtos.add(DtoUtility.convertToDto(loan));
      }
      return ResponseEntity.ok(loanDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
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
      return ResponseEntity.ok(loanDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
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

      return ResponseEntity
          .ok(DtoUtility.convertToDto(loanService.putLoanById(loanDto.getLoanId(), loanDto.getRequestAccepted())));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
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
      return ResponseEntity.ok(persistedLoanDto);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
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

      Loan loan = loanService.getLoanById(loanId);
      if (!AuthenticationUtility.isStaffMember(session)) {
        if (!AuthenticationUtility.checkUserId(session, loan.getVisitor().getMuseumUserId())) {
          // Check if loan is associated with the logged in visitor
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this loan");
        }
        // Check if loan is accepted and the delete is done by a visitor
        if (loan.getRequestAccepted() != null && loan.getRequestAccepted()) {
          return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
              .body("You are not authorized to delete a loan that has been accepted");
        }
      }

      loanService.deleteLoanByLoanId(loanId);
      return ResponseEntity.ok("Loan deleted");
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

}
