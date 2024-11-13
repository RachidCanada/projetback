package com.stagora.dao.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.stagora.entities.users.Connexion;

public interface DaoConnexion extends JpaRepository<Connexion, Long>{
	@Query("select c from Connexion c where c.user.id = :x")   
	public Connexion findConnexionByUserId(@Param("x")Long idUser);
	
	@Query("select c from Connexion c where c.tokenResetPassword = :token")
	public Connexion findByTokenResetPassword(@Param("token") String token);

}
