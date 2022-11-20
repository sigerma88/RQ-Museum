package ca.mcgill.ecse321.museum.controller.utilities;

import ca.mcgill.ecse321.museum.dto.*;
import ca.mcgill.ecse321.museum.model.*;

import java.text.SimpleDateFormat;

public class DtoUtility {

  /**
   * Method to convert an employee to a DTO
   *
   * @param employee - Employee
   * @return employee DTO
   * @author Siger
   */

  public static EmployeeDto convertToDto(Employee employee) {
    // Error handling
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    // Convert schedule to DTO
    ScheduleDto scheduleDto = convertToDto(employee.getSchedule());
    return new EmployeeDto(employee.getMuseumUserId(), employee.getEmail(), employee.getName(), employee.getPassword(), scheduleDto);
  }

  /**
   * Method to convert a visitor to a DTO
   *
   * @param visitor - Visitor
   * @return visitor DTO
   * @author Kevin
   */

  public static VisitorDto convertToDto(Visitor visitor) {
    if (visitor == null) {
      throw new IllegalArgumentException("There is no such visitor");
    }

    VisitorDto visitorDto = new VisitorDto();
    visitorDto.setEmail(visitor.getEmail());
    visitorDto.setName(visitor.getName());
    visitorDto.setPassword(visitor.getPassword());
    visitorDto.setMuseumUserId(visitor.getMuseumUserId());
    return visitorDto;
  }

  /**
   * Method to convert an Manager to a DTO
   *
   * @param manager - Manager
   * @return visitor DTO
   * @author Kevin
   */

  public static ManagerDto convertToDto(Manager manager) {
    if (manager == null) {
      throw new IllegalArgumentException("There is no such manager");
    }

    ManagerDto managerDto = new ManagerDto();
    managerDto.setEmail(manager.getEmail());
    managerDto.setName(manager.getName());
    managerDto.setPassword(manager.getPassword());
    managerDto.setMuseumUserId(manager.getMuseumUserId());
    return managerDto;
  }

  /**
   * Method to convert a schedule to a DTO
   *
   * @param schedule - Schedule
   * @return schedule DTO
   * @author Siger
   */

  public static ScheduleDto convertToDto(Schedule schedule) {
    // Error handling
    if (schedule == null) {
      throw new IllegalArgumentException("There is no such schedule");
    }

    // Convert schedule to DTO
    return new ScheduleDto(schedule.getScheduleId());
  }

  /**
   * Method to convert a time period to a DTO.
   * It converts the start and end date to a String.
   *
   * @param timePeriod
   * @return
   * @author VZ
   */

  public static TimePeriodDto convertToDto(TimePeriod timePeriod) {
    if (timePeriod == null) {
      throw new IllegalArgumentException("There is no such time period");
    }
    String startDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timePeriod.getStartDate());
    String endDateString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timePeriod.getEndDate());
    return new TimePeriodDto(timePeriod.getTimePeriodId(), startDateString, endDateString);
  }

  /**
   * Method to convert a loan to a DTO
   *
   * @param loan - Loan
   * @return loan DTO
   * @author Eric
   */
  public static LoanDto convertToDto(Loan loan) {
    if (loan == null) {
      throw new IllegalArgumentException("There is no such Loan");
    }
    VisitorDto visitorDto = convertToDto(loan.getVisitor());
    ArtworkDto artworkDto = convertToDto(loan.getArtwork());
    LoanDto loanDto = new LoanDto(loan.getLoanId(), loan.getRequestAccepted(), visitorDto, artworkDto);
    return loanDto;
  }

  /**
   * Method to convert an artwork to DTO
   *
   * @param artwork - Artwork
   * @return artwork DTO
   * @author Zahra
   * @author Siger
   */

  public static ArtworkDto convertToDto(Artwork artwork) {
    // Error handling
    if (artwork == null) {
      throw new IllegalArgumentException("There is no such artwork");
    }

    // Convert room to DTO
    RoomDto roomDto = null;
    if (artwork.getRoom() != null) {
      roomDto = convertToDto(artwork.getRoom());
    }

    // Convert artwork to DTO
    return new ArtworkDto(artwork.getArtworkId(), artwork.getName(), artwork.getArtist(),
        artwork.getIsAvailableForLoan(), artwork.getLoanFee(), artwork.getImage(), artwork.getIsOnLoan(), roomDto);
  }

  /**
   * Method to convert a room to DTO
   *
   * @param room - Room
   * @return room DTO
   * @author Siger
   */

  public static RoomDto convertToDto(Room room) {
    // Error handling
    if (room == null) {
      throw new IllegalArgumentException("There is no such room");
    }

    // Convert museum to DTO
    MuseumDto museumDto = convertToDto(room.getMuseum());

    // Convert room to DTO
    return new RoomDto(room.getRoomId(), room.getRoomName(), room.getRoomType(), room.getCurrentNumberOfArtwork(),
        museumDto);
  }

  /**
   * Method to convert a museum to DTO
   *
   * @param museum - Museum
   * @return museum DTO
   * @author Siger
   * @author Victor
   */

  public static MuseumDto convertToDto(Museum museum) {
    // Error handling
    if (museum == null) {
      throw new IllegalArgumentException("There is no such museum");
    }

    // Convert schedule to DTO
    ScheduleDto scheduleDto = convertToDto(museum.getSchedule());

    // Convert museum to DTO
    return new MuseumDto(museum.getMuseumId(), museum.getName(), museum.getVisitFee(), scheduleDto);
  }


  /**
   * Method to convert a ticket to a DTO
   *
   * @param ticket - Ticket
   * @return ticket DTO
   * @author Zahra
   */
  public static TicketDto convertToDto(Ticket ticket) {
    // Error handling
    if (ticket == null) {
      throw new IllegalArgumentException("There is no such ticket");
    }

    // Convert visitor to DTO
    VisitorDto visitorDto = convertToDto(ticket.getVisitor());

    return new TicketDto(ticket.getTicketId(), ticket.getVisitDate().toString(), visitorDto);
  }
}
