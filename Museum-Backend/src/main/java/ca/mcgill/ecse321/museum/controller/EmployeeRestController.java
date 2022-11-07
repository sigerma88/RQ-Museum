package ca.mcgill.ecse321.museum.controller;

import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.mcgill.ecse321.museum.dto.ScheduleDto;
import ca.mcgill.ecse321.museum.service.EmployeeService;

@CrossOrigin(origins = "*")
@RequestMapping("/api/employee")
@RestController
public class EmployeeRestController {

@GetMapping(value = { "/schedule/{id}"})
public ScheduleDto getEmployeeSchedule(@PathVariable("id") long id) {

	return EmployeeService.getEmployeeSchedule(id).toDto();
}

@PostMapping(value = { "/persons/{name}", "/persons/{name}/" })
public PersonDto createPerson(@PathVariable("name") String name) throws IllegalArgumentException {
	Person person = service.createPerson(name);
	return convertToDto(person);
}
    
}
