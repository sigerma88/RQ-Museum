/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.*;

// line 75 "model.ump"
// line 173 "model.ump"
@Entity
public class Room {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Room Attributes
  private long roomId;
  private String roomName;
  private int currentNumberOfArtwork;
  private RoomType roomType;

  // Room Associations
  private Museum museum;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public Room() {}

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setRoomId(long aRoomId) {
    boolean wasSet = false;
    roomId = aRoomId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomName(String aRoomName) {
    boolean wasSet = false;
    roomName = aRoomName;
    wasSet = true;
    return wasSet;
  }

  public boolean setCurrentNumberOfArtwork(int aCurrentNumberOfArtwork) {
    boolean wasSet = false;
    currentNumberOfArtwork = aCurrentNumberOfArtwork;
    wasSet = true;
    return wasSet;
  }

  public boolean setRoomType(RoomType aRoomType) {
    boolean wasSet = false;
    roomType = aRoomType;
    wasSet = true;
    return wasSet;
  }

  @GeneratedValue
  @Id
  public long getRoomId() {
    return roomId;
  }

  @Column(nullable = false)
  public String getRoomName() {
    return roomName;
  }

  @Column(nullable = false)
  public int getCurrentNumberOfArtwork() {
    return currentNumberOfArtwork;
  }

  @Column(nullable = false)
  public RoomType getRoomType() {
    return roomType;
  }

  /* Code from template association_GetOne */
  @ManyToOne()
  @JoinColumn(name = "museum_id", referencedColumnName = "museumId", nullable = false)
  public Museum getMuseum() {
    return museum;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setMuseum(Museum aNewMuseum) {
    boolean wasSet = false;
    if (aNewMuseum != null) {
      museum = aNewMuseum;
      wasSet = true;
    }
    return wasSet;
  }

  public String toString() {
    return super.toString() + "[" + "roomId" + ":" + getRoomId() + "," + "roomName" + ":"
        + getRoomName() + "," + "currentNumberOfArtwork" + ":" + getCurrentNumberOfArtwork() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "museum = "
        + (getMuseum() != null ? Integer.toHexString(System.identityHashCode(getMuseum())) : "null")
        + System.getProperties().getProperty("line.separator");
  }
}
