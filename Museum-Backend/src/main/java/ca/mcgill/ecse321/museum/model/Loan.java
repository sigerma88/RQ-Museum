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
  private boolean requestedAccepted;

  // Loan Associations
  private Visitor visitor;
  private Artwork artwork;
  private MuseumSystem museumSystem;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------
  
  //no arg constructor
  public Loan(){}
  public Loan(long aLoanId, boolean aRequestedAccepted, Visitor aVisitor, Artwork aArtwork,
      MuseumSystem aMuseumSystem) {
    loanId = aLoanId;
    requestedAccepted = aRequestedAccepted;
    if (!setVisitor(aVisitor)) {
      throw new RuntimeException(
          "Unable to create Loan due to aVisitor. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    if (!setArtwork(aArtwork)) {
      throw new RuntimeException(
          "Unable to create Loan due to aArtwork. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
    boolean didAddMuseumSystem = setMuseumSystem(aMuseumSystem);
    if (!didAddMuseumSystem) {
      throw new RuntimeException(
          "Unable to create loan due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setLoanId(long aLoanId) {
    boolean wasSet = false;
    loanId = aLoanId;
    wasSet = true;
    return wasSet;
  }

  public boolean setRequestedAccepted(boolean aRequestedAccepted) {
    boolean wasSet = false;
    requestedAccepted = aRequestedAccepted;
    wasSet = true;
    return wasSet;
  }

  @Id
  @GeneratedValue
  public long getLoanId() {
    return loanId;
  }

  public boolean getRequestedAccepted() {
    return requestedAccepted;
  }

  /* Code from template association_GetOne */
  @ManyToOne(optional = false)
  public Visitor getVisitor() {
    return visitor;
  }

  /* Code from template association_GetOne */
  @OneToOne(optional = false)
  public Artwork getArtwork() {
    return artwork;
  }

  /* Code from template association_GetOne */
  @ManyToOne(optional = false)
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

  /* Code from template association_SetUnidirectionalOne */
  public boolean setArtwork(Artwork aNewArtwork) {
    boolean wasSet = false;
    if (aNewArtwork != null) {
      artwork = aNewArtwork;
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
      existingMuseumSystem.removeLoan(this);
    }
    museumSystem.addLoan(this);
    wasSet = true;
    return wasSet;
  }

  public void delete() {
    visitor = null;
    artwork = null;
    MuseumSystem placeholderMuseumSystem = museumSystem;
    this.museumSystem = null;
    if (placeholderMuseumSystem != null) {
      placeholderMuseumSystem.removeLoan(this);
    }
  }


  public String toString() {
    return super.toString() + "[" + "loanId" + ":" + getLoanId() + "," + "requestedAccepted" + ":"
        + getRequestedAccepted() + "]" + System.getProperties().getProperty("line.separator") + "  "
        + "visitor = "
        + (getVisitor() != null ? Integer.toHexString(System.identityHashCode(getVisitor()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "artwork = "
        + (getArtwork() != null ? Integer.toHexString(System.identityHashCode(getArtwork()))
            : "null")
        + System.getProperties().getProperty("line.separator") + "  " + "museumSystem = "
        + (getMuseumSystem() != null
            ? Integer.toHexString(System.identityHashCode(getMuseumSystem()))
            : "null");
  }
}
