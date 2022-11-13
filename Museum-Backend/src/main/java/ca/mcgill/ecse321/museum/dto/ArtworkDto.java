package ca.mcgill.ecse321.museum.dto;

/**
 * Artwork DTO
 *
 * @author Zahra
 * @author Siger
 */
public class ArtworkDto {

  private Long artworkId;
  private String name;
  private String artist;
  private Boolean isAvailableForLoan;
  private Double loanFee;
  private String image;
  private Boolean isOnLoan;
  private RoomDto room;

  public ArtworkDto() {
  }

  // Constructor with every attribute including associations
  public ArtworkDto(Long artworkId, String name, String artist, Boolean isAvailableForLoan, Double loanFee, String image, Boolean isOnLoan, RoomDto room) {
    this.artworkId = artworkId;
    this.name = name;
    this.artist = artist;
    this.isAvailableForLoan = isAvailableForLoan;
    this.loanFee = loanFee;
    this.image = image;
    this.isOnLoan = isOnLoan;
    this.room = room;
  }

  // Constructor with all attributes except associations - associations can be set
  public ArtworkDto(long artworkId, String name, String artist, boolean isAvailableForLoan, double loanFee, String image, boolean isOnLoan) {
    this.artworkId = artworkId;
    this.name = name;
    this.artist = artist;
    this.isAvailableForLoan = isAvailableForLoan;
    this.loanFee = loanFee;
    this.image = image;
    this.isOnLoan = isOnLoan;
}

  public Long getArtworkId() {
    return artworkId;
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

  public RoomDto getRoom() {
    return room;
  }

  public void setArtworkId(Long artworkId) {
    this.artworkId = artworkId;
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

  public void setRoom(RoomDto room) {
    this.room = room;
  }
}
