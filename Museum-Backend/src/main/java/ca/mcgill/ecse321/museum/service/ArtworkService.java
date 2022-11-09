package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArtworkService {

  @Autowired
  ArtworkRepository artworkRepository;

  @Autowired
  RoomRepository roomRepository;

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
      if (artwork.getRoom().equals(room)) artworksInRoom.add(artwork);
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
