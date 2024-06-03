package com.example.mkjli.medcin.home;

import androidx.lifecycle.ViewModel;

import java.util.PrimitiveIterator;

public class MaladeView extends ViewModel {
    private String nameMalade;
    private String numberMalade;

    private String heurRDV;
    private String jourRDV;
    private String prixConsultation;
    private String id;
    private String idrdv;

    public MaladeView(String nameMalade, String numberMalade, String heurRDV, String jourRDV,String id,String idrdv) {
        this.nameMalade = nameMalade;
        this.numberMalade = numberMalade;
        this.heurRDV = heurRDV;
        this.jourRDV = jourRDV;
        this.id = id;
        this.idrdv = idrdv;
    }

    public String getIdrdv() {
        return idrdv;
    }

    public void setIdrdv(String idrdv) {
        this.idrdv = idrdv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameMalade() {
        return nameMalade;
    }

    public void setNameMalade(String nameMalade) {
        this.nameMalade = nameMalade;
    }

    public String getNumberMalade() {
        return numberMalade;
    }

    public void setNumberMalade(String numberMalade) {
        this.numberMalade = numberMalade;
    }

    public String getHeurRDV() {
        return heurRDV;
    }

    public void setHeurRDV(String heurRDV) {
        this.heurRDV = heurRDV;
    }

    public String getJourRDV() {
        return jourRDV;
    }

    public void setJourRDV(String jourRDV) {
        this.jourRDV = jourRDV;
    }

    public String getPrixConsultation() {
        return prixConsultation;
    }

    public void setPrixConsultation(String prixConsultation) {
        this.prixConsultation = prixConsultation;
    }
}
