package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Loan;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.TimePeriod;
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

    private static final long LOAN_ID = 1;
    private static final boolean LOAN_REQUESTACCEPTED = false;

    private static final long SECOND_LOAN_ID = 2;
    private static final boolean SECOND_LOAN_REQUESTACCEPTED = false;

    private static final long VISITOR_ID = 1;

    private static final long ARTWORK_ID =1;
    private static final String ARTWORK_NAME = "George";
    private static final String ARTWORK_ARTIST = "Curious";
    private static final boolean ARTWORK_ISAVAILABLEFORLOAN = false;
    private static final boolean ARTWORK_ISONLOAN = false; 
    private static final double ARTWORK_LOANFEE = 54.2;
    private static final String ARTWORK_IMAGE = "bruh";

    private static final long SECOND_ARTWORK_ID =2;
    private static final String SECOND_ARTWORK_NAME = "HELLO";
    private static final String SECOND_ARTWORK_ARTIST = "WORLD";
    private static final boolean SECOND_ARTWORK_ISAVAILABLEFORLOAN = true;
    private static final boolean SECOND_ARTWORK_ISONLOAN = false; 
    private static final double SECOND_ARTWORK_LOANFEE = 63.5;
    private static final String SECOND_ARTWORK_IMAGE = "bruuuuh";

    private static final long THIRD_ARTWORK_ID =3;
    private static final String THIRD_ARTWORK_NAME = "dna";
    private static final String THIRD_ARTWORK_ARTIST = "code";
    private static final boolean THIRD_ARTWORK_ISAVAILABLEFORLOAN = true;
    private static final boolean THIRD_ARTWORK_ISONLOAN = true; 
    private static final double THIRD_ARTWORK_LOANFEE = 69.69;
    private static final String THIRD_ARTWORK_IMAGE = "bruuuuuuuuuuuuuuuuuh";

    @BeforeEach
    public void setMockOutput() {
        
        lenient().when(loanRepository.findLoanByLoanId(anyLong())).thenAnswer((InvocationOnMock invocation) -> 
        {
            if (invocation.getArgument(0).equals(LOAN_ID)) {

                Loan loan1 = new Loan();
                loan1.setLoanId(LOAN_ID);
                loan1.setRequestAccepted(LOAN_REQUESTACCEPTED);
                return loan1;       
            } else {
                return null;
            }
        });
    }

    @Test
    public void testGetLoan() {
        Loan loan = loanService.getLoanById(LOAN_ID);
        assertEquals(LOAN_ID, loan.getLoanId());
        assertEquals(LOAN_REQUESTACCEPTED, loan.getRequestAccepted());
        assertEquals(VISITOR_ID, loan.getVisitor());
        assertEquals(SECOND_ARTWORK_ID, loan.getArtwork());
    }

}