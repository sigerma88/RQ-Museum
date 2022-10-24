/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

// line 75 "model.ump"
// line 173 "model.ump"
@Entity
public class Room {

  // ------------------------
  // ENUMERATIONS
  // ------------------------

  public enum RoomType {
    Small, Large, Storage
  }

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Room Attributes
  private long roomId;
  private String roomName;
  private int currentNumberOfArtwork;

  // Room Associations
  private Museum museum;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------
//no arg constructor
  public Room(){}
  public Room(long aRoomId, String aRoomName, int aCurrentNumberOfArtwork, Museum aMuseum,
      MuseumSystem aMuseumSystem) {
    roomId = aRoomId;
    roomName = aRoomName;
    currentNumberOfArtwork = aCurrentNumberOfArtwork;
    if (!setMuseum(aMuseum)) {
      throw new RuntimeException(
          "Unable to create Room due to aMuseum. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create room due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

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

  @GeneratedValue
  @Id
  public long getRoomId() {
    return roomId;
  }

  public String getRoomName() {
    return roomName;
  }

  public int getCurrentNumberOfArtwork() {
    return currentNumberOfArtwork;
  }

  /* Code from template association_GetOne */
  @ManyToOne
  public Museum getMuseum() {
    return museum;
  }

  /* Code from template association_GetOne */
  @ManyToOne
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
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

  /* Code from template association_SetOneToMany */
  public boolean setMuseumSystem(MuseumSystem aMuseumSystem) {
    boolean wasSet = false;
    if (aMuseumSystem == null) {
      return wasSet;
    }

    MuseumSystem existingMuseumSystem = museumSystem;
    museumSystem = aMuseumSystem;
    if (existingMuseumSystem != null && !existingMuseumSystem.equals(aMuseumSystem)) {
      existingMuseumSystem.removeRoom(this);
    }
    museumSystem.addRoom(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    museum = null;
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeRoom(this);
    }
  }


  public String toString() {
    return super.toString() + "[" + "roomId" + ":" + getRoomId() + "," + "roomName" + ":"
        + getRoomName() + "," + "currentNumberOfArtwork" + ":" + getCurrentNumberOfArtwork() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "museum = "
        + (getMuseum() != null ? Integer.toHexString(System.identityHashCode(getMuseum())) : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
