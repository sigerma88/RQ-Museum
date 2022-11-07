package ca.mcgill.ecse321.museum.dto;

import java.sql.Timestamp;

public class TimePeriodDto {
    
      // TimePeriod Attributes
  private Timestamp startDate;
  private Timestamp endDate;

  // ------------------------
  // CONSTRUCTOR
  // ------------------------

  public TimePeriodDto() {}

  public TimePeriodDto(Timestamp startDate, Timestamp endDate) {
      this.startDate = startDate;
      this.endDate = endDate;
  }

  public Timestamp getStartDate() {
      return startDate;
  }
  
  public Timestamp getEndDate() {
      return endDate;
  }

}
