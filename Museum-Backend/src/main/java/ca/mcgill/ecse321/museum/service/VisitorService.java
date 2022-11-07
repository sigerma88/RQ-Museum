package ca.mcgill.ecse321.museum.service;

import javax.el.ELException;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ca.mcgill.ecse321.museum.dao.VisitorRepository;
import ca.mcgill.ecse321.museum.dto.VisitorDto;
import ca.mcgill.ecse321.museum.model.Visitor;

@Service
public class VisitorService {
    @Autowired
    private VisitorRepository visitorRepository;

    @Transactional
    public VisitorDto createVisitor(String email, String password, String name) throws Exception {
        if (email == null || password == null || name == null) {
            throw new Exception("Email, password and name must be filled");
        }

        if (visitorRepository.findVisitorByEmail(email) != null) {
            throw new Exception("An account with the email " + email + " already exists.");
        }

        if (visitorRepository.findVisitorByName(name) != null) {
            throw new Exception("Please choose another username. " + name + " already exists. ");
        }

        String passwordCheck = checkPasswordValidity(password);
        System.out.println(passwordCheck.length());

        if (passwordCheck.length() != 0) {
            throw new Exception(passwordCheck);
        }


        Visitor visitor = new Visitor();
        visitor.setEmail(email);
        visitor.setPassword(password);
        visitor.setName(name);

        visitorRepository.save(visitor);

        return visitorToDto(visitor);
    }

    @Transactional
    public VisitorDto getVisitorPersonalInformation(long visitorId) throws Exception {
        Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorId);
        if (visitor == null) {
            throw new Exception("Account was not found in out system. ");
        }
        return visitorToDto(visitor);
    }

    // @Transactional
    // public VisitorDto editVisitorInformation(long visitorId, String email, String password,
    // String name) throws Exception {
    // Visitor visitor = visitorRepository.findVisitorByMuseumUserId(visitorId);
    // if (visitor == null) {
    // throw new Exception("Account was not found in the system. ");
    // }

    // if()



    // return;

    // }


    private VisitorDto visitorToDto(Visitor visitor) {
        VisitorDto visitorDto = new VisitorDto();
        visitorDto.setEmail(visitor.getEmail());
        visitorDto.setName(visitor.getName());
        visitorDto.setPassword(visitor.getPassword());
        visitorDto.setUserId(visitor.getMuseumUserId());
        return visitorDto;
    }

    public String checkPasswordValidity(String password) {
        String errorMessage = "";

        if (password.length() < 8) {
            errorMessage =
                    "Your password must have at least 8 characters composed of digits, lowercase characters and uppercase characters.";
            return errorMessage;
        }

        // TODO: Check for email validity

        int upperCaseCharacterCount = 0;
        int lowerCaseCharacterCount = 0;
        int digitCount = 0;


        for (int index = 0; index < password.length(); index++) {
            if (Character.isUpperCase(password.charAt(index))) {
                upperCaseCharacterCount += 1;
            } else if (Character.isLowerCase(password.charAt(index))) {
                lowerCaseCharacterCount += 1;
            } else if (Character.isDigit(password.charAt(index))) {
                digitCount += 1;
            }
        }

        if (upperCaseCharacterCount == 0 || lowerCaseCharacterCount == 0 || digitCount == 0) {
            errorMessage = "Your password must contain\n";
            if (upperCaseCharacterCount == 0) {
                errorMessage += "- an uppercase character\n";
            }
            if (lowerCaseCharacterCount == 0) {
                errorMessage += "- a lowercase character\n";
            }
            if (digitCount == 0) {
                errorMessage += "- a digit\n";
            }
        }

        return errorMessage;
    }
}
