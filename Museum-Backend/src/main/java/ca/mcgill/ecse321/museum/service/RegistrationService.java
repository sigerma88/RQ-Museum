package ca.mcgill.ecse321.museum.service;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.ManagerRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Manager;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Business logic for registrationController
 *
 * @author Kevin
 */

@Service
public class RegistrationService {

  @Autowired
  private VisitorRepository visitorRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ManagerRepository managerRepository;

  /**
   * Register a new visitor
   *
   * @param email - email of visitor
   * @param password - password of visitor
   * @param name - name of visitor
   * @return Visitor - the visitor that was created
   * @author Kevin
   */

  @Transactional
  public Visitor createVisitor(String email, String password, String name) throws Exception {
    if (email == null || password == null || name == null) {
      throw new Exception("Email, password and name must be filled");
    }

    if (!emailValidityChecker(email)) {
      throw new Exception("Invalid email. ");
    }

    if (!nameChecker(name)) {
      throw new Exception("Invalid name. ");
    }

    if (!passwordValidityChecker(password)) {
      throw new Exception(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ");
    }

    if (checkIfEmailExists(email)) {
      throw new Exception("An account with the email " + email + " already exists.");
    }

    Visitor visitor = new Visitor();
    visitor.setEmail(email);
    visitor.setPassword(password);
    visitor.setName(name);

    visitorRepository.save(visitor);

    return visitor;
  }

  /**
   * Get visitor personal information
   *
   * @param museumUserId - id of visitor
   * @return Visitor - the visitor that was found
   * @author Kevin
   */

  @Transactional
  public Visitor getVisitorPersonalInformation(long museumUserId) throws Exception {
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(museumUserId);
    if (visitor == null) {
      throw new Exception("Account was not found in out system. ");
    }
    return visitor;
  }

  /**
   * Get employee personal information
   *
   * @param museumUserId - id of museumUser
   * @return Employee - the employee that was found
   * @author Kevin
   */

  public Employee getEmployeePersonalInformation(long museumUserId) throws Exception {
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(museumUserId);
    if (employee == null) {
      throw new Exception("Account was not found in out system. ");
    }

    return employee;
  }

  /**
   * Update visitor personal information
   *
   * @param visitorId - id of visitor
   * @param email - new email of visitor
   * @param oldPassword - old password of visitor
   * @param newPassword - new password of visitor
   * @param name - new name of visitor
   * @return Visitor - the visitor that was updated
   * @author Kevin
   */

  @Transactional
  public Visitor editVisitorInformation(long visitorId, String email, String oldPassword,
      String newPassword, String name) throws Exception {
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorId);

    if (visitor == null) {
      throw new Exception("Account was not found in the system. ");
    }


    String visitorCurrentPassword = visitor.getPassword();

    if (email != null) {
      if (checkIfEmailExists(email)) {
        throw new Exception("An account with the email " + email + " already exists.");
      } else if (!emailValidityChecker(email)) {
        throw new Exception("Invalid email. ");
      } else {
        visitor.setEmail(email);
      }
    }

    if (newPassword != null) {
      if (visitorCurrentPassword.equals(oldPassword)) {
        if (!passwordValidityChecker(newPassword)) {
          throw new Exception(
              "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ");
        }

        visitor.setPassword(newPassword);

      } else {
        throw new Exception("Old password incorrect");
      }
    }

    if (name != null) {
      if (!nameChecker(name)) {
        throw new Exception("Invalid name. ");
      } else {
        visitor.setName(name);
      }
    }

    visitorRepository.save(visitor);
    return visitor;
  }


  /**
   * Register a new employee
   *
   * @param name - name of employee
   * @return Employee - the employee that was created
   * @author Kevin
   */

  public Employee createEmployee(String name) throws Exception {

    if (name == null) {
      throw new Exception("Name must be filled");
    }

    String employeeName = name.trim();
    String[] fullName = name.trim().split(" ");

    if (fullName.length < 2) {
      throw new Exception("Name must be in the format of Firstname Lastname");
    }

    String names = String.join(".", fullName);
    String email = names.toLowerCase() + "@museum.ca";

    if (checkIfEmailExists(email)) {
      boolean isEmailSet = false;
      int count = 0;
      while (!isEmailSet) {
        count += 1;
        email = names.toLowerCase() + count + "@museum.ca";
        if (!checkIfEmailExists(email)) {
          isEmailSet = true;
        }
      }
    }

    if (!nameChecker(employeeName)) {
      throw new Exception("Invalid name. ");
    }



    String password = passwordGenerator();
    Employee employee = new Employee();
    employee.setEmail(email);
    employee.setPassword(password);
    employee.setName(employeeName);
    employee.setSchedule(new Schedule());

    employeeRepository.save(employee);

    return employee;
  }

  /**
   * Edit employee information
   *
   * @param oldPassword - old password of employee
   * @param newPassword - new password of employee
   * @return Employee - the employee that was updated
   * @author Kevin
   */

  public Employee editEmployeeInformation(long employeeId, String oldPassword, String newPassword)
      throws Exception {
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(employeeId);

    if (employee == null) {
      throw new Exception("Account was not found in the system. ");
    }

    String currentPassword = employee.getPassword();
    if (oldPassword == null || newPassword == null) {
      throw new Exception("Old password and new password must be filled");
    }

    if (!currentPassword.equals(oldPassword)) {
      throw new Exception("Old password incorrect");
    }

    if (passwordValidityChecker(newPassword)) {
      employee.setPassword(newPassword);
    } else {
      throw new Exception(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ");
    }

    employeeRepository.save(employee);
    return employee;
  }


  /**
   * Check password validity
   *
   * @param password - password to be checked
   * @return boolean - true if password is valid, false otherwise
   * @author Kevin
   */

  public boolean passwordValidityChecker(String password) {
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    Pattern pattern = Pattern.compile(passwordPattern);
    Matcher passwordMatcher = pattern.matcher(password);
    return passwordMatcher.matches();

  }


  /**
   * Check email validity
   *
   * @param email - email to be checked
   * @return boolean - true if email is valid, false otherwise
   * @author Kevin
   */

  public boolean emailValidityChecker(String email) {
    String expectedPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    Pattern pattern = Pattern.compile(expectedPattern);
    Matcher emailMatcher = pattern.matcher(email);
    return emailMatcher.matches();
  }

  /**
   * Edit manager information
   *
   * @param id - id of manager
   * @param oldPassword - old password of manager
   * @param newPassword - new password of manager
   * @return Manager - the manager that was updated
   * @author Kevin
   */

  public Manager editManagerInformation(long id, String oldPassword, String newPassword)
      throws Exception {
    Manager manager = managerRepository.findManagerByMuseumUserId(id);

    if (manager == null) {
      throw new Exception("Account was not found in the system. ");
    }

    String currentPassword = manager.getPassword();
    if (oldPassword == null || newPassword == null) {
      throw new Exception("Old password and new password must be filled");
    }

    if (!currentPassword.equals(oldPassword)) {
      throw new Exception("Old password incorrect");
    }

    if (passwordValidityChecker(newPassword)) {
      manager.setPassword(newPassword);
    } else {
      throw new Exception(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ");
    }

    managerRepository.save(manager);
    return manager;
  }


  /**
   * Generate a random password
   *
   * @return String - the generated password
   * @author Kevin
   */

  public String passwordGenerator() {
    final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
    final char[] numbers = "0123456789".toCharArray();
    final char[] symbols = "!@#$%^&*()_+{}|'[]:>?</".toCharArray();
    final char[] allAllowed =
        "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789^$?!@#%&".toCharArray();

    ArrayList<Character> password = new ArrayList<Character>();
    password.add(lowercase[getRandomNumber(0, lowercase.length)]);
    password.add(uppercase[getRandomNumber(0, uppercase.length)]);
    password.add(numbers[getRandomNumber(0, numbers.length)]);
    password.add(symbols[getRandomNumber(0, symbols.length)]);

    for (int i = 0; i < 4; i++) {
      password.add(allAllowed[getRandomNumber(0, allAllowed.length)]);
    }

    Collections.shuffle(password);

    String employeePassword = "";

    for (char letter : password) {
      employeePassword += letter;
    }

    return employeePassword;
  }


  /**
   * Get a random number
   *
   * @param min - minimum number
   * @param max - maximum number
   * @return int - the random number
   * @author Kevin
   */

  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }


  /**
   * Check validity of name
   *
   * @param name - name to be checked
   * @return boolean - true if name is valid, false otherwise
   * @author Kevin
   */

  public boolean nameChecker(String name) {
    String namePattern = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
    Pattern pattern = Pattern.compile(namePattern);
    Matcher nameMatcher = pattern.matcher(name);
    return nameMatcher.matches();
  }

  /**
   * Check if email exists
   *
   * @param email - email to be checked
   * @return boolean - true if email exists, false otherwise
   * @author Kevin
   */
  public boolean checkIfEmailExists(String email) throws Exception {
    Visitor visitor = visitorRepository.findVisitorByEmail(email);
    Employee employee = employeeRepository.findEmployeeByEmail(email);
    Manager manager = managerRepository.findManagerByEmail(email);

    return visitor != null || employee != null || manager != null;
  }

}
