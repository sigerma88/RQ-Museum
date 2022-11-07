package ca.mcgill.ecse321.museum.dto;

public class ArtworkDto {

  private String name;
  private String artist;
  private boolean isAvailableForLoan;
  private double loanFee;
  private String image;

  public ArtworkDto() {

  }

  public ArtworkDto(String name, String artist, boolean isAvailableForLoan, double loanFee, String image) {
    this.name = name;
    this.artist = artist;
    this.isAvailableForLoan = isAvailableForLoan;
    this.loanFee = loanFee;
    this.image = image;

  }

  public String getName() {

    return name;
  }

  public boolean isAvailableForLoan() {
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
}
