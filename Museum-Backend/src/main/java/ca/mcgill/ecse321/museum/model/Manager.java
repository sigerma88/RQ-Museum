/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


// line 38 "model.ump"
// line 151 "model.ump"
public class Manager extends Staff {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Manager Attributes
  private long managerId;

  // Manager Associations
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Manager(String aEmail, String aName, String aPassword, long aManagerId,
      MuseumSystem aMuseumSystem) {
    super(aEmail, aName, aPassword);
    managerId = aManagerId;
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create manager due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setManagerId(long aManagerId) {
    boolean wasSet = false;
    managerId = aManagerId;
    wasSet = true;
    return wasSet;
  }

  public long getManagerId() {
    return managerId;
  }

  /* Code from template association_GetOne */
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
  }

  /* Code from template association_SetOneToOptionalOne */
  public boolean setMuseumSystem(MuseumSystem aNewMuseumSystem) {
    boolean wasSet = false;
    if (aNewMuseumSystem == null) {
      // Unable to setMuseumSystem to null, as manager must always be associated to a museumSystem
      return wasSet;
    }

    Manager existingManager = aNewMuseumSystem.getManager();
    if (existingManager != null && !equals(existingManager)) {
      // Unable to setMuseumSystem, the current museumSystem already has a manager, which would be
      // orphaned if it were re-assigned
      return wasSet;
    }

    MuseumSystem anOldMuseumSystem = museumSystem;
    museumSystem = aNewMuseumSystem;
    museumSystem.setManager(this);

    if (anOldMuseumSystem != null) {
      anOldMuseumSystem.setManager(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    MuseumSystem existingMuseumSystem = museumSystem;
    museumSystem = null;
    if (existingMuseumSystem != null) {
      existingMuseumSystem.setManager(null);
    }
    super.delete();
  }


  public String toString() {
    return super.toString() + "[" + "managerId" + ":" + getManagerId() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}