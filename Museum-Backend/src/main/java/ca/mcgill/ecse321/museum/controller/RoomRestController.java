package ca.mcgill.ecse321.museum.controller;

import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dto.RoomDto;
import ca.mcgill.ecse321.museum.dto.RoomDtoNoIdRequest;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.service.MuseumService;
import ca.mcgill.ecse321.museum.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * RoomRestController class is used as a controller where we call
 * our API for our web application
 *
 * @author Siger
 * @author kieyanmamiche
 */

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/room")
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

  @PostMapping(value = { "/", "" }, produces = "application/json")
  public ResponseEntity<?> createRoom(@RequestBody RoomDtoNoIdRequest roomDtoNoIdRequest) {
    try {
      // Get museum
      Museum museum = museumService.getMuseum(roomDtoNoIdRequest.getMuseumId());
      if (museum == null) {
        return ResponseEntity.badRequest()
            .body("Museum with id " + roomDtoNoIdRequest.getMuseumId() + " does not exist");
      }

      // Create room
      Room result = roomService.createRoom(roomDtoNoIdRequest.getRoomName(), roomDtoNoIdRequest.getRoomType(), museum);
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

  @GetMapping(value = { "/", "" })
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

  @GetMapping(value = { "/{roomId}", "/{roomId}/" })
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

  @GetMapping(value = { "/museum/{museumId}", "/museum/{museumId}/" })
  public ResponseEntity<?> getAllRoomsByMuseumId(@PathVariable Long museumId) {
    try {
      // Get museum
      Museum museum = museumService.getMuseum(museumId);
      if (museum == null) {
        return ResponseEntity.badRequest().body("Museum with id " + museumId + " does not exist");
      }

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

  @PutMapping(value = { "/{roomId}", "/{roomId}/" }, produces = "application/json")
  public ResponseEntity<?> editRoom(@PathVariable("roomId") Long roomId,
      @RequestBody RoomDtoNoIdRequest roomDtoNoIdRequest) {
    try {
      // Get museum
      Museum museum = null;
      if (roomDtoNoIdRequest.getMuseumId() != null) {
        museum = museumService.getMuseum(roomDtoNoIdRequest.getMuseumId());
        if (museum == null) {
          return ResponseEntity.badRequest()
              .body("Museum with id " + roomDtoNoIdRequest.getMuseumId() + " does not exist");
        }
      }

      // Update room
      Room result = roomService.editRoom(roomId, roomDtoNoIdRequest.getRoomName(), roomDtoNoIdRequest.getRoomType(),
          museum);
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

  @DeleteMapping(value = { "/{roomId}", "/{roomId}/" })
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
   * @return maximum number of artworks of the room with the given id
   * @author Siger
   */

  @GetMapping(value = { "/maxArtworks/{roomId}", "/maxArtworks/{roomId}/" })
  public ResponseEntity<?> getMaxArtworks(@PathVariable("roomId") Long roomId) {
    try {
      Room room = roomService.getRoomById(roomId);
      if (room == null) {
        return ResponseEntity.badRequest().body("Room with id " + roomId + " does not exist");
      }
      return ResponseEntity.ok(roomService.getMaxNumberOfArtwork(room.getRoomType()));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  /**
   * RESTful API to view the capacity of a certain room
   * <p>
   * FR3 -> View room capacity
   *
   * @param roomId - The id of a room we want to get the capacity of
   * @return The capacity of the room
   * @author kieyanmamiche
   */
  @GetMapping(value = { "/getRoomCapacity/{roomId}", "/getRoomCapacity/{roomId}/" })
  public ResponseEntity<?> getRoomCapacity(@PathVariable("roomId") long roomId) {
    try {
      int capacity = roomService.getRoomCapacity(roomId);
      return ResponseEntity.ok(capacity);
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
