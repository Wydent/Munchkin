package com.example.jojo.appliprojet;

import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Malediction extends Carte {
    Joueur cible;
    int temps;

    public Malediction(String nom, String description, String moment, ArrayList<Effet> effects, String type, Joueur cible, int temps) {
        super(nom, description, moment, effects, type);
        this.cible = cible;
        this.temps = temps;
    }

    public Joueur getCible() {
        return cible;
    }

    public void setCible(Joueur cible) {
        this.cible = cible;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }
}
