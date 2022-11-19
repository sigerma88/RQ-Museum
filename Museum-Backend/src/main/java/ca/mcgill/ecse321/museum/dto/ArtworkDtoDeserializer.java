package ca.mcgill.ecse321.museum.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArtworkDtoDeserializer {
    @JsonProperty("ArtworkDto")
    private Long artworkId;
    @JsonProperty("ArtworkDto")
    private String name;
    @JsonProperty("ArtworkDto")
    private String artist;
    @JsonProperty("ArtworkDto")
    private Boolean isAvailableForLoan;
    @JsonProperty("ArtworkDto")
    private Double loanFee;
    @JsonProperty("ArtworkDto")
    private String image;
    @JsonProperty("ArtworkDto")
    private Boolean isOnLoan;
    @JsonProperty("ArtworkDto")
    private RoomDto room;
    
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
