package ca.mcgill.ecse321.museum.controller;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.service.EmployeeService;

@CrossOrigin(origins = "*")
@RestController
public class EmployeeRestController {

@GetMapping(value = { "/persons", "/persons/" })
public ScheduleDto getEmployeeSchedule(@PathVariable("Schedule") ) {

	return EmployeeService.getEmployeeSchedule().stream().map(p -> convertToDto(p)).collect(Collectors.toList());
}

@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
	Person person = service.createPerson(name);
	return convertToDto(person);
}
    
}
