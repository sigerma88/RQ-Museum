package ca.mcgill.ecse321.museum.dto;

/**
 * Artwork DTO
 *
 * @author Zahra
 */
public class ArtworkDto {

  private long artworkId;
  private String name;
  private String artist;
  private boolean isAvailableForLoan;
  private double loanFee;
  private String image;
  private Boolean isOnLoan;

  public ArtworkDto() {
  }

  public ArtworkDto(Long artworkId, String name, String artist, Boolean isAvailableForLoan, double loanFee, String image, Boolean isOnLoan) {
    this.artworkId = artworkId;
    this.name = name;
    this.artist = artist;
    this.isAvailableForLoan = isAvailableForLoan;
    this.loanFee = loanFee;
    this.image = image;
    this.isOnLoan = isOnLoan;
  }

  public String getName() {
    return name;
  }

  public boolean getIsAvailableForLoan() {
    return isAvailableForLoan;
  }

  public String getArtist() {
    return artist;
  }

  public double getLoanFee() {
    return loanFee;
  }

  public String getImage() {
    return image;
  }

  public Boolean getIsOnLoan() {
    return isOnLoan;
  }

  public long getArtworkId() {
    return artworkId;
  }

  public void setArtworkId(long artworkId) {
    this.artworkId = artworkId;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public void setIsAvailableForLoan(boolean availableForLoan) {
    isAvailableForLoan = availableForLoan;
  }

  public void setLoanFee(double loanFee) {
    this.loanFee = loanFee;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public void setIsOnLoan(Boolean onLoan) {
    isOnLoan = onLoan;
  }
}
