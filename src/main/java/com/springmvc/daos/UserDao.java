package com.springmvc.daos;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.springmvc.models.User;

/**
 * A DAO for the entity User is simply created by extending the CrudRepository
 * interface provided by spring. The following methods are some of the ones
 * available from such interface: save, delete, deleteAll, findOne and findAll.
 * The magic is that such methods must not be implemented, and moreover it is
 * possible create new query methods working only by defining their signature!
 * 
 * @author netgloo
 */
@Transactional
public interface UserDao extends CrudRepository<User, Long> {

  /**
   * Return the user having the passed name or null if no user is found.
   * 
   * @param name the user name.
   */
  public User findByName(String name);

}