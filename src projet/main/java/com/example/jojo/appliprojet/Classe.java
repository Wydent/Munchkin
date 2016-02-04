package com.example.jojo.appliprojet;


import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Classe extends Carte {
    ArrayList action;

    public Classe(String nom, String description, String moment, ArrayList<Effet> effects, String type, ArrayList action) {
        super(nom, description, moment, effects, type);
        this.action = action;
    }

    public ArrayList getAction() {
        return action;
    }

    public void setAction(ArrayList action) {
        this.action = action;
    }



}
