package ca.mcgill.ecse321.museum.dto;

import ca.mcgill.ecse321.museum.model.Room;


/**
 * @author kianmamicheafara
 * ArtworkDto class is used as the object which we return from our API to our web application
 */
public class ArtworkDto {

    // Artwork Attributes
    private long artworkId;
    private String name;
    private String artist;
    private boolean isAvailableForLoan;
    private double loanFee;
    private String image;

    private boolean isOnLoan;

    // Artwork Associations
    private Room room;

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

    // Getters for ArtworkDTO
    public long getArtworkId() {
        return artworkId;
    }
    public String getName() {
        return name;
    }
    public String getArtist() {
        return artist;
    }
    public boolean getIsAvailableForLoan() {
        return isAvailableForLoan;
    }
    public double getLoanFee() {
        return loanFee;
    }
    public String getImage() {
        return image;
    }
    public Room getRoom() {
        return room;
    }
    public Boolean getIsOnLoan() {return isOnLoan;}

    // Setter for linked attribute to the ArtworkDto
    public void setRoom(Room room){
        this.room = room;
    }

}
