package ca.mcgill.ecse321.museum.service;


import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.MuseumUser;
import ca.mcgill.ecse321.museum.model.Visitor;

@Service
public class AuthenticationService {
    @Autowired
    private VisitorRepository visitorRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Transactional
    public MuseumUser authenticateUser(String email, String password) throws Exception {
        if (email == null || password == null) {
            new Exception("Email and password must be filled when logging in.");
        }

        Visitor visitor = visitorRepository.findVisitorByEmail(email);
        Manager manager = managerRepository.findManagerByEmail(email);
        Employee employee = employeeRepository.findEmployeeByEmail(email);

        if (visitor != null) {
            if (visitor.getPassword().equals(password)) {
                return visitor;
            } else {
                throw new Exception("Incorrect password.");
            }
        } else if (manager != null) {
            if (manager.getPassword().equals(password)) {
                return manager;
            } else {
                throw new Exception("Incorrect password.");
            }
        } else if (employee != null) {
            if (employee.getPassword().equals(password)) {
                return employee;
            } else {
                throw new Exception("Incorrect password.");
            }
        }

        throw new Exception("No account with the email " + email + " exists.");
    }
}
