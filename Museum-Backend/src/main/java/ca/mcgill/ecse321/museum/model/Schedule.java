/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


// line 53 "model.ump"
// line 163 "model.ump"
public class Schedule {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Schedule Attributes
  private long schedule;

  // Schedule Associations
  private Employee employee;
  private Museum museum;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public Schedule(long aSchedule, Employee aEmployee, Museum aMuseum, MuseumSystem aMuseumSystem) {
    schedule = aSchedule;
    if (aEmployee == null || aEmployee.getSchedule() != null) {
      throw new RuntimeException(
          "Unable to create Schedule due to aEmployee. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    employee = aEmployee;
    if (aMuseum == null || aMuseum.getSchedule() != null) {
      throw new RuntimeException(
          "Unable to create Schedule due to aMuseum. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    museum = aMuseum;
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create schedule due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  public Schedule(long aSchedule, String aEmailForEmployee, String aNameForEmployee,
      String aPasswordForEmployee, long aEmployeeIdForEmployee,
      MuseumSystem aMuseumSystemForEmployee, long aMuseumIdForMuseum, String aNameForMuseum,
      double aVisitFeeForMuseum, MuseumSystem aMuseumSystemForMuseum, MuseumSystem aMuseumSystem) {
    schedule = aSchedule;
    employee = new Employee(aEmailForEmployee, aNameForEmployee, aPasswordForEmployee,
        aEmployeeIdForEmployee, this, aMuseumSystemForEmployee);
    museum = new Museum(aMuseumIdForMuseum, aNameForMuseum, aVisitFeeForMuseum, this,
        aMuseumSystemForMuseum);
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create schedule due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setSchedule(long aSchedule) {
    boolean wasSet = false;
    schedule = aSchedule;
    wasSet = true;
    return wasSet;
  }

  public long getSchedule() {
    return schedule;
  }

  /* Code from template association_GetOne */
  public Employee getEmployee() {
    return employee;
  }

  /* Code from template association_GetOne */
  public Museum getMuseum() {
    return museum;
  }

  /* Code from template association_GetOne */
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
      existingMuseumSystem.removeSchedule(this);
    }
    museumSystem.addSchedule(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    Employee existingEmployee = employee;
    employee = null;
    if (existingEmployee != null) {
      existingEmployee.delete();
    }
    Museum existingMuseum = museum;
    museum = null;
    if (existingMuseum != null) {
      existingMuseum.delete();
    }
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeSchedule(this);
    }
  }


  public String toString() {
    return super.toString() + "[" + "schedule" + ":" + getSchedule() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "employee = "
        + (getEmployee() != null ? Integer.toHexString(System.identityHashCode(getEmployee()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museum = "
        + (getMuseum() != null ? Integer.toHexString(System.identityHashCode(getMuseum())) : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
