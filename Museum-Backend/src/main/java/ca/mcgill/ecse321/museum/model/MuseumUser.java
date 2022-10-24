/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;


// line 10 "model.ump"
// line 122 "model.ump"
public abstract class MuseumUser {

  // ------------------------
  // MEMBER VARIABLES
  // ------------------------

  // MuseumUser Attributes
  private String email;
  private String name;
  private String password;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public MuseumUser(String aEmail, String aName, String aPassword) {
    email = aEmail;
    name = aName;
    password = aPassword;
  }

  // ------------------------
  // INTERFACE
  // ------------------------

  public boolean setEmail(String aEmail) {
    boolean wasSet = false;
    email = aEmail;
    wasSet = true;
    return wasSet;
  }

  public boolean setName(String aName) {
    boolean wasSet = false;
    name = aName;
    wasSet = true;
    return wasSet;
  }

  public boolean setPassword(String aPassword) {
    boolean wasSet = false;
    password = aPassword;
    wasSet = true;
    return wasSet;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public void delete() {}


  public String toString() {
    return super.toString() + "[" + "email" + ":" + getEmail() + "," + "name" + ":" + getName()
        + "," + "password" + ":" + getPassword() + "]";
  }
}
