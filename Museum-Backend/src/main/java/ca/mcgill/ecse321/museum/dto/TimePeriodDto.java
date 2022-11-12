package ca.mcgill.ecse321.museum.dto;

import java.sql.Timestamp;

public class TimePeriodDto {

    // TimePeriod Attributes
    private Long timePeriodId;
    private Timestamp startDate;
    private Timestamp endDate;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public TimePeriodDto() {
    }

    public TimePeriodDto(Long timePeriodId, Timestamp startDate, Timestamp endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getTimePeriodId() {
        return timePeriodId;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

}
