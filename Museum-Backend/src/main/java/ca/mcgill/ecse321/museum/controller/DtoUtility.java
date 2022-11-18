package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.dto.RoomDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.dto.LoanDto;;

public class DtoUtility {
  /**
   * Method to convert a schedule to a DTO
   *
   * @param schedule - Schedule
   * @return schedule DTO
   * @author Siger
   */
  static ScheduleDto convertToDto(Schedule schedule) {
    // Error handling
    if (schedule == null) {
      throw new IllegalArgumentException("There is no such schedule");
    }

    // Convert schedule to DTO
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
    // Error handling
    if (employee == null) {
      throw new IllegalArgumentException("There is no such employee");
    }

    // Convert schedule to DTO
    ScheduleDto scheduleDto = convertToDto(employee.getSchedule());

    // Convert employee to DTO
    return new EmployeeDto(employee.getMuseumUserId(), employee.getEmail(), employee.getName(), employee.getPassword(), scheduleDto);
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
    LoanDto loanDto = new LoanDto(loan.getRequestAccepted(), visitorDto, artworkDto);
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
    return new ArtworkDto(artwork.getArtworkId(), artwork.getName(), artwork.getArtist(), artwork.getIsAvailableForLoan(), artwork.getLoanFee(), artwork.getImage(), artwork.getIsOnLoan(), roomDto);
  }

  /**
   * Method to convert a room to DTO
   * 
   * @param room - Room
   * @return room DTO
   * @author Siger
   */
  static RoomDto convertToDto(Room room) {
    // Error handling
    if (room == null) {
      throw new IllegalArgumentException("There is no such room");
    }

    // Convert museum to DTO
    MuseumDto museumDto = convertToDto(room.getMuseum());

    // Convert room to DTO
    return new RoomDto(room.getRoomId(), room.getRoomName(), room.getRoomType(), room.getCurrentNumberOfArtwork(), museumDto);
  }

  /**
   * Method to convert a museum to DTO
   * 
   * @param museum - Museum
   * @return museum DTO
   * @author Siger
   */
  static MuseumDto convertToDto(Museum museum) {
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
   * Method to convert an visitor to a DTO
   * 
   * @param museumUser - Visitor
   * @return visitor DTO
   * @author Kevin
   */
  public static VisitorDto convertToDto(Visitor visitor) {
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

}
