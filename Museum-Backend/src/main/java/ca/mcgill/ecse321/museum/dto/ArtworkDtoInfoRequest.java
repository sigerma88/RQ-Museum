package ca.mcgill.ecse321.museum.dto;

/**
 * Artwork DTO
 *
 * @author Siger
 */

public class ArtworkDtoInfoRequest {

  private String name;
  private String artist;
  private String image;

  public ArtworkDtoInfoRequest() {
  }

  // Constructor with every attribute including associations
  public ArtworkDtoInfoRequest(String name, String artist, String image) {
    this.name = name;
    this.artist = artist;
    this.image = image;
  }

  public String getName() {
    return name;
  }

  public String getArtist() {
    return artist;
  }

  public String getImage() {
    return image;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setArtist(String artist) {
    this.artist = artist;
  }

  public void setImage(String image) {
    this.image = image;
  }
}
