package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;

@Service
public class RoomService {
  
  @Autowired
  RoomRepository roomRepository;
  
  @Autowired
  MuseumRepository museumRepository;
  
  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  ArtworkService artworkService;
  
  /**
   * Method to create a room
   * 
   * @param roomName - name of the room
   * @param museum - museum of the room
   * @return room
   * @author Siger
   */
  @Transactional
  public Room createRoom(String roomName, RoomType roomType, Museum museum) {
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
    Room room = roomRepository.findRoomByRoomId(roomId);
    return room;
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
    return toList(roomRepository.findRoomByMuseum(museum));
  }

  /**
   * Method to edit a room
   * 
   * @param roomId - id of the room
   * @param roomName - name of the room
   * @param roomType - type of the room
   * @param museum - museum of the room
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

    // Set new values
    if (roomName != null) room.setRoomName(roomName);
    if (roomType != null) room.setRoomType(roomType);
    if (museum != null) room.setMuseum(museum);
    roomRepository.save(room);
    return room;
  }

  /**
   * Method to change current number of artworks in a room
   * 
   * @param roomId - id of the room
   * @param currentNumberOfArtworks - number of artworks in the room
   * @return room
   * @author Siger
   */
  @Transactional
  public Room changeCurrentNumberOfArtworks(Long roomId, int currentNumberOfArtworks) {
    // Get room and check if it exists
    Room room = roomRepository.findRoomByRoomId(roomId);
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    // Set new value
    room.setCurrentNumberOfArtwork(currentNumberOfArtworks);
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
    // Get room and check if it exists
    Room room = roomRepository.findRoomByRoomId(roomId);
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    // Delete artworks in the room
    List<Artwork> artworks = toList(artworkRepository.findArtworkByRoom(room));
    for (Artwork artwork : artworks) {
      boolean b = artworkService.deleteArtwork(artwork.getArtworkId());
    }

    // Delete room
    roomRepository.delete(room);
    return room;
  }

  /**
   * Method to convert an Iterable to a List
   * 
   * @param iterable - Iterable
   * @return List
   * @author From tutorial notes
   */
  private <T> List<T> toList(Iterable<T> iterable){
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}
