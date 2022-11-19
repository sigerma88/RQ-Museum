package ca.mcgill.ecse321.museum.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;

/**
 * This is the test class for the MuseumService class.
 * 
 * @author VZ
 * 
 */

@ExtendWith(MockitoExtension.class)
public class TestMuseumService {

    @Mock
    private MuseumRepository museumRepository;
    @Mock
    private ScheduleRepository scheduleRepository;
    @Mock
    private TimePeriodRepository timePeriodRepository;
    @Mock
    private ScheduleOfTimePeriodRepository scheduleOfTimePeriodRepository;

    @InjectMocks
    private MuseumService museumService;

    private static final Long MUSEUM_ID = 1L;
    private static final String MUSEUM_NAME = "RQ museum";
    private static final Double MUSEUM_VISITFEE = 10.0;
    private static final Schedule MUSEUM_SCHEDULE = new Schedule();

    private static final String NEW_MUSEUM_NAME = "NEW";
    private static final Double NEW_MUSEUM_VISITFEE = 20.0;
    private static final Schedule NEW_MUSEUM_SCHEDULE = new Schedule();

    private static final Long NON_EXISTING_MUSEUM_ID = 2L;
    private static final Double NEGATIVE_VISITFEE = -10.0;

    private static final Long SCHEDULE_ID = 1L;
    private static final Long SECOND_SCHEDULE_ID = 2L;

    private static final Long TIMEPERIOD_ID = 1L;
    private static final Timestamp STARTDATE = Timestamp.valueOf("2022-01-01 08:30:00.0");
    private static final Timestamp ENDDATE = Timestamp.valueOf("2022-01-01 17:30:00.0");

    private static final Long SCHEDULE_OF_TIME_PERIOD_ID = 1L;

    /**
     * This method is called before each test and is used to set up the mock objects
     * 
     * @author VZ
     */

    @BeforeEach
    public void setMockOutput() {
        lenient().when(museumRepository.findMuseumByMuseumId(anyLong())).thenAnswer((InvocationOnMock invocation) -> {
            if (invocation.getArgument(0).equals(MUSEUM_ID)) {
                Museum museum = new Museum();
                museum.setMuseumId(MUSEUM_ID);
                museum.setName(MUSEUM_NAME);
                museum.setVisitFee(MUSEUM_VISITFEE);
                museum.setSchedule(MUSEUM_SCHEDULE);
                return museum;
            } else {
                return null;
            }
        });

        lenient().when(scheduleRepository.findScheduleByScheduleId(anyLong()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(SCHEDULE_ID)) {
                        Schedule schedule = new Schedule();
                        schedule.setScheduleId(SCHEDULE_ID);
                        return schedule;
                    } else {
                        return null;
                    }
                });

        lenient().when(scheduleRepository.findScheduleByScheduleId(anyLong()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(SECOND_SCHEDULE_ID)) {
                        Schedule schedule = new Schedule();
                        schedule.setScheduleId(SECOND_SCHEDULE_ID);
                        return schedule;
                    } else {
                        return null;
                    }
                });

        lenient().when(timePeriodRepository.findTimePeriodByTimePeriodId(anyLong()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(TIMEPERIOD_ID)) {
                        TimePeriod timePeriod = new TimePeriod();
                        timePeriod.setTimePeriodId(TIMEPERIOD_ID);
                        timePeriod.setStartDate(STARTDATE);
                        timePeriod.setEndDate(ENDDATE);
                        return timePeriod;
                    } else {
                        return null;
                    }
                });

        lenient()
                .when(scheduleOfTimePeriodRepository
                        .findScheduleOfTimePeriodByScheduleOfTimePeriodId(anyLong()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(SCHEDULE_OF_TIME_PERIOD_ID)) {
                        ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
                        scheduleOfTimePeriod.setScheduleOfTimePeriodId(SCHEDULE_OF_TIME_PERIOD_ID);
                        scheduleOfTimePeriod.setSchedule(scheduleRepository.findScheduleByScheduleId(SCHEDULE_ID));
                        scheduleOfTimePeriod
                                .setTimePeriod(timePeriodRepository.findTimePeriodByTimePeriodId(TIMEPERIOD_ID));
                        return scheduleOfTimePeriod;
                    } else {
                        return null;
                    }
                });

        // whenever anything is saved, just return the parameter object
        Answer<?> returnParameterAsAnswer = (InvocationOnMock invocation) -> {
            return invocation.getArgument(0);
        };

        lenient().when(museumRepository.save(any(Museum.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(scheduleRepository.save(any(Schedule.class))).thenAnswer(returnParameterAsAnswer);
        lenient().when(scheduleOfTimePeriodRepository.save(any(ScheduleOfTimePeriod.class)))
                .thenAnswer(returnParameterAsAnswer);
        lenient().when(timePeriodRepository.save(any(TimePeriod.class))).thenAnswer(returnParameterAsAnswer);
    }

    /**
     * This method is used to test the creation of a museum
     *
     * @author VZ
     */
    @Test
    public void testCreateMuseum() {
        assertEquals(0, museumService.getAllMuseums().size());
        Museum museum = null;
        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(museum);
        assertEquals(MUSEUM_NAME, museum.getName());
        assertEquals(MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(MUSEUM_SCHEDULE, museum.getSchedule());

    }

    /**
     * This test checks if the museum service throws an exception when the
     * museum name is null
     * 
     * @author VZ
     * 
     */
    @Test
    public void testCreateMuseumNullName() {
        assertEquals(0, museumService.getAllMuseums().size());
        Museum museum = null;
        String error = null;
        try {
            museum = museumService.createMuseum(null, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(museum);
        assertEquals("Name cannot be empty!", error);
        assertEquals(0, museumService.getAllMuseums().size());
    }

    /**
     * test for creating a museum with an empty name
     * 
     * @author VZ
     * 
     */
    @Test
    public void testCreateMuseumEmptyName() {
        assertEquals(0, museumService.getAllMuseums().size());
        Museum museum = null;
        String error = null;
        try {
            museum = museumService.createMuseum("", MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(museum);
        assertEquals("Name cannot be empty!", error);
        assertEquals(0, museumService.getAllMuseums().size());
    }

    /**
     * Test for creating a museum with name [space]
     * 
     * @author VZ
     * 
     */
    @Test
    public void testCreateMuseumSpaceName() {
        assertEquals(0, museumService.getAllMuseums().size());
        String name = " ";
        double visitFee = 10.0;
        Schedule schedule = new Schedule();
        Museum museum = null;
        String error = null;
        try {
            museum = museumService.createMuseum(name, visitFee, schedule);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(museum);
        assertEquals("Name cannot be empty!", error);
        assertEquals(0, museumService.getAllMuseums().size());
    }

    /**
     * test for creating a museum with a negative visit fee
     * 
     * @author VZ
     */
    @Test
    public void testCreateMuseumWithNegativeVisitFee() {
        Museum museum = null;
        String error = null;
        try {

            museum = museumService.createMuseum(MUSEUM_NAME, NEGATIVE_VISITFEE, MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(museum);
        assertEquals("Visit fee cannot be negative!", error);
    }

    /**
     * test for creating a museum with no schedule
     * 
     * @author VZ
     */
    @Test
    public void testCreateMuseumWithNoSchedule() {
        Museum museum = null;
        String error = null;
        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(museum);
        assertEquals("Schedule cannot be null!", error);
    }

    /**
     * test for editing a museum's attributes
     * 
     * @author VZ
     * 
     */
    @Test
    public void testEditMuseum() {
        Museum museum = null;
        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
            museum = museumService.editMuseum(MUSEUM_ID, NEW_MUSEUM_NAME, NEW_MUSEUM_VISITFEE, NEW_MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(museum);
        assertEquals(NEW_MUSEUM_NAME, museum.getName());
        assertEquals(NEW_MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(NEW_MUSEUM_SCHEDULE, museum.getSchedule());
    }

    /**
     * test method for editing a museum that doesn't exist.
     * 
     * @author VZ
     * 
     */
    @Test
    public void testEditNonExistingMuseum() {
        assertThrows(IllegalArgumentException.class,
                () -> museumService.editMuseum(NON_EXISTING_MUSEUM_ID, MUSEUM_NAME, MUSEUM_VISITFEE, new Schedule()));
    }

    /**
     * test for editing a museum by only changing name, visit fee and schedule are
     * null
     * 
     * @author VZ
     */
    @Test
    public void testEditMuseumWithNameOnly() {
        Museum museum = null;
        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
            museum = museumService.editMuseum(MUSEUM_ID, NEW_MUSEUM_NAME, null, null);

        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(museum);
        assertEquals(NEW_MUSEUM_NAME, museum.getName());
        assertEquals(MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(MUSEUM_SCHEDULE, museum.getSchedule());
    }

    /**
     * test for trying to edit a museum by setting an empty name
     * 
     * @author VZ
     */
    @Test
    public void testEditMuseumWithEmptyName() {
        Museum museum = null;
        String error = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
            museum = museumService.editMuseum(MUSEUM_ID, "", MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals(MUSEUM_NAME, museum.getName());
        assertEquals(MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(MUSEUM_SCHEDULE, museum.getSchedule());
        assertEquals("Name cannot be empty!", error);
    }

    /**
     * test for trying to edit a museum by setting a name with only space
     *
     * @author VZ
     */
    @Test
    public void testEditMuseumWithSpaceName() {
        Museum museum = null;
        String name = " ";
        String error = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
            museum = museumService.editMuseum(MUSEUM_ID, name, NEW_MUSEUM_VISITFEE, NEW_MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals(MUSEUM_NAME, museum.getName());
        assertEquals(MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(MUSEUM_SCHEDULE, museum.getSchedule());
        assertEquals("Name cannot be empty!", error);

    }

    /**
     * test for editing a museum by setting a negative visit fee
     * 
     * @author VZ
     * 
     */
    @Test
    public void testEditMuseumWithNegativeVisitFee() {
        Museum museum = null;
        String error = null;
        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
            museum = museumService.editMuseum(MUSEUM_ID, NEW_MUSEUM_NAME, NEGATIVE_VISITFEE, NEW_MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals(MUSEUM_NAME, museum.getName());
        assertEquals(MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(MUSEUM_SCHEDULE, museum.getSchedule());
        assertEquals("Visit fee cannot be negative!", error);
    }

    /**
     *
     * test for editing a museum by only changing schedule
     * 
     * @author VZ
     */
    @Test
    public void testEditMuseumWithScheduleOnly() {
        Museum museum = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
            museum = museumService.editMuseum(MUSEUM_ID, null, null, NEW_MUSEUM_SCHEDULE);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(museum);
        assertEquals(MUSEUM_NAME, museum.getName());
        assertEquals(MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(NEW_MUSEUM_SCHEDULE, museum.getSchedule());
    }

    /**
     * Test for editing a museum by empty all null fields
     * 
     * @author VZ
     */
    @Test
    public void testEditMuseumWithhAllFieldsNull() {
        Museum museum = null;
        String error = null;
        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, MUSEUM_SCHEDULE);
            museum = museumService.editMuseum(MUSEUM_ID, null, null, null);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals(MUSEUM_NAME, museum.getName());
        assertEquals(MUSEUM_VISITFEE, museum.getVisitFee());
        assertEquals(MUSEUM_SCHEDULE, museum.getSchedule());
        assertEquals("Nothing to edit, all fields are empty", error);
    }

    /**
     * Test for getting a museum schedule
     * 
     * @author VZ
     */
    @Test
    public void testGetMuseumSchedule() {
        Schedule schedule = null;
        try {
            schedule = museumService.getMuseumSchedule(MUSEUM_ID);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(schedule);
    }

    /**
     * Test for getting a museum schedule with an invalid museum id
     * 
     * @author VZ
     */
    @Test
    public void testGetMuseumScheduleWithInvalidMuseumId() {
        Schedule schedule = null;
        String error = null;
        try {
            schedule = museumService.getMuseumSchedule(NON_EXISTING_MUSEUM_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(schedule);
        assertEquals("Museum doesn't exist!", error);
    }

    /**
     * Test for getting museum timeperiods
     * 
     * @author VZ
     */
    @Test
    public void testGetMuseumTimePeriods() {

        // CREATE TIME PERIODS
        final TimePeriod timePeriod = new TimePeriod();
        final Long id1 = 1L;
        final Timestamp startDate = new Timestamp(0);
        final Timestamp endDate = new Timestamp(1);
        timePeriod.setTimePeriodId(id1);
        timePeriod.setStartDate(startDate);
        timePeriod.setEndDate(endDate);

        final TimePeriod timePeriod2 = new TimePeriod();
        final Long id2 = 2L;
        final Timestamp startDate2 = new Timestamp(2);
        final Timestamp endDate2 = new Timestamp(3);
        timePeriod2.setTimePeriodId(id2);
        timePeriod2.setStartDate(startDate2);
        timePeriod2.setEndDate(endDate2);

        // CREATE SCHEDULE
        final Schedule schedule = new Schedule();
        final Long scheduleId = 1L;
        schedule.setScheduleId(scheduleId);

        // CREATE SCHEDULEOFTIMEPERIODS
        final List<ScheduleOfTimePeriod> scheduleOfTimePeriods = new ArrayList<>();
        final ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
        scheduleOfTimePeriod.setSchedule(schedule);
        scheduleOfTimePeriod.setTimePeriod(timePeriod);
        final ScheduleOfTimePeriod scheduleOfTimePeriod2 = new ScheduleOfTimePeriod();
        scheduleOfTimePeriod2.setSchedule(schedule);
        scheduleOfTimePeriod2.setTimePeriod(timePeriod2);
        scheduleOfTimePeriods.add(scheduleOfTimePeriod);
        scheduleOfTimePeriods.add(scheduleOfTimePeriod2);

        // CREATE MUSEUM
        final Museum museum = new Museum();
        final Long museumId = 1L;
        museum.setMuseumId(museumId);
        museum.setSchedule(schedule);

        List<TimePeriod> timePeriods = null;

        when(museumRepository.findMuseumByMuseumId(museumId)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodBySchedule(schedule))
                .thenAnswer((InvocationOnMock invocation) -> scheduleOfTimePeriods);

        try {
            timePeriods = museumService.getMuseumTimePeriods(museumId);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(timePeriods);
        assertEquals(2, timePeriods.size());

    }

    /**
     * Test for getting museum timeperiods with an invalid museum id
     * 
     * @author VZ
     */
    @Test
    public void testGetMuseumTimePeriodsWithInvalidId() {
        List<TimePeriod> timePeriods = null;
        String error = null;
        try {
            timePeriods = museumService.getMuseumTimePeriods(NON_EXISTING_MUSEUM_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(timePeriods);
        assertEquals("Museum doesn't exist!", error);
    }

    /**
     * Test for timeperiods of a museum that has no timeperiods
     * 
     * @author VZ
     */
    @Test
    public void testGetMuseumTimePeriodsWithoutShifts() {
        // CREATE SCHEDULE
        final Schedule schedule = new Schedule();
        final Long scheduleId = 1L;
        schedule.setScheduleId(scheduleId);

        // CREATE MUSEUM
        final Museum museum = new Museum();
        final Long museumId = 1L;
        museum.setMuseumId(museumId);
        museum.setSchedule(schedule);

        List<TimePeriod> timePeriods = new ArrayList<>();
        when(museumRepository.findMuseumByMuseumId(museumId)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodBySchedule(schedule))
                .thenAnswer((InvocationOnMock invocation) -> null);

        try {
            timePeriods = museumService.getMuseumTimePeriods(museumId);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNull(timePeriods);
    }

    /**
     * test for adding a timeperiod to a museum schedule
     * 
     * @author VZ
     */

    @Test
    public void testAddMuseumTimePeriodAssociation() {
        // CREATE MUSEUM WITH SCHEDULE
        final Museum museum = new Museum();
        final Schedule schedule = new Schedule();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);
        museum.setSchedule(schedule);

        // CREATE TIMEPERIOD WE WANT TO ADD
        final TimePeriod timePeriod = new TimePeriod();
        timePeriod.setTimePeriodId(TIMEPERIOD_ID);
        timePeriod.setStartDate(STARTDATE);
        timePeriod.setEndDate(ENDDATE);

        // ADD SHIFT TO MUSEUM SCHEDULE
        final ScheduleOfTimePeriod sotp = new ScheduleOfTimePeriod();
        sotp.setSchedule(schedule);
        sotp.setTimePeriod(timePeriod);

        Museum testMuseum = null;

        when(museumRepository.findMuseumByMuseumId(MUSEUM_ID)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(timePeriodRepository.findTimePeriodByTimePeriodId(TIMEPERIOD_ID))
                .thenAnswer((InvocationOnMock invocation) -> timePeriod);
        // test that we get back a museum
        testMuseum = museumService.addMuseumTimePeriodAssociation(MUSEUM_ID, TIMEPERIOD_ID);
        assertNotNull(testMuseum);
        // test that service actually saved museum and schedule
        verify(museumRepository, times(1)).save(museum);
        verify(scheduleRepository, times(1)).save(schedule);
    }

    /**
     * 
     * test for adding a timeperiod association to a museum with an invalid museum
     * id
     * 
     * @author VZ
     */
    @Test
    public void testAddMuseumTimePeriodAssociationInvalidMuseumId() {

        final Long invalidId = 99L;
        final Long timePeriodId = 1L;
        String error = "";
        when(museumRepository.findMuseumByMuseumId(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);
        try {
            museumService.addMuseumTimePeriodAssociation(invalidId, timePeriodId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Museum doesn't exist!", error);
    }

    /**
     * test for adding a timeperiod association where the timeperiod doesn't exist
     * 
     * @author VZ
     */
    @Test
    public void testAddMuseumTimePeriodAssociationInvalidTimePeriod() {
        final Long invalidId = 99L;
        // CREATE MUSEUM
        Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);
        museum.setSchedule(new Schedule());

        String error = "";

        when(museumRepository.findMuseumByMuseumId(MUSEUM_ID)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(timePeriodRepository.findTimePeriodByTimePeriodId(invalidId))
                .thenAnswer((InvocationOnMock invocation) -> null);

        try {
            museumService.addMuseumTimePeriodAssociation(MUSEUM_ID, invalidId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Time period doesn't exist!", error);
    }

    /**
     * test for removing a timeperiod association from a museum's schedule
     * 
     * @author VZ
     */
    @Test
    public void testRemoveMuseumTimePeriodAssociation() {
        // CREATE MUSEUM WITH SCHEDULE
        final Museum museum = new Museum();
        final Schedule schedule = new Schedule();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);
        museum.setSchedule(schedule);
        // CREATE TIMEPERIOD WE WANT TO DELETE
        final TimePeriod tp = new TimePeriod();
        tp.setTimePeriodId(TIMEPERIOD_ID);
        tp.setStartDate(STARTDATE);
        tp.setEndDate(ENDDATE);

        // ADD SHIFT TO MUSEUM SCHEDULE
        final ScheduleOfTimePeriod sotp = new ScheduleOfTimePeriod();
        sotp.setSchedule(schedule);
        sotp.setTimePeriod(tp);

        Museum testMuseum = null;

        when(museumRepository.findMuseumByMuseumId(MUSEUM_ID)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(timePeriodRepository.findTimePeriodByTimePeriodId(TIMEPERIOD_ID))
                .thenAnswer((InvocationOnMock invocation) -> tp);
        when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, tp))
                .thenAnswer((InvocationOnMock invocation) -> sotp);
        // test that we get back a museum
        testMuseum = museumService.deleteMuseumTimePeriodAssociation(MUSEUM_ID, TIMEPERIOD_ID);
        assertNotNull(testMuseum);
        // test that service actually saved museum
        verify(museumRepository, times(1)).save(museum);

    }

    /**
     * test for removing a timeperiod association from a museum's schedule that
     * doesn't have the timeperiod we want to remove
     * 
     * @author VZ
     */
    @Test
    public void testRemoveMuseumTimePeriodAssociationWithoutTimePeriod() {
        // CREATE MUSEUM WITH SCHEDULE
        final Museum museum = new Museum();
        final Schedule schedule = new Schedule();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);
        museum.setSchedule(schedule);
        // CREATE TIMEPERIOD WE WANT TO DELETE
        final TimePeriod tp = new TimePeriod();
        tp.setTimePeriodId(TIMEPERIOD_ID);
        tp.setStartDate(STARTDATE);
        tp.setEndDate(ENDDATE);

        String error = "";

        Museum testMuseum = null;
        when(museumRepository.findMuseumByMuseumId(MUSEUM_ID)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(timePeriodRepository.findTimePeriodByTimePeriodId(TIMEPERIOD_ID))
                .thenAnswer((InvocationOnMock invocation) -> tp);
        when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, tp))
                .thenAnswer((InvocationOnMock invocation) -> null);

        try {
            testMuseum = museumService.deleteMuseumTimePeriodAssociation(MUSEUM_ID, TIMEPERIOD_ID);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        ;
        assertNull(testMuseum);
        assertEquals("Time period doesn't exist in museum's schedule!", error);
    }

    /**
     * test for removing a timePeriod association from a museum that doesn't exist
     * 
     * @author VZ
     */
    @Test
    public void testRemoveMuseumTimePeriodAssociationInvalidMuseumId() {
        final Long invalidId = 99L;
        final Long timePeriodId = 1L;
        String error = "";
        when(museumRepository.findMuseumByMuseumId(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);
        try {
            museumService.deleteMuseumTimePeriodAssociation(invalidId, timePeriodId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Museum doesn't exist!", error);
    }

    /**
     * test for removing a timePeriod association where the timeperiod doesn't exist
     * 
     * @author VZ
     */
    @Test
    public void testRemoveMuseumTimePeriodAssociationInvalidTimePeriodId() {
        Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);
        museum.setSchedule(new Schedule());

        String error = "";
        final Long invalidId = 99L;
        when(museumRepository.findMuseumByMuseumId(MUSEUM_ID)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(timePeriodRepository.findTimePeriodByTimePeriodId(invalidId))
                .thenAnswer((InvocationOnMock invocation) -> null);

        try {
            museumService.deleteMuseumTimePeriodAssociation(MUSEUM_ID, invalidId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Time period doesn't exist!", error);
    }

    /**
     * test for getting all museums
     * 
     * @author VZ
     */
    @Test
    public void testGetAllMuseums() {
        // CREATE FIRST MUSEUM
        final Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);

        // CREATE SECOND MUSEUM
        final Museum museum2 = new Museum();
        final Long id = 1L;
        final String name = "asdf";
        final double visitFee = 10;
        museum2.setMuseumId(id);
        museum2.setName(name);
        museum2.setVisitFee(visitFee);

        List<Museum> museums = new ArrayList<>();

        museums.add(museum);
        museums.add(museum2);
        when(museumRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> museums);

        List<Museum> testMuseums = museumService.getAllMuseums();
        assertEquals(museums, testMuseums);
        assertEquals(2, testMuseums.size());

    }

    /**
     * Test for getting all museums when there is one
     * 
     * @author VZ
     *
     */
    @Test
    public void testGetAllMuseumsOne() {
        // CREATE FIRST MUSEUM
        final Museum museum = new Museum();
        museum.setMuseumId(MUSEUM_ID);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);

        List<Museum> museums = new ArrayList<>();
        museums.add(museum);

        when(museumRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> museums);

        List<Museum> testMuseums = museumService.getAllMuseums();
        assertEquals(museums, testMuseums);
        assertEquals(1, testMuseums.size());

    }

    /**
     * test for getting all museums when there are none
     * 
     * @author VZ
     */
    @Test
    public void testGetAllMuseumsEmpty() {
        List<Museum> museums = new ArrayList<>();

        when(museumRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> museums);

        List<Museum> testMuseums = museumService.getAllMuseums();
        assertEquals(museums, testMuseums);
        assertEquals(0, testMuseums.size());
    }

}
