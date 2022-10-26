/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import java.sql.Date;
import javax.persistence.*;

// line 2 "model.ump"
// line 123 "model.ump"
@Entity
public class Ticket {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Ticket Attributes
  private long ticketId;
  private Date visitDate;

  // Ticket Associations
  private Visitor visitor;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public Ticket() {}

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

  @Column(nullable = false)
  public Date getVisitDate() {
    return visitDate;
  }

  /* Code from template association_GetOne */
  @ManyToOne()
  @JoinColumn(name="visitor_id", referencedColumnName = "museumUserId", nullable = false)
  public Visitor getVisitor() {
    return visitor;
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
        + System.getProperties().getProperty("line.separator");
  }
}
