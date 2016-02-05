package com.example.jojo.serveur;

import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Serveur {
    public ArrayList<Joueur> joueurs;


    public Serveur() {

    }
    public Joueur getJoueur(int i){
        return joueurs.get(i);
    }
    public void setJoueurs(Joueur j , int i){
        joueurs.set(i,j);
    }
}