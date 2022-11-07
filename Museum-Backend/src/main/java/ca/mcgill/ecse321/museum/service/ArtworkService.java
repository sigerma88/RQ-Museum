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

  /*@Transactional
  public Artwork createArtwork() {
    Artwork artwork = new Artwork();
    artwork

  }
*/
  @Transactional
  public List<Artwork> getAllArtworks() {

    return toList(artworkRepository.findAll());


  }

  @Transactional
  public List<Artwork> getAllArtworksByRoom(Room room) {

    List<Artwork> artworksInRoom = new ArrayList<>();
    for (Artwork a : getAllArtworks()) {
      if (a.getRoom().equals(room)) artworksInRoom.add(a);
    }

    return artworksInRoom;

  }


  public boolean getArtworkAvailabilityForLoan(Artwork artwork) {

    return artwork.getIsAvailableForLoan();
  }

  public double getArtworkLoanFee(Artwork artwork) {

    return artwork.getLoanFee();
  }

  private <T> List<T> toList(Iterable<T> iterable) {
    List<T> requestedList = new ArrayList<T>();
    for (T t : iterable) {
      requestedList.add(t);
    }
    return requestedList;
  }


}
