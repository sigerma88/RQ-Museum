/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


import javax.persistence.*;

// line 24 "model.ump"
// line 127 "model.ump"
@Entity
public class Employee extends MuseumUser {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Employee Attributes
  private long employeeId;

  // Employee Associations
  private Schedule schedule;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Employee(String aEmail, String aName, String aPassword, long aEmployeeId,
      Schedule aSchedule, MuseumSystem aMuseumSystem) {
    super(aEmail, aName, aPassword);
    employeeId = aEmployeeId;
    if (aSchedule == null || aSchedule.getEmployee() != null) {
      throw new RuntimeException(
          "Unable to create Employee due to aSchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    schedule = aSchedule;
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create employee due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Employee(String aEmail, String aName, String aPassword, long aEmployeeId,
      long aScheduleIdForSchedule, Museum aMuseumForSchedule, MuseumSystem aMuseumSystemForSchedule,
      MuseumSystem aMuseumSystem) {
    super(aEmail, aName, aPassword);
    employeeId = aEmployeeId;
    schedule =
        new Schedule(aScheduleIdForSchedule, this, aMuseumForSchedule, aMuseumSystemForSchedule);
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create employee due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setEmployeeId(long aEmployeeId) {
    boolean wasSet = false;
    employeeId = aEmployeeId;
    wasSet = true;
    return wasSet;
  }

  @Id
  @GeneratedValue
  public long getEmployeeId() {
    return employeeId;
  }

  /* Code from template association_GetOne */
  @OneToOne(optional = true)
  public Schedule getSchedule() {
    return schedule;
  }

  /* Code from template association_GetOne */
  @ManyToOne(optional = false)
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
      existingMuseumSystem.removeEmployee(this);
    }
    museumSystem.addEmployee(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Schedule existingSchedule = schedule;
    schedule = null;
    if (existingSchedule != null) {
      existingSchedule.delete();
    }
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeEmployee(this);
    }
    super.delete();
  }


  public String toString() {
    return super.toString() + "[" + "employeeId" + ":" + getEmployeeId() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "schedule = "
        + (getSchedule() != null ? Integer.toHexString(System.identityHashCode(getSchedule()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
