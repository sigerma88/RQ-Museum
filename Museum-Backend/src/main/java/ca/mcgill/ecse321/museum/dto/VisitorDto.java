package ca.mcgill.ecse321.museum.dto;

public class VisitorDto extends MuseumUserDto {
    private long userId;
    private String email;
    private String name;
    private String password;
    private String sessionId;

    public VisitorDto() {}
}
