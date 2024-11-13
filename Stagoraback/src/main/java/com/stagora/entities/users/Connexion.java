package com.stagora.entities.users;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Connexion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date_connexion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime date_deconnexion;

    private int nombre_connexion = 1;

    private String tokenResetPassword; // Champ pour le token de réinitialisation de mot de passe

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User user;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate_connexion() {
        return date_connexion;
    }

    public void setDate_connexion(LocalDateTime date_connexion) {
        this.date_connexion = date_connexion;
    }

    public LocalDateTime getDateConnexion() {
        return date_connexion; // Corrigé pour renvoyer `date_connexion`
    }

    public void setDateConnexion(LocalDateTime dateConnexion) {
        this.date_connexion = dateConnexion; // Corrigé pour mettre à jour `date_connexion`
    }

    public LocalDateTime getDate_deconnexion() {
        return date_deconnexion;
    }

    public void setDate_deconnexion(LocalDateTime date_deconnexion) {
        this.date_deconnexion = date_deconnexion;
    }

    public int getNombre_connexion() {
        return nombre_connexion;
    }

    public void setNombre_connexion(int nombre_connexion) {
        this.nombre_connexion = nombre_connexion;
    }

    public String getTokenResetPassword() {
        return tokenResetPassword;
    }

    public void setTokenResetPassword(String tokenResetPassword) {
        this.tokenResetPassword = tokenResetPassword;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Constructeurs
    public Connexion(Long id, LocalDateTime date_connexion, LocalDateTime date_deconnexion, int nombre_connexion,
            User user, String tokenResetPassword) {
        super();
        this.id = id;
        this.date_connexion = date_connexion;
        this.date_deconnexion = date_deconnexion;
        this.nombre_connexion = nombre_connexion;
        this.user = user;
        this.tokenResetPassword = tokenResetPassword;
    }

    public Connexion() {
        super();
    }
}
