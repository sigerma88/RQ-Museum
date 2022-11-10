package ca.mcgill.ecse321.museum.dto;

import ca.mcgill.ecse321.museum.model.RoomType;

/**
 * Room DTO
 * 
 * @author Siger
 */
public class RoomDto {

  private Long roomId;
  private String roomName;
  private RoomType roomType;
  private Integer currentNumberOfArtworks;
  private MuseumDto museum;

  public RoomDto() {
  }

  public RoomDto(Long roomId, String roomName, RoomType roomType, Integer currentNumberOfArtworks,  MuseumDto museum) {
    this.roomId = roomId;
    this.roomName = roomName;
    this.roomType = roomType;
    this.currentNumberOfArtworks = currentNumberOfArtworks;
    this.museum = museum;
  }

  public Long getRoomId() {
    return roomId;
  }

  public void setRoomId(Long roomId) {
    this.roomId = roomId;
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

  public Integer getCurrentNumberOfArtworks() {
    return currentNumberOfArtworks;
  }

  public void setCurrentNumberOfArtworks(Integer currentNumberOfArtworks) {
    this.currentNumberOfArtworks = currentNumberOfArtworks;
  }

  public MuseumDto getMuseum() {
    return museum;
  }

  public void setMuseum(MuseumDto museum) {
    this.museum = museum;
  }
}
