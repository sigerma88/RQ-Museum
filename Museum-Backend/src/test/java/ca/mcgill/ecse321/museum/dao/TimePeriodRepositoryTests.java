package ca.mcgill.ecse321.museum.dao;

import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;  

import ca.mcgill.ecse321.museum.model.TimePeriod;

@SpringBootTest
public class TimePeriodRepositoryTests {
  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @AfterEach
  public void clearDatabase() {
    timePeriodRepository.deleteAll();
  }

  @Test
  public void testPersistandLoadTimePeriod() {
    //create TimePeriod
    Timestamp startDate = Timestamp.valueOf("2022-10-28 08:30:00.0");
    Timestamp endDate = Timestamp.valueOf("2022-10-28 17:35:00.0");
    TimePeriod timePeriod = new TimePeriod();
    timePeriod.setStartDate(startDate);
    timePeriod.setEndDate(endDate);

    //save TimePeriod
    timePeriodRepository.save(timePeriod);
    long timePeriodId = timePeriod.getTimePeriodId();

    //read TimePeriod from database
    timePeriod = timePeriodRepository.findTimePeriodByTimePeriodId(timePeriodId);

    //assert that TimePeriod has correct attributes
    assertNotNull(timePeriod);
  }

}
