package ca.mcgill.ecse321.museum.controller.utilities;


import javax.servlet.http.HttpSession;

public class AuthenticationUtility {
  /**
   * Method to check if the user is a manager
   * 
   * @param role - String
   * @return if the user is a manager
   * @author Kevin
   */
  public static boolean isManager(HttpSession session) {
    if (session.getAttribute("role") != null) {
      if (session.getAttribute("role").equals("manager")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to check if the user is an employee
   * 
   * @param role - String
   * @return if the user is an employee
   * @author Kevin
   */
  public static boolean isStaffMember(HttpSession session) {
    if (session.getAttribute("role") != null) {
      if (session.getAttribute("role").equals("employee")
          || session.getAttribute("role").equals("manager")) {
        return true;
      }
    }
    return false;
  }

  public static boolean isEmployee(HttpSession session) {
    if (session.getAttribute("role") != null) {
      if (session.getAttribute("role").equals("employee")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to check if the user is a visitor
   * 
   * @param role - String
   * @return if the user is a visitor
   * @author Kevin
   */
  public static boolean isVisitor(HttpSession session) {
    if (session.getAttribute("role") != null) {
      if (session.getAttribute("role").equals("visitor")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to check if user is a museum user
   * 
   * @param role - String
   * @return if the user is logged in
   * @author Kevin
   */
  public static boolean isMuseumUser(HttpSession session) {
    if (session.getAttribute("role") != null) {
      if (session.getAttribute("role").equals("visitor")
          || session.getAttribute("role").equals("employee")
          || session.getAttribute("role").equals("manager")) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to check if the user is logged in
   * 
   * @param role - String
   * @return if the user is logged in
   * @author Kevin
   */
  public static boolean isLoggedIn(HttpSession session) {
    if (session.getAttribute("user_id") != null) {
      return true;
    }
    return false;
  }


  /**
   * Method to compare id
   * 
   * @param role - String
   * @return the user id
   * @author Kevin
   */
  public static boolean checkUserId(HttpSession session, long id) {
    if (session.getAttribute("user_id") != null) {
      return session.getAttribute("user_id").equals(id);
    }
    return false;
  }


}
