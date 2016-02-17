
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Equipement extends Carte {

    public Equipement( String nom, String description, String moment, ArrayList<Method> effects, String type, String partie_corps, boolean gros, ArrayList<String> contraintes) {
        super( nom, description, moment, effects, type);
        this.partie_corps = partie_corps;
        this.gros = gros;
        this.contraintes = contraintes;
    }

    String partie_corps;
    boolean gros;
    ArrayList<String> contraintes;

    public String getPartie_corps() {
    	return partie_corps;
    }
    
    public void setPartie_corps(String pc) {
    	partie_corps = pc;
    }

    public boolean isGros() {
        return gros;
    }

    public void setGros(boolean gros) {
        this.gros = gros;
    }

    public ArrayList<String> getContraintes() {
        return contraintes;
    }

    public void setContraintes(ArrayList<String> contraintes) {
        this.contraintes = contraintes;
    }


}
