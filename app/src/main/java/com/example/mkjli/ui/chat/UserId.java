package com.example.mkjli.ui.chat;

import com.google.firebase.firestore.PropertyName;

public class UserId {
    private String id_patient;
    private String id_medcin;
    private String heur;
    private String date;

    // Constructeur vide requis pour Firestore
    public UserId() {}

    @PropertyName("id_patient")
    public String getId_patient() {
        return id_patient;
    }

    @PropertyName("id_patient")
    public void setId_patient(String id_patient) {
        this.id_patient = id_patient;
    }

    @PropertyName("id_medcin")
    public String getId_medcin() {
        return id_medcin;
    }

    @PropertyName("id_medcin")
    public void setId_medcin(String id_medcin) {
        this.id_medcin = id_medcin;
    }

    @PropertyName("heur")
    public String getHeur() {
        return heur;
    }

    @PropertyName("heur")
    public void setHeur(String heur) {
        this.heur = heur;
    }

    @PropertyName("date")
    public String getDate() {
        return date;
    }

    @PropertyName("date")
    public void setDate(String date) {
        this.date = date;
    }
}
