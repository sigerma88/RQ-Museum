package ca.mcgill.ecse321.museum.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.model.MuseumUser;

public class AuthenticationService {
    @Autowired
    private VisitorRepository visitorRepository;

    @Transactional
    public boolean authenticateUser(String email, String password) throws Exception {
        if (email == null || password == null) {
            new Exception("Email and password must be filled when logging in.");
        }

        MuseumUser visitor = visitorRepository.findVisitorByEmail(email);
        if (visitor == null) {
            throw new Exception("Email does not exist on our system.");
        }

        String visitorPassword = visitor.getPassword();
        return visitorPassword.equals(password);
    }

}
