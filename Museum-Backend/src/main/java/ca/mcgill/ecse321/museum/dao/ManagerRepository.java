package ca.mcgill.ecse321.museum.dao;

import org.springframework.data.repository.CrudRepository;

import ca.mcgill.ecse321.museum.model.Manager;

/**
 * This is the repository for the Manager class
 * 
 * @author VZ
 */
public interface ManagerRepository extends CrudRepository<Manager, Long>{

  Manager findManagerByMuseumUserId(Long museumUserId);

}