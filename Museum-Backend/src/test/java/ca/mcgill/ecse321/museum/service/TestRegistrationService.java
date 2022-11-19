package ca.mcgill.ecse321.museum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

/**
 * This class tests the Registration Service
 * 
 * @author Kevin
 * 
 */

@ExtendWith(MockitoExtension.class)
public class TestRegistrationService {
  @Mock
  private VisitorRepository visitorRepository;

  @Mock
  private EmployeeRepository employeeRepository;

  @Mock
  private ManagerRepository managerRepository;

  @InjectMocks
  private RegistrationService registrationService;

  private static final String FIRST_VISITOR_VALID_EMAIL = "sebastienVettel@gmail.com";
  private static final String FIRST_VALID_VISITOR_NAME = "Sebastien Vettel";
  private static final long FIRST_VISITOR_ID = 1;

  private static final String SECOND_VISITOR_VALID_EMAIL = "george@gmail.com";
  private static final String SECOND_VALID_VISITOR_NAME = "george russel";
  private static final long SECOND_VISITOR_ID = 2;

  private static final long EMPLOYEE_ID = 3;
  private static final String EMPLOYEE_VALID_EMAIL = "sergio.perez@museum.ca";
  private static final String EMPLOYEE_VALID_NAME = "Sergio Perez";

  private static final long MANAGER_ID = 4;
  private static final String MANAGER_VALID_EMAIL = "nicholas.latifi@admin.ca";
  private static final String MANAGER_VALID_NAME = "Nicholas Latifi";

  private static final String VALID_PASSWORD = "Speed123#$";

  private static final String INVALID_PASSWORD = "Speed123";
  private static final String INVALID_EMAIL = "lewisgmailcom";


  @BeforeEach
  public void setMockOutput() {
    lenient().when(visitorRepository.findVisitorByMuseumUserId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(FIRST_VISITOR_ID)) {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(FIRST_VISITOR_ID);
            visitor.setEmail(FIRST_VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(FIRST_VALID_VISITOR_NAME);
            return visitor;
          } else if (invocation.getArgument(0).equals(SECOND_VISITOR_ID)) {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(SECOND_VISITOR_ID);
            visitor.setEmail(SECOND_VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(SECOND_VALID_VISITOR_NAME);
            return visitor;
          } else {
            return null;
          }

        });

    lenient().when(visitorRepository.findVisitorByEmail(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(FIRST_VISITOR_VALID_EMAIL)) {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(FIRST_VISITOR_ID);
            visitor.setEmail(FIRST_VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(FIRST_VALID_VISITOR_NAME);
            return visitor;
          } else if (invocation.getArgument(0).equals(SECOND_VISITOR_VALID_EMAIL)) {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(SECOND_VISITOR_ID);
            visitor.setEmail(SECOND_VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(SECOND_VALID_VISITOR_NAME);
            return visitor;
          } else {
            return null;
          }
        });

    lenient().when(visitorRepository.findVisitorByName(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(FIRST_VALID_VISITOR_NAME)) {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(FIRST_VISITOR_ID);
            visitor.setEmail(FIRST_VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(FIRST_VALID_VISITOR_NAME);
            return visitor;
          } else if (invocation.getArgument(0).equals(SECOND_VALID_VISITOR_NAME)) {
            Visitor visitor = new Visitor();
            visitor.setMuseumUserId(SECOND_VISITOR_ID);
            visitor.setEmail(SECOND_VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(SECOND_VALID_VISITOR_NAME);
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
            employee.setMuseumUserId(EMPLOYEE_ID);
            return employee;
          } else {
            return null;
          }
        });

    lenient().when(employeeRepository.findEmployeeByName(anyString()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(EMPLOYEE_VALID_NAME)) {
            Employee employee = new Employee();
            employee.setEmail(EMPLOYEE_VALID_EMAIL);
            employee.setMuseumUserId(EMPLOYEE_ID);
            employee.setPassword(VALID_PASSWORD);
            employee.setName(EMPLOYEE_VALID_NAME);
            return employee;
          } else {
            return null;
          }
        });


    lenient().when(employeeRepository.findEmployeeByMuseumUserId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(EMPLOYEE_ID)) {
            Employee employee = new Employee();
            employee.setEmail(EMPLOYEE_VALID_EMAIL);
            employee.setMuseumUserId(EMPLOYEE_ID);
            employee.setPassword(VALID_PASSWORD);
            employee.setName(EMPLOYEE_VALID_NAME);
            return employee;
          } else {
            return null;
          }
        });

    lenient().when(managerRepository.findManagerByMuseumUserId(anyLong()))
        .thenAnswer((InvocationOnMock invocation) -> {
          if (invocation.getArgument(0).equals(MANAGER_ID)) {
            Manager manager = new Manager();
            manager.setEmail(EMPLOYEE_VALID_EMAIL);
            manager.setMuseumUserId(EMPLOYEE_ID);
            manager.setPassword(VALID_PASSWORD);
            manager.setName(EMPLOYEE_VALID_NAME);
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

  /**
   * Test that a visitor can be created successfully.
   * 
   * @author Kevin
   */

  @Test
  public void createValidVisitor() {
    VisitorDto visitorDto = null;
    try {
      visitorDto = new VisitorDto();
      visitorDto.setEmail("lewis@mail.com");
      visitorDto.setPassword("Speed123#$");
      visitorDto.setName("lewis");

      registrationService.createVisitor(visitorDto.getEmail(), visitorDto.getPassword(),
          visitorDto.getName());
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals("lewis@mail.com", visitorDto.getEmail());
    assertEquals("Speed123#$", visitorDto.getPassword());
    assertEquals("lewis", visitorDto.getName());
  }

  /**
   * Test that a visitor cannot be created with null fields.
   * 
   * @author Kevin
   */

  @Test
  public void testCreateVisitorWithNullFields() {
    Visitor visitor = new Visitor();
    visitor.setEmail(null);
    visitor.setPassword(null);
    visitor.setName(null);
    try {
      registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
          visitor.getName());
    } catch (Exception e) {
      assertEquals("Email, password and name must be filled", e.getMessage());

    }
  }

  /**
   * Test that a visitor cannot be created with an invalid email.
   * 
   * @author Kevin
   */

  @Test
  public void testCreateVisitorWithInvalidEmail() {
    Visitor visitor = new Visitor();
    visitor.setEmail(INVALID_EMAIL);
    visitor.setPassword(VALID_PASSWORD);
    visitor.setName(FIRST_VALID_VISITOR_NAME);
    try {
      registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
          visitor.getName());
    } catch (Exception e) {
      assertEquals("Invalid email. ", e.getMessage());
    }
  }

  /**
   * Test that a visitor cannot be created with an invalid password.
   * 
   * @author Kevin
   */

  @Test
  public void testCreateVisitorWithInvalidPassword() {
    Visitor visitor = new Visitor();
    visitor.setEmail(FIRST_VISITOR_VALID_EMAIL);
    visitor.setPassword(INVALID_PASSWORD);
    visitor.setName(FIRST_VALID_VISITOR_NAME);
    try {
      registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
          visitor.getName());
    } catch (Exception e) {
      assertEquals(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
          e.getMessage());
    }
  }

  /**
   * Test that a visitor cannot be created with an invalid email.
   * 
   * @author Kevin
   */

  @Test
  public void testCreateVisitorWithExistingEmail() {
    try {
      Visitor visitor = new Visitor();
      visitor.setEmail(SECOND_VISITOR_VALID_EMAIL);
      visitor.setPassword(VALID_PASSWORD);
      visitor.setName("Lewis Hamilton");
      registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
          visitor.getName());
    } catch (Exception e) {
      assertEquals("An account with the email " + SECOND_VISITOR_VALID_EMAIL + " already exists.",
          e.getMessage());
    }

    try {
      Visitor visitor = new Visitor();
      visitor.setEmail(EMPLOYEE_VALID_EMAIL);
      visitor.setPassword(VALID_PASSWORD);
      visitor.setName(FIRST_VALID_VISITOR_NAME);
      registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
          visitor.getName());
    } catch (Exception e) {
      assertEquals("An account with the email " + EMPLOYEE_VALID_EMAIL + " already exists.",
          e.getMessage());
    }
  }

  /**
   * Test that getting an existing visitor.
   * 
   * @author Kevin
   */

  @Test
  public void testGetVisitorPersonalInformation() {
    Visitor visitor = new Visitor();
    try {
      visitor = registrationService.getVisitorPersonalInformation(FIRST_VISITOR_ID);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(FIRST_VISITOR_VALID_EMAIL, visitor.getEmail());
    assertEquals(FIRST_VALID_VISITOR_NAME, visitor.getName());
    assertEquals(FIRST_VISITOR_ID, visitor.getMuseumUserId());
  }

  /**
   * Test that getting a non-existing visitor.
   * 
   * @author Kevin
   */

  @Test
  public void testGetVisitorPersonalInformationWithInvalidId() {
    try {
      registrationService.getVisitorPersonalInformation(FIRST_VISITOR_ID + 1);
    } catch (Exception e) {
      assertEquals("Account was not found in out system. ", e.getMessage());
    }
  }

  /**
   * Test that editing an existing employee.
   * 
   * @author Kevin
   */

  @Test
  public void testEditVisitor() {
    Visitor visitor = new Visitor();
    String newPassword = "#BrazilGP2022";
    String newEmail = "pierre.gasly@mail.com";
    String newUserName = "Alonso";

    try {
      visitor = registrationService.editVisitorInformation(FIRST_VISITOR_ID, newEmail,
          VALID_PASSWORD, newPassword, newUserName);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(newPassword, visitor.getPassword());
    assertEquals(newEmail, visitor.getEmail());
    assertEquals(newUserName, visitor.getName());
  }

  /**
   * Test that editing a non-existing employee.
   * 
   * @author Kevin
   */

  @Test
  public void testEditVisitorWithInvalidId() {
    try {
      registrationService.editVisitorInformation(2, null, null, null, null);
    } catch (Exception e) {
      assertEquals("Account was not found in the system. ", e.getMessage());
    }
  }

  /**
   * Test that editing an employee with an invalid email.
   * 
   * @author Kevin
   */

  @Test
  public void testEditVisitorWithInvalidEmail() {
    try {
      registrationService.editVisitorInformation(FIRST_VISITOR_ID, INVALID_EMAIL, null, null, null);
    } catch (Exception e) {
      assertEquals("Invalid email. ", e.getMessage());
    }
  }

  /**
   * Test that editing an employee to an email that already exists.
   * 
   * @author Kevin
   */

  @Test
  public void testEditVisitorExistingEmail() {
    try {
      registrationService.editVisitorInformation(FIRST_VISITOR_ID, SECOND_VISITOR_VALID_EMAIL, null,
          null, null);
    } catch (Exception e) {
      assertEquals("An account with the email " + SECOND_VISITOR_VALID_EMAIL + " already exists.",
          e.getMessage());
    }
  }

  /**
   * Test that editing an employee with an invalid new password.
   * 
   * @author Kevin
   */

  @Test
  public void testEditVisitorWithInvalidNewPassword() {
    try {
      registrationService.editVisitorInformation(FIRST_VISITOR_ID, null, VALID_PASSWORD,
          INVALID_PASSWORD, null);
    } catch (Exception e) {
      assertEquals(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
          e.getMessage());
    }
  }

  /**
   * Test that editing an employee with an invalid old password.
   * 
   * @author Kevin
   */

  @Test
  public void testEditVisitorWithWrongOldPassword() {
    try {
      registrationService.editVisitorInformation(FIRST_VISITOR_ID, null, INVALID_PASSWORD,
          VALID_PASSWORD, null);
    } catch (Exception e) {
      assertEquals("Old password incorrect", e.getMessage());
    }
  }

  /**
   * Test that create new employee.
   * 
   * @author Kevin
   */

  @Test
  public void testCreateEmployee() {
    Employee employee = null;
    try {
      employee = new Employee();
      employee.setName("Lando Norris");
      employee = registrationService.createEmployee(employee.getName());
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals("Lando Norris", employee.getName());
    assertEquals("lando.norris@museum.ca", employee.getEmail());
  }

  /**
   * Test that create employee with invalid name.
   * 
   * @author Kevin
   */

  @Test
  public void testCreateEmployeeWithInvalidName() {
    try {
      Employee employee = new Employee();
      employee.setName("LandoNorris");
      registrationService.createEmployee(employee.getName());
    } catch (Exception e) {
      assertEquals("Name must be in the format of Firstname Lastname", e.getMessage());
    }
  }

  /**
   * Test that generate employee email.
   * 
   * @author Kevin
   */

  @Test
  public void testCreateEmployeeEmail() {
    Employee employee = null;
    try {
      employee = new Employee();
      employee.setName("Sergio Perez");
      employee = registrationService.createEmployee(employee.getName());
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals("sergio.perez1@museum.ca", employee.getEmail());
  }

  /**
   * Test that edit employee.
   * 
   * @author Kevin
   */

  @Test
  public void testEditEmployee() {
    Employee employee = null;
    String newPassword = "#BrazilGP2022";

    try {
      employee =
          registrationService.editEmployeeInformation(EMPLOYEE_ID, VALID_PASSWORD, newPassword);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(newPassword, employee.getPassword());
  }

  /**
   * Test that get employee information.
   * 
   * @author Kevin
   */

  @Test
  public void testGetEmployeePersonalInformation() {
    Employee employee = new Employee();
    try {
      employee = registrationService.getEmployeePersonalInformation(EMPLOYEE_ID);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(EMPLOYEE_VALID_EMAIL, employee.getEmail());
    assertEquals(EMPLOYEE_VALID_NAME, employee.getName());
    assertEquals(EMPLOYEE_ID, employee.getMuseumUserId());

  }

  /**
   * Test that get employee information with invalid id.
   * 
   * @author Kevin
   */

  @Test
  public void testGetEmployeePersonalInformationWithInvalidId() {
    try {
      registrationService.getEmployeePersonalInformation(EMPLOYEE_ID + 1);
    } catch (Exception e) {
      assertEquals("Account was not found in out system. ", e.getMessage());
    }
  }

  /**
   * Test that edit employee with invalid id.
   * 
   * @author Kevin
   */

  @Test
  public void testEditEmployeeWithInvalidId() {
    try {
      registrationService.editEmployeeInformation(EMPLOYEE_ID + 1, null, null);
    } catch (Exception e) {
      assertEquals("Account was not found in the system. ", e.getMessage());
    }
  }

  /**
   * Test that edit employee with invalid new password.
   * 
   * @author Kevin
   */

  @Test
  public void testEditEmployeeWithInvalidNewPassword() {
    try {
      registrationService.editEmployeeInformation(EMPLOYEE_ID, VALID_PASSWORD, INVALID_PASSWORD);
    } catch (Exception e) {
      assertEquals(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
          e.getMessage());
    }
  }

  /**
   * Test that edit manager
   * 
   * @author Kevin
   */

  @Test
  public void testEditManager() {
    Manager manager = null;
    String newPassword = "#BrazilGP2022";

    try {
      manager = registrationService.editManagerInformation(MANAGER_ID, VALID_PASSWORD, newPassword);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    assertEquals(newPassword, manager.getPassword());
  }

  @Test
  /**
   * Test that edit manager with wrong password
   * 
   * @author Kevin
   */
  public void testEditManagerWithWrongOldPassword() {
    try {
      registrationService.editManagerInformation(MANAGER_ID, INVALID_PASSWORD, VALID_PASSWORD);
    } catch (Exception e) {
      assertEquals("Old password incorrect", e.getMessage());
    }
  }

  @Test
  /**
   * Test that edit manager with invalid new password
   * 
   * @author Kevin
   */
  public void testEditManagerWithInvalidNewPassword() {
    try {
      registrationService.editManagerInformation(MANAGER_ID, VALID_PASSWORD, INVALID_PASSWORD);
    } catch (Exception e) {
      assertEquals(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
          e.getMessage());
    }
  }
}


