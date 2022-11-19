package ca.mcgill.ecse321.museum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@RestController
@SpringBootApplication
public class MuseumApplication {

  public static void main(String[] args) {
    SpringApplication.run(MuseumApplication.class, args);
  }

  @RequestMapping("/")
  public String greeting() {
    return "Hello world!";
  }
  
}
