/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


import javax.persistence.*;

// line 24 "model.ump"
// line 138 "model.ump"
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

  //no arg constructor
  public Employee(){}

  // ------------------------
  // INTERFACE
  // ------------------------
  /* Code from template association_GetOne */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "schedule_id", referencedColumnName = "scheduleId", nullable = false)
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
