/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

// line 51 "model.ump"
// line 148 "model.ump"
@Entity
public class Museum {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Museum Attributes
  private long museumId;
  private String name;
  private double visitFee;

  // Museum Associations
  private Schedule schedule;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public Museum() {}

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
    if (aMuseumSystem == null || aMuseumSystem.getMuseum() != null) {
      throw new RuntimeException(
          "Unable to create Museum due to aMuseumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    museumSystem = aMuseumSystem;
  }

  public Museum(long aMuseumId, String aName, double aVisitFee, long aScheduleIdForSchedule,
      Employee aEmployeeForSchedule, MuseumSystem aMuseumSystemForSchedule,
      Manager aManagerForMuseumSystem) {
    museumId = aMuseumId;
    name = aName;
    visitFee = aVisitFee;
    schedule =
        new Schedule(aScheduleIdForSchedule, aEmployeeForSchedule, this, aMuseumSystemForSchedule);
    museumSystem = new MuseumSystem(this, aManagerForMuseumSystem);
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

  @Id
  @GeneratedValue
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
  @OneToOne(optional = false)
  public Schedule getSchedule() {
    return schedule;
  }

  /* Code from template association_GetOne */
  @OneToOne(optional = false)
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
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
      existingMuseumSystem.delete();
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
