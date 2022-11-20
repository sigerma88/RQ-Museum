package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Business logic for roomController
 *
 * @author Siger
 * @author kieyanmamiche
 */

@Service
public class RoomService {

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private ArtworkRepository artworkRepository;

  /**
   * Method to create a room
   *
   * @param roomName - name of the room
   * @param roomType - type of the room
   * @param museum   - museum of the room
   * @return room
   * @author Siger
   */

  @Transactional
  public Room createRoom(String roomName, RoomType roomType, Museum museum) {
    // Error handling
    if (roomName == null || roomName.trim().length() == 0) {
      throw new IllegalArgumentException("Room name cannot be empty");
    }
    if (roomType == null) {
      throw new IllegalArgumentException("Room type cannot be empty");
    }
    if (museum == null) {
      throw new IllegalArgumentException("Museum cannot be empty");
    }

    // Create room
    Room room = new Room();
    room.setRoomName(roomName);
    room.setRoomType(roomType);
    room.setCurrentNumberOfArtwork(0);
    room.setMuseum(museum);
    roomRepository.save(room);
    return room;
  }

  /**
   * Method to get room by id
   *
   * @param roomId - id of the room
   * @return room
   * @author Siger
   */

  @Transactional
  public Room getRoomById(Long roomId) {
    // Error handling
    if (roomId == null) {
      throw new IllegalArgumentException("Room id cannot be empty");
    }

    // Get room
    return roomRepository.findRoomByRoomId(roomId);
  }

  /**
   * Method to get all rooms
   *
   * @return list of rooms
   * @author Siger
   */

  @Transactional
  public List<Room> getAllRooms() {
    return toList(roomRepository.findAll());
  }

  /**
   * Method to get all rooms by museum
   *
   * @param museum - museum of the room
   * @return list of rooms
   * @author Siger
   */

  @Transactional
  public List<Room> getAllRoomsByMuseum(Museum museum) {
    // Error handling
    if (museum == null) {
      throw new IllegalArgumentException("Museum cannot be empty");
    }

    return toList(roomRepository.findRoomByMuseum(museum));
  }

  /**
   * Method to edit a room
   *
   * @param roomId   - id of the room
   * @param roomName - name of the room
   * @param roomType - type of the room
   * @param museum   - museum of the room
   * @return room
   * @author Siger
   */

  @Transactional
  public Room editRoom(Long roomId, String roomName, RoomType roomType, Museum museum) {
    // Get room and check if it exists
    Room room = roomRepository.findRoomByRoomId(roomId);
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    if ((roomName == null || roomName.trim().length() == 0) && roomType == null && museum == null) {
      throw new IllegalArgumentException("Nothing to edit, all fields are empty");
    }

    // Set new values
    if (roomName != null)
      room.setRoomName(roomName);
    if (roomType != null)
      room.setRoomType(roomType);
    if (museum != null)
      room.setMuseum(museum);
    roomRepository.save(room);
    return room;
  }

  /**
   * Method to delete a room
   *
   * @param roomId - id of the room
   * @return room
   * @author Siger
   */

  @Transactional
  public Room deleteRoom(Long roomId) {
    // Get room and check if it exists and error handling
    Room room = roomRepository.findRoomByRoomId(roomId);
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    // Check if there are artworks in the room
    List<Artwork> artworks = toList(artworkRepository.findArtworkByRoom(room));
    if (artworks.size() > 0) {
      throw new IllegalArgumentException("There are still artworks in the room, remove them first");
    }

    // Delete room
    roomRepository.delete(room);
    return room;
  }

  /**
   * Method to get the maximum number of artworks in a room
   * -1 if room has no limit
   *
   * @param roomType - type of the room
   * @return maximum number of artworks
   * @author Siger
   */

  @Transactional
  public int getMaxNumberOfArtwork(RoomType roomType) {
    // Error handling
    if (roomType == null) {
      throw new IllegalArgumentException("Room type cannot be empty");
    }

    // Get maximum number of artworks
    int maxNumberOfArtwork = 0;
    if (roomType.equals(RoomType.Small)) {
      maxNumberOfArtwork = 200;
    } else if (roomType.equals(RoomType.Large)) {
      maxNumberOfArtwork = 300;
    } else if (roomType.equals(RoomType.Storage)) {
      maxNumberOfArtwork = -1;
    } else {
      throw new IllegalArgumentException("Room type is invalid");
    }

    return maxNumberOfArtwork;
  }

  /**
   * Method to change current number of artworks in a room
   *
   * @param roomId                 - id of the room
   * @param currentNumberOfArtwork - number of artworks in the room
   * @return room
   * @author Siger
   */

  public Room changeCurrentNumberOfArtwork(Long roomId, Integer currentNumberOfArtwork) {
    // Get room and check if it exists and error handling
    Room room = roomRepository.findRoomByRoomId(roomId);
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    if (currentNumberOfArtwork == null) {
      throw new IllegalArgumentException("Current number of artworks cannot be empty");
    } else if (currentNumberOfArtwork < 0) {
      throw new IllegalArgumentException("Current number of artworks cannot be negative");
    }

    // Check if current number of artworks is less than the maximum number of
    // artworks
    int maxNumberOfArtwork = getMaxNumberOfArtwork(room.getRoomType());
    if (maxNumberOfArtwork != -1 && currentNumberOfArtwork > maxNumberOfArtwork) {
      throw new IllegalArgumentException("Current number of artworks " + currentNumberOfArtwork
          + " cannot be greater than the maximum number of artworks " + maxNumberOfArtwork);
    }

    // Set new value
    room.setCurrentNumberOfArtwork(currentNumberOfArtwork);
    roomRepository.save(room);
    return room;
  }

  /**
   * Method to get room capacity
   * -1 if room has no limit
   *
   * @param roomId - ID of room we want to capacity of
   * @return The capacity of a room
   * @author kieyanmamiche
   * @author Siger
   */

  @Transactional
  public int getRoomCapacity(long roomId) {

    Room room = roomRepository.findRoomByRoomId(roomId);

    // Check if the room is not in the DB
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    int maxCapacity = getMaxNumberOfArtwork(room.getRoomType());

    if (maxCapacity == -1) {
      return -1;
    } else {
      return maxCapacity - room.getCurrentNumberOfArtwork();
    }
  }

  /**
   * Method to convert an Iterable to a List
   *
   * @param iterable - Iterable
   * @return List
   * @author From tutorial notes
   */

  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}