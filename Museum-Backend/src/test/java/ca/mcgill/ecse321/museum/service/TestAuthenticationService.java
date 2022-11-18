package ca.mcgill.ecse321.museum.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.Visitor;

@ExtendWith(MockitoExtension.class)
public class TestAuthenticationService {
  @Mock
  private VisitorRepository visitorRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ManagerRepository managerRepository;

  @InjectMocks
  private AuthenticationService authenticationService;

  private static final String VISITOR_VALID_EMAIL = "lewis@gmail.com";
  private static final String VALID_PASSWORD = "Speed123#$";

  private static final String INVALID_PASSWORD = "Speed123";
  private static final String NON_EXISTING_EMAIL = "lewis@test.com";

  private static final String EMPLOYEE_VALID_EMAIL = "employee@museum.com";
  private static final String ADMIN_VALID_EMAIL = "admin@museum.com";

  @BeforeEach
  public void setMockOutput() {
    lenient().when(visitorRepository.findVisitorByEmail(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(VISITOR_VALID_EMAIL)) {
            Visitor visitor = new Visitor();
            visitor.setEmail(VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            return visitor;
          } else {
            return null;
          }
        });

    lenient().when(visitorRepository.findVisitorByEmail(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(VISITOR_VALID_EMAIL)) {
            Visitor visitor = new Visitor();
            visitor.setEmail(VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            return visitor;
          } else {
            return null;
          }
        });


    lenient().when(employeeRepository.findEmployeeByEmail(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(EMPLOYEE_VALID_EMAIL)) {
            Employee employee = new Employee();
            employee.setEmail(EMPLOYEE_VALID_EMAIL);
            employee.setPassword(VALID_PASSWORD);
            return employee;
          } else {
            return null;
          }
        });

    lenient().when(managerRepository.findManagerByEmail(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(ADMIN_VALID_EMAIL)) {
            Manager manager = new Manager();
            manager.setEmail(ADMIN_VALID_EMAIL);
            manager.setPassword(VALID_PASSWORD);
            return manager;
          } else {
            return null;
          }
        });

    lenient().when(employeeRepository.save(any(Employee.class)))
        .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));

    lenient().when(visitorRepository.save(any(Visitor.class)))
        .thenAnswer((InvocationOnMock invocation) -> invocation.getArgument(0));
  }



  @Test
  public void testAuthenticateValidMuseumUser() {
    try {
      VisitorDto visitorDto = new VisitorDto();
      visitorDto.setEmail(VISITOR_VALID_EMAIL);
      visitorDto.setPassword(VALID_PASSWORD);
      authenticationService.authenticateUser(visitorDto.getEmail(), visitorDto.getPassword());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testAuthenticateInvalidPasswordMuseumUser() {
    assertThrows(Exception.class, () -> {
      VisitorDto visitorDto = new VisitorDto();
      visitorDto.setEmail(VISITOR_VALID_EMAIL);
      visitorDto.setPassword(INVALID_PASSWORD);
      authenticationService.authenticateUser(visitorDto.getEmail(), visitorDto.getPassword());
    });
  }

  @Test
  public void testAuthenticateInvalidEmailMuseumUser() {
    assertThrows(Exception.class, () -> {
      VisitorDto visitorDto = new VisitorDto();
      visitorDto.setEmail(NON_EXISTING_EMAIL);
      visitorDto.setPassword(VALID_PASSWORD);
      authenticationService.authenticateUser(visitorDto.getEmail(), visitorDto.getPassword());
    });
  }

  @Test
  public void testNoEmailEntered() {
    assertThrows(Exception.class, () -> {
      VisitorDto visitorDto = new VisitorDto();
      visitorDto.setEmail(null);
      visitorDto.setPassword(VALID_PASSWORD);
      authenticationService.authenticateUser(visitorDto.getEmail(), visitorDto.getPassword());
    });
  }

  @Test
  public void testNoPasswordEntered() {
    assertThrows(Exception.class, () -> {
      VisitorDto visitorDto = new VisitorDto();
      visitorDto.setEmail(VISITOR_VALID_EMAIL);
      visitorDto.setPassword(null);
      authenticationService.authenticateUser(visitorDto.getEmail(), visitorDto.getPassword());
    });
  }
}


