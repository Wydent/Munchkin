


import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Carte qui représente les classes dans le jeu , les classes n'ont pas été vraiment géré 
 * Elle était associé à une classe Action qui permettait de stocker les actions supplémentaires disponibles de la classe
 * 
 * Created by jojo on 02/02/2016.
 */
public class Classe extends Carte {
    ArrayList<Action> action;

    public Classe(String nom, String description, String moment, ArrayList<Method> effects, String type, ArrayList action) {
        super(nom, description, moment, effects, type);
        this.action = action;
    }

    public ArrayList<Action> getAction() {
        return action;
    }

    public void setAction(ArrayList action) {
        this.action = action;
    }



}
