package com.example.mkjli.ui.lorem;

public class RDV {
    private String date;
    private String heur;

    public RDV(String date, String heur) {
        this.date = date;
        this.heur = heur;
    }

    public RDV(String heur) {
        this.heur = heur;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeur() {
        return heur;
    }

    public void setHeur(String heur) {
        this.heur = heur;
    }
}
