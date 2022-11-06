package ca.mcgill.ecse321.museum.dto;

public class LoanDto {

    private boolean requestAccepted;
    private VisitorDto visitorDto;
    private ArtworkDto artworkDto;
    
    public LoanDto(){
        this.requestAccepted = false;
    }

    public LoanDto(boolean requestStatus, VisitorDto visitorDto, ArtworkDto artworkDto) {
        this.requestAccepted = requestStatus;
        this.artworkDto = artworkDto;
        this.visitorDto = visitorDto;
    }

    public boolean getRequestAccepted() {
        return requestAccepted;
    }

    public VisitorDto getVisitorDto(){
        return visitorDto;
    }

    public ArtworkDto 

    public void setVisitor(VisitorDto visitorDto) {
        this.visitorDto = visitorDto;
    }

    public void setArtworkDto(ArtworkDto artworkDto){
        this.artworkDto = artworkDto;
    }
}
