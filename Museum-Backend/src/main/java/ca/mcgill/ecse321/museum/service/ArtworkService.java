package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for Artwork class
 *
 * @author Zahra
 * @author Siger
 * @author kieyanmamiche
 */

@Service
public class ArtworkService {

  @Autowired
  private ArtworkRepository artworkRepository;

  @Autowired
  private RoomRepository roomRepository;

  @Autowired
  private LoanRepository loanRepository;

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

    if (isAvailableForLoan) {
      if (loanFee == null) {
        throw new IllegalArgumentException("Loan fee cannot be null if artwork is available for loan");
      }
    } else if (loanFee != null) {
      throw new IllegalArgumentException("Loan fee must be null if artwork is not available for loan");
    }

    if (image == null || image.trim().length() == 0) {
      throw new IllegalArgumentException("Image cannot be empty");
    }

    if (isOnLoan == null) {
      throw new IllegalArgumentException("Artwork must be either on loan or not on loan");
    } else if (isOnLoan) {
      if (room != null) {
        throw new IllegalArgumentException("Room must be null if artwork is on loan");
      }
    } else if (room == null) {
      throw new IllegalArgumentException("Room cannot be null if artwork is not on loan");
    } else {
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
   * Method to get all artworks in a room
   *
   * @param roomId - id of room
   * @return all artworks in specific room
   * @author Zahra
   * @author Siger
   * @author kieyanmamiche
   */

  @Transactional
  public List<Artwork> getAllArtworksByRoom(Long roomId) {
    // Get room
    Room room = roomService.getRoomById(roomId);

    // Error handling
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    return toList(artworkRepository.findArtworkByRoom(room));
  }

  /**
   * Method to get all artworks by if they are available for loan
   *
   * @param isAvailableForLoan - availability of the artwork
   * @return Artworks that are available for loan
   * @author Zahra
   * @author Siger
   */

  @Transactional
  public List<Artwork> getAllArtworksByAvailabilityForLoan(Boolean isAvailableForLoan) {
    // Error handling
    if (isAvailableForLoan == null) {
      throw new IllegalArgumentException("Artwork availability cannot be null");
    }

    return toList(artworkRepository.findArtworkByIsAvailableForLoan(isAvailableForLoan));
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

    if ((name == null || name.trim().length() == 0) && (artist == null || artist.trim().length() == 0) && (image == null || image.trim().length() == 0)) {
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
    if (isAvailableForLoan) {
      if (loanFee == null) {
        throw new IllegalArgumentException("Loan fee cannot be null if artwork is available for loan");
      }
    } else if (loanFee != null) {
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
    List<Loan> loan = loanRepository.findLoanByArtwork(artwork);
    if (loan != null && loan.size() > 0) {
      loanRepository.deleteLoanByArtwork(artwork);
    }

    // Delete artwork
    artworkRepository.deleteArtworkByArtworkId(artworkId);

    return artwork;
  }

  /**
   * Method to get artwork status
   *
   * @param artworkId - ID of artwork we want the status of
   * @return The status of an artwork
   * @author kieyanmamiche
   */

  @Transactional
  public String getArtworkStatus(long artworkId) {
    Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);

    // Make sure artwork exists
    if (artwork == null) {
      throw new IllegalArgumentException("Artwork does not exist");
    }

    // Find out the status of the artwork OPTIONS: loan / on display / in storage
    // 1. Check if it is on loan:
    if (artwork.getIsOnLoan()) {
      return "loan";
    }

    Room roomOfArtwork = artwork.getRoom();
    if (roomOfArtwork == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    RoomType roomType = roomOfArtwork.getRoomType();
    if (roomType == RoomType.Storage) {
      return "storage";
    }

    if (roomType == RoomType.Small || roomType == RoomType.Large) {
      return "display";
    }

    throw new IllegalArgumentException("Artwork not initialized correctly");
  }

  /**
   * Method which checks how many artworks in a room
   * <p>
   * FR3 Function which checks how many artworks in single room
   *
   * @param roomId - ID of room we want to see how many artworks are in
   * @return The number of artworks in a room
   * @author kieyanmamiche
   */

  @Transactional
  public int getNumberOfArtworksInRoom(long roomId) {
    List<Artwork> artworkList = new ArrayList<Artwork>();

    Room room = roomRepository.findRoomByRoomId(roomId);
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }
    artworkList = artworkRepository.findArtworkByRoom(room);

    return artworkList.size();
  }


  /**
   * Method to move artwork to a specific room
   * <p>
   * FR3 Function which moves artwork to corresponding room
   *
   * @param artworkId - ID of artwork we want to move
   * @param roomId    - ID of room we want to move artwork to
   * @return The artwork we moved
   * @author kieyanmamiche
   */

  @Transactional
  public Artwork moveArtworkToRoom(long artworkId, long roomId) {
    // Find artwork
    Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);

    // corresponds to error finding artwork
    if (artwork == null) {
      throw new IllegalArgumentException("Artwork does not exist");
    }

    // Find room
    Room room = roomRepository.findRoomByRoomId(roomId);

    // corresponds to error finding room
    if (room == null) {
      throw new IllegalArgumentException("Room does not exist");
    }

    // Update room of artwork if the room has capacity
    if (roomService.getRoomCapacity(roomId) == -1 || roomService.getRoomCapacity(roomId) > 0) {
      // Reduce amount of artwork in old room
      Room oldRoom = artwork.getRoom();
      if (oldRoom != null) {
        roomService.changeCurrentNumberOfArtwork(oldRoom.getRoomId(), oldRoom.getCurrentNumberOfArtwork() - 1);
      }

      // increase amount of artwork in new room if there is capacity
      roomService.changeCurrentNumberOfArtwork(room.getRoomId(), room.getCurrentNumberOfArtwork() + 1);
      artwork.setRoom(room);
      return artworkRepository.save(artwork);
    } else {
      // corresponds to room being full
      throw new IllegalArgumentException("Room is at full capacity");
    }
  }

  /**
   * Method to remove artwork from a specific room
   * <p>
   * FR3 Function which removes artwork from room
   *
   * @param artworkId - ID of artwork we want to remove
   * @return The artwork we removed
   * @author kieyanmamiche
   */

  @Transactional
  public Artwork removeArtworkFromRoom(long artworkId) {
    // Find artwork
    Artwork artwork = artworkRepository.findArtworkByArtworkId(artworkId);

    // corresponds to error
    if (artwork == null) {
      throw new IllegalArgumentException("Artwork does not exist");
    }

    // decrement number of artworks in room by 1
    Room room = artwork.getRoom();
    if (room != null) {
      roomService.changeCurrentNumberOfArtwork(room.getRoomId(), room.getCurrentNumberOfArtwork() - 1);
    }

    // Update room
    artwork.setRoom(null);
    artworkRepository.save(artwork);


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
