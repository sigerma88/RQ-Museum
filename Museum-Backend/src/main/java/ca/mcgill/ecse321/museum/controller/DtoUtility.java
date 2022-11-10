package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.dto.RoomDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;

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
    return new EmployeeDto(employee.getMuseumUserId(), employee.getEmail(), employee.getName(), employee.getPassword(), scheduleDto);
  }

  /**
   * Method to convert an artwork to DTO
   *
   * @param artwork - Artwork
   * @return artwork DTO
   * @author Zahra
   * @author Siger
   */
  static ArtworkDto convertToDto(Artwork artwork) {
    if (artwork == null) {
      throw new IllegalArgumentException("There is no such artwork");
    }
    RoomDto roomDto = convertToDto(artwork.getRoom());
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
    if (room == null) {
      throw new IllegalArgumentException("There is no such room");
    }
    MuseumDto museumDto = convertToDto(room.getMuseum());
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
    if (museum == null) {
      throw new IllegalArgumentException("There is no such museum");
    }
    ScheduleDto scheduleDto = convertToDto(museum.getSchedule());
    return new MuseumDto(museum.getMuseumId(), museum.getName(), museum.getVisitFee(), scheduleDto);
  }
}
