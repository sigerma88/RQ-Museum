package ca.mcgill.ecse321.museum.dto;

public class VisitorDto {
    private Long userId;
    private String email;
    private String name;
    private String password;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
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

    // public String getPassword() {
    // return password;
    // }

    public void setPassword(String password) {
        this.password = password;
    }
}
