package com.example.jojo.appliprojet;

import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Carte {
    int id;
    String nom;
    String description;
    String moment;
    ArrayList<Effet> effects;
    String type;

    public Carte(String nom, String description, String moment, ArrayList<Effet> effects, String type) {
        this.nom = nom;
        this.description = description;
        this.moment = moment;
        this.effects = effects;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public ArrayList<Effet> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<Effet> effects) {
        this.effects = effects;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
