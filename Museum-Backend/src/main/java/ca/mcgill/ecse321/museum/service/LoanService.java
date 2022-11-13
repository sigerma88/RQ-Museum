package ca.mcgill.ecse321.museum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.mcgill.ecse321.museum.dao.ArtworkRepository;
import ca.mcgill.ecse321.museum.dao.LoanRepository;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;

@Service
public class LoanService {

    @Autowired
    LoanRepository loanRepository;
    @Autowired
    ArtworkRepository artworkRepository;
    @Autowired 
    VisitorRepository visitorRepository;
    

    
}
