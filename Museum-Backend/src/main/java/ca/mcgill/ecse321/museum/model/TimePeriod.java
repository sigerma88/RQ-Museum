/* PLEASE DO NOT EDIT THIS CODE */
/* This code was generated using the UMPLE 1.31.1.5860.78bb27cc6 modeling language! */

package ca.mcgill.ecse321.museum.model;

import java.sql.Date;

// line 85 "model.ump"
// line 178 "model.ump" public class TimePeriod
{
 
  //------------------------
  //  MEMBER VARIABLES
  //------------------------
 
  //TimePeriod Attributes
  private long timePeriodId;
  private Date startDate;
  private Date endDate;
 
  //TimePeriod Associations
  private MuseumSystem museumSystem;
 
  //------------------------
  //  CONSTRUCTOR
  //------------------------

       public TimePeriod(long aTimePeriodId, Date aStartDate, Date aEndDate, MuseumSystem aMuseumSystem)
  {
    timePeriodId = aTimePeriodId;
    startDate = aStartDate;
    endDate = aEndDate;
    boolean didAddMuseumSyst if (!didAddMuseumSystem)
    {
          
      throw new RuntimeException("Unable to create timePeriod due to museumSystem. See http://manual.umple.org?RE002ViolationofAssociationMultiplicity.html");
    }
  }
 
  //------------------------
  //  INTERFACE
  //------------------------
 public boolean setTimePeriodId(long aTimePeriodId)
  {
    boolean wasSet = false;
    timePeriodId = aTimePeriodId;
    wasSet = true;
    return wasSet;
  }
 public boolean setStartDate(Date aStartDate)
  {
    boolean wasSet = false;
    startDate = aStartDate;
    wasSet = true;
    return wasSet;
  }
 public boolean setEndDate(Date aEndDate)
  {
    boolean wasSet = false;
    endDate = aEndDate;
    wasSet = true;
    return wasSet;
  }
 public long getTimePeriodId()
  {
    return timePeriodId;
  }
 public Date getStartDate()
  {
    return startDate;
  }
 public Date getEndDate()
  {
   

  }
  /* Code from template association_Get public MuseumSystem getMuseumSystem()
  {
   

  }
  /* Code from template association_SetOneToMany */ public boolean setMuseumSystem(MuseumSystem aMuseumSystem)
  {
    boolean wasSet = false; if (aMuseumSystem == null)
    {
      return wasSet;
    }

    MuseumSystem existingMuseumSystem = museumSystem;
    museumSystem = aMuseumSystem; if (existingMuseumSystem != null && !existingMuseumSystem.equals(aMuseumSystem))
    {
      existingMuseumSystem.removeTimePeriod(this);
    }
    museumSystem.addTimePeriod(this);
    wasSet = true;
    return wasSet;
  }
 public void delete()
  {
    MuseumSystem placeholderMuseumSystem = museumSystem;
    th is.museumSystem = null; if(placeholderMuseumSystem != null)
    {
      placeholderMuseumSystem.removeTimePeriod(this);
    }
  }

 public String toString()
  {  uper.toString() + "["+ 
         "timePeriodId" + ":" + g
        tTimePeriodId()+ "]" + Sy
            tem.getProperties().getPropert "line.separator") + 
                
            
         "  " + "startDate" + "
        " + (getStartDate() != 
            ull ? !getStartDate().equals his)  ? getStartDate().toString().replaceA ll("  ","    ") 
             "this" :
        "null") + System.getProperties().getProperty("line.separ "  " + "endDate" + "=" +
          (getEndDate() != n ul l ? 
            ! getEndDate().equals(this)  ? getEndDate().toString().replaceAll
            ( "  ","    ") : "this" : "null") + System.getProperties().getProperty("line.separator") +
            "  " + "museumSystem = "+(getMuseumSystem()!=null?Integer.toHexString(System.identityHashCode(getMuseumSystem())):"null");
 
 }
}