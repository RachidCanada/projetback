package com.stagora.services;

import java.util.UUID;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.stagora.dao.employers.DaoEmployeur;
import com.stagora.dao.students.DaoEtablissement;
import com.stagora.dao.students.DaoEtudiant;
import com.stagora.dao.users.DaoConnexion;
import com.stagora.dao.users.DaoUser;
import com.stagora.entities.employers.Employeur;
import com.stagora.entities.students.Etablissement;
import com.stagora.entities.students.Etudiant;
import com.stagora.entities.users.Connexion;
import com.stagora.entities.users.User;
import com.stagora.utils.exceptions.EmailNonDisponibleException;
import com.stagora.utils.user.RequestInscriptionEmployeur;
import com.stagora.utils.user.RequestInscriptionEtudiant;
import com.stagora.utils.user.TypeCompte;

@Service
public class ServiceUser {

    @Autowired
    private DaoUser daoUser;

    @Autowired
    private DaoEtudiant daoEtudiant;

    @Autowired
    private DaoEtablissement daoEtablissement;

    @Autowired
    private DaoEmployeur daoEmployeur;

    @Autowired
    private DaoConnexion daoConnexion;

    public ResponseEntity<Map<String, String>> inscriptionEtudiant(RequestInscriptionEtudiant req) {
        User user = req.getUser();
        this.verifDisponibiliteEmail(user);

        Etudiant etudiant = req.getEtudiant();
        Optional<Etablissement> etablissement = daoEtablissement.findById(req.getId_etablissement());

        if (!etablissement.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.reponse("Établissement non trouvé"));
        }

        user.setTypeCompte(TypeCompte.ETUDIANT);
        daoUser.save(user);

        etudiant.setUser(user);
        etudiant.setEtablissement(etablissement.get());
        daoEtudiant.save(etudiant);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.reponse("Inscription réussie"));
    }

    public ResponseEntity<Map<String, String>> inscriptionEmployeur(RequestInscriptionEmployeur req) {
        User user = req.getUser();
        this.verifDisponibiliteEmail(user);

        Employeur employeur = req.getEmployeur();
        user.setTypeCompte(TypeCompte.EMPLOYEUR);
        daoUser.save(user);

        employeur.setUser(user);
        employeur.getSites().forEach(site -> site.setEmployeur(employeur));

        daoEmployeur.save(employeur);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.reponse("Inscription réussie"));
    }

    public ResponseEntity<Map<String, String>> connection(String email, String mdp) {
        User user = daoUser.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.reponse("Utilisateur non trouvé"));
        }

        if (this.verifMotdPasse(user, mdp)) {
            Connexion connexion = daoConnexion.findConnexionByUserId(user.getId());
            if (connexion != null) {
                connexion.setNombre_connexion(connexion.getNombre_connexion() + 1);
            } else {
                connexion = new Connexion();
            }
            connexion.setDateConnexion(LocalDateTime.now());
            connexion.setUser(user);
            daoConnexion.save(connexion);

            return ResponseEntity.status(HttpStatus.OK).body(this.reponse("Connection réussie"));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(this.reponse("Mauvais mot de passe"));
        }
    }

    public ResponseEntity<Map<String, String>> demandeReinitialisationMotDePasse(String email) {
        User user = daoUser.findByEmail(email);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(this.reponse("Utilisateur non trouvé"));
        }

        // Génération d'un token aléatoire pour la réinitialisation
        String token = UUID.randomUUID().toString();

        Connexion connexion = daoConnexion.findConnexionByUserId(user.getId());
        if (connexion == null) {
            connexion = new Connexion();
            connexion.setUser(user);
        }
        connexion.setTokenResetPassword(token);
        connexion.setDateConnexion(LocalDateTime.now());
        daoConnexion.save(connexion);

        // Simule l'envoi de l'email
        System.out.println("Lien de réinitialisation : http://localhost:8080/user/motdepasse/reinitialisation?token=" + token);

        return ResponseEntity.status(HttpStatus.OK).body(this.reponse("Email de réinitialisation envoyé"));
    }

    public ResponseEntity<Map<String, String>> reinitialisationMotDePasse(String token, String nouveauMotDePasse) {
        Connexion connexion = daoConnexion.findByTokenResetPassword(token);
        if (connexion == null || connexion.getDateConnexion().plusHours(1).isBefore(LocalDateTime.now())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(this.reponse("Token invalide ou expiré"));
        }

        User user = connexion.getUser();
        user.setMot_de_passe(nouveauMotDePasse);
        daoUser.save(user);

        connexion.setTokenResetPassword(null); // Invalidation du token
        daoConnexion.save(connexion);

        return ResponseEntity.status(HttpStatus.OK).body(this.reponse("Mot de passe réinitialisé avec succès"));
    }

    private void verifDisponibiliteEmail(User user) {
        if (daoUser.findByEmail(user.getEmail()) != null) {
            throw new EmailNonDisponibleException("Email non disponible");
        }
    }

    private boolean verifMotdPasse(User user, String mdp) {
        return user.getMot_de_passe().equals(mdp);
    }

    private Map<String, String> reponse(String message) {
        Map<String, String> reponse = new HashMap<>();
        reponse.put("message", message);
        return reponse;
    }
}
