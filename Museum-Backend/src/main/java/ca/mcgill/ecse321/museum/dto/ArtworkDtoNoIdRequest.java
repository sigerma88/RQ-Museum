package ca.mcgill.ecse321.museum.dto;

/**
 * Artwork DTO
 * 
 * @author Siger
 */

public class ArtworkDtoNoIdRequest {

  private String name;
  private String artist;
  private Boolean isAvailableForLoan;
  private Double loanFee;
  private String image;
  private Boolean isOnLoan;
  private Long roomId;

  public ArtworkDtoNoIdRequest() {
  }

  // Constructor with every attribute including associations
  public ArtworkDtoNoIdRequest(String name, String artist, Boolean isAvailableForLoan, Double loanFee, String image,
      Boolean isOnLoan, Long roomId) {
    this.name = name;
    this.artist = artist;
    this.isAvailableForLoan = isAvailableForLoan;
    this.loanFee = loanFee;
    this.image = image;
    this.isOnLoan = isOnLoan;
    this.roomId = roomId;
  }

  public String getName() {
    return name;
  }

  public String getArtist() {
    return artist;
  }

  public Boolean getIsAvailableForLoan() {
    return isAvailableForLoan;
  }

  public Double getLoanFee() {
    return loanFee;
  }

  public String getImage() {
    return image;
  }

  public Boolean getIsOnLoan() {
    return isOnLoan;
  }

  public Long getRoomId() {
    return roomId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public void setIsAvailableForLoan(Boolean availableForLoan) {
    isAvailableForLoan = availableForLoan;
  }

  public void setLoanFee(Double loanFee) {
    this.loanFee = loanFee;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setIsOnLoan(Boolean onLoan) {
    isOnLoan = onLoan;
  }

  public void setRoom(Long roomId) {
    this.roomId = roomId;
  }
}
