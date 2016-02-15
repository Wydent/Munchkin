
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Monstre extends Carte{
    int niveau;
    int attaque;
    Method incident_facheux;
    int recompense_niveau;
    int recompense_tresors;

    public Monstre(String nom, String description, String moment, ArrayList<Method> effects, String type, int recompense_tresors, int recompense_niveau, Method incident_facheux, int niveau) {
        super(nom, description, moment, effects, type);
        this.attaque = niveau;
        this.recompense_tresors = recompense_tresors;
        this.recompense_niveau = recompense_niveau;
        this.incident_facheux = incident_facheux;
        this.niveau = niveau;
    }
    
    public int getAttaque() {
    	return attaque;
    }
    
    public void setAttaque(int a) {
    	attaque = a;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public Method getIncident_facheux() {
        return incident_facheux;
    }

    public void setIncident_facheux(Method incident_facheux) {
        this.incident_facheux = incident_facheux;
    }

    public int getRecompense_niveau() {
        return recompense_niveau;
    }

    public void setRecompense_niveau(int recompense_niveau) {
        this.recompense_niveau = recompense_niveau;
    }

    public int getRecompense_tresors() {
        return recompense_tresors;
    }

    public void setRecompense_tresors(int recompense_tresors) {
        this.recompense_tresors = recompense_tresors;
    }
}
