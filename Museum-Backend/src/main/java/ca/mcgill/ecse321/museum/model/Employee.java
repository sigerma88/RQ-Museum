package ca.mcgill.ecse321.museum.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Model class for an Employee, the code was partially generated by Umple.
 * Manager extends the abstract class MuseumUser.
 *
 * @author Siger
 * @author Kevin
 */
@Entity
public class Employee extends MuseumUser {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Employee Associations
  private Schedule schedule;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Employee() {
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  /* Code from template association_GetOne */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "schedule_id", referencedColumnName = "scheduleId", nullable = false, unique = true)
  public Schedule getSchedule() {
    return schedule;
  }

  /**
   * Setter got Schedule
   *
   * @param schedule - Schedule object to be set
   * @author Siger
   */
  public void setSchedule(Schedule schedule) {
    this.schedule = schedule;
  }

}
