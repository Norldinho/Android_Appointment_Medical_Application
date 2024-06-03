package com.example.mkjli.ui.lorem;

public class Consultation {

    String dure_n;
    String dure_d;
    String dure_c;
    String prix_n;
    String prix_d;
    String prix_c;

    public Consultation(String dure_n, String dure_d, String dure_c, String prix_n, String prix_d, String prix_c) {
        this.dure_n = dure_n;
        this.dure_d = dure_d;
        this.dure_c = dure_c;
        this.prix_n = prix_n;
        this.prix_d = prix_d;
        this.prix_c = prix_c;
    }

    public String getDure_n() {
        return dure_n;
    }

    public void setDure_n(String dure_n) {
        this.dure_n = dure_n;
    }

    public String getDure_d() {
        return dure_d;
    }

    public void setDure_d(String dure_d) {
        this.dure_d = dure_d;
    }

    public String getDure_c() {
        return dure_c;
    }

    public void setDure_c(String dure_c) {
        this.dure_c = dure_c;
    }

    public String getPrix_n() {
        return prix_n;
    }

    public void setPrix_n(String prix_n) {
        this.prix_n = prix_n;
    }

    public String getPrix_d() {
        return prix_d;
    }

    public void setPrix_d(String prix_d) {
        this.prix_d = prix_d;
    }

    public String getPrix_c() {
        return prix_c;
    }

    public void setPrix_c(String prix_c) {
        this.prix_c = prix_c;
    }
}
