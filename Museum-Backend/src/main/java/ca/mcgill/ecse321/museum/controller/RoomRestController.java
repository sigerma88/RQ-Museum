package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.dto.RoomDto;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
public class RoomRestController {

  @Autowired
  private RoomService roomService;

  @Autowired
  private MuseumService museumService;

  /**
   * RESTful API to create a room
   * 
   * @param roomName - name of the room
   * @param roomType - type of the room
   * @param museumId - id of the museum of the room
   * @return created room
   * @author Siger
   */
  @PostMapping(value = { "/room", "/room/" }, produces = "application/json")
  public ResponseEntity<?> createRoom(@RequestParam(name = "roomName") String roomName, @RequestParam(name = "roomType") RoomType roomType, @RequestParam(name = "museumId") Long museumId) {
    try {
      // Get museum
      Museum museum = museumService.getMuseum(museumId);

      // Create room
      Room result = roomService.createRoom(roomName, roomType, museum);
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get all rooms
   * 
   * @return List of all rooms
   * @author Siger
   */
  @GetMapping(value = { "/rooms", "/rooms/" })
  public ResponseEntity<?> getAllRooms() {
    try {
      List<RoomDto> roomDtos = new ArrayList<>();
      for (Room room : roomService.getAllRooms()) {
        roomDtos.add(DtoUtility.convertToDto(room));
      }
      return ResponseEntity.ok(roomDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get a room by id
   * 
   * @param roomId - id of the room
   * @return room with the given id
   * @author Siger
   */
  @GetMapping(value = { "/room/{roomId}", "/room/{roomId}/" })
  public ResponseEntity<?> getRoomById(@PathVariable Long roomId) {
    try {
      Room room = roomService.getRoomById(roomId);
      return ResponseEntity.ok(DtoUtility.convertToDto(room));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get all rooms of a museum
   * 
   * @param museumId - id of the museum
   * @return List of all rooms of the museum with the given id
   * @author Siger
   */
  @GetMapping(value = { "/rooms/{museumId}", "/rooms/{museumId}/" })
  public ResponseEntity<?> getAllRoomsByMuseumId(@PathVariable Long museumId) {
    try {
      // Get museum
      Museum museum = museumService.getMuseum(museumId);

      // Get rooms
      List<RoomDto> roomDtos = new ArrayList<>();
      for (Room room : roomService.getAllRoomsByMuseum(museum)) {
        roomDtos.add(DtoUtility.convertToDto(room));
      }
      return ResponseEntity.ok(roomDtos);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to update a room
   * 
   * @param roomId   - id of the room
   * @param roomName - name of the room
   * @param roomType - type of the room
   * @param museumId - id of the museum of the room
   * @return updated room
   * @author Siger
   */
  @PutMapping(value = { "/room/{roomId}", "/room/{roomId}/" }, produces = "application/json")
  public ResponseEntity<?> editRoom(@PathVariable("roomId") Long roomId, @RequestParam(name = "roomName", required = false) String roomName, @RequestParam(name = "roomType", required = false) RoomType roomType, @RequestParam(name = "museumId", required = false) Long museumId) {
    try {
      // Get museum
      Museum museum = null;
      if (museumId != null) {
        museum = museumService.getMuseum(museumId);
      }

      // Update room
      Room result = roomService.editRoom(roomId, roomName, roomType, museum);
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to delete a room
   * 
   * @param roomId - id of the room
   * @return deleted room
   * @author Siger
   */
  @DeleteMapping(value = { "/room/{roomId}", "/room/{roomId}/" })
  public ResponseEntity<?> deleteRoom(@PathVariable("roomId") Long roomId) {
    try {
      Room result = roomService.deleteRoom(roomId);
      return ResponseEntity.ok(DtoUtility.convertToDto(result));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to get the maximum number of artworks of a room
   * 
   * @param roomId - id of the room
   */
  @GetMapping(value = { "/room/{roomId}/maxArtworks", "/room/{roomId}/maxArtworks/" })
  public ResponseEntity<?> getMaxArtworks(@PathVariable("roomId") Long roomId) {
    try {
      Room room = roomService.getRoomById(roomId);
      return ResponseEntity.ok(roomService.getMaxNumberOfArtwork(room.getRoomType()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
