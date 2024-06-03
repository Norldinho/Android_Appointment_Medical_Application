//package com.example.mkjli.ui.home;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//public class HomeViewModel extends ViewModel {
//
//    private final MutableLiveData<String> mText;
//
//    public HomeViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is home fragment");
//    }
//
//    public LiveData<String> getText() {
//        return mText;
//    }
//}
package com.example.mkjli.ui.home;

import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {
    private String nameDoctr;
    private String spisialitedoctor;
    private String iddoctor;
    private String heurRDV;
    private String jourRDV;
    private String telephone;
    private String email;
    private String sexe;




    public HomeViewModel(String nameDoctr, String spisialitedoctor, String heurRDV, String jourRDV, String iddoctor,String telephone,String email, String sexe) {
        this.nameDoctr = nameDoctr;
        this.spisialitedoctor = spisialitedoctor;
        this.heurRDV = heurRDV;
        this.jourRDV = jourRDV;
        this.iddoctor = iddoctor;
        this.telephone = telephone;
        this.email = email;
        this.sexe = sexe;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNameDoctr() {
        return nameDoctr;
    }

    public void setNameDoctr(String nameDoctr) {
        this.nameDoctr = nameDoctr;
    }

    public String getSpisialitedoctor() {
        return spisialitedoctor;
    }

    public void setSpisialitedoctor(String spisialitedoctor) {
        this.spisialitedoctor = spisialitedoctor;
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

    public String getIddoctor() {
        return iddoctor;
    }

    public void setIddoctor(String iddoctor) {
        this.iddoctor = iddoctor;
    }

    public String getnameDoctr() {
        return nameDoctr;
    }

    public void setnameDoctr(String nameDoctr) {
        this.nameDoctr = nameDoctr;
    }

    public String getspisialitedoctor() {
        return spisialitedoctor;
    }

    public void setspisialitedoctor(String spisialitedoctor) {
        this.spisialitedoctor = spisialitedoctor;
    }

    public String getheurRDV() {
        return heurRDV;
    }

    public void setheurRDV(String heurRDV) {
        this.heurRDV = heurRDV;
    }
    public String getjourRDV() {
        return jourRDV;
    }

    public void setjourRDV(String jourRDV) {
        this.jourRDV = jourRDV;
    }
}
