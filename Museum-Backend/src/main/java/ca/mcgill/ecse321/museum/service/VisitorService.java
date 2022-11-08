package ca.mcgill.ecse321.museum.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.Visitor;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

@Service
public class VisitorService {
    @Autowired
    private VisitorRepository visitorRepository;

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
    public Visitor getVisitorPersonalInformation(long visitorId) throws Exception {
        Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorId);
        if (visitor == null) {
            throw new Exception("Account was not found in out system. ");
        }
        return visitor;
    }

    @Transactional
    public Visitor editInformation(long visitorId, String email, String password, String name)
            throws Exception {
        Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorId);
        if (visitor == null) {
            throw new Exception("Account was not found in the system. ");
        }

        if (email != null) {
            if (visitorRepository.findVisitorByEmail(email) != null) {
                throw new Exception("An account with the email " + email + " already exists.");
            } else if (!emailValidityChecker(email)) {
                throw new Exception("Invalid email. ");
            } else {
                visitor.setEmail(email);
            }
        }
        if (password != null) {
            if (!passwordValidityChecker(password)) {
                throw new Exception(
                        "Password must contain at least 8 characters, 1 uppercase, 1 lowercase, 1 number and 1 special character. ");
            } else {
                visitor.setPassword(password);
            }
        }

        if (name != null) {
            Visitor visitorWithNewName = visitorRepository.findVisitorByName(name);
            if (visitorWithNewName != null) {
                throw new Exception(
                        "Please choose another username. " + name + " already exists. ");
            }
            visitor.setName(name);
        }

        visitorRepository.save(visitor);
        return visitor;

    }


    public boolean passwordValidityChecker(String password) {
        String passwordPattern =
                "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
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
}
