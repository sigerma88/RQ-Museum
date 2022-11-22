package ca.mcgill.ecse321.museum.dto;

import ca.mcgill.ecse321.museum.model.RoomType;

/**
 * Room DTO
 *
 * @author Siger
 */

public class RoomDtoNoIdRequest {

  private String roomName;
  private RoomType roomType;
  private Integer currentNumberOfArtwork;
  private Long museumId;

  public RoomDtoNoIdRequest() {
  }

  public RoomDtoNoIdRequest(String roomName, RoomType roomType, Integer currentNumberOfArtwork, Long museumId) {
    this.roomName = roomName;
    this.roomType = roomType;
    this.currentNumberOfArtwork = currentNumberOfArtwork;
    this.museumId = museumId;
  }

  public String getRoomName() {
    return roomName;
  }

  public void setRoomName(String roomName) {
    this.roomName = roomName;
  }

  public RoomType getRoomType() {
    return roomType;
  }

  public void setRoomType(RoomType roomType) {
    this.roomType = roomType;
  }

  public Integer getCurrentNumberOfArtwork() {
    return currentNumberOfArtwork;
  }

  public void setCurrentNumberOfArtwork(Integer currentNumberOfArtwork) {
    this.currentNumberOfArtwork = currentNumberOfArtwork;
  }

  public Long getMuseumId() {
    return museumId;
  }

  public void setMuseumId(Long museumId) {
    this.museumId = museumId;
  }
}
