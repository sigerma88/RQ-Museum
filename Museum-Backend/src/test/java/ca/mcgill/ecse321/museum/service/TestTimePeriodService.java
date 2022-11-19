package ca.mcgill.ecse321.museum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;


import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.TimePeriod;

/**
 * This is the test class for the TimePeriod
 * @author VZ
 */
@ExtendWith(MockitoExtension.class)
public class TestTimePeriodService {
    @Mock
    private TimePeriodRepository timePeriodRepository;
    @InjectMocks
    private TimePeriodService timePeriodService;

    private static final Long TIME_PERIOD_ID = 1L;
    private static final Timestamp START_DATE = Timestamp.valueOf("2022-11-18 08:30:00");
    private static final Timestamp END_DATE = Timestamp.valueOf("2022-11-18 17:35:00");

    private static final Long TIME_PERIOD_ID_2 = 2L;
    private static final Timestamp START_DATE_2 = Timestamp.valueOf("2022-11-18 08:30:00");
    private static final Timestamp END_DATE_2 = Timestamp.valueOf("2022-11-18 17:35:00");

    private static final Timestamp NEW_START_DATE = Timestamp.valueOf("2022-11-19 08:30:00");
    private static final Timestamp NEW_END_DATE = Timestamp.valueOf("2022-11-19 17:35:00");

    private static final Long NON_EXISTING_TIME_PERIOD_ID = -2L;


    @BeforeEach
    public void setMockOutput() {
        lenient().when(timePeriodRepository.findTimePeriodByTimePeriodId(anyLong()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(TIME_PERIOD_ID)) {
                        TimePeriod timePeriod = new TimePeriod();
                        timePeriod.setTimePeriodId(TIME_PERIOD_ID);
                        timePeriod.setStartDate(START_DATE);
                        timePeriod.setEndDate(END_DATE);
                        return timePeriod;
                    } else {
                        return null;
                    }
                });

        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };
        lenient().when(timePeriodRepository.save(any(TimePeriod.class))).thenAnswer(returnParameterAsAnswer);

    }

    /**
     * Test to check creation of time period
     * @author VZ
     */
    @Test
    public void testCreateTimePeriod() {
        assertEquals(0, timePeriodService.getAllTimePeriods().size());
        TimePeriod timePeriod = null;
        try {
            timePeriod = timePeriodService.createTimePeriod(START_DATE, END_DATE);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(timePeriod);
        assertEquals(START_DATE, timePeriod.getStartDate());
        assertEquals(END_DATE, timePeriod.getEndDate());
    }

    /**
     * Test to check creation of time period with start date after end date
     * @author VZ
     */
    @Test
    public void testCreateTimePeriodBadDates(){
        assertEquals(0, timePeriodService.getAllTimePeriods().size());
        TimePeriod timePeriod = null;
        String error = null;
        try {
            timePeriod = timePeriodService.createTimePeriod(END_DATE, START_DATE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timePeriod);
        assertEquals("Start date cannot be after end date", error);
    }

    /**
     * Test to get a time period by id 
     * @author VZ
     * 
     */
    @Test 
    public void testGetTimePeriod() {
        TimePeriod timePeriod = null;
        try {
            timePeriod = timePeriodService.getTimePeriod(TIME_PERIOD_ID);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(timePeriod);
        assertEquals(TIME_PERIOD_ID, timePeriod.getTimePeriodId());
        assertEquals(START_DATE, timePeriod.getStartDate());
        assertEquals(END_DATE, timePeriod.getEndDate());
    }

    /**
     * Test to get a time period by id that does not exist
     * @author VZ
     */
    @Test
    public void testGetTimePeriodInvalidId() {
        TimePeriod timePeriod = null;
        String error = null;
        try {
            timePeriod = timePeriodService.getTimePeriod(NON_EXISTING_TIME_PERIOD_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timePeriod);
        assertEquals("Time period does not exist", error);
    }

    /**
     * Test to edit a timeperiod by id
     * @author VZ
     */
    @Test
    public void testEditTimePeriod() {
        TimePeriod timePeriod = null;
        try {
            timePeriod = timePeriodService.createTimePeriod(START_DATE, END_DATE);
            timePeriod = timePeriodService.editTimePeriod(TIME_PERIOD_ID, NEW_START_DATE, NEW_END_DATE);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(timePeriod);
        assertEquals(TIME_PERIOD_ID, timePeriod.getTimePeriodId());
        assertEquals(NEW_START_DATE, timePeriod.getStartDate());
        assertEquals(NEW_END_DATE, timePeriod.getEndDate());
    }

    /**
     * Test to edit a timeperiod by id that does not exist
     * @author VZ
     */
    @Test
    public void testEditTimePeriodInvalidId() {
        TimePeriod timePeriod = null;
        String error = null;
        try {
            timePeriod = timePeriodService.editTimePeriod(NON_EXISTING_TIME_PERIOD_ID, NEW_START_DATE, NEW_END_DATE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timePeriod);
        assertEquals("Time period does not exist", error);
    }

    /**
     * Test to edit a timeperiod by id with start date after end date
     * @author VZ
     */
    @Test
    public void testEditTimePeriodBadDates() {
        TimePeriod timePeriod = null;
        String error = null;
        try {
            timePeriod = timePeriodService.createTimePeriod(START_DATE, END_DATE);
            timePeriod = timePeriodService.editTimePeriod(TIME_PERIOD_ID, NEW_END_DATE, NEW_START_DATE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(timePeriod);
        assertEquals(START_DATE, timePeriod.getStartDate());
        assertEquals(END_DATE, timePeriod.getEndDate());
        assertEquals("Start date cannot be after end date", error);
    }
    /**
     * Test to delete a time period by id
     * @author VZ
     */
    @Test
    public void testDeleteTimePeriod() {
        try {
            timePeriodService.deleteTimePeriod(TIME_PERIOD_ID);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    /**
     * Test to delete a time period by id that does not exist
     * @author VZ
     */
    @Test
    public void testDeleteTimePeriodInvalidId() {
        String error = null;
        try {
            timePeriodService.deleteTimePeriod(NON_EXISTING_TIME_PERIOD_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Time period does not exist", error);
    }

    /**
     * Test to get all time periods
     * @author VZ
     */
    @Test
    public void testGetAllTimePeriods() {
        //CREATE TIME PERIODS 
        final TimePeriod timePeriod1 = new TimePeriod();
        timePeriod1.setTimePeriodId(TIME_PERIOD_ID);
        timePeriod1.setStartDate(START_DATE);
        timePeriod1.setEndDate(END_DATE);

        final TimePeriod timePeriod2 = new TimePeriod();
        timePeriod2.setTimePeriodId(TIME_PERIOD_ID_2);
        timePeriod2.setStartDate(START_DATE_2);
        timePeriod2.setEndDate(END_DATE_2);
        
        final List<TimePeriod> timePeriods = new ArrayList<>();
        timePeriods.add(timePeriod1);
        timePeriods.add(timePeriod2);

        when(timePeriodRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> {
            return timePeriods;
        });
        
        List<TimePeriod> timePeriodsReturned = null;
        try {
            timePeriodsReturned = timePeriodService.getAllTimePeriods();
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(timePeriodsReturned);
        assertEquals(2, timePeriodsReturned.size());
        assertEquals(TIME_PERIOD_ID, timePeriodsReturned.get(0).getTimePeriodId());
        assertEquals(START_DATE, timePeriodsReturned.get(0).getStartDate());
        assertEquals(END_DATE, timePeriodsReturned.get(0).getEndDate());
    }



}
