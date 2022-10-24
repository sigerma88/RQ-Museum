/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


<<<<<<< HEAD
import javax.persistence.*;

// line 31 "model.ump"
// line 132 "model.ump"
@Entity
=======
// line 30 "model.ump"
// line 143 "model.ump"
>>>>>>> issue32
public class Manager extends MuseumUser {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Manager Associations
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

<<<<<<< HEAD
  //no arg constructor
  public Manager(){}
  public Manager(String aEmail, String aName, String aPassword, long aManagerId,
=======
  public Manager(String aEmail, String aName, String aPassword, long aMuseumUserId,
>>>>>>> issue32
      MuseumSystem aMuseumSystem) {
    super(aEmail, aName, aPassword, aMuseumUserId);
    if (aMuseumSystem == null || aMuseumSystem.getManager() != null) {
      throw new RuntimeException(
          "Unable to create Manager due to aMuseumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    museumSystem = aMuseumSystem;
  }

  public Manager(String aEmail, String aName, String aPassword, long aMuseumUserId,
      Museum aMuseumForMuseumSystem) {
    super(aEmail, aName, aPassword, aMuseumUserId);
    museumSystem = new MuseumSystem(aMuseumForMuseumSystem, this);
  }

  // ------------------------
  // INTERFACE
  // ------------------------
<<<<<<< HEAD

  public boolean setManagerId(long aManagerId) {
    boolean wasSet = false;
    managerId = aManagerId;
    wasSet = true;
    return wasSet;
  }

  @Id
  @GeneratedValue
  public long getManagerId() {
    return managerId;
  }

=======
>>>>>>> issue32
  /* Code from template association_GetOne */
  @OneToOne(optional = false)
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
  }

  public void delete() {
    MuseumSystem existingMuseumSystem = museumSystem;
    museumSystem = null;
    if (existingMuseumSystem != null) {
      existingMuseumSystem.delete();
    }
    super.delete();
  }

}
