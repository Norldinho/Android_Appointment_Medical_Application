package com.example.mkjli.ui.add;

import androidx.lifecycle.ViewModel;

public class Mdol1View extends ViewModel {
    private String namedoctor;
    private String spicialite;
    private String wilaya;

    public Mdol1View(String namedoctor, String spicialite,String wilaya) {
        this.namedoctor = namedoctor;
        this.spicialite = spicialite;
        this.wilaya = wilaya;
    }

    public String getWilaya() {
        return wilaya;
    }

    public void setWilaya(String wilaya) {
        this.wilaya = wilaya;
    }

    public String getNamedoctor() {
        return namedoctor;
    }

    public void setNamedoctor(String namedoctor) {
        this.namedoctor = namedoctor;
    }

    public String getSpicialite() {
        return spicialite;
    }

    public void setSpicialite(String spicialite) {
        this.spicialite = spicialite;
    }

}
