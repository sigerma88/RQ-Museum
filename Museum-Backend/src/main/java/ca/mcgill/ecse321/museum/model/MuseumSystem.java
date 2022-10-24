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
  private int museumSystemId;

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

  public MuseumSystem(Museum aMuseum, Manager aManager) {
    if (aMuseum == null || aMuseum.getMuseumSystem() != null) {
      throw new RuntimeException(
          "Unable to create MuseumSystem due to aMuseum. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    museum = aMuseum;
    room = new ArrayList<Room>();
    timePeriod = new ArrayList<TimePeriod>();
    artwork = new ArrayList<Artwork>();
    loan = new ArrayList<Loan>();
    employee = new ArrayList<Employee>();
    if (aManager == null || aManager.getMuseumSystem() != null) {
      throw new RuntimeException(
          "Unable to create MuseumSystem due to aManager. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    manager = aManager;
    schedule = new ArrayList<Schedule>();
    visitor = new ArrayList<Visitor>();
    ticket = new ArrayList<Ticket>();
    scheduleOfTimePeriod = new ArrayList<ScheduleOfTimePeriod>();
  }

  public MuseumSystem(long aMuseumIdForMuseum, String aNameForMuseum, double aVisitFeeForMuseum,
      Schedule aScheduleForMuseum, String aEmailForManager, String aNameForManager,
      String aPasswordForManager, long aMuseumUserIdForManager) {
    museum = new Museum(aMuseumIdForMuseum, aNameForMuseum, aVisitFeeForMuseum, aScheduleForMuseum,
        this);
    room = new ArrayList<Room>();
    timePeriod = new ArrayList<TimePeriod>();
    artwork = new ArrayList<Artwork>();
    loan = new ArrayList<Loan>();
    employee = new ArrayList<Employee>();
    manager = new Manager(aEmailForManager, aNameForManager, aPasswordForManager,
        aMuseumUserIdForManager, this);
    schedule = new ArrayList<Schedule>();
    visitor = new ArrayList<Visitor>();
    ticket = new ArrayList<Ticket>();
    scheduleOfTimePeriod = new ArrayList<ScheduleOfTimePeriod>();
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setMuseumSystemId(int aMuseumSystemId) {
    boolean wasSet = false;
    museumSystemId = aMuseumSystemId;
    wasSet = true;
    return wasSet;
  }

  @Id
  @GeneratedValue
  public int getMuseumSystemId() {
    return museumSystemId;
  }

  /* Code from template association_GetOne */
  @OneToOne(optional = false)
  public Museum getMuseum() {
    return museum;
  }

  /* Code from template association_GetMany */
  public Room getRoom(int index) {
    Room aRoom = room.get(index);
    return aRoom;
  }

  @OneToMany
  public List<Room> getRoom() {
    List<Room> newRoom = Collections.unmodifiableList(room);
    return newRoom;
  }

  public int numberOfRoom() {
    int number = room.size();
    return number;
  }

  public boolean hasRoom() {
    boolean has = room.size() > 0;
    return has;
  }

  public int indexOfRoom(Room aRoom) {
    int index = room.indexOf(aRoom);
    return index;
  }

  /* Code from template association_GetMany */
  public TimePeriod getTimePeriod(int index) {
    TimePeriod aTimePeriod = timePeriod.get(index);
    return aTimePeriod;
  }

  @OneToMany
  public List<TimePeriod> getTimePeriod() {
    List<TimePeriod> newTimePeriod = Collections.unmodifiableList(timePeriod);
    return newTimePeriod;
  }

  public int numberOfTimePeriod() {
    int number = timePeriod.size();
    return number;
  }

  public boolean hasTimePeriod() {
    boolean has = timePeriod.size() > 0;
    return has;
  }

  public int indexOfTimePeriod(TimePeriod aTimePeriod) {
    int index = timePeriod.indexOf(aTimePeriod);
    return index;
  }

  /* Code from template association_GetMany */
  public Artwork getArtwork(int index) {
    Artwork aArtwork = artwork.get(index);
    return aArtwork;
  }

  @OneToMany
  public List<Artwork> getArtwork() {
    List<Artwork> newArtwork = Collections.unmodifiableList(artwork);
    return newArtwork;
  }

  public int numberOfArtwork() {
    int number = artwork.size();
    return number;
  }

  public boolean hasArtwork() {
    boolean has = artwork.size() > 0;
    return has;
  }

  public int indexOfArtwork(Artwork aArtwork) {
    int index = artwork.indexOf(aArtwork);
    return index;
  }

  /* Code from template association_GetMany */
  public Loan getLoan(int index) {
    Loan aLoan = loan.get(index);
    return aLoan;
  }

  @OneToMany
  public List<Loan> getLoan() {
    List<Loan> newLoan = Collections.unmodifiableList(loan);
    return newLoan;
  }

  public int numberOfLoan() {
    int number = loan.size();
    return number;
  }

  public boolean hasLoan() {
    boolean has = loan.size() > 0;
    return has;
  }

  public int indexOfLoan(Loan aLoan) {
    int index = loan.indexOf(aLoan);
    return index;
  }

  /* Code from template association_GetMany */
  public Employee getEmployee(int index) {
    Employee aEmployee = employee.get(index);
    return aEmployee;
  }

  @OneToMany
  public List<Employee> getEmployee() {
    List<Employee> newEmployee = Collections.unmodifiableList(employee);
    return newEmployee;
  }

  public int numberOfEmployee() {
    int number = employee.size();
    return number;
  }

  public boolean hasEmployee() {
    boolean has = employee.size() > 0;
    return has;
  }

  public int indexOfEmployee(Employee aEmployee) {
    int index = employee.indexOf(aEmployee);
    return index;
  }

  /* Code from template association_GetOne */
  @OneToOne
  public Manager getManager() {
    return manager;
  }

  /* Code from template association_GetMany */
  public Schedule getSchedule(int index) {
    Schedule aSchedule = schedule.get(index);
    return aSchedule;
  }

  @OneToMany
  public List<Schedule> getSchedule() {
    List<Schedule> newSchedule = Collections.unmodifiableList(schedule);
    return newSchedule;
  }

  public int numberOfSchedule() {
    int number = schedule.size();
    return number;
  }

  public boolean hasSchedule() {
    boolean has = schedule.size() > 0;
    return has;
  }

  public int indexOfSchedule(Schedule aSchedule) {
    int index = schedule.indexOf(aSchedule);
    return index;
  }

  /* Code from template association_GetMany */
  public Visitor getVisitor(int index) {
    Visitor aVisitor = visitor.get(index);
    return aVisitor;
  }

  @OneToMany
  public List<Visitor> getVisitor() {
    List<Visitor> newVisitor = Collections.unmodifiableList(visitor);
    return newVisitor;
  }

  public int numberOfVisitor() {
    int number = visitor.size();
    return number;
  }

  public boolean hasVisitor() {
    boolean has = visitor.size() > 0;
    return has;
  }

  public int indexOfVisitor(Visitor aVisitor) {
    int index = visitor.indexOf(aVisitor);
    return index;
  }

  /* Code from template association_GetMany */
  public Ticket getTicket(int index) {
    Ticket aTicket = ticket.get(index);
    return aTicket;
  }

  @OneToMany
  public List<Ticket> getTicket() {
    List<Ticket> newTicket = Collections.unmodifiableList(ticket);
    return newTicket;
  }

  public int numberOfTicket() {
    int number = ticket.size();
    return number;
  }

  public boolean hasTicket() {
    boolean has = ticket.size() > 0;
    return has;
  }

  public int indexOfTicket(Ticket aTicket) {
    int index = ticket.indexOf(aTicket);
    return index;
  }

  /* Code from template association_GetMany */
  public ScheduleOfTimePeriod getScheduleOfTimePeriod(int index) {
    ScheduleOfTimePeriod aScheduleOfTimePeriod = scheduleOfTimePeriod.get(index);
    return aScheduleOfTimePeriod;
  }

  @OneToMany
  public List<ScheduleOfTimePeriod> getScheduleOfTimePeriod() {
    List<ScheduleOfTimePeriod> newScheduleOfTimePeriod =
        Collections.unmodifiableList(scheduleOfTimePeriod);
    return newScheduleOfTimePeriod;
  }

  public int numberOfScheduleOfTimePeriod() {
    int number = scheduleOfTimePeriod.size();
    return number;
  }

  public boolean hasScheduleOfTimePeriod() {
    boolean has = scheduleOfTimePeriod.size() > 0;
    return has;
  }

  public int indexOfScheduleOfTimePeriod(ScheduleOfTimePeriod aScheduleOfTimePeriod) {
    int index = scheduleOfTimePeriod.indexOf(aScheduleOfTimePeriod);
    return index;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfRoom() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public Room addRoom(long aRoomId, String aRoomName, int aCurrentNumberOfArtwork, Museum aMuseum) {
    return new Room(aRoomId, aRoomName, aCurrentNumberOfArtwork, aMuseum, this);
  }

  public boolean addRoom(Room aRoom) {
    boolean wasAdded = false;
    if (room.contains(aRoom)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aRoom.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aRoom.setMuseumSystem(this);
    } else {
      room.add(aRoom);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeRoom(Room aRoom) {
    boolean wasRemoved = false;
    // Unable to remove aRoom, as it must always have a museumSystem
    if (!this.equals(aRoom.getMuseumSystem())) {
      room.remove(aRoom);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addRoomAt(Room aRoom, int index) {
    boolean wasAdded = false;
    if (addRoom(aRoom)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfRoom()) {
        index = numberOfRoom() - 1;
      }
      room.remove(aRoom);
      room.add(index, aRoom);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveRoomAt(Room aRoom, int index) {
    boolean wasAdded = false;
    if (room.contains(aRoom)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfRoom()) {
        index = numberOfRoom() - 1;
      }
      room.remove(aRoom);
      room.add(index, aRoom);
      wasAdded = true;
    } else {
      wasAdded = addRoomAt(aRoom, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTimePeriod() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public TimePeriod addTimePeriod(long aTimePeriodId, Date aStartDate, Date aEndDate) {
    return new TimePeriod(aTimePeriodId, aStartDate, aEndDate, this);
  }

  public boolean addTimePeriod(TimePeriod aTimePeriod) {
    boolean wasAdded = false;
    if (timePeriod.contains(aTimePeriod)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aTimePeriod.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aTimePeriod.setMuseumSystem(this);
    } else {
      timePeriod.add(aTimePeriod);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTimePeriod(TimePeriod aTimePeriod) {
    boolean wasRemoved = false;
    // Unable to remove aTimePeriod, as it must always have a museumSystem
    if (!this.equals(aTimePeriod.getMuseumSystem())) {
      timePeriod.remove(aTimePeriod);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addTimePeriodAt(TimePeriod aTimePeriod, int index) {
    boolean wasAdded = false;
    if (addTimePeriod(aTimePeriod)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfTimePeriod()) {
        index = numberOfTimePeriod() - 1;
      }
      timePeriod.remove(aTimePeriod);
      timePeriod.add(index, aTimePeriod);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTimePeriodAt(TimePeriod aTimePeriod, int index) {
    boolean wasAdded = false;
    if (timePeriod.contains(aTimePeriod)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfTimePeriod()) {
        index = numberOfTimePeriod() - 1;
      }
      timePeriod.remove(aTimePeriod);
      timePeriod.add(index, aTimePeriod);
      wasAdded = true;
    } else {
      wasAdded = addTimePeriodAt(aTimePeriod, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfArtwork() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public Artwork addArtwork(long aArtworkId, String aName, String aArtist,
      boolean aIsAvailableForLoan, double aLoanFee, String aImage, Room aRoom) {
    return new Artwork(aArtworkId, aName, aArtist, aIsAvailableForLoan, aLoanFee, aImage, aRoom,
        this);
  }

  public boolean addArtwork(Artwork aArtwork) {
    boolean wasAdded = false;
    if (artwork.contains(aArtwork)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aArtwork.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aArtwork.setMuseumSystem(this);
    } else {
      artwork.add(aArtwork);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeArtwork(Artwork aArtwork) {
    boolean wasRemoved = false;
    // Unable to remove aArtwork, as it must always have a museumSystem
    if (!this.equals(aArtwork.getMuseumSystem())) {
      artwork.remove(aArtwork);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addArtworkAt(Artwork aArtwork, int index) {
    boolean wasAdded = false;
    if (addArtwork(aArtwork)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfArtwork()) {
        index = numberOfArtwork() - 1;
      }
      artwork.remove(aArtwork);
      artwork.add(index, aArtwork);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveArtworkAt(Artwork aArtwork, int index) {
    boolean wasAdded = false;
    if (artwork.contains(aArtwork)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfArtwork()) {
        index = numberOfArtwork() - 1;
      }
      artwork.remove(aArtwork);
      artwork.add(index, aArtwork);
      wasAdded = true;
    } else {
      wasAdded = addArtworkAt(aArtwork, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLoan() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public Loan addLoan(long aLoanId, boolean aRequestedAccepted, Visitor aVisitor,
      Artwork aArtwork) {
    return new Loan(aLoanId, aRequestedAccepted, aVisitor, aArtwork, this);
  }

  public boolean addLoan(Loan aLoan) {
    boolean wasAdded = false;
    if (loan.contains(aLoan)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aLoan.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aLoan.setMuseumSystem(this);
    } else {
      loan.add(aLoan);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLoan(Loan aLoan) {
    boolean wasRemoved = false;
    // Unable to remove aLoan, as it must always have a museumSystem
    if (!this.equals(aLoan.getMuseumSystem())) {
      loan.remove(aLoan);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addLoanAt(Loan aLoan, int index) {
    boolean wasAdded = false;
    if (addLoan(aLoan)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLoan()) {
        index = numberOfLoan() - 1;
      }
      loan.remove(aLoan);
      loan.add(index, aLoan);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLoanAt(Loan aLoan, int index) {
    boolean wasAdded = false;
    if (loan.contains(aLoan)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfLoan()) {
        index = numberOfLoan() - 1;
      }
      loan.remove(aLoan);
      loan.add(index, aLoan);
      wasAdded = true;
    } else {
      wasAdded = addLoanAt(aLoan, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfEmployee() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public Employee addEmployee(String aEmail, String aName, String aPassword, long aMuseumUserId,
      Schedule aSchedule) {
    return new Employee(aEmail, aName, aPassword, aMuseumUserId, aSchedule, this);
  }

  public boolean addEmployee(Employee aEmployee) {
    boolean wasAdded = false;
    if (employee.contains(aEmployee)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aEmployee.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aEmployee.setMuseumSystem(this);
    } else {
      employee.add(aEmployee);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeEmployee(Employee aEmployee) {
    boolean wasRemoved = false;
    // Unable to remove aEmployee, as it must always have a museumSystem
    if (!this.equals(aEmployee.getMuseumSystem())) {
      employee.remove(aEmployee);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addEmployeeAt(Employee aEmployee, int index) {
    boolean wasAdded = false;
    if (addEmployee(aEmployee)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfEmployee()) {
        index = numberOfEmployee() - 1;
      }
      employee.remove(aEmployee);
      employee.add(index, aEmployee);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveEmployeeAt(Employee aEmployee, int index) {
    boolean wasAdded = false;
    if (employee.contains(aEmployee)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfEmployee()) {
        index = numberOfEmployee() - 1;
      }
      employee.remove(aEmployee);
      employee.add(index, aEmployee);
      wasAdded = true;
    } else {
      wasAdded = addEmployeeAt(aEmployee, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfSchedule() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public Schedule addSchedule(long aScheduleId, Employee aEmployee, Museum aMuseum) {
    return new Schedule(aScheduleId, aEmployee, aMuseum, this);
  }

  public boolean addSchedule(Schedule aSchedule) {
    boolean wasAdded = false;
    if (schedule.contains(aSchedule)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aSchedule.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aSchedule.setMuseumSystem(this);
    } else {
      schedule.add(aSchedule);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeSchedule(Schedule aSchedule) {
    boolean wasRemoved = false;
    // Unable to remove aSchedule, as it must always have a museumSystem
    if (!this.equals(aSchedule.getMuseumSystem())) {
      schedule.remove(aSchedule);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addScheduleAt(Schedule aSchedule, int index) {
    boolean wasAdded = false;
    if (addSchedule(aSchedule)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfSchedule()) {
        index = numberOfSchedule() - 1;
      }
      schedule.remove(aSchedule);
      schedule.add(index, aSchedule);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveScheduleAt(Schedule aSchedule, int index) {
    boolean wasAdded = false;
    if (schedule.contains(aSchedule)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfSchedule()) {
        index = numberOfSchedule() - 1;
      }
      schedule.remove(aSchedule);
      schedule.add(index, aSchedule);
      wasAdded = true;
    } else {
      wasAdded = addScheduleAt(aSchedule, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfVisitor() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public Visitor addVisitor(String aEmail, String aName, String aPassword, long aMuseumUserId) {
    return new Visitor(aEmail, aName, aPassword, aMuseumUserId, this);
  }

  public boolean addVisitor(Visitor aVisitor) {
    boolean wasAdded = false;
    if (visitor.contains(aVisitor)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aVisitor.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aVisitor.setMuseumSystem(this);
    } else {
      visitor.add(aVisitor);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeVisitor(Visitor aVisitor) {
    boolean wasRemoved = false;
    // Unable to remove aVisitor, as it must always have a museumSystem
    if (!this.equals(aVisitor.getMuseumSystem())) {
      visitor.remove(aVisitor);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addVisitorAt(Visitor aVisitor, int index) {
    boolean wasAdded = false;
    if (addVisitor(aVisitor)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfVisitor()) {
        index = numberOfVisitor() - 1;
      }
      visitor.remove(aVisitor);
      visitor.add(index, aVisitor);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveVisitorAt(Visitor aVisitor, int index) {
    boolean wasAdded = false;
    if (visitor.contains(aVisitor)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfVisitor()) {
        index = numberOfVisitor() - 1;
      }
      visitor.remove(aVisitor);
      visitor.add(index, aVisitor);
      wasAdded = true;
    } else {
      wasAdded = addVisitorAt(aVisitor, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfTicket() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public Ticket addTicket(long aTicketId, Date aVisitDate, Visitor aVisitor) {
    return new Ticket(aTicketId, aVisitDate, aVisitor, this);
  }

  public boolean addTicket(Ticket aTicket) {
    boolean wasAdded = false;
    if (ticket.contains(aTicket)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aTicket.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aTicket.setMuseumSystem(this);
    } else {
      ticket.add(aTicket);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeTicket(Ticket aTicket) {
    boolean wasRemoved = false;
    // Unable to remove aTicket, as it must always have a museumSystem
    if (!this.equals(aTicket.getMuseumSystem())) {
      ticket.remove(aTicket);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addTicketAt(Ticket aTicket, int index) {
    boolean wasAdded = false;
    if (addTicket(aTicket)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfTicket()) {
        index = numberOfTicket() - 1;
      }
      ticket.remove(aTicket);
      ticket.add(index, aTicket);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveTicketAt(Ticket aTicket, int index) {
    boolean wasAdded = false;
    if (ticket.contains(aTicket)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfTicket()) {
        index = numberOfTicket() - 1;
      }
      ticket.remove(aTicket);
      ticket.add(index, aTicket);
      wasAdded = true;
    } else {
      wasAdded = addTicketAt(aTicket, index);
    }
    return wasAdded;
  }

  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfScheduleOfTimePeriod() {
    return 0;
  }

  /* Code from template association_AddManyToOne */
  public ScheduleOfTimePeriod addScheduleOfTimePeriod(long aScheduleOfTimePeriodId,
      Schedule aSchedule, TimePeriod aTimePeriod) {
    return new ScheduleOfTimePeriod(aScheduleOfTimePeriodId, aSchedule, aTimePeriod, this);
  }

  public boolean addScheduleOfTimePeriod(ScheduleOfTimePeriod aScheduleOfTimePeriod) {
    boolean wasAdded = false;
    if (scheduleOfTimePeriod.contains(aScheduleOfTimePeriod)) {
      return false;
    }
    MuseumSystem existingMuseumSystem = aScheduleOfTimePeriod.getMuseumSystem();
    boolean isNewMuseumSystem = existingMuseumSystem != null && !this.equals(existingMuseumSystem);
    if (isNewMuseumSystem) {
      aScheduleOfTimePeriod.setMuseumSystem(this);
    } else {
      scheduleOfTimePeriod.add(aScheduleOfTimePeriod);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeScheduleOfTimePeriod(ScheduleOfTimePeriod aScheduleOfTimePeriod) {
    boolean wasRemoved = false;
    // Unable to remove aScheduleOfTimePeriod, as it must always have a museumSystem
    if (!this.equals(aScheduleOfTimePeriod.getMuseumSystem())) {
      scheduleOfTimePeriod.remove(aScheduleOfTimePeriod);
      wasRemoved = true;
    }
    return wasRemoved;
  }

  /* Code from template association_AddIndexControlFunctions */
  public boolean addScheduleOfTimePeriodAt(ScheduleOfTimePeriod aScheduleOfTimePeriod, int index) {
    boolean wasAdded = false;
    if (addScheduleOfTimePeriod(aScheduleOfTimePeriod)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfScheduleOfTimePeriod()) {
        index = numberOfScheduleOfTimePeriod() - 1;
      }
      scheduleOfTimePeriod.remove(aScheduleOfTimePeriod);
      scheduleOfTimePeriod.add(index, aScheduleOfTimePeriod);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveScheduleOfTimePeriodAt(ScheduleOfTimePeriod aScheduleOfTimePeriod,
      int index) {
    boolean wasAdded = false;
    if (scheduleOfTimePeriod.contains(aScheduleOfTimePeriod)) {
      if (index < 0) {
        index = 0;
      }
      if (index > numberOfScheduleOfTimePeriod()) {
        index = numberOfScheduleOfTimePeriod() - 1;
      }
      scheduleOfTimePeriod.remove(aScheduleOfTimePeriod);
      scheduleOfTimePeriod.add(index, aScheduleOfTimePeriod);
      wasAdded = true;
    } else {
      wasAdded = addScheduleOfTimePeriodAt(aScheduleOfTimePeriod, index);
    }
    return wasAdded;
  }

  public void delete() {
    Museum existingMuseum = museum;
    museum = null;
    if (existingMuseum != null) {
      existingMuseum.delete();
    }
    while (room.size() > 0) {
      Room aRoom = room.get(room.size() - 1);
      aRoom.delete();
      room.remove(aRoom);
    }

    while (timePeriod.size() > 0) {
      TimePeriod aTimePeriod = timePeriod.get(timePeriod.size() - 1);
      aTimePeriod.delete();
      timePeriod.remove(aTimePeriod);
    }

    while (artwork.size() > 0) {
      Artwork aArtwork = artwork.get(artwork.size() - 1);
      aArtwork.delete();
      artwork.remove(aArtwork);
    }

    while (loan.size() > 0) {
      Loan aLoan = loan.get(loan.size() - 1);
      aLoan.delete();
      loan.remove(aLoan);
    }

    while (employee.size() > 0) {
      Employee aEmployee = employee.get(employee.size() - 1);
      aEmployee.delete();
      employee.remove(aEmployee);
    }

    Manager existingManager = manager;
    manager = null;
    if (existingManager != null) {
      existingManager.delete();
    }
    while (schedule.size() > 0) {
      Schedule aSchedule = schedule.get(schedule.size() - 1);
      aSchedule.delete();
      schedule.remove(aSchedule);
    }

    while (visitor.size() > 0) {
      Visitor aVisitor = visitor.get(visitor.size() - 1);
      aVisitor.delete();
      visitor.remove(aVisitor);
    }

    while (ticket.size() > 0) {
      Ticket aTicket = ticket.get(ticket.size() - 1);
      aTicket.delete();
      ticket.remove(aTicket);
    }

    while (scheduleOfTimePeriod.size() > 0) {
      ScheduleOfTimePeriod aScheduleOfTimePeriod =
          scheduleOfTimePeriod.get(scheduleOfTimePeriod.size() - 1);
      aScheduleOfTimePeriod.delete();
      scheduleOfTimePeriod.remove(aScheduleOfTimePeriod);
    }

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
