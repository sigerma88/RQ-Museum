package ca.mcgill.ecse321.museum.dto;

/**
 * Artwork DTO
 *
 * @author Siger
 */

public class ArtworkDtoLoanInfoRequest {

  private Boolean isAvailableForLoan;
  private Double loanFee;

  public ArtworkDtoLoanInfoRequest() {
  }

  // Constructor with every attribute including associations
  public ArtworkDtoLoanInfoRequest(Boolean isAvailableForLoan, Double loanFee) {
    this.isAvailableForLoan = isAvailableForLoan;
    this.loanFee = loanFee;
  }

  public Boolean getIsAvailableForLoan() {
    return isAvailableForLoan;
  }

  public Double getLoanFee() {
    return loanFee;
  }

  public void setIsAvailableForLoan(Boolean availableForLoan) {
    isAvailableForLoan = availableForLoan;
  }

  public void setLoanFee(Double loanFee) {
    this.loanFee = loanFee;
  }
}
