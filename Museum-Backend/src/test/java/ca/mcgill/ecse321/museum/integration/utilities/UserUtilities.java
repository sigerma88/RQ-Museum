package ca.mcgill.ecse321.museum.integration.utilities;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import ca.mcgill.ecse321.museum.controller.utilities.DtoUtility;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.EmployeeDto;
import ca.mcgill.ecse321.museum.dto.ManagerDto;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;

public class UserUtilities {

    /**
     * Create an visitorDto
     *
     * @param visitor - the visitorDto to be created
     * @return the created visitorDto
     * @author Kevin
     */

    public static VisitorDto createVisitorDto(Visitor visitor) {
        return DtoUtility.convertToDto(visitor);
    }

    /**
     * Create a visitor
     *
     * @param name - name of the visitor
     * @param email - email of the visitor
     * @param password - password of the visitor
     * @return Visitor - the visitor created
     * @author Kevin
     */

    public static Visitor createVisitor(String name, String email, String password) {
        Visitor visitor = new Visitor();
        visitor.setEmail(email);
        visitor.setPassword(password);
        visitor.setName(name);

        return visitor;
    }

    /**
     * Create employeeDto
     *
     * @param employee - employee
     * @return employeeDto - the employeeDto created
     * @author Kevin
     */

    public static EmployeeDto createEmployeeDto(Employee employee) {
        return DtoUtility.convertToDto(employee);
    }

    /**
     * Create a visitor and login
     *
     * @param name - name of visitor to create and login
     * @param email - email of visitor to create and login
     * @param password - password of visitor to create and login
     * @return the logged in visitor
     * @author Kevin
     */

    public static Employee createEmployee(String name, String email, String password) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setEmail(email);
        employee.setPassword(password);
        employee.setSchedule(new Schedule());

        return employee;
    }

    /**
     * Create a managerDTO
     *
     * @param manager - the manager to be created
     * @return ManagerDto
     * @author Kevin
     */

    public static ManagerDto createManagerDto(Manager manager) {
        return DtoUtility.convertToDto(manager);
    }

    /**
     * Create a manager
     *
     * @param name - name of the manager
     * @param email - email of the manager
     * @param password - password of the manager
     * @return Manager - the manager created
     * @author Kevin
     */

    public static Manager createManager(String name, String email, String password) {
        Manager manager = new Manager();
        manager.setName(name);
        manager.setEmail(email);
        manager.setPassword(password);

        return manager;
    }
}
