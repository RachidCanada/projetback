package com.stagora.dao.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stagora.entities.users.User;


public interface DaoUser extends JpaRepository<User, Long>{
	
	@Query("select u from User u where u.email like :x")   // Attendiont 'User' doit être identique au nom de l'entité et non la table
	public User findUserByEmail(@Param("x")String email);
	
	@Query("SELECT u FROM User u WHERE u.email = :email")
	User findByEmail(@Param("email") String email);

	
}
