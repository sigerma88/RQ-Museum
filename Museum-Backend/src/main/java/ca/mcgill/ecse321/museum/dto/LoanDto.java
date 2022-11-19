package ca.mcgill.ecse321.museum.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoanDto {

    private Long loanId;
    private Boolean requestAccepted;
    private VisitorDto visitorDto;
    private ArtworkDto artworkDto;

    public LoanDto(){
        
    }

    public LoanDto(Long aLoanId, Boolean aRequestAccepted) {
        loanId = aLoanId;
        requestAccepted = aRequestAccepted;
        visitorDto = null;
        artworkDto = null;
    }

    public LoanDto(Boolean aRequestAccepted) {
        requestAccepted = aRequestAccepted;
        visitorDto = null;
        artworkDto = null;
    }

    public LoanDto(Boolean aRequestAccepted, VisitorDto aVisitorDto, ArtworkDto aArtworkDto) {
        requestAccepted = aRequestAccepted;
        visitorDto = aVisitorDto;
        artworkDto = aArtworkDto;
    }

    public LoanDto(Long aLoanId, Boolean aRequestAccepted, VisitorDto aVisitorDto, ArtworkDto aArtworkDto) {
        loanId = aLoanId;
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

    public void setVisitorDto(VisitorDto aVisitorDto) {
        visitorDto = aVisitorDto;
    }

    public void setArtworkDto(ArtworkDto aArtworkDto) {
        artworkDto = aArtworkDto;
    }

    @JsonProperty("ArtworkDto")
    public void setArtworkId(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setArtworkId(artworkDtoDeserializer.getArtworkId());
    }

    @JsonProperty("ArtworkDto")
    public void setName(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setName(artworkDtoDeserializer.getName());
    }

    @JsonProperty("ArtworkDto")
    public void setArtist(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setArtist(artworkDtoDeserializer.getArtist());
    }

    @JsonProperty("ArtworkDto")
    public void setIsAvailableForLoan(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setIsAvailableForLoan(artworkDtoDeserializer.getIsAvailableForLoan());
    }

    @JsonProperty("ArtworkDto")
    public void setLoanFee(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setLoanFee(artworkDtoDeserializer.getLoanFee());
    }

    @JsonProperty("ArtworkDto")
    public void setImage(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setImage(artworkDtoDeserializer.getImage());
    }

    @JsonProperty("ArtworkDto")
    public void setIsOnLoan(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setIsOnLoan(artworkDtoDeserializer.getIsOnLoan());
    }

    @JsonProperty("ArtworkDto")
    public void setRoom(ArtworkDtoDeserializer artworkDtoDeserializer) {
        artworkDto.setRoom(artworkDtoDeserializer.getRoom());
    }
}

