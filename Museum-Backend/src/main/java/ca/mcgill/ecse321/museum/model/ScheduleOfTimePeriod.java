/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


// line 70 "model.ump"
// line 158 "model.ump"
public class ScheduleOfTimePeriod {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // ScheduleOfTimePeriod Attributes
  private long scheduleOfTimePeriodId;

  // ScheduleOfTimePeriod Associations
  private Schedule schedule;
  private TimePeriod timePeriod;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public ScheduleOfTimePeriod(long aScheduleOfTimePeriodId, Schedule aSchedule,
      TimePeriod aTimePeriod, MuseumSystem aMuseumSystem) {
    scheduleOfTimePeriodId = aScheduleOfTimePeriodId;
    if (!setSchedule(aSchedule)) {
      throw new RuntimeException(
          "Unable to create ScheduleOfTimePeriod due to aSchedule. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setTimePeriod(aTimePeriod)) {
      throw new RuntimeException(
          "Unable to create ScheduleOfTimePeriod due to aTimePeriod. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create scheduleOfTimePeriod due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setScheduleOfTimePeriodId(long aScheduleOfTimePeriodId) {
    boolean wasSet = false;
    scheduleOfTimePeriodId = aScheduleOfTimePeriodId;
    wasSet = true;
    return wasSet;
  }

  public long getScheduleOfTimePeriodId() {
    return scheduleOfTimePeriodId;
  }

  /* Code from template association_GetOne */
  public Schedule getSchedule() {
    return schedule;
  }

  /* Code from template association_GetOne */
  public TimePeriod getTimePeriod() {
    return timePeriod;
  }

  /* Code from template association_GetOne */
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setSchedule(Schedule aNewSchedule) {
    boolean wasSet = false;
    if (aNewSchedule != null) {
      schedule = aNewSchedule;
      wasSet = true;
    }
    return wasSet;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setTimePeriod(TimePeriod aNewTimePeriod) {
    boolean wasSet = false;
    if (aNewTimePeriod != null) {
      timePeriod = aNewTimePeriod;
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
      existingMuseumSystem.removeScheduleOfTimePeriod(this);
    }
    museumSystem.addScheduleOfTimePeriod(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    schedule = null;
    timePeriod = null;
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeScheduleOfTimePeriod(this);
    }
  }


  public String toString() {
    return super.toString() + "[" + "scheduleOfTimePeriodId" + ":" + getScheduleOfTimePeriodId()
        + "]" + System.getProperties().getProperty("line.separator") + "  " + "schedule = "
        + (getSchedule() != null ? Integer.toHexString(System.identityHashCode(getSchedule()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "timePeriod = "
        + (getTimePeriod() != null ? Integer.toHexString(System.identityHashCode(getTimePeriod()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
