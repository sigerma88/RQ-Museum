package ca.mcgill.ecse321.museum.dto;



public class TimePeriodDto {

    // TimePeriod Attributes
    // startDate and endDate are strings, but we convert them to Timestamps in the integration tests
    private Long timePeriodId;
    private String startDate;
    private String endDate;

    // ------------------------
    // CONSTRUCTOR
    // ------------------------

    public TimePeriodDto() {
    }

    public TimePeriodDto(Long timePeriodId, String startDate, String endDate) {
        this.timePeriodId = timePeriodId;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public TimePeriodDto(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getTimePeriodId() {
        return timePeriodId;
    }

    public void setTimePeriodId(Long timePeriodId){
        this.timePeriodId = timePeriodId;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

}
