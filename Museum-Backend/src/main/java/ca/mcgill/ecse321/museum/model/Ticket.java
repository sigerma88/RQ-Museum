/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import java.sql.Date;
import javax.annotation.Generated;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

// line 2 "model.ump"
<<<<<<< HEAD
// line 112 "model.ump"

@Entity
=======
// line 123 "model.ump"
>>>>>>> issue32
public class Ticket {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Ticket Attributes
  private long ticketId;
  private Date visitDate;

  // Ticket Associations
  private Visitor visitor;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  //no arg constructor
  public Ticket(){}
  
  public Ticket(long aTicketId, Date aVisitDate, Visitor aVisitor, MuseumSystem aMuseumSystem) {
    ticketId = aTicketId;
    visitDate = aVisitDate;
    if (!setVisitor(aVisitor)) {
      throw new RuntimeException(
          "Unable to create Ticket due to aVisitor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create ticket due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setTicketId(long aTicketId) {
    boolean wasSet = false;
    ticketId = aTicketId;
    wasSet = true;
    return wasSet;
  }

  public boolean setVisitDate(Date aVisitDate) {
    boolean wasSet = false;
    visitDate = aVisitDate;
    wasSet = true;
    return wasSet;
  }

  @GeneratedValue
  @Id
  public long getTicketId() {
    return ticketId;
  }

  public Date getVisitDate() {
    return visitDate;
  }

  /* Code from template association_GetOne */
  @ManyToOne
  public Visitor getVisitor() {
    return visitor;
  }

  /* Code from template association_GetOne */
  @ManyToOne
  public MuseumSystem getMuseumSystem() {
    return museumSystem;
  }

  /* Code from template association_SetUnidirectionalOne */
  public boolean setVisitor(Visitor aNewVisitor) {
    boolean wasSet = false;
    if (aNewVisitor != null) {
      visitor = aNewVisitor;
      wasSet = true;
    }
    return wasSet;
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
      existingMuseumSystem.removeTicket(this);
    }
    museumSystem.addTicket(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    visitor = null;
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeTicket(this);
    }
  }


  public String toString() {
    return super.toString() + "[" + "ticketId" + ":" + getTicketId() + "]"
        + System.getProperties().getProperty("line.separator") + "  " + "visitDate" + "="
        + (getVisitDate() != null
            ? !getVisitDate().equals(this) ? getVisitDate().toString().replaceAll("  ", "    ")
                : "this"
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "visitor = "
        + (getVisitor() != null ? Integer.toHexString(System.identityHashCode(getVisitor()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
