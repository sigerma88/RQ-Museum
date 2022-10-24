/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import java.sql.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

// line 87 "model.ump"
// line 168 "model.ump"

@Entity
public class TimePeriod {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // TimePeriod Attributes
  private long timePeriodId;
  private Date startDate;
  private Date endDate;

  // TimePeriod Associations
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public TimePeriod(long aTimePeriodId, Date aStartDate, Date aEndDate,
      MuseumSystem aMuseumSystem) {
    timePeriodId = aTimePeriodId;
    startDate = aStartDate;
    endDate = aEndDate;
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create timePeriod due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

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

  public Date getStartDate() {
    return startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  /* Code from template association_GetOne */
  @ManyToOne(cascade = CascadeType.ALL)
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
      existingMuseumSystem.removeTimePeriod(this);
    }
    museumSystem.addTimePeriod(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeTimePeriod(this);
    }
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
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
