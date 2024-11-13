package com.stagora.restcontrollers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.stagora.services.ServiceUser;
import com.stagora.utils.user.RequestInscriptionEmployeur;
import com.stagora.utils.user.RequestInscriptionEtudiant;

import jakarta.transaction.Transactional;

@RestController
@CrossOrigin("*") 
@RequestMapping("/user")
public class ControllerUser {
	
	@Autowired
	private ServiceUser serviceUser;	
	
	
	// Inscription étudiant
	@PostMapping(value="/inscription/etudiant")
	@Transactional
	public ResponseEntity<Map<String, String>> addEtudiant(@RequestBody RequestInscriptionEtudiant req){
		
		return serviceUser.inscriptionEtudiant(req);
	}
	
	
	//Inscription Employeur
	@PostMapping("/inscription/employeur")
	@Transactional
	public ResponseEntity<Map<String, String>> addEmployeur(@RequestBody RequestInscriptionEmployeur req){
		
		return serviceUser.inscriptionEmployeur(req);
	}
	
	// Demande de réinitialisation de mot de passe
	@PostMapping("/motdepasse/demande-reinitialisation")
	public ResponseEntity<Map<String, String>> demandeReinitialisation(@RequestParam String email) {
	    return serviceUser.demandeReinitialisationMotDePasse(email);
	}

	// Réinitialisation du mot de passe
	@PostMapping("/motdepasse/reinitialisation")
	public ResponseEntity<Map<String, String>> reinitialisationMotDePasse(
	        @RequestParam String token, 
	        @RequestParam String nouveauMotDePasse) {
	    return serviceUser.reinitialisationMotDePasse(token, nouveauMotDePasse);
	}

	
	
	@PostMapping("/connexion")
	public ResponseEntity<Map<String, String>> connect(@RequestParam String email, @RequestParam String mdp){
		
		return serviceUser.connection(email, mdp);
	}




}
