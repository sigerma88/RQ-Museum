/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.*;
import org.hibernate.annotations.ManyToAny;

// line 44 "model.ump"
// line 153 "model.ump"
@Entity
public class Schedule {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Schedule Attributes
  private long scheduleId;

  // Schedule Associations
  private Employee employee;
  private Museum museum;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public Schedule() {}

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setScheduleId(long aScheduleId) {
    boolean wasSet = false;
    scheduleId = aScheduleId;
    wasSet = true;
    return wasSet;
  }

  @GeneratedValue
  @Id
  public long getScheduleId() {
    return scheduleId;
  }

  /* Code from template association_GetOne */
  @OneToOne(mappedBy = "schedule")
  public Employee getEmployee() {
    return employee;
  }

  /**
   * Setter for employee
   * 
   * @param employee - Employee object to be set
   * @author Siger
   */
  public void setEmployee(Employee employee) {
    this.employee = employee;
  }

  /* Code from template association_GetOne */
  @OneToOne(mappedBy = "schedule")
  public Museum getMuseum() {
    return museum;
  }

  /**
   * Setter for museum
   * 
   * @param museum - Museum object to be set
   * @author Siger
   */
  public void setMuseum(Museum museum) {
    this.museum = museum;
  }

  public String toString() {
    return super.toString() + "[" + "scheduleId" + ":" + getScheduleId() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "employee = "
        + (getEmployee() != null ? Integer.toHexString(System.identityHashCode(getEmployee()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museum = "
        + (getMuseum() != null ? Integer.toHexString(System.identityHashCode(getMuseum())) : "null")
        + System.getProperties().getProperty("line.separator");
  }
}
