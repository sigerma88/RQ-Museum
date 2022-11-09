package ca.mcgill.ecse321.museum.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.museum.dao.EmployeeRepository;
import ca.mcgill.ecse321.museum.model.Employee;

@Service
public class EmployeeService {

  @Autowired
  EmployeeRepository employeeRepository;  

  /**
   * Method to get an employee by their id
   * 
   * @param id - long
   * @return employee
   * @author Siger
   */
  @Transactional
  public Employee getEmployee(long id) {
    return employeeRepository.findEmployeeByMuseumUserId(id);
  }

  /**
   * Method to get all the employees in the database
   * Allows the manager to view the list of employees
   * 
   * @return List of all employees
   * @author Siger
   */
  @Transactional
  public List<Employee> getAllEmployees() {
    return toList(employeeRepository.findAll());
  }

  /**
   * Method to delete an employee from the database by their id
   * Allows the manager to delete an employee
   * 
   * @param id - long
   * @return if the employee was deleted (success)
   * @author Siger
   */
  @Transactional
  public boolean deleteEmployee(long id) {
    Employee employee = employeeRepository.findEmployeeByMuseumUserId(id);
    if (employee == null) {
      throw new IllegalArgumentException("Employee does not exist");
    }

    // Delete employee
    employeeRepository.deleteEmployeeByMuseumUserId(id);

    // Check if employee was deleted
    return getEmployee(id) == null;
  }

  /**
   * Method to convert an Iterable to a List
   * 
   * @param iterable - Iterable
   * @return List
   * @author From tutorial notes
   */
  private <T> List<T> toList(Iterable<T> iterable){
    List<T> resultList = new ArrayList<T>();
    for (T t : iterable) {
      resultList.add(t);
    }
    return resultList;
  }
}