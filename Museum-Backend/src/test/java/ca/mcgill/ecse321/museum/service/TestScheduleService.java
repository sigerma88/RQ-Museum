package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.ScheduleRepository;
import ca.mcgill.ecse321.museum.model.Schedule;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * This is the test class for ScheduleService
 *
 * @author VZ
 */
@ExtendWith(MockitoExtension.class)
public class TestScheduleService {
  @Mock
  private ScheduleRepository scheduleRepository;
  @InjectMocks
  private ScheduleService scheduleService;

  /**
   * This is the test method for creating a schedule
   *
   * @author VZ
   */
  @Test
  public void testCreateSchedule() {
    when(scheduleRepository.save(any(Schedule.class))).thenAnswer((InvocationOnMock invocation) -> {
      return invocation.getArgument(0);
    });
    Schedule schedule = null;
    try {
      schedule = scheduleService.createSchedule();
    } catch (IllegalArgumentException e) {
      fail();
    }
    assertNotNull(schedule);

  }
}
