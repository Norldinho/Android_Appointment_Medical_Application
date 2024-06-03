package com.example.mkjli.ui.chat;





public class ChatView {
    private String heur;
    private String namedoctor;
    private String lastMessage;
    private String id_medcin;

    public ChatView(String heur, String namedoctor, String lastMessage) {
        this.heur = heur;
        this.namedoctor = namedoctor;
        this.lastMessage = lastMessage;
    }
    public ChatView(){}

    public String getId_medcin() {
        return id_medcin;
    }

    public void setId_medcin(String id_medcin) {
        this.id_medcin = id_medcin;
    }

    public String getHeur() {
        return heur;
    }

    public void setHeur(String heur) {
        this.heur = heur;
    }

    public String getNamedoctor() {
        return namedoctor;
    }

    public void setNamedoctor(String namedoctor) {
        this.namedoctor = namedoctor;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
