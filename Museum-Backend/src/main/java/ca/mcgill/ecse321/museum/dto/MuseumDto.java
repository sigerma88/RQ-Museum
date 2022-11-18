package ca.mcgill.ecse321.museum.dto;

/**
 * Museum DTO
 * 
 * @author VZ
 */

public class MuseumDto {
    private Long museumId;
    private String name;
    private double visitFee;
    private ScheduleDto schedule;

    public MuseumDto() {
    }

    public MuseumDto(Long museumId, String name, double visitFee, ScheduleDto schedule) {
        this.museumId = museumId;
        this.name = name;
        this.visitFee = visitFee;
        this.schedule = schedule;
    }
    public MuseumDto(String name, double visitFee, ScheduleDto schedule) {
        this.name = name;
        this.visitFee = visitFee;
        this.schedule = schedule;
    }

    public void setMuseumId(Long museumId) {
        this.museumId = museumId;
    }
    public Long getMuseumId() {
        return museumId;
    }
    public String getMuseumName() {
        return name;
    }
    public void setMuseumName(String name) {
        this.name = name;
    }
    public double getVisitFee() {
        return visitFee;
    }
    public void setVisitFee(double visitFee) {
        this.visitFee = visitFee;
    }
    public ScheduleDto getSchedule() {
        return schedule;
    }
    public void setSchedule(ScheduleDto schedule) {
        this.schedule = schedule;
    }
}
