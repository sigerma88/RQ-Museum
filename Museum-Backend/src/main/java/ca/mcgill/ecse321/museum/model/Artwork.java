/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.*;

// line 57 "model.ump"
// line 163 "model.ump"
@Entity
public class Artwork {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Artwork Attributes
  private long artworkId;
  private String name;
  private String artist;
  private boolean isAvailableForLoan;
  private double loanFee;
  private String image;

  // Artwork Associations
  private Room room;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public Artwork() {}

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setArtworkId(long aArtworkId) {
    boolean wasSet = false;
    artworkId = aArtworkId;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setArtist(String aArtist) {
    boolean wasSet = false;
    artist = aArtist;
    wasSet = true;
    return wasSet;
  }

  public boolean setIsAvailableForLoan(boolean aIsAvailableForLoan) {
    boolean wasSet = false;
    isAvailableForLoan = aIsAvailableForLoan;
    wasSet = true;
    return wasSet;
  }

  public boolean setLoanFee(double aLoanFee) {
    boolean wasSet = false;
    loanFee = aLoanFee;
    wasSet = true;
    return wasSet;
  }

  public boolean setImage(String aImage) {
    boolean wasSet = false;
    image = aImage;
    wasSet = true;
    return wasSet;
  }

  @Id
  @GeneratedValue
  public long getArtworkId() {
    return artworkId;
  }

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  @Column(nullable = false)
  public String getArtist() {
    return artist;
  }

  @Column(nullable = false)
  public boolean getIsAvailableForLoan() {
    return isAvailableForLoan;
  }

  @Column(nullable = true)
  public double getLoanFee() {
    return loanFee;
  }

  @Column(nullable = false)
  public String getImage() {
    return image;
  }

  /* Code from template association_GetOne */
  @ManyToOne()
  @JoinColumn(name = "room_id", referencedColumnName = "roomId", nullable = true)
  public Room getRoom() {
    return room;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setRoom(Room aNewRoom) {
    boolean wasSet = false;
    if (aNewRoom != null) {
      room = aNewRoom;
      wasSet = true;
    }
    return wasSet;
  }


  public String toString() {
    return super.toString() + "[" + "artworkId" + ":" + getArtworkId() + "," + "name" + ":"
        + getName() + "," + "artist" + ":" + getArtist() + "," + "isAvailableForLoan" + ":"
        + getIsAvailableForLoan() + "," + "loanFee" + ":" + getLoanFee() + "," + "image" + ":"
        + getImage() + "]" + System.getProperties().getProperty("line.separator") + "  " + "room = "
        + (getRoom() != null ? Integer.toHexString(System.identityHashCode(getRoom())) : "null")
        + System.getProperties().getProperty("line.separator");
  }
}
