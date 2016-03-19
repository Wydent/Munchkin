
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Classe qui représente un équipement dans le jeu , et qui hérite de carte car un équipement est une carte
 * en plus d'hériter des attributs de cartes , il possède une partie_corps pour savoir où se place l'équipement
 * un boolean gros pour savoir si l'objet est gros ou pas car un joueur ne peut posséder qu'un seul gros objet sauf si il est nain
 * et les contraintes qui représentent les contraintes de classes ou de race mais n'ayant pas géré les classes , ces contraintes
 * ont été mis de coté
 * Un attribut attaque aussi pour connaitre l'attaque que procurait l'équipement il s'agit seulement du premier paramètre des paramètres d'effet du premier effet
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
    int Attaque;

    /**
	 * @return the attaque
	 */
	public int getAttaque() {
		return (Integer) parametres_effets.get(0)[0];
	}

	/**
	 * @param attaque the attaque to set
	 */
	public void setAttaque(int attaque) {
		Attaque = attaque;
	}

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
