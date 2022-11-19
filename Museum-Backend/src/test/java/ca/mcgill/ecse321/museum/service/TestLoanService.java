package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.museum.controller.DtoUtility;
import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.ArtworkDto;
import ca.mcgill.ecse321.museum.dto.LoanDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Artwork;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Room;
import ca.mcgill.ecse321.museum.model.RoomType;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.TimePeriod;
import ca.mcgill.ecse321.museum.model.Visitor;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;

/**
 * This is the test class for the LoanService class
 * 
 * @author Eric
 */
@ExtendWith(MockitoExtension.class)
public class TestLoanService {

    @Mock
    private LoanRepository loanRepository;

    @Mock 
    private VisitorRepository visitorRepository;

    @Mock
    private ArtworkRepository artworkRepository;

    @InjectMocks
    private LoanService loanService;

    @Mock
    private ArtworkService artworkService;

    private static final long LOAN_ID = 1;
    private static final Boolean LOAN_REQUESTACCEPTED = null;

    private static final long SECOND_LOAN_ID = 2;
    private static final Boolean SECOND_LOAN_REQUESTACCEPTED = null;

    // Loan that should fail because associations with bad artwork or visitor
    private static final long THIRD_LOAN_ID = 3;
    private static final boolean THIRD_LOAN_REQUESTACCEPTED = true; 

    private static final long FOURTH_LOAN_ID = 4;
    private static final boolean FOURTH_LOAN_REQUESTACCEPTED = false;
    
    private static final long FIFTH_LOAN_ID = 5;
    private static final boolean FIFTH_LOAN_REQUESTACCEPTED = false;

    private static final long NONE_EXISTING_LOAN_ID = 6;

    private static final long VISITOR_ID = 1;
    private static final String VISITOR_EMAIL = "IAMAVISITOR@email.com";
    private static final String VISITOR_NAME = "Steve";
    private static final String VISITOR_PASSWORD = "Password123";

    private static final long SECOND_VISITOR_ID = 2;
    private static final String SECOND_VISITOR_EMAIL = "IAMAVISITOR@email.com";
    private static final String SECOND_VISITOR_NAME = "Steve";
    private static final String SECOND_VISITOR_PASSWORD = "Password123";

    private static final long NONE_EXISTING_VISITOR_ID = 3;

    // Artwork that can't be loaned
    private static final long ARTWORK_ID =1;
    private static final String ARTWORK_NAME = "George";
    private static final String ARTWORK_ARTIST = "Curious";
    private static final boolean ARTWORK_ISAVAILABLEFORLOAN = false;
    private static final boolean ARTWORK_ISONLOAN = false; 
    private static final double ARTWORK_LOANFEE = 54.2;
    private static final String ARTWORK_IMAGE = "bruh";

    // Artwork that can be loaned and isn't on loan
    private static final long SECOND_ARTWORK_ID =2;
    private static final String SECOND_ARTWORK_NAME = "HELLO";
    private static final String SECOND_ARTWORK_ARTIST = "WORLD";
    private static final boolean SECOND_ARTWORK_ISAVAILABLEFORLOAN = true;
    private static final boolean SECOND_ARTWORK_ISONLOAN = false; 
    private static final double SECOND_ARTWORK_LOANFEE = 63.5;
    private static final String SECOND_ARTWORK_IMAGE = "bruuuuh";

    // Artwork that can be loaned but is on loan
    private static final long THIRD_ARTWORK_ID =3;
    private static final String THIRD_ARTWORK_NAME = "dna";
    private static final String THIRD_ARTWORK_ARTIST = "code";
    private static final boolean THIRD_ARTWORK_ISAVAILABLEFORLOAN = true;
    private static final boolean THIRD_ARTWORK_ISONLOAN = true; 
    private static final double THIRD_ARTWORK_LOANFEE = 69.69;
    private static final String THIRD_ARTWORK_IMAGE = "bruuuuuuuuuuuuuuuuuh";

    private static final long FOURTH_ARTWORK_ID = 4;
    private static final String FOURTH_ARTWORK_NAME = "hEY";
    private static final String FOURTH_ARTWORK_ARTIST = "HeyMonika";
    private static final boolean FOURTH_ARTWORK_ISAVAILABLEFORLOAN = true;
    private static final boolean FOURTH_ARTWORK_ISONLOAN = false; 
    private static final double FOURTH_ARTWORK_LOANFEE = 39.69;
    private static final String FOURTH_ARTWORK_IMAGE = "wikipedia.com";

    private static final long NONE_EXISTING_ARTWORK_ID = 5;

    // Room
    private static final long ROOM_ID =1;
    private static final String ROOM_ROOMNAME = "George";
    private static final int ROOM_CURRENTNUMBEROFARTWORK = 5;
    private static final RoomType ROOM_ROOMTYPE = RoomType.Small;


    @BeforeEach
    public void setMockOutput() {

        /**
         * When anything is saved, just return the parameter object
         * @author TA
         */
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
			return invocation.getArgument(0);
		};

        lenient().when(loanRepository.save(any(Loan.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(artworkRepository.save(any(Artwork.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(visitorRepository.save(any(Visitor.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(loanRepository.saveAll(any(Iterable.class))).thenAnswer(returnParameterAsAnswer);

        
        lenient().when(loanRepository.findLoanByLoanId(LOAN_ID)).thenAnswer((InvocationOnMock invocation) -> 
        {
            if (invocation.getArgument(0).equals(LOAN_ID)) {

                Loan loan1 = new Loan();
                loan1.setLoanId(LOAN_ID);
                loan1.setRequestAccepted(LOAN_REQUESTACCEPTED);
                loan1.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID));
                loan1.setArtwork(artworkRepository.findArtworkByArtworkId(SECOND_ARTWORK_ID));
                return loan1;       
            } else {
                return null;
            }
        });

        lenient().when(loanRepository.findLoanByLoanId(SECOND_LOAN_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(SECOND_LOAN_ID)) {

                Loan loan2 = new Loan();
                loan2.setLoanId(SECOND_LOAN_ID);
                loan2.setRequestAccepted(SECOND_LOAN_REQUESTACCEPTED);
                loan2.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID));
                loan2.setArtwork(artworkRepository.findArtworkByArtworkId(SECOND_ARTWORK_ID));
                return loan2;
            } else {
                return null;
            }
        });

        lenient().when(loanRepository.findLoanByLoanId(THIRD_LOAN_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(THIRD_LOAN_ID)) {

                Loan loan3 = new Loan();
                loan3.setLoanId(THIRD_LOAN_ID);
                loan3.setRequestAccepted(THIRD_LOAN_REQUESTACCEPTED);
                loan3.setVisitor(visitorRepository.findVisitorByMuseumUserId(NONE_EXISTING_VISITOR_ID));
                loan3.setArtwork(artworkRepository.findArtworkByArtworkId(NONE_EXISTING_ARTWORK_ID));
                return loan3;
            } else {
                return null;
            }
        });

        lenient().when(loanRepository.findLoanByLoanId(FOURTH_LOAN_ID)).thenAnswer((InvocationOnMock invocation) -> 
        {
            if (invocation.getArgument(0).equals(FOURTH_LOAN_ID)) {

                Loan loan4 = new Loan();
                loan4.setLoanId(FOURTH_LOAN_ID);
                loan4.setRequestAccepted(FOURTH_LOAN_REQUESTACCEPTED);
                loan4.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID));
                loan4.setArtwork(artworkRepository.findArtworkByArtworkId(FOURTH_ARTWORK_ID));

                return loan4;       
            } else {
                return null;
            }
        });

        lenient().when(loanRepository.findLoanByLoanId(FIFTH_LOAN_ID)).thenAnswer((InvocationOnMock invocation) -> 
        {
            if (invocation.getArgument(0).equals(FIFTH_LOAN_ID)) {

                Loan loan5 = new Loan();
                loan5.setLoanId(FIFTH_LOAN_ID);
                loan5.setRequestAccepted(FIFTH_LOAN_REQUESTACCEPTED);
                loan5.setVisitor(visitorRepository.findVisitorByMuseumUserId(SECOND_VISITOR_ID));
                loan5.setArtwork(artworkRepository.findArtworkByArtworkId(FOURTH_ARTWORK_ID));

                return loan5;       
            } else {
                return null;
            }
        });

        lenient().when(loanRepository.findAll()).thenAnswer((InvocationOnMock invocation) ->
        {
            Iterable<Loan> loans = new ArrayList<Loan>();
            Loan loan1 = new Loan();
            loan1.setLoanId(LOAN_ID);
            loan1.setRequestAccepted(LOAN_REQUESTACCEPTED);
            loan1.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID));
            loan1.setArtwork(artworkRepository.findArtworkByArtworkId(SECOND_ARTWORK_ID));
            ((ArrayList<Loan>) loans).add(loan1);

            Loan loan2 = new Loan();
            loan2.setLoanId(SECOND_LOAN_ID);
            loan2.setRequestAccepted(SECOND_LOAN_REQUESTACCEPTED);
            loan2.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID));
            loan2.setArtwork(artworkRepository.findArtworkByArtworkId(SECOND_ARTWORK_ID));
            ((ArrayList<Loan>) loans).add(loan2);

            Loan loan3 = new Loan();
            loan3.setLoanId(THIRD_LOAN_ID);
            loan3.setRequestAccepted(THIRD_LOAN_REQUESTACCEPTED);
            loan3.setVisitor(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID));
            loan3.setArtwork(artworkRepository.findArtworkByArtworkId(SECOND_ARTWORK_ID));
            ((ArrayList<Loan>) loans).add(loan3);

        return loans;
        });

        lenient().when(loanRepository.findLoanByArtworkAndVisitor(any(Artwork.class), any(Visitor.class))).thenAnswer((InvocationOnMock invocation) ->
        {
            Visitor visitor = (Visitor) invocation.getArgument(1);
            visitor = visitorRepository.findVisitorByMuseumUserId(visitor.getMuseumUserId());

            Loan loan1 = loanRepository.findLoanByLoanId(FIFTH_LOAN_ID);
            Loan loan2 = loanRepository.findLoanByLoanId(SECOND_LOAN_ID);

            if (visitor.getMuseumUserId() == 2) {
                return loan1;
            }
            else {
                return loan2;
            }
        });

        

        lenient().when(visitorRepository.findVisitorByMuseumUserId(VISITOR_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(VISITOR_ID)) {

                Visitor visitor = new Visitor();
                visitor.setMuseumUserId(VISITOR_ID);
                visitor.setName(VISITOR_NAME);
                visitor.setEmail(VISITOR_EMAIL);
                visitor.setPassword(VISITOR_PASSWORD);
                return visitor;
            } else {
                return null;
            }
        });

        lenient().when(visitorRepository.findVisitorByMuseumUserId(SECOND_VISITOR_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(SECOND_VISITOR_ID)) {

                Visitor visitor = new Visitor();
                visitor.setMuseumUserId(SECOND_VISITOR_ID);
                visitor.setName(SECOND_VISITOR_NAME);
                visitor.setEmail(SECOND_VISITOR_EMAIL);
                visitor.setPassword(SECOND_VISITOR_PASSWORD);
                return visitor;
            } else {
                return null;
            }
        });


        lenient().when(artworkRepository.findArtworkByArtworkId(ARTWORK_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(ARTWORK_ID)) {

                Artwork artwork = new Artwork();
                artwork.setArtworkId(ARTWORK_ID);
                artwork.setArtist(ARTWORK_ARTIST);
                artwork.setName(ARTWORK_NAME);
                artwork.setImage(ARTWORK_IMAGE);
                artwork.setLoanFee(ARTWORK_LOANFEE);
                artwork.setIsOnLoan(ARTWORK_ISONLOAN);
                artwork.setIsAvailableForLoan(ARTWORK_ISAVAILABLEFORLOAN);
                return artwork;
                
            } else {
                return null;
            }
        });

        lenient().when(artworkRepository.findArtworkByArtworkId(SECOND_ARTWORK_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(SECOND_ARTWORK_ID)) {

                Artwork artwork = new Artwork();
                artwork.setArtworkId(SECOND_ARTWORK_ID);
                artwork.setArtist(SECOND_ARTWORK_ARTIST);
                artwork.setName(SECOND_ARTWORK_NAME);
                artwork.setImage(SECOND_ARTWORK_IMAGE);
                artwork.setLoanFee(SECOND_ARTWORK_LOANFEE);
                artwork.setIsOnLoan(SECOND_ARTWORK_ISONLOAN);
                artwork.setIsAvailableForLoan(SECOND_ARTWORK_ISAVAILABLEFORLOAN);
                return artwork;
                
            } else {
                return null;
            }
        });

        lenient().when(artworkRepository.findArtworkByArtworkId(THIRD_ARTWORK_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(THIRD_ARTWORK_ID)) {

                Artwork artwork = new Artwork();
                artwork.setArtworkId(THIRD_ARTWORK_ID);
                artwork.setArtist(THIRD_ARTWORK_ARTIST);
                artwork.setName(THIRD_ARTWORK_NAME);
                artwork.setImage(THIRD_ARTWORK_IMAGE);
                artwork.setLoanFee(THIRD_ARTWORK_LOANFEE);
                artwork.setIsOnLoan(THIRD_ARTWORK_ISONLOAN);
                artwork.setIsAvailableForLoan(THIRD_ARTWORK_ISAVAILABLEFORLOAN);
                return artwork;
                
            } else {
                return null;
            }
        });

        lenient().when(artworkRepository.findArtworkByArtworkId(FOURTH_ARTWORK_ID)).thenAnswer((InvocationOnMock invocation) ->
        {
            if (invocation.getArgument(0).equals(FOURTH_ARTWORK_ID)) {

                Room room = new Room();
                room.setRoomId(ROOM_ID);
                room.setRoomName(ROOM_ROOMNAME);
                room.setCurrentNumberOfArtwork(ROOM_CURRENTNUMBEROFARTWORK);
                room.setRoomType(ROOM_ROOMTYPE);

                Artwork artwork = new Artwork();
                artwork.setArtworkId(FOURTH_ARTWORK_ID);
                artwork.setArtist(FOURTH_ARTWORK_ARTIST);
                artwork.setName(FOURTH_ARTWORK_NAME);
                artwork.setImage(FOURTH_ARTWORK_IMAGE);
                artwork.setLoanFee(FOURTH_ARTWORK_LOANFEE);
                artwork.setIsOnLoan(FOURTH_ARTWORK_ISONLOAN);
                artwork.setIsAvailableForLoan(FOURTH_ARTWORK_ISAVAILABLEFORLOAN);
                artwork.setRoom(room);
                return artwork;
                
            } else {
                return null;
            }
        });

        lenient().when(artworkService.removeArtworkFromRoom(FOURTH_ARTWORK_ID)).thenAnswer((InvocationOnMock invocation) -> 
        {

            if ((invocation.getArgument(0).equals(FOURTH_ARTWORK_ID))) {

                Room room = new Room();
                room.setRoomId(ROOM_ID);
                room.setRoomName(ROOM_ROOMNAME);
                room.setRoomType(ROOM_ROOMTYPE);
                room.setCurrentNumberOfArtwork(ROOM_CURRENTNUMBEROFARTWORK -1); 

                Artwork artwork = new Artwork();
                artwork.setArtworkId(FOURTH_ARTWORK_ID);
                artwork.setArtist(FOURTH_ARTWORK_ARTIST);
                artwork.setName(FOURTH_ARTWORK_NAME);
                artwork.setImage(FOURTH_ARTWORK_IMAGE);
                artwork.setLoanFee(FOURTH_ARTWORK_LOANFEE);
                artwork.setIsOnLoan(true);
                artwork.setIsAvailableForLoan(FOURTH_ARTWORK_ISAVAILABLEFORLOAN);
                artwork.setRoom(null);

                return artwork;
            } else {
                return null;
            }
        });


    }

    /**
     * Test method for getting a loan by its id
     * 
     * @author Eric
     */
    @Test
    public void testGetLoan() {
        Loan loan = loanService.getLoanById(LOAN_ID);
        assertEquals(LOAN_ID, loan.getLoanId());
        assertEquals(LOAN_REQUESTACCEPTED, loan.getRequestAccepted());
        assertEquals(VISITOR_ID, loan.getVisitor().getMuseumUserId());
        assertEquals(SECOND_ARTWORK_ID, loan.getArtwork().getArtworkId());
    }

    /**
     * Test method for getting a loan by its id but the loan doesn't exist
     * 
     * @author Eric
     */
    @Test
    public void testGetLoanNonExisting() {
        String exceptionMessage = null;
        try {  
            Loan loan = loanService.getLoanById(NONE_EXISTING_LOAN_ID);
        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Loan does not exist", exceptionMessage);
    }

    /**
     * Test method for getting all loans
     * 
     * @author Eric
     */
    @Test
    public void testGetAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        assertEquals(3, loans.size());
    }

    /**
     * Test method for creating a loan successfully
     * 
     * @author Eric
     */
    @Test
    public void testCreateLoan() {
        String exceptionMessage = null;
        Loan createdLoan = null;
        Artwork artwork = new Artwork();
        Visitor visitor = new Visitor();

        try {
            visitor.setMuseumUserId(VISITOR_ID);
            visitor.setName(VISITOR_NAME);
            visitor.setEmail(VISITOR_EMAIL);
            visitor.setPassword(VISITOR_PASSWORD);
            VisitorDto visitorDto = DtoUtility.convertToDto(visitor);

            artwork.setArtworkId(SECOND_ARTWORK_ID);
            artwork.setArtist(SECOND_ARTWORK_ARTIST);
            artwork.setName(SECOND_ARTWORK_NAME);
            artwork.setImage(SECOND_ARTWORK_IMAGE);
            artwork.setLoanFee(SECOND_ARTWORK_LOANFEE);
            artwork.setIsOnLoan(SECOND_ARTWORK_ISONLOAN);
            artwork.setIsAvailableForLoan(SECOND_ARTWORK_ISAVAILABLEFORLOAN);
            ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);

            LoanDto loanDto = new LoanDto(LOAN_ID, null);
            loanDto.setArtwork(artworkDto);
            loanDto.setVisitor(visitorDto);

            createdLoan = loanService.createLoan(loanDto);
        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertNotNull(createdLoan);
        assertEquals(null, createdLoan.getRequestAccepted());
        assertEquals(artwork.getArtworkId(), createdLoan.getArtwork().getArtworkId());
        assertEquals(visitor.getMuseumUserId(), createdLoan.getVisitor().getMuseumUserId());   
    }
    
    /**
     * Test method for creating a loan with invalid requestAccepted status at creation
     * 
     * @author Eric
     */
    @Test
    public void testCreateLoanWithTrueRequestAccepted() {
        String exceptionMessage = null;
        try {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(VISITOR_ID);
            visitor.setName(VISITOR_NAME);
            visitor.setEmail(VISITOR_EMAIL);
            visitor.setPassword(VISITOR_PASSWORD);
            VisitorDto visitorDto = DtoUtility.convertToDto(visitor);

            Artwork artwork = new Artwork();
            artwork.setArtworkId(SECOND_ARTWORK_ID);
            artwork.setArtist(SECOND_ARTWORK_ARTIST);
            artwork.setName(SECOND_ARTWORK_NAME);
            artwork.setImage(SECOND_ARTWORK_IMAGE);
            artwork.setLoanFee(SECOND_ARTWORK_LOANFEE);
            artwork.setIsOnLoan(SECOND_ARTWORK_ISONLOAN);
            artwork.setIsAvailableForLoan(SECOND_ARTWORK_ISAVAILABLEFORLOAN);
            ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);

            LoanDto loanDto = new LoanDto(LOAN_ID, true);
            loanDto.setArtwork(artworkDto);
            loanDto.setVisitor(visitorDto);


            Loan createdLoan = loanService.createLoan(loanDto);
        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Loan getRequestAccepted must be null because only an employee can define", exceptionMessage);
    }
    
    /**
     * Test method for creating a loan that was already created by a visitor for the same artwork
     * 
     * @author Eric
     */
    @Test
    public void testCreateDuplicateLoan() {
        String exceptionMessage = null;
        try {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(SECOND_VISITOR_ID);
            visitor.setName(SECOND_VISITOR_NAME);
            visitor.setEmail(SECOND_VISITOR_EMAIL);
            visitor.setPassword(SECOND_VISITOR_PASSWORD);
            VisitorDto visitorDto = DtoUtility.convertToDto(visitor);
            visitorRepository.save(visitor);

            Artwork artwork = new Artwork();
            artwork.setArtworkId(SECOND_ARTWORK_ID);
            artwork.setArtist(SECOND_ARTWORK_ARTIST);
            artwork.setName(SECOND_ARTWORK_NAME);
            artwork.setImage(SECOND_ARTWORK_IMAGE);
            artwork.setLoanFee(SECOND_ARTWORK_LOANFEE);
            artwork.setIsOnLoan(SECOND_ARTWORK_ISONLOAN);
            artwork.setIsAvailableForLoan(SECOND_ARTWORK_ISAVAILABLEFORLOAN);
            ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);
            artworkRepository.save(artwork);

            LoanDto loanDto = new LoanDto(FIFTH_LOAN_ID, null);
            loanDto.setArtwork(artworkDto);
            loanDto.setVisitor(visitorDto);

            Loan createdLoan = loanService.createLoan(loanDto);

        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Cannot create a duplicate loan request", exceptionMessage);
    }

    /**
     * Test method for creating a loan associated to a null visitor
     * 
     * @author Eric
     */
    @Test
    public void testCreateLoanForNoneExistingVisitor() {
        String exceptionMessage = null;
        try {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(NONE_EXISTING_ARTWORK_ID);
            visitor.setName(VISITOR_NAME);
            visitor.setEmail(VISITOR_EMAIL);
            visitor.setPassword(VISITOR_PASSWORD);
            VisitorDto visitorDto = DtoUtility.convertToDto(visitor);

            Artwork artwork = new Artwork();
            artwork.setArtworkId(SECOND_ARTWORK_ID);
            artwork.setArtist(SECOND_ARTWORK_ARTIST);
            artwork.setName(SECOND_ARTWORK_NAME);
            artwork.setImage(SECOND_ARTWORK_IMAGE);
            artwork.setLoanFee(SECOND_ARTWORK_LOANFEE);
            artwork.setIsOnLoan(SECOND_ARTWORK_ISONLOAN);
            artwork.setIsAvailableForLoan(SECOND_ARTWORK_ISAVAILABLEFORLOAN);
            ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);

            LoanDto loanDto = new LoanDto(LOAN_ID, null);
            loanDto.setArtwork(artworkDto);
            loanDto.setVisitor(visitorDto);


            Loan createdLoan = loanService.createLoan(loanDto);
        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Visitor does not exist", exceptionMessage);
    }

    /**
     * Test method for creating a loan associated to a null artwork
     * 
     * @author Eric
     */
    @Test
    public void testCreateLoanForNoneExistingArtwork() {
        String exceptionMessage = null;
        try {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(VISITOR_ID);
            visitor.setName(VISITOR_NAME);
            visitor.setEmail(VISITOR_EMAIL);
            visitor.setPassword(VISITOR_PASSWORD);
            VisitorDto visitorDto = DtoUtility.convertToDto(visitor);

            Artwork artwork = new Artwork();
            artwork.setArtworkId(NONE_EXISTING_ARTWORK_ID);
            artwork.setArtist(SECOND_ARTWORK_ARTIST);
            artwork.setName(SECOND_ARTWORK_NAME);
            artwork.setImage(SECOND_ARTWORK_IMAGE);
            artwork.setLoanFee(SECOND_ARTWORK_LOANFEE);
            artwork.setIsOnLoan(SECOND_ARTWORK_ISONLOAN);
            artwork.setIsAvailableForLoan(SECOND_ARTWORK_ISAVAILABLEFORLOAN);
            ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);

            LoanDto loanDto = new LoanDto(LOAN_ID, null);
            loanDto.setArtwork(artworkDto);
            loanDto.setVisitor(visitorDto);


            Loan createdLoan = loanService.createLoan(loanDto);
        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Artwork does not exist", exceptionMessage);
    }

    /**
     * Test method for creating a loan associated to an artwork that has isAvailableForLoan == false
     * 
     * @author Eric
     */
    @Test
    public void testCreateLoanForArtworkIsAvailableForLoanFalse() {
        String exceptionMessage = null;
        try {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(VISITOR_ID);
            visitor.setName(VISITOR_NAME);
            visitor.setEmail(VISITOR_EMAIL);
            visitor.setPassword(VISITOR_PASSWORD);
            VisitorDto visitorDto = DtoUtility.convertToDto(visitor);

            Artwork artwork = new Artwork();
            artwork.setArtworkId(ARTWORK_ID);
            artwork.setArtist(ARTWORK_ARTIST);
            artwork.setName(ARTWORK_NAME);
            artwork.setImage(ARTWORK_IMAGE);
            artwork.setLoanFee(ARTWORK_LOANFEE);
            artwork.setIsOnLoan(ARTWORK_ISONLOAN);
            artwork.setIsAvailableForLoan(ARTWORK_ISAVAILABLEFORLOAN);
            ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);

            LoanDto loanDto = new LoanDto(LOAN_ID, null);
            loanDto.setArtwork(artworkDto);
            loanDto.setVisitor(visitorDto);


            Loan createdLoan = loanService.createLoan(loanDto);
        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Artwork is not available for loan", exceptionMessage);
    }

    /**
     * Test method for creating a loan associated to an artwork that has isAvailableForLoan == true but isOnLoan == true
     * 
     * @author Eric
     */
    @Test
    public void testCreateLoanForArtworkIsOnLoanTrue() {
        String exceptionMessage = null;
        try {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(VISITOR_ID);
            visitor.setName(VISITOR_NAME);
            visitor.setEmail(VISITOR_EMAIL);
            visitor.setPassword(VISITOR_PASSWORD);
            VisitorDto visitorDto = DtoUtility.convertToDto(visitor);

            Artwork artwork = new Artwork();
            artwork.setArtworkId(THIRD_ARTWORK_ID);
            artwork.setArtist(THIRD_ARTWORK_ARTIST);
            artwork.setName(THIRD_ARTWORK_NAME);
            artwork.setImage(THIRD_ARTWORK_IMAGE);
            artwork.setLoanFee(THIRD_ARTWORK_LOANFEE);
            artwork.setIsOnLoan(THIRD_ARTWORK_ISONLOAN);
            artwork.setIsAvailableForLoan(THIRD_ARTWORK_ISAVAILABLEFORLOAN);
            ArtworkDto artworkDto = DtoUtility.convertToDto(artwork);

            LoanDto loanDto = new LoanDto(LOAN_ID, null);
            loanDto.setArtwork(artworkDto);
            loanDto.setVisitor(visitorDto);


            Loan createdLoan = loanService.createLoan(loanDto);
        } catch (IllegalArgumentException e) {
            exceptionMessage = e.getMessage();
        }
        assertEquals("Cannot create a loan request for an artwork that is on loan", exceptionMessage);
    }

    /**
     * Test method for patching a loan to accept the loan request
     * 
     * @author Eric
     */
    @Test
    public void testPatchLoanAcceptRequest() {
        Loan patchedLoan = loanService.putLoanById(FOURTH_LOAN_ID, true);
        assertEquals(true, patchedLoan.getRequestAccepted());
        assertEquals(true, patchedLoan.getArtwork().getIsOnLoan());
        assertNull(patchedLoan.getArtwork().getRoom());
    }

    /**
     * Test method for patching a loan to reject the loan request
     * 
     * @author Eric
     */
    @Test
    public void testPatchLoanRefuseRequest() {
        Loan loan = loanService.getLoanById(FOURTH_LOAN_ID);
        Loan patchedLoan = loanService.putLoanById(loan.getLoanId(), false);
        assertEquals(false, patchedLoan.getRequestAccepted());
        assertNotNull(patchedLoan.getArtwork().getRoom());   
    }

}