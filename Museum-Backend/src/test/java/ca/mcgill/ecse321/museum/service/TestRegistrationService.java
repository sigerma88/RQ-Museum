package ca.mcgill.ecse321.museum.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
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

    private static final String VISITOR_VALID_EMAIL = "max@gmail.com";
    private static final String VALID_VISITOR_NAME = "max";
    private static final long VISITOR_ID = 1;

    private static final String VALID_PASSWORD = "Speed123#$";

    private static final String INVALID_PASSWORD = "Speed123";
    private static final String INVALID_EMAIL = "lewisgmailcom";


    private static final long EMPLOYEE_ID = 2;
    private static final String EMPLOYEE_VALID_EMAIL = "sergio.perez@museum.ca";
    private static final String EMPLOYEE_VALID_NAME = "Sergio Perez";
    private static final String ADMIN_VALID_EMAIL = "admin@museum.com";

    @BeforeEach
    public void setMockOutput() {
        lenient().when(visitorRepository.findVisitorByMuseumUserId(anyLong()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(VISITOR_ID)) {
                        Visitor visitor = new Visitor();
                        visitor.setMuseumUserId(VISITOR_ID);
                        visitor.setEmail(VISITOR_VALID_EMAIL);
                        visitor.setPassword(VALID_PASSWORD);
                        visitor.setName(VALID_VISITOR_NAME);
                        return visitor;
                    } else {
                        return null;
                    }
                });

        lenient().when(visitorRepository.findVisitorByEmail(anyString()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(VISITOR_VALID_EMAIL)) {
                        Visitor visitor = new Visitor();
                        visitor.setMuseumUserId(VISITOR_ID);
                        visitor.setEmail(VISITOR_VALID_EMAIL);
                        visitor.setPassword(VALID_PASSWORD);
                        visitor.setName(VALID_VISITOR_NAME);
                        return visitor;
                    } else {
                        return null;
                    }
                });

        lenient().when(visitorRepository.findVisitorByName(anyString()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(VALID_VISITOR_NAME)) {
                        Visitor visitor = new Visitor();
                        visitor.setMuseumUserId(VISITOR_ID);
                        visitor.setEmail(VISITOR_VALID_EMAIL);
                        visitor.setPassword(VALID_PASSWORD);
                        visitor.setName(VALID_VISITOR_NAME);
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

        lenient().when(employeeRepository.findEmployeeByName(anyString()))
                .thenAnswer((InvocationOnMock invocation) -> {
                    if (invocation.getArgument(0).equals(EMPLOYEE_VALID_NAME)) {
                        Employee employee = new Employee();
                        employee.setEmail(EMPLOYEE_VALID_EMAIL);
                        employee.setPassword(VALID_PASSWORD);
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
    }

    @Test
    public void createValidVisitor() {
        try {
            VisitorDto visitorDto = new VisitorDto();
            visitorDto.setEmail("lewis@mail.com");
            visitorDto.setPassword("Speed123#$");
            visitorDto.setName("lewis");

            registrationService.createVisitor(visitorDto.getEmail(), visitorDto.getPassword(),
                    visitorDto.getName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void createVisitorWithNullFields() {
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

    @Test
    public void createVisitorWithInvalidEmail() {
        Visitor visitor = new Visitor();
        visitor.setEmail(INVALID_EMAIL);
        visitor.setPassword(VALID_PASSWORD);
        visitor.setName(VALID_VISITOR_NAME);
        try {
            registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
                    visitor.getName());
        } catch (Exception e) {
            assertEquals("Invalid email. ", e.getMessage());
        }
    }

    @Test
    public void createVisitorWithInvalidPassword() {
        Visitor visitor = new Visitor();
        visitor.setEmail(VISITOR_VALID_EMAIL);
        visitor.setPassword(INVALID_PASSWORD);
        visitor.setName(VALID_VISITOR_NAME);
        try {
            registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
                    visitor.getName());
        } catch (Exception e) {
            assertEquals(
                    "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
                    e.getMessage());
        }
    }

    @Test
    public void testRegisteringWithExistingEmail() {
        try {
            Visitor visitor = new Visitor();
            visitor.setEmail(VISITOR_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(VALID_VISITOR_NAME);
            registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
                    visitor.getName());
        } catch (Exception e) {
            assertEquals("An account with the email " + VISITOR_VALID_EMAIL + " already exists.",
                    e.getMessage());
        }

        try {
            Visitor visitor = new Visitor();
            visitor.setEmail(EMPLOYEE_VALID_EMAIL);
            visitor.setPassword(VALID_PASSWORD);
            visitor.setName(VALID_VISITOR_NAME);
            registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
                    visitor.getName());
        } catch (Exception e) {
            assertEquals("An account with the email " + EMPLOYEE_VALID_EMAIL + " already exists.",
                    e.getMessage());
        }
    }

    // @Test
    // public void testRegisterVisitorWithExistingUsername() {
    // try {
    // Visitor visitor = new Visitor();
    // visitor.setEmail("test@mail.com");
    // visitor.setPassword(VALID_PASSWORD);
    // visitor.setName(FIRST_VALID_VISITOR_NAME);
    // registrationService.createVisitor(visitor.getEmail(), visitor.getPassword(),
    // visitor.getName());
    // } catch (Exception e) {
    // assertEquals("Please choose another username. " + FIRST_VALID_VISITOR_NAME
    // + " already exists. ", e.getMessage());
    // }
    // }

    @Test
    public void testGetVisitorPersonalInformation() {
        try {
            registrationService.getVisitorPersonalInformation(VISITOR_ID);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetVisitorPersonalInformationWithInvalidId() {
        try {
            registrationService.getVisitorPersonalInformation(VISITOR_ID + 1);
        } catch (Exception e) {
            assertEquals("Account was not found in out system. ", e.getMessage());
        }
    }

    @Test
    public void testEditVisitor() {
        try {
            String newPassword = "#BrazilGP2022";
            String newEmail = "pierre.gasly@mail.com";
            String newUserName = "Alonso";
            registrationService.editVisitorInformation(VISITOR_ID, newEmail, VALID_PASSWORD,
                    newPassword, newUserName);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testEditVisitorWithInvalidId() {
        try {
            registrationService.editVisitorInformation(2, null, null, null, null);
        } catch (Exception e) {
            assertEquals("Account was not found in the system. ", e.getMessage());
        }
    }

    @Test
    public void testEditVisitorWithInvalidEmail() {
        try {
            registrationService.editVisitorInformation(VISITOR_ID, INVALID_EMAIL, null, null, null);
        } catch (Exception e) {
            assertEquals("Invalid email. ", e.getMessage());
        }
    }

    @Test
    public void testEditVisitorExistingEmail() {
        try {
            registrationService.editVisitorInformation(VISITOR_ID, VISITOR_VALID_EMAIL, null, null,
                    null);
        } catch (Exception e) {
            assertEquals("An account with the email " + VISITOR_VALID_EMAIL + " already exists.",
                    e.getMessage());
        }
    }

    @Test
    public void testEditVisitorWithInvalidNewPassword() {
        try {
            registrationService.editVisitorInformation(VISITOR_ID, null, VALID_PASSWORD,
                    INVALID_PASSWORD, null);
        } catch (Exception e) {
            assertEquals(
                    "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
                    e.getMessage());
        }
    }

    @Test
    public void testEditVisitorWithWrongOldPassword() {
        try {
            registrationService.editVisitorInformation(VISITOR_ID, null, INVALID_PASSWORD,
                    VALID_PASSWORD, null);
        } catch (Exception e) {
            assertEquals("Old password incorrect", e.getMessage());
        }
    }

    // @Test
    // public void testEditVisitorWithExistingUsername() {
    // try {
    // registrationService.editVisitorInformation(FIRST_VISITOR_ID, null, null, null,
    // FIRST_VALID_VISITOR_NAME);
    // registrationService.editVisitorInformation(FIRST_VISITOR_ID + 1, null, null, null,
    // EMPLOYEE_VALID_NAME);
    // } catch (Exception e) {
    // assertEquals("Please choose another username. " + FIRST_VALID_VISITOR_NAME
    // + " already exists. ", e.getMessage());
    // }
    // }

    @Test
    public void testCreateEmployee() {
        try {
            Employee employee = new Employee();
            employee.setName("Lando Norris");
            registrationService.createEmployee(employee.getName());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

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

    @Test
    public void testEditEmployee() {
        try {
            String newPassword = "#BrazilGP2022";
            registrationService.editEmployeeInformation(EMPLOYEE_ID, VALID_PASSWORD, newPassword);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetEmployeePersonalInformation() {
        try {
            registrationService.getEmployeePersonalInformation(EMPLOYEE_ID);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testGetEmployeePersonalInformationWithInvalidId() {
        try {
            registrationService.getEmployeePersonalInformation(EMPLOYEE_ID + 1);
        } catch (Exception e) {
            assertEquals("Account was not found in out system. ", e.getMessage());
        }
    }


    @Test
    public void testEditEmployeeWithInvalidId() {
        try {
            registrationService.editEmployeeInformation(EMPLOYEE_ID + 1, null, null);
        } catch (Exception e) {
            assertEquals("Account was not found in the system. ", e.getMessage());
        }
    }

    @Test
    public void testEditEmployeeWithInvalidNewPassword() {
        try {
            registrationService.editEmployeeInformation(EMPLOYEE_ID, VALID_PASSWORD,
                    INVALID_PASSWORD);
        } catch (Exception e) {
            assertEquals(
                    "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ",
                    e.getMessage());
        }
    }
}


