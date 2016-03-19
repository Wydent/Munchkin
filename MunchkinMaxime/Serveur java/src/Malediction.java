
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Classe qui représente une malédiction dans le jeu , et qui hérite de carte car une malédiction est une carte
 * en plus d'hériter des attributs de cartes, elle possède une cible qui est le joueur pris pour cible de la malédiction
 *  et un temps initial et restant car certaines malédictions sont chronométrés en tour.
 * Created by jojo on 02/02/2016.
 */
public class Malediction extends Carte {
    Joueur cible;
    int tempsInitial;
    int tempsRestant;

    public Malediction(String nom, String description, String moment, ArrayList<Method> effects,
    		String type, Joueur cible, int tempsInitial) {
        super(nom, description, moment, effects, type);
        this.cible = cible;
        this.tempsInitial = tempsInitial;
        this.tempsInitial=this.tempsRestant;
    }

    public Joueur getCible() {
        return cible;
    }

    public void setCible(Joueur cible) {
        this.cible = cible;
    }

    public int getTempsInitial() {
        return tempsInitial;
    }

    public void setTempsInitial(int tempsInitial) {
        this.tempsInitial = tempsInitial;
    }
    
    public int getTempsRestant() {
        return tempsRestant;
    }

    public void setTempsRestant(int tempsRestant) {
        this.tempsRestant = tempsRestant;
    }
}

