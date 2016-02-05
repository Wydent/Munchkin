package com.example.jojo.serveur;

import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Partie {
    ArrayList<Joueur> joueurs;
    ArrayList<Carte> paquet_donjons;
    ArrayList<Carte> paquet_tresors;
    ArrayList<Carte> defausse_donjons;
    ArrayList<Carte> defausse_tresors;
    int tourjoueur;
    ArrayList<Participation> liste_participants;

    public Partie(){

    }
    public Partie(ArrayList<Joueur> joueurs, ArrayList<Carte> paquet_donjons, ArrayList<Carte> paquet_tresors, ArrayList<Carte> defausse_donjons, ArrayList<Carte> defausse_tresors, int tourjoueur, ArrayList<Participation> liste_participants) {
        this.joueurs = joueurs;
        this.paquet_donjons = paquet_donjons;
        this.paquet_tresors = paquet_tresors;
        this.defausse_donjons = defausse_donjons;
        this.defausse_tresors = defausse_tresors;
        this.tourjoueur = tourjoueur;
        this.liste_participants = liste_participants;
    }

    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }

    public void setJoueurs(ArrayList<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public ArrayList<Carte> getPaquet_donjons() {
        return paquet_donjons;
    }

    public void setPaquet_donjons(ArrayList<Carte> paquet_donjons) {
        this.paquet_donjons = paquet_donjons;
    }

    public ArrayList<Carte> getPaquet_tresors() {
        return paquet_tresors;
    }

    public void setPaquet_tresors(ArrayList<Carte> paquet_tresors) {
        this.paquet_tresors = paquet_tresors;
    }

    public ArrayList<Carte> getDefausse_donjons() {
        return defausse_donjons;
    }

    public void setDefausse_donjons(ArrayList<Carte> defausse_donjons) {
        this.defausse_donjons = defausse_donjons;
    }

    public ArrayList<Carte> getDefausse_tresors() {
        return defausse_tresors;
    }

    public void setDefausse_tresors(ArrayList<Carte> defausse_tresors) {
        this.defausse_tresors = defausse_tresors;
    }

    public int getTourjoueur() {
        return tourjoueur;
    }

    public void setTourjoueur(int tourjoueur) {
        this.tourjoueur = tourjoueur;
    }

    public ArrayList<Participation> getListe_participants() {
        return liste_participants;
    }

    public void setListe_participants(ArrayList<Participation> liste_participants) {
        this.liste_participants = liste_participants;
    }
}
