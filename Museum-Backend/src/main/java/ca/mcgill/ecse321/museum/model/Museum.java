/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

// line 51 "model.ump"
// line 155 "model.ump"
@Entity
public class Museum {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Museum Attributes
  @Id
  @GeneratedValue
  private long museumId;
  private String name;
  private double visitFee;

  // Museum Associations
  private Schedule schedule;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Museum(long aMuseumId, String aName, double aVisitFee, Schedule aSchedule,
      MuseumSystem aMuseumSystem) {
    museumId = aMuseumId;
    name = aName;
    visitFee = aVisitFee;
    if (aSchedule == null || aSchedule.getMuseum() != null) {
      throw new RuntimeException(
          "Unable to create Museum due to aSchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    schedule = aSchedule;
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create museum due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Museum(long aMuseumId, String aName, double aVisitFee, long aScheduleIdForSchedule,
      Employee aEmployeeForSchedule, MuseumSystem aMuseumSystemForSchedule,
      MuseumSystem aMuseumSystem) {
    museumId = aMuseumId;
    name = aName;
    visitFee = aVisitFee;
    schedule =
        new Schedule(aScheduleIdForSchedule, aEmployeeForSchedule, this, aMuseumSystemForSchedule);
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create museum due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setMuseumId(long aMuseumId) {
    boolean wasSet = false;
    museumId = aMuseumId;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setVisitFee(double aVisitFee) {
    boolean wasSet = false;
    visitFee = aVisitFee;
    wasSet = true;
    return wasSet;
  }

  public long getMuseumId() {
    return museumId;
  }

  public String getName() {
    return name;
  }

  public double getVisitFee() {
    return visitFee;
  }

  /* Code from template association_GetOne */
  public Schedule getSchedule() {
    return schedule;
  }

  /* Code from template association_GetOne */
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
  }

  /* Code from template association_SetOneToOptionalOne */
  public boolean setMuseumSystem(MuseumSystem aNewMuseumSystem) {
    boolean wasSet = false;
    if (aNewMuseumSystem == null) {
      // Unable to setMuseumSystem to null, as museum must always be associated to a museumSystem
      return wasSet;
    }

    Museum existingMuseum = aNewMuseumSystem.getMuseum();
    if (existingMuseum != null && !equals(existingMuseum)) {
      // Unable to setMuseumSystem, the current museumSystem already has a museum, which would be
      // orphaned if it were re-assigned
      return wasSet;
    }

    MuseumSystem anOldMuseumSystem = museumSystem;
    museumSystem = aNewMuseumSystem;
    museumSystem.setMuseum(this);

    if (anOldMuseumSystem != null) {
      anOldMuseumSystem.setMuseum(null);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Schedule existingSchedule = schedule;
    schedule = null;
    if (existingSchedule != null) {
      existingSchedule.delete();
    }
    MuseumSystem existingMuseumSystem = museumSystem;
    museumSystem = null;
    if (existingMuseumSystem != null) {
      existingMuseumSystem.setMuseum(null);
    }
  }


  public String toString() {
    return super.toString() + "[" + "museumId" + ":" + getMuseumId() + "," + "name" + ":"
        + getName() + "," + "visitFee" + ":" + getVisitFee() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "schedule = "
        + (getSchedule() != null ? Integer.toHexString(System.identityHashCode(getSchedule()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
