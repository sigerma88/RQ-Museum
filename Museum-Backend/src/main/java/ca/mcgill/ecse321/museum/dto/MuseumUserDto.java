package ca.mcgill.ecse321.museum.dto;

/**
 * MuseumUser DTO
 *
 * @author Kevin
 */

public class MuseumUserDto {
  private long userId;
  private String email;
  private String name;
  private String password;
  private String sessionId;
  private String role;

  public MuseumUserDto() {}

  public MuseumUserDto(long userId, String email, String name, String password) {
    this.userId = userId;
    this.email = email;
    this.name = name;
    this.password = password;
  }

  public Long getMuseumUserId() {
    return userId;
  }

  public void setMuseumUserId(Long userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }
}
