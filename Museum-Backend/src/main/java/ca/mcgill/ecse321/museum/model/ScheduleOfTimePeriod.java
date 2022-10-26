/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.*;

// line 68 "model.ump"
// line 168 "model.ump"
@Entity
public class ScheduleOfTimePeriod {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // ScheduleOfTimePeriod Attributes
  private long scheduleOfTimePeriodId;

  // ScheduleOfTimePeriod Associations
  private Schedule schedule;
  private TimePeriod timePeriod;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public ScheduleOfTimePeriod() {}

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setScheduleOfTimePeriodId(long aScheduleOfTimePeriodId) {
    boolean wasSet = false;
    scheduleOfTimePeriodId = aScheduleOfTimePeriodId;
    wasSet = true;
    return wasSet;
  }

  @GeneratedValue
  @Id
  public long getScheduleOfTimePeriodId() {
    return scheduleOfTimePeriodId;
  }

  /* Code from template association_GetOne */
  @ManyToOne()
  @JoinColumn(name="schedule_id", referencedColumnName = "scheduleId", nullable = false)
  public Schedule getSchedule() {
    return schedule;
  }

  /* Code from template association_GetOne */
  @ManyToOne()
  @JoinColumn(name="time_period_id", referencedColumnName = "timePeriodId", nullable = false)
  public TimePeriod getTimePeriod() {
    return timePeriod;
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

  public String toString() {
    return super.toString() + "[" + "scheduleOfTimePeriodId" + ":" + getScheduleOfTimePeriodId()
        + "]" + System.getProperties().getProperty("line.separator") + "  " + "schedule = "
        + (getSchedule() != null ? Integer.toHexString(System.identityHashCode(getSchedule()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "timePeriod = "
        + (getTimePeriod() != null ? Integer.toHexString(System.identityHashCode(getTimePeriod()))
            : "null")
        + System.getProperties().getProperty("line.separator");
  }
}
