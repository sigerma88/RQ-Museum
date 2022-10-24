/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

<<<<<<< HEAD
// line 18 "model.ump"
// line 122 "model.ump"

@Entity
=======
// line 19 "model.ump"
// line 133 "model.ump"
>>>>>>> issue32
public class Visitor extends MuseumUser {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Visitor Associations
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

<<<<<<< HEAD
  //no arg constructor 
  public Visitor(){}
  public Visitor(String aEmail, String aName, String aPassword, long aVisitorId,
=======
  public Visitor(String aEmail, String aName, String aPassword, long aMuseumUserId,
>>>>>>> issue32
      MuseumSystem aMuseumSystem) {
    super(aEmail, aName, aPassword, aMuseumUserId);
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create visitor due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------
<<<<<<< HEAD

  public boolean setVisitorId(long aVisitorId) {
    boolean wasSet = false;
    visitorId = aVisitorId;
    wasSet = true;
    return wasSet;
  }

  @GeneratedValue
  @Id
  public long getVisitorId() {
    return visitorId;
  }


=======
>>>>>>> issue32
  /* Code from template association_GetOne */
  @ManyToOne
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
      existingMuseumSystem.removeVisitor(this);
    }
    museumSystem.addVisitor(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeVisitor(this);
    }
    super.delete();
  }

}
