package ca.mcgill.ecse321.museum.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.Employee;
import ca.mcgill.ecse321.museum.model.Schedule;
import ca.mcgill.ecse321.museum.model.Visitor;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Matcher;

@Service
public class RegistrationService {
  @Autowired
  private VisitorRepository visitorRepository;

  @Autowired
  private EmployeeRepository employeeRepository;

  @Transactional
  public Visitor createVisitor(String email, String password, String name) throws Exception {
    if (email == null || password == null || name == null) {
      throw new Exception("Email, password and name must be filled");
    }

    if (!emailValidityChecker(email)) {
      throw new Exception("Invalid email. ");

    }

    if (!passwordValidityChecker(password)) {
      throw new Exception(
          "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ");
    }

    if (visitorRepository.findVisitorByEmail(email) != null) {
      throw new Exception("An account with the email " + email + " already exists.");
    }

    if (visitorRepository.findVisitorByName(name) != null) {
      throw new Exception("Please choose another username. " + name + " already exists. ");
    }

    Visitor visitor = new Visitor();
    visitor.setEmail(email);
    visitor.setPassword(password);
    visitor.setName(name);

    visitorRepository.save(visitor);

    return visitor;
  }

  @Transactional
  public Visitor getVisitorPersonalInformation(long museumUserId) throws Exception {
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(museumUserId);
    // Employee employee = employeeRepository.findEmployeeByMuseumUserId(museumUserId);
    if (visitor == null) {
      throw new Exception("Account was not found in out system. ");
    }
    return visitor;
  }

  @Transactional
  public Visitor editInformation(long visitorId, String email, String newPassword,
      String oldPassword, String name) throws Exception {
    Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorId);

    if (visitor == null) {
      throw new Exception("Account was not found in the system. ");
    }

    String visitorCurrentPassword = visitor.getPassword();

    if (email != null) {
      if (visitorRepository.findVisitorByEmail(email) != null) {
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
        } else {
          visitor.setPassword(newPassword);
        }
      } else {
        throw new Exception("Old password incorrect");
      }
    }

    if (name != null) {
      Visitor visitorWithNewName = visitorRepository.findVisitorByName(name);
      if (visitorWithNewName != null) {
        throw new Exception("Please choose another username. " + name + " already exists. ");
      }
      visitor.setName(name);
    }

    visitorRepository.save(visitor);
    return visitor;
  }

  public Employee registerEmployee(String name) throws Exception {
    if (name == null) {
      throw new Exception("Name must be filled");
    }

    if (employeeRepository.findVisitorByName(name) != null) {
      throw new Exception("Please choose another username. " + name + " already exists. ");
    }

    String email = name + "@museum.com";
    String password = passwordGenerator();

    Employee employee = new Employee();
    employee.setEmail(email);
    employee.setPassword(password);
    employee.setName(name);
    employee.setSchedule(new Schedule());

    employeeRepository.save(employee);

    return employee;
  }


  public boolean passwordValidityChecker(String password) {
    String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
    Pattern pattern = Pattern.compile(passwordPattern);
    Matcher passwordMatcher = pattern.matcher(password);
    return passwordMatcher.matches();

  }

  public boolean emailValidityChecker(String email) {
    String expectedPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

    Pattern pattern = Pattern.compile(expectedPattern);
    Matcher emailMatcher = pattern.matcher(email);
    return emailMatcher.matches();
  }

  public String passwordGenerator() {
    final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    final char[] uppercase = "ABCDEFGJKLMNPRSTUVWXYZ".toCharArray();
    final char[] numbers = "0123456789".toCharArray();
    final char[] symbols = "^$?!@#%&".toCharArray();
    final char[] allAllowed =
        "abcdefghijklmnopqrstuvwxyzABCDEFGJKLMNPRSTUVWXYZ0123456789^$?!@#%&".toCharArray();

    ArrayList<Character> password = new ArrayList<Character>();
    password.add(lowercase[getRandomNumber(0, lowercase.length)]);
    password.add(lowercase[getRandomNumber(0, uppercase.length)]);
    password.add(lowercase[getRandomNumber(0, numbers.length)]);
    password.add(lowercase[getRandomNumber(0, symbols.length)]);

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

  public int getRandomNumber(int min, int max) {
    return (int) ((Math.random() * (max - min)) + min);
  }
}
