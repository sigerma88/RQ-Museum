package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ArtworkService {

  @Autowired
  ArtworkRepository artworkRepository;


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
   * Method to get all artworks with their
   * corresponding availability for loan
   *
   * @return artworksWithAvailability
   * @author Zahra
   */
  @Transactional
  public List<Artwork> getUnavailableArtworks() {
    List<Artwork> unAvailableArtworks = artworkRepository.findArtworkByIsAvailableForLoan(false);

    return unAvailableArtworks;
  }


  /**
   * Method to
   *
   * @return
   * @author Zahra
   */
  @Transactional
  public List<Artwork> getAllAvailableArtworks() {
    List<Artwork> availableArtworks = artworkRepository.findArtworkByIsAvailableForLoan(true);

    return availableArtworks;
  }
  /*
  public Map<String, Double> getArtworkWithLoanFee() {

    Map<String, Double> artworksWithLoanFee = new HashMap<>();
    for (Artwork artwork : getAllArtworks()) {
      artworkRepository.findArtworkByIsAvailableForLoan();
      artworksWithLoanFee.put(artwork.getName(), artwork.getLoanFee());
    }
    return artworksWithLoanFee;
  }*/


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
