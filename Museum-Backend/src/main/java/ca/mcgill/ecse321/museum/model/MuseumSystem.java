/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.*;
import java.util.*;
import java.sql.Date;

// line 92 "model.ump"
// line 109 "model.ump"
// line 183 "model.ump"
@Entity
public class MuseumSystem {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // MuseumSystem Attributes
  private long museumSystemId;

  // MuseumSystem Associations
  private Museum museum;
  private List<Room> room;
  private List<TimePeriod> timePeriod;
  private List<Artwork> artwork;
  private List<Loan> loan;
  private List<Employee> employee;
  private Manager manager;
  private List<Schedule> schedule;
  private List<Visitor> visitor;
  private List<Ticket> ticket;
  private List<ScheduleOfTimePeriod> scheduleOfTimePeriod;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public MuseumSystem(){}

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setMuseumSystemId(long aMuseumSystemId) {
    boolean wasSet = false;
    museumSystemId = aMuseumSystemId;
    wasSet = true;
    return wasSet;
  }

  @Id
  @GeneratedValue
  public long getMuseumSystemId() {
    return museumSystemId;
  }

  /* Code from template association_GetOne */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "museumId", referencedColumnName = "museumId", nullable = false)
  public Museum getMuseum() {
    return museum;
  }

  /**
   * Setter for Museum
   *
   * @param museum - Museum object to be set
   * @author Siger
   */
  public void setMuseum(Museum museum) {
    this.museum = museum;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_room", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "room_id", referencedColumnName = "roomId"))
  public List<Room> getRoom() {
    List<Room> newRoom = Collections.unmodifiableList(room);
    return newRoom;
  }

  /**
   * Setter for Room
   *
   * @param room - List of Room objects to be set
   * @author Siger
   */
  public void setRoom(List<Room> room) {
    this.room = room;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_time_period", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "time_period_id", referencedColumnName = "timePeriodId"))
  public List<TimePeriod> getTimePeriod() {
    List<TimePeriod> newTimePeriod = Collections.unmodifiableList(timePeriod);
    return newTimePeriod;
  }

  /**
   * Setter for TimePeriod
   *
   * @param timePeriod - List of TimePeriod objects to be set
   */
  public void setTimePeriod(List<TimePeriod> timePeriod) {
    this.timePeriod = timePeriod;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_artwork", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "artwork_id", referencedColumnName = "artworkId"))
  public List<Artwork> getArtwork() {
    List<Artwork> newArtwork = Collections.unmodifiableList(artwork);
    return newArtwork;
  }

  /**
   * Setter for Artwork
   * 
   * @param artwork - List of Artwork object to be set
   * @author Siger
   */
  public void setArtwork(List<Artwork> artwork) {
    this.artwork = artwork;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_loan", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "loan_id", referencedColumnName = "loanId"))
  public List<Loan> getLoan() {
    List<Loan> newLoan = Collections.unmodifiableList(loan);
    return newLoan;
  }

  /**
   * Setter for Loan
   *
   * @param loan - List of Loan objects to be set
   * @author Siger
   */
  public void setLoan(List<Loan> loan) {
    this.loan = loan;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_employee", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "museumUserId"))
  public List<Employee> getEmployee() {
    List<Employee> newEmployee = Collections.unmodifiableList(employee);
    return newEmployee;
  }

  /**
   * Setter for Employee
   *
   * @param employee - List of Employee objects to be set
   * @author Siger
   */
  public void setEmployee(List<Employee> employee) {
    this.employee = employee;
  }

  /* Code from template association_GetOne */
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "manager_id", referencedColumnName = "museumUserId", nullable = false)
  public Manager getManager() {
    return manager;
  }

  /**
   * Setter for Manager
   *
   * @param manager - Manager object to be set
   * @author Siger
   */
  public void setManager(Manager manager) {
    this.manager = manager;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_schedule", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "schedule_id", referencedColumnName = "scheduleId"))
  public List<Schedule> getSchedule() {
    List<Schedule> newSchedule = Collections.unmodifiableList(schedule);
    return newSchedule;
  }

  /**
   * Setter for Schedule
   *
   * @param schedule - List of Schedule objects to be set
   * @author Siger
   */
  public void setSchedule(List<Schedule> schedule) {
    this.schedule = schedule;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_visitor", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "visitor_id", referencedColumnName = "museumUserId"))
  public List<Visitor> getVisitor() {
    List<Visitor> newVisitor = Collections.unmodifiableList(visitor);
    return newVisitor;
  }

  /**
   * Setter for Visitor
   *
   * @param visitor - List of Visitor objects to be set
   * @author Siger
   */
  public void setVisitor(List<Visitor> visitor) {
    this.visitor = visitor;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_ticket", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "ticket_id", referencedColumnName = "ticketId"))
  public List<Ticket> getTicket() {
    List<Ticket> newTicket = Collections.unmodifiableList(ticket);
    return newTicket;
  }

  /**
   * Setter for Ticket
   *
   * @param ticket - List of Ticket objects to be set
   * @author Siger
   */
  public void setTicket(List<Ticket> ticket) {
    this.ticket = ticket;
  }

  /* Code from template association_GetMany */
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "museum_system_schedule_of_time_period", joinColumns = @JoinColumn(name = "museum_system_id", referencedColumnName = "museumSystemId"), inverseJoinColumns = @JoinColumn(name = "schedule_of_time_period_id", referencedColumnName = "scheduleOfTimePeriodId"))
  public List<ScheduleOfTimePeriod> getScheduleOfTimePeriod() {
    List<ScheduleOfTimePeriod> newScheduleOfTimePeriod =
        Collections.unmodifiableList(scheduleOfTimePeriod);
    return newScheduleOfTimePeriod;
  }

  /**
   * Setter for ScheduleOfTimePeriod
   *
   * @param scheduleOfTimePeriod - List of ScheduleOfTimePeriod objects to be set
   * @author Siger
   */
  public void setScheduleOfTimePeriod(List<ScheduleOfTimePeriod> scheduleOfTimePeriod) {
    this.scheduleOfTimePeriod = scheduleOfTimePeriod;
  }

  public String toString() {
    return super.toString() + "[" + "museumSystemId" + ":" + getMuseumSystemId() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "museum = "
        + (getMuseum() != null ? Integer.toHexString(System.identityHashCode(getMuseum())) : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "manager = "
        + (getManager() != null ? Integer.toHexString(System.identityHashCode(getManager()))
            : "null");
  }
}
