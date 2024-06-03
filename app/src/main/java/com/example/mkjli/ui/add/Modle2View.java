package com.example.mkjli.ui.add;

import androidx.lifecycle.ViewModel;

public class Modle2View extends ViewModel {
    private String spisialite;

    public Modle2View(String spisialite) {
        this.spisialite = spisialite;
    }

    public String getSpisialite() {
        return spisialite;
    }

    public void setSpisialite(String spisialite) {
        this.spisialite = spisialite;
    }
}
