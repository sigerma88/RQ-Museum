/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.*;

// line 49 "model.ump"
// line 158 "model.ump"
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

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public Museum() {}

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

  @Column(nullable = false)
  public String getName() {
    return name;
  }

  @Column(nullable = false)
  public double getVisitFee() {
    return visitFee;
  }

  /* Code from template association_GetOne */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "schedule_id", referencedColumnName = "scheduleId", nullable = false)
  public Schedule getSchedule() {
    return schedule;
  }


  /**
   * Setter for Schedule
   * 
   * @param schedule - Schedule object to be set
   * @author Siger
   */
  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

  public String toString() {
    return super.toString() + "[" + "museumId" + ":" + getMuseumId() + "," + "name" + ":"
        + getName() + "," + "visitFee" + ":" + getVisitFee() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "schedule = "
        + (getSchedule() != null ? Integer.toHexString(System.identityHashCode(getSchedule()))
            : "null")
        + System.getProperties().getProperty("line.separator");
  }
}

