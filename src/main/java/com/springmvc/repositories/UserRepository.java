package com.springmvc.repositories;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

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
@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> { //QueryDslPredicateExecutor<User>

  /**
   * Return the user having the passed name or null if no user is found.
   * 
   * @param username the user name.
   */
  public User findByUsername(String username);
  
  //@Query(value="SELECT u FROM User u JOIN FETCH u.person JOIN FETCH u.roles",
  //		countQuery="SELECT count(u) FROM User u")
  //Page<User> findAll(Specification<User> spec, Pageable pageable);

}