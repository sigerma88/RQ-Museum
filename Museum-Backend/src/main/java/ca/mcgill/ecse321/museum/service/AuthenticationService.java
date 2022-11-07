package ca.mcgill.ecse321.museum.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;

public class AuthenticationService {
    @Autowired
    private VisitorRepository visitorRepository;

}
