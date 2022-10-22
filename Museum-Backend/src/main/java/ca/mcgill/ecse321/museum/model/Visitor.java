/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


// line 18 "model.ump"
// line 135 "model.ump"
public class Visitor extends MuseumUser {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Visitor Attributes
  private long visitorId;
  private int numOfPass;

  // Visitor Associations
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Visitor(String aEmail, String aName, String aPassword, long aVisitorId, int aNumOfPass,
      MuseumSystem aMuseumSystem) {
    super(aEmail, aName, aPassword);
    visitorId = aVisitorId;
    numOfPass = aNumOfPass;
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create visitor due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setVisitorId(long aVisitorId) {
    boolean wasSet = false;
    visitorId = aVisitorId;
    wasSet = true;
    return wasSet;
  }

  public boolean setNumOfPass(int aNumOfPass) {
    boolean wasSet = false;
    numOfPass = aNumOfPass;
    wasSet = true;
    return wasSet;
  }

  public long getVisitorId() {
    return visitorId;
  }

  public int getNumOfPass() {
    return numOfPass;
  }

  /* Code from template association_GetOne */
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
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
      existingMuseumSystem.removeVisitor(this);
    }
    museumSystem.addVisitor(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeVisitor(this);
    }
    super.delete();
  }


  public String toString() {
    return super.toString() + "[" + "visitorId" + ":" + getVisitorId() + "," + "numOfPass" + ":"
        + getNumOfPass() + "]" + System.getProperties().getProperty("line.separator") + "  "
        + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
