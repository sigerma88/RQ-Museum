package ca.mcgill.ecse321.museum.dao;

import ca.mcgill.ecse321.museum.model.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.AfterEach;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class MuseumSystemRepositoryTests {
    @Autowired
    MuseumSystemRepository museumSystemRepository;


    @AfterEach
    public void clearDatabase() {
        museumSystemRepository.deleteAll();
    }

    @Test
    public void testPersistenceMuseumSystem() {
        Schedule schedule = new Schedule();

        Museum museum = new Museum();
        museum.setName("Rougon-Macquart");
        museum.setVisitFee(12.5);
        museum.setSchedule(schedule);

        Manager manager = new Manager();
        manager.setEmail("test@museum.com");
        manager.setName("Test");
        manager.setPassword("test");

        ArrayList<Visitor> visitors = new ArrayList<Visitor>();
        Visitor visitor = new Visitor();
        visitor.setEmail("test2@museum.com");
        visitor.setName("Test2");
        visitor.setPassword("test2");
        visitors.add(visitor);

        ArrayList<Room> rooms = new ArrayList<Room>();
        Room room = new Room();
        room.setRoomName("Room 1");
        room.setRoomType(RoomType.Large);
        room.setCurrentNumberOfArtwork(0);
        room.setMuseum(museum);
        rooms.add(room);

        ArrayList<Employee> employees = new ArrayList<Employee>();
        Employee employee = new Employee();
        employee.setEmail("employee@museum.com");
        employee.setName("Employee");
        employee.setPassword("employee");
        employee.setSchedule(schedule);
        employees.add(employee);

        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        schedules.add(schedule);

        ArrayList<Artwork> artworks = new ArrayList<Artwork>();
        Artwork artwork = new Artwork();
        artwork.setName("The Art");
        artwork.setArtist("Kevin");
        artwork.setIsAvailableForLoan(true);
        artwork.setLoanFee(12.5);
        artwork.setImage("https://source.unsplash.com/C54OKB99iuw");
        artwork.setRoom(room);
        artwork.setName("Artist 1");
        artworks.add(artwork);

        ArrayList<Loan> loans = new ArrayList<Loan>();
        Loan loan = new Loan();
        loan.setRequestAccepted(true);
        loan.setVisitor(visitor);
        loan.setArtwork(artwork);
        loans.add(loan);

        ArrayList<Ticket> tickets = new ArrayList<Ticket>();
        Ticket ticket = new Ticket();
        ticket.setVisitor(visitor);
        ticket.setVisitDate(Date.valueOf("2020-12-12"));
        tickets.add(ticket);

        ArrayList<TimePeriod> timePeriods = new ArrayList<TimePeriod>();
        TimePeriod timePeriod = new TimePeriod();


        MuseumSystem museumSystem = new MuseumSystem();
        museumSystem.setMuseum(museum);
        museumSystem.setManager(manager);
        museumSystem.setVisitor(visitors);
        museumSystem.setRoom(rooms);
        museumSystem.setEmployee(employees);
        museumSystem.setSchedule(schedules);
        museumSystem.setArtwork(artworks);
        museumSystem.setLoan(loans);
        museumSystem.setTicket(tickets);


        MuseumSystem savedMuseumSystem = museumSystemRepository.save(museumSystem);
        assertEquals(museum.getMuseumId(), savedMuseumSystem.getMuseum().getMuseumId());
        assertEquals(manager.getMuseumUserId(), savedMuseumSystem.getManager().getMuseumUserId());
        assertEquals(visitor.getMuseumUserId(),
                savedMuseumSystem.getVisitor().get(0).getMuseumUserId());
        assertEquals(room.getRoomId(), savedMuseumSystem.getRoom().get(0).getRoomId());
        assertEquals(employee.getMuseumUserId(),
                savedMuseumSystem.getEmployee().get(0).getMuseumUserId());
        assertEquals(schedule.getScheduleId(),
                savedMuseumSystem.getSchedule().get(0).getScheduleId());
        assertEquals(artwork.getArtworkId(), savedMuseumSystem.getArtwork().get(0).getArtworkId());
        assertEquals(ticket.getTicketId(), savedMuseumSystem.getTicket().get(0).getTicketId());
        assertEquals(loan.getLoanId(), savedMuseumSystem.getLoan().get(0).getLoanId());


    }
}
