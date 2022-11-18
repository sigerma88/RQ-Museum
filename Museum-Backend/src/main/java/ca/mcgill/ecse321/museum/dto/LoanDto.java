package ca.mcgill.ecse321.museum.dto;

public class LoanDto {

    private Long loanId;
    private Boolean requestAccepted;
    private VisitorDto visitorDto;
    private ArtworkDto artworkDto;

    public LoanDto(){
        
    }

    public LoanDto(boolean aRequestAccepted) {
        requestAccepted = aRequestAccepted;
        visitorDto = null;
        artworkDto = null;
    }

    public LoanDto(boolean aRequestAccepted, VisitorDto aVisitorDto, ArtworkDto aArtworkDto) {
        requestAccepted = aRequestAccepted;
        visitorDto = aVisitorDto;
        artworkDto = aArtworkDto;
    }

    public Long getLoanId() {
        return loanId; 
    }
    
    public Boolean getRequestAccepted() {
        return requestAccepted;
    }

    public VisitorDto getVisitorDto() {
        return visitorDto;
    }

    public ArtworkDto getArtworkDto() {
        return artworkDto;
    }

    public void setVisitor(VisitorDto aVisitorDto) {
        visitorDto = aVisitorDto;
    }

    public void setArtwork(ArtworkDto aArtworkDto) {
        artworkDto = aArtworkDto;
    }
}
