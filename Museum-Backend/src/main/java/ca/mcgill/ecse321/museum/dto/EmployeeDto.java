package ca.mcgill.ecse321.museum.dto;

import ca.mcgill.ecse321.museum.model.Schedule;

public class EmployeeDto {

    //attributes 
    private String email;
    private String name;
    private String password;

    private Schedule schedule;

    public EmployeeDto() {
    }

    public EmployeeDto(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
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
    
    public Schedule getSchedule() {
        return schedule;
    }
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }


}