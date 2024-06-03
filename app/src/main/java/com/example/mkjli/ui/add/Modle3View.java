package com.example.mkjli.ui.add;

import androidx.lifecycle.ViewModel;

public class Modle3View extends ViewModel {
    private String adress;

    public Modle3View(String adress) {
        this.adress = adress;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }
}
