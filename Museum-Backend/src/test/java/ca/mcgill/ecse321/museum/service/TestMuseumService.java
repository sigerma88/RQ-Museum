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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.exceptions.util.ScenarioPrinter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.scheduling.concurrent.ScheduledExecutorFactoryBean;

import ca.mcgill.ecse321.museum.dao.MuseumRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleOfTimePeriodRepository;
import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.dao.TimePeriodRepository;
import ca.mcgill.ecse321.museum.dto.MuseumDto;
import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.dto.ScheduleOfTimePeriodDto;
import ca.mcgill.ecse321.museum.model.Museum;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.ScheduleOfTimePeriod;
import ca.mcgill.ecse321.museum.model.TimePeriod;

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

    private static final Long NON_EXISTING_MUSEUM_ID = 2L;

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
                Schedule schedule = new Schedule();
                schedule.setScheduleId(SCHEDULE_ID);
                museum.setSchedule(schedule);
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
     * @author VZ
     *         This method is used to test the creation of a museum
     */
    @Test
    public void testCreateMuseum() {
        assertEquals(0, museumService.getAllMuseums().size());
        String name = "The Louvre";
        double visitFee = 10.0;
        Museum museum = null;
        Schedule schedule = new Schedule();
        try {
            museum = museumService.createMuseum(name, visitFee, schedule);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(museum);
        assertEquals(name, museum.getName());
        assertEquals(visitFee, museum.getVisitFee());
        assertEquals(schedule, museum.getSchedule());
    }

    /**
     * @author VZ
     *         This test checks if the museum service throws an exception when the
     *         museum
     *         name is null
     */
    @Test
    public void testCreateMuseumNullName() {
        assertEquals(0, museumService.getAllMuseums().size());
        String name = null;
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
     * @author VZ
     *         test for creating a museum with an empty name
     */
    @Test
    public void testCreateMuseumEmptyName() {
        assertEquals(0, museumService.getAllMuseums().size());
        String name = "";
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
     * @author VZ
     *         Test for creating a museum with name [space]
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
     * @author VZ
     *         test method for creating a museum that doesn't exist.
     */
    @Test
    public void testCreateNonExistingMuseum() {
        assertNull(museumService.getMuseum(NON_EXISTING_MUSEUM_ID));
    }

    /**
     * @author VZ
     *         test for creating a museum with a negative visit fee
     */
    @Test
    public void testCreateMuseumWithNegativeVisitFee() {
        String name = "The Louvre";
        double visitFee = -10.0;
        Schedule schedule = new Schedule();
        Museum museum = null;
        String error = null;
        try {

            museum = museumService.createMuseum(name, visitFee, schedule);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(museum);
        assertEquals("Visit fee cannot be negative!", error);
    }

    /**
     * @author VZ
     *         test for creating a museum with no schedule
     */
    @Test
    public void testCreateMuseumWithNoSchedule() {
        String name = "The Louvre";
        double visitFee = 10.0;
        Schedule schedule = null;
        Museum museum = null;
        String error = null;
        try {
            museum = museumService.createMuseum(name, visitFee, schedule);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNull(museum);
        assertEquals("Schedule cannot be null!", error);
    }

    /**
     * @author VZ
     *         test for editing a museum's attributes
     */
    @Test
    public void testEditMuseum() {
        Schedule schedule = new Schedule();
        Museum museum = null;
        String name = "L";
        double visitFee = 20.0;
        Schedule new_schedule = new Schedule();
        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
            museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);
        } catch (IllegalArgumentException e) {
            fail();
        }
        assertNotNull(museum);
        assertEquals(name, museum.getName());
        assertEquals(visitFee, museum.getVisitFee());
        assertEquals(new_schedule, museum.getSchedule());

    }

    /**
     * @author VZ
     *         test for editing a museum by setting the name to null
     */
    @Test
    public void testEditMuseumWithNullName() {
        Schedule schedule = new Schedule();
        Museum museum = null;
        String name = null;
        double visitFee = 20.0;
        Schedule new_schedule = new Schedule();
        String error = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
            museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);

        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals("Name cannot be empty!", error);
    }

    /**
     * @author VZ
     *         test for trying to edit a museum by setting an empty name
     */
    @Test
    public void testEditMuseumWithEmptyName() {
        Museum museum = null;
        Schedule schedule = new Schedule();
        String name = "";
        double visitFee = 20.0;
        Schedule new_schedule = new Schedule();
        String error = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
            museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals("Name cannot be empty!", error);

    }

    /**
     * @author VZ
     *         test for trying to edit a museum by setting a name with only space
     */
    @Test
    public void testEditMuseumWithSpaceName() {
        Museum museum = null;
        Schedule schedule = new Schedule();
        String name = " ";
        double visitFee = 20.0;
        Schedule new_schedule = new Schedule();
        String error = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
            museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals("Name cannot be empty!", error);

    }

    /**
     * @author VZ
     *         test for editing a museum by setting a negative visit fee
     */
    @Test
    public void testEditMuseumWithNegativeVisitFee() {
        Museum museum = null;
        Schedule schedule = new Schedule();
        String name = "L";
        double visitFee = -20.0;
        Schedule new_schedule = new Schedule();
        String error = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
            museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals("Visit fee cannot be negative!", error);
    }

    /**
     * @author VZ
     *         test for editing a museum by setting a null schedule
     */
    @Test
    public void testEditMuseumWithNoSchedule() {
        Museum museum = null;
        Schedule schedule = new Schedule();
        String name = "L";
        double visitFee = 20.0;
        Schedule new_schedule = null;
        String error = null;

        try {
            museum = museumService.createMuseum(MUSEUM_NAME, MUSEUM_VISITFEE, schedule);
            museum = museumService.editMuseum(MUSEUM_ID, name, visitFee, new_schedule);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertNotNull(museum);
        assertEquals("Schedule cannot be null!", error);
    }

    /**
     * @author VZ
     *         Test for getting a museum schedule
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
     * @author VZ
     *         Test for getting a museum schedule with an invalid museum id
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
     * @author VZ
     *         Test for getting museum timeperiods
     */
    @Test
    public void testGetMuseumTimePeriods() {
        List<TimePeriod> timePeriods = null;
        try {
            timePeriods = museumService.getMuseumTimePeriods(MUSEUM_ID);
        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(timePeriods);

    }

    /**
     * @author VZ
     *         Test for getting museum timeperiods with an invalid museum id
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

    @Test
    public void testAddMuseumTimePeriodAssociation() {
        // CREATE MUSEUM WITH SCHEDULE
        Museum museum = new Museum();
        Schedule schedule = new Schedule();
        schedule.setScheduleId(SCHEDULE_ID);
        museum.setMuseumId(MUSEUM_ID);
        museum.setSchedule(schedule);
        museum.setName(MUSEUM_NAME);
        museum.setVisitFee(MUSEUM_VISITFEE);
        // CREATE TIMEPERIOD WE WANT TO ADD
        TimePeriod timeperiod = new TimePeriod();
        timeperiod.setTimePeriodId(TIMEPERIOD_ID);

        ScheduleOfTimePeriod dummyScheduleOfTimePeriod = null;

        // finding SOTPs associated with the museum's schedule.
        lenient().when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodBySchedule(any(Schedule.class)))
                .thenAnswer((InvocationOnMock invocation) -> {
                    // create 2 SOTPs and associate them with the schedule, associate only one with
                    // timeperiod
                    List<ScheduleOfTimePeriod> scheduleOfTimePeriods = new ArrayList<>();
                    ScheduleOfTimePeriod scheduleOfTimePeriod = new ScheduleOfTimePeriod();
                    ScheduleOfTimePeriod scheduleOfTimePeriod2 = new ScheduleOfTimePeriod();

                    scheduleOfTimePeriod.setSchedule(schedule);
                    scheduleOfTimePeriod2.setSchedule(schedule);

                    scheduleOfTimePeriod.setTimePeriod(timeperiod);
                    scheduleOfTimePeriod2.setTimePeriod(new TimePeriod());

                    scheduleOfTimePeriods.add(scheduleOfTimePeriod);
                    scheduleOfTimePeriods.add(scheduleOfTimePeriod2);
                    return scheduleOfTimePeriods;
                });

        try {
            museum = museumService.addMuseumTimePeriodAssociation(MUSEUM_ID, TIMEPERIOD_ID);

            List<ScheduleOfTimePeriod> scheduleOfTimePeriods = scheduleOfTimePeriodRepository
                    .findScheduleOfTimePeriodBySchedule(museum.getSchedule());

            // check if the timeperiod was added to the museum's schedule
            for (ScheduleOfTimePeriod sotp : scheduleOfTimePeriods) {
                if (sotp.getTimePeriod().getTimePeriodId() == TIMEPERIOD_ID) {
                    dummyScheduleOfTimePeriod = sotp;
                }
            }

        } catch (IllegalArgumentException e) {
            fail(e.getMessage());
        }
        assertNotNull(museum);
        assertNotNull(dummyScheduleOfTimePeriod);
        assertEquals(timeperiod, dummyScheduleOfTimePeriod.getTimePeriod());
    }

    /**
     * test for removing a timeperiod association from a museum's schedule
     * @author VZ
     */
    @Test
    public void testRemoveMuseumTimePeriodAssociation() {
        // CREATE MUSEUM WITH SCHEDULE
        final Long id = 1L;
        final String name = "asdf";
        final double visitFee = 10L;
        final Museum museum = new Museum();
        final Schedule schedule = new Schedule();
        museum.setMuseumId(id);
        museum.setName(name);
        museum.setVisitFee(visitFee);
        museum.setSchedule(schedule);
        // CREATE TIMEPERIOD WE WANT TO DELETE
        final Long tpId = 1L;
        final Timestamp startDate = new Timestamp(0);
        final Timestamp endDate = new Timestamp(1);
        final TimePeriod tp = new TimePeriod();
        tp.setTimePeriodId(tpId);
        tp.setStartDate(startDate);
        tp.setEndDate(endDate);

        // ADD SHIFT TO MUSEUM SCHEDULE
        final ScheduleOfTimePeriod sotp = new ScheduleOfTimePeriod();
        sotp.setSchedule(schedule);
        sotp.setTimePeriod(tp);

        Museum testMuseum = null;

        when(museumRepository.findMuseumByMuseumId(id)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(timePeriodRepository.findTimePeriodByTimePeriodId(tpId)).thenAnswer((InvocationOnMock invocation) -> tp);
        when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, tp))
                .thenAnswer((InvocationOnMock invocation) -> sotp);
        // test that we get back a museum
        testMuseum = museumService.removeMuseumTimePeriodAssociation(id, tpId);
        assertNotNull(testMuseum);
        // test that service actually saved museum
        verify(museumRepository, times(1)).save(museum);
    }

    /**
     * test for removing a timeperiod association from a museum's schedule that doesn't have the 
     * timeperiod we want to remove
     * @author VZ
     */
    @Test
    public void testRemoveMuseumTimePeriodAssociationWithoutTimePeriod() {
        // CREATE MUSEUM WITH SCHEDULE
        final Long id = 1L;
        final String name = "asdf";
        final double visitFee = 10L;
        final Museum museum = new Museum();
        final Schedule schedule = new Schedule();
        museum.setMuseumId(id);
        museum.setName(name);
        museum.setVisitFee(visitFee);
        museum.setSchedule(schedule);
        // CREATE TIMEPERIOD WE WANT TO DELETE
        final Long tpId = 1L;
        final Timestamp startDate = new Timestamp(0);
        final Timestamp endDate = new Timestamp(1);
        final TimePeriod tp = new TimePeriod();
        tp.setTimePeriodId(tpId);
        tp.setStartDate(startDate);
        tp.setEndDate(endDate);

        String error = "";

        Museum testMuseum = null;
        when(museumRepository.findMuseumByMuseumId(id)).thenAnswer((InvocationOnMock invocation) -> museum);
        when(timePeriodRepository.findTimePeriodByTimePeriodId(tpId)).thenAnswer((InvocationOnMock invocation) -> tp);
        when(scheduleOfTimePeriodRepository.findScheduleOfTimePeriodByScheduleAndTimePeriod(schedule, tp))
                .thenAnswer((InvocationOnMock invocation) -> null);

        try { testMuseum = museumService.removeMuseumTimePeriodAssociation(id, tpId);
        } catch (IllegalArgumentException e){
            error = e.getMessage();
        };
        assertNull(testMuseum);
        assertEquals("Time period doesn't exist in museum's schedule!", error);
    }
    /**
     * test for removing a timePeriod association from a museum that doesn't exist
     * @author VZ
     */
    @Test
    public void testRemoveMuseumTimePeriodAssociationInvalidMuseumId() {
        final Long invalidId = 99L;
        final Long timePeriodId = 1L;
        String error = "";
        when(museumRepository.findMuseumByMuseumId(invalidId)).thenAnswer((InvocationOnMock invocation) -> null);
        try {
            museumService.removeMuseumTimePeriodAssociation(invalidId, timePeriodId);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();
        }
        assertEquals("Museum doesn't exist!", error);
    }

    /**
     * test for getting all museums 
     * @author VZ
     */
    @Test
    public void testGetAllMuseums(){
        //CREATE FIRST MUSEUM
        final Long id = 1L;
        final String name = "asdf";
        final double visitFee = 10;
        final Museum museum = new Museum();
        museum.setMuseumId(id);
        museum.setName(name);
        museum.setVisitFee(visitFee);

        //CREATE SECOND MUSEUM
        final Long id2 = 2L;
        final String name2 = "fdsa";
        final double visitFee2 = 9;
        final Museum museum2 = new Museum();
        List<Museum> museums = new ArrayList<>();
        
        museums.add(museum);
        museums.add(museum2);
        when(museumRepository.findAll()).thenAnswer((InvocationOnMock invocation) -> museums);
        
        List<Museum> testMuseums = museumService.getAllMuseums();
        assertEquals(museums, testMuseums);

    }

}
