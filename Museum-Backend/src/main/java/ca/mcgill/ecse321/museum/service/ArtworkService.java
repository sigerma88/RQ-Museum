package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
   * Method to get all artworks that are available for loan
   *
   * @return Artworks that are available for loan
   * @author Zahra
   * @author Siger
   */
  @Transactional
  public List<Artwork> getArtworksAvailableForLoan() {
    return toList(artworkRepository.findArtworkByIsAvailableForLoan(true));
  }

  /**
   * Method to get all artworks that are not available for loan
   *
   * @return Artworks that are not available for loan
   * @author Zahra
   * @author Siger
   */
  @Transactional
  public List<Artwork> getArtworksNotAvailableForLoan() {
    return toList(artworkRepository.findArtworkByIsAvailableForLoan(false));
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
