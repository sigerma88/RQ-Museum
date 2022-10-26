/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import java.sql.Date;
import javax.persistence.*;

// line 85 "model.ump"
// line 178 "model.ump"
@Entity
public class TimePeriod {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // TimePeriod Attributes
  private long timePeriodId;
  private Date startDate;
  private Date endDate;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public TimePeriod() {}

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setTimePeriodId(long aTimePeriodId) {
    boolean wasSet = false;
    timePeriodId = aTimePeriodId;
    wasSet = true;
    return wasSet;
  }

  public boolean setStartDate(Date aStartDate) {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }

  public boolean setEndDate(Date aEndDate) {
    boolean wasSet = false;
    endDate = aEndDate;
    wasSet = true;
    return wasSet;
  }

  @GeneratedValue
  @Id
  public long getTimePeriodId() {
    return timePeriodId;
  }

  @Column(nullable = false)
  public Date getStartDate() {
    return startDate;
  }

  @Column(nullable = false)
  public Date getEndDate() {
    return endDate;
  }

  public String toString() {
    return super.toString() + "[" + "timePeriodId" + ":" + getTimePeriodId() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "startDate" + "="
        + (getStartDate() != null
            ? !getStartDate().equals(this) ? getStartDate().toString().replaceAll("  ", "    ")
                : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "endDate" + "="
        + (getEndDate() != null
            ? !getEndDate().equals(this) ? getEndDate().toString().replaceAll("  ", "    ") : "this"
            : "null")
        + System.getProperties().getProperty("line.separator");
  }
}
