
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
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

