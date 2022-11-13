package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TestArtworkService {

  @Mock
  private ArtworkRepository artworkRepository;

  @Mock
  private RoomRepository roomRepository;


  @InjectMocks
  private ArtworkService artworkService;

  // Artwork
  private static final long ARTWORK_ID = 678;
  private static final String ARTWORK_NAME = "La Peur";
  private static final String ARTWORK_ARTIST = "Ernest";
  private static final Boolean IS_AVAILABLE_FOR_LOAN = true;
  private static final double LOAN_FEE_1 = 100;
  private static final String IMAGE_1 = "ooo";
  private static final Boolean IS_ON_LOAN_1 = false;

  @BeforeEach
  public void setMockOutput() {


  }

}
