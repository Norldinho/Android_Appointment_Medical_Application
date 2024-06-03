package com.example.mkjli.medcin.home;

import androidx.lifecycle.ViewModel;

public class PatentDonnee extends ViewModel {
    private String nom;
    private String prenom;
    private String numtel;
    private String email;
    private String adress;
    private String sexe;
    private String dateNaissence;
    private String id;

    public PatentDonnee(String nom, String prenom, String numtel, String email, String adress, String sexe, String dateNaissence,String id) {
        this.nom = nom;
        this.prenom = prenom;
        this.numtel = numtel;
        this.email = email;
        this.adress = adress;
        this.sexe = sexe;
        this.dateNaissence = dateNaissence;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getDateNaissence() {
        return dateNaissence;
    }

    public void setDateNaissence(String dateNaissence) {
        this.dateNaissence = dateNaissence;
    }
}
