/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import javax.persistence.*;

// line 36 "model.ump"
// line 118 "model.ump"
// line 148 "model.ump"
@Entity
public class Loan {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // Loan Attributes
  private long loanId;
  private boolean requestAccepted;

  // Loan Associations
  private Visitor visitor;
  private Artwork artwork;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  // no arg constructor
  public Loan() {}

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setLoanId(long aLoanId) {
    boolean wasSet = false;
    loanId = aLoanId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRequestAccepted(boolean aRequestAccepted) {
    boolean wasSet = false;
    requestAccepted = aRequestAccepted;
    wasSet = true;
    return wasSet;
  }

  @Id
  @GeneratedValue
  public long getLoanId() {
    return loanId;
  }

  @Column(nullable = false)
  public boolean getRequestAccepted() {
    return requestAccepted;
  }

  /* Code from template association_GetOne */
  @ManyToOne()
  @JoinColumn(name = "visitor_id", referencedColumnName = "museumUserId", nullable = false)
  public Visitor getVisitor() {
    return visitor;
  }

  /* Code from template association_GetOne */
  @OneToOne()
  @JoinColumn(name = "artwork_id", referencedColumnName = "artworkId", nullable = false)
  public Artwork getArtwork() {
    return artwork;
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

  /* Code from template association_SetUnidirectionalOne */
  public boolean setArtwork(Artwork aNewArtwork) {
    boolean wasSet = false;
    if (aNewArtwork != null) {
      artwork = aNewArtwork;
      wasSet = true;
    }
    return wasSet;
  }

  public String toString() {
    return super.toString() + "[" + "loanId" + ":" + getLoanId() + "," + "requestAccepted" + ":"
        + getRequestAccepted() + "]" + System.getProperties().getProperty("line.separator") + "  "
        + "visitor = "
        + (getVisitor() != null ? Integer.toHexString(System.identityHashCode(getVisitor()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "artwork = "
        + (getArtwork() != null ? Integer.toHexString(System.identityHashCode(getArtwork()))
            : "null")
        + System.getProperties().getProperty("line.separator");
  }
}
