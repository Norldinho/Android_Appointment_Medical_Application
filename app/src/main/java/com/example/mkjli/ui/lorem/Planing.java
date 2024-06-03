package com.example.mkjli.ui.lorem;

public class Planing {
    private String heur_depart;
    private String heur_fin;
    private String jour_depart;
    private String jour_fin;
    private String patient_jour;
    private String id_me;

    public Planing(){

    }

    public Planing(String heur_depart, String heur_fin, String jour_depart, String jour_fin, String patient_jour) {
        this.heur_depart = heur_depart;
        this.heur_fin = heur_fin;
        this.jour_depart = jour_depart;
        this.jour_fin = jour_fin;
        this.patient_jour = patient_jour;
    }
    public Planing(String heur_depart, String heur_fin, String jour_depart, String jour_fin, String patient_jour,String id_me) {
        this.heur_depart = heur_depart;
        this.heur_fin = heur_fin;
        this.jour_depart = jour_depart;
        this.jour_fin = jour_fin;
        this.patient_jour = patient_jour;
        this.id_me = id_me;
    }

    public String getId_me() {
        return id_me;
    }

    public void setId_me(String id_me) {
        this.id_me = id_me;
    }

    public String getHeur_depart() {
        return heur_depart;
    }

    public void setHeur_depart(String heur_depart) {
        this.heur_depart = heur_depart;
    }

    public String getHeur_fin() {
        return heur_fin;
    }

    public void setHeur_fin(String heur_fin) {
        this.heur_fin = heur_fin;
    }

    public String getJour_depart() {
        return jour_depart;
    }

    public void setJour_depart(String jour_depart) {
        this.jour_depart = jour_depart;
    }

    public String getJour_fin() {
        return jour_fin;
    }

    public void setJour_fin(String jour_fin) {
        this.jour_fin = jour_fin;
    }

    public String getPatient_jour() {
        return patient_jour;
    }

    public void setPatient_jour(String patient_jour) {
        this.patient_jour = patient_jour;
    }
}
