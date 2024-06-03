package com.example.mkjli.ui.add;

import androidx.lifecycle.ViewModel;

public class DoctorDonnee extends ViewModel {
    private String nom;
    private String prenom;
    private String id;
    private String wilaya;
    private String spisia;

    private String email;
    private String telephon;
    private String sexe;

    public DoctorDonnee(String nom, String prenom, String id,String wilaya,String spisia,String email, String telephon, String sexe) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = id;
        this.wilaya = wilaya;
        this.spisia = spisia;
        this.email = email;
        this.telephon = telephon;
        this.sexe = sexe;
    }
    public DoctorDonnee(String nom, String prenom, String id,String wilaya,String spisia) {
        this.nom = nom;
        this.prenom = prenom;
        this.id = id;
        this.wilaya = wilaya;
        this.spisia = spisia;
    }
    public DoctorDonnee(String nom,String spisia,String wilaya) {
        this.nom = nom;
        this.wilaya = wilaya;
        this.spisia = spisia;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephon() {
        return telephon;
    }

    public void setTelephon(String telephon) {
        this.telephon = telephon;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getSpisia() {
        return spisia;
    }

    public void setSpisia(String spisia) {
        this.spisia = spisia;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
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

    public String getIdd() {
        return id;
    }

    public void setidd(String numtel) {
        this.id = numtel;
    }
}
