package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Room;

@Service
public class ArtworkService {

  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  LoanRepository loanRepository;

  @Autowired
  RoomService roomService;

  /**
   * Method to create an artwork
   * 
   * @param name               - name of the artwork
   * @param artist             - artist of the artwork
   * @param isAvailableForLoan - availability of the artwork
   * @param loanFee            - loan fee of the artwork
   * @param image              - image of the artwork
   * @param isOnLoan           - loan status of the artwork
   * @param room               - room of the artwork
   * @return artwork
   * @author Siger
   */
  @Transactional
  public Artwork createArtwork(String name, String artist, Boolean isAvailableForLoan, Double loanFee, String image,
      Boolean isOnLoan, Room room) {
    // Error handling
    if (name == null || name.trim().length() == 0) {
      throw new IllegalArgumentException("Artwork name cannot be empty");
    }

    if (artist == null || artist.trim().length() == 0) {
      throw new IllegalArgumentException("Artist name cannot be empty");
    }

    if (isAvailableForLoan == true) {
      if (loanFee == null) {
        throw new IllegalArgumentException("Loan fee cannot be null if artwork is available for loan");
      }
    } else if (isAvailableForLoan == false && loanFee != null) {
      throw new IllegalArgumentException("Loan fee must be null if artwork is not available for loan");
    }

    if (image == null || image.trim().length() == 0) {
      throw new IllegalArgumentException("Image cannot be empty");
    }

    if (isOnLoan == null) {
      throw new IllegalArgumentException("Artwork must be either on loan or not on loan");
    } else if (isOnLoan == true) {
      if (room != null) {
        throw new IllegalArgumentException("Room must be null if artwork is on loan");
      }
    } else if (isOnLoan == false && room == null) {
      throw new IllegalArgumentException("Room cannot be null if artwork is not on loan");
    } else if (isOnLoan == false && room != null) {
      // Update room artwork count
      roomService.changeCurrentNumberOfArtwork(room.getRoomId(), room.getCurrentNumberOfArtwork() + 1);
    }

    Artwork artwork = new Artwork();
    artwork.setName(name);
    artwork.setArtist(artist);
    artwork.setIsAvailableForLoan(isAvailableForLoan);
    artwork.setLoanFee(loanFee);
    artwork.setImage(image);
    artwork.setIsOnLoan(isOnLoan);
    if (room != null)
      artwork.setRoom(room);
    artworkRepository.save(artwork);
    return artwork;
  }

  /**
   * Method to get artwork by id
   * 
   * @param artworkId - id of the artwork
   * @return artwork
   * @author Siger
   */
  @Transactional
  public Artwork getArtwork(Long artworkId) {
    // Error handling
    if (artworkId == null) {
      throw new IllegalArgumentException("Artwork id cannot be null");
    }

    return artworkRepository.findArtworkByArtworkId(artworkId);
  }

  /**
   * Method to get a list of all artworks in the database
   * Allows visitor to browse all artworks
   *
   * @return list of all artworks
   * @author Zahra
   */
  @Transactional
  public List<Artwork> getAllArtworks() {
    return toList(artworkRepository.findAll());
  }

  /**
   * Method to get a list of all artworks in a room
   *
   * @param room - Room
   * @return artworksInRoom - all artworks in room
   * @author Zahra
   */
  @Transactional
  public List<Artwork> getAllArtworksByRoom(Room room) {
    List<Artwork> artworksInRoom = new ArrayList<>();
    for (Artwork artwork : getAllArtworks()) {
      if (artwork.getRoom().equals(room))
        artworksInRoom.add(artwork);
    }
    return artworksInRoom;
  }

  public boolean getArtworkAvailabilityForLoan(Artwork artwork) {
    return artwork.getIsAvailableForLoan();
  }

  public double getArtworkLoanFee(Artwork artwork) {
    return artwork.getLoanFee();
  }

  /**
   * Method to edit an artwork's information and image
   * 
   * @param artworkId - ID of artwork to be edited
   * @param name      - name of the artwork
   * @param artist    - artist of the artwork
   * @param image     - image of the artwork
   * @return artwork
   * @author Siger
   */
  @Transactional
  public Artwork editArtworkInfo(Long artworkId, String name, String artist, String image) {
    // Get artwork and check if it exists and error handling
    Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);
    if (artwork == null) {
      throw new IllegalArgumentException("Artwork does not exist");
    }

    if ((name == null || name.trim().length() == 0) && (artist == null || artist.trim().length() == 0)
        && (image == null || image.trim().length() == 0)) {
      throw new IllegalArgumentException("Nothing to edit, all fields are empty");
    }

    // Edit artwork information
    if (name != null)
      artwork.setName(name);
    if (artist != null)
      artwork.setArtist(artist);
    if (image != null)
      artwork.setImage(image);
    return artworkRepository.save(artwork);
  }

  /**
   * Method to edit an artwork's loan availability and loan fee
   * 
   * @param artworkId          - ID of artwork to be edited
   * @param isAvailableForLoan - availability of the artwork
   * @param loanFee            - loan fee of the artwork
   * @return artwork
   * @author Siger
   */
  @Transactional
  public Artwork editArtworkLoanInfo(Long artworkId, Boolean isAvailableForLoan, Double loanFee) {
    // Get artwork and check if it exists and error handling
    Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);
    if (artwork == null) {
      throw new IllegalArgumentException("Artwork does not exist");
    }

    if (isAvailableForLoan == null) {
      isAvailableForLoan = artwork.getIsAvailableForLoan();
    }
    if (isAvailableForLoan == true) {
      if (loanFee == null) {
        throw new IllegalArgumentException("Loan fee cannot be null if artwork is available for loan");
      }
    } else if (isAvailableForLoan == false && loanFee != null) {
      throw new IllegalArgumentException("Loan fee must be null if artwork is not available for loan");
    }

    // Change loan availability
    artwork.setIsAvailableForLoan(isAvailableForLoan);
    artwork.setLoanFee(loanFee);
    return artworkRepository.save(artwork);
  }

  /**
   * Method to delete an artwork
   * 
   * @param artworkId - ID of artwork to be deleted
   * @return if the artwork was deleted (success)
   * @author Siger
   */
  @Transactional
  public Artwork deleteArtwork(Long artworkId) {
    // Get artwork and check if it exists and error handling
    Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);
    if (artwork == null) {
      throw new IllegalArgumentException("Artwork does not exist");
    }

    // Delete loan if artwork is on loan
    Loan loan = loanRepository.findLoanByArtwork(artwork);
    if (loan != null) {
      loanRepository.deleteLoanByArtwork(artwork);
    }

    // Delete artwork
    artworkRepository.deleteArtworkByArtworkId(artworkId);

    return artwork;
  }

  /**
   * Method to convert an Iterable to a List
   *
   * @param iterable -Iterable
   * @return List
   * @author Tutorial notes
   */
  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}
