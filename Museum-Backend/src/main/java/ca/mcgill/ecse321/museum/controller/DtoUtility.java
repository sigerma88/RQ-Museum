package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.TicketDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.model.Ticket;
import ca.mcgill.ecse321.museum.dto.VisitorDto;

public class DtoUtility {
  /**
   * Method to convert a schedule to a DTO
   *
   * @param schedule - Schedule
   * @return schedule DTO
   * @author Siger
   */
  static ScheduleDto convertToDto(Schedule schedule) {
    if (schedule == null) {
      throw new IllegalArgumentException("There is no such schedule");
    }
    return new ScheduleDto(schedule.getScheduleId());
  }

  /**
   * Method to convert an employee to a DTO
   *
   * @param employee - Employee
   * @return employee DTO
   * @author Siger
   */
  static EmployeeDto convertToDto(Employee employee) {
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }
    ScheduleDto scheduleDto = convertToDto(employee.getSchedule());
    return new EmployeeDto(employee.getMuseumUserId(), employee.getEmail(), employee.getName(),
            employee.getPassword(), scheduleDto);
  }

  /**
   * Method to convert a visitor to a DTO
   *
   * @param visitor - Visitor
   * @return visitor DTO
   * @author Kevin
   */
  static VisitorDto convertToDto(Visitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    VisitorDto visitorDto = new VisitorDto();
    visitorDto.setEmail(visitor.getEmail());
    visitorDto.setName(visitor.getName());
    visitorDto.setPassword(visitor.getPassword());
    visitorDto.setUserId(visitor.getMuseumUserId());
    return visitorDto;
  }


  /**
   * Method to convert a ticket to a DTO
   *
   * @param ticket - Ticket
   * @return ticket DTO
   * @author Zahra
   */
  static TicketDto convertToDto(Ticket ticket) {
    if (ticket == null) {
      throw new IllegalArgumentException("There is no such ticket");
    }
    VisitorDto visitorDto = convertToDto(ticket.getVisitor());

    TicketDto ticketDto = new TicketDto();
    ticketDto.setVisitor(visitorDto);
    ticketDto.setTicketId(ticket.getTicketId());
    ticketDto.setVisitDate(ticket.getVisitDate());
    return ticketDto;
  }


}
