

/**
 * Created by jojo on 02/02/2016.
 */
public class Effet {
	public void modifierNiveau(Integer nbniveau,Joueur joueur){
		joueur.setNiveau(joueur.getNiveau()+nbniveau);
	}
	public void modifierAttaque(Integer nbniveau,Joueur joueur){
		joueur.setAttaque(joueur.getAttaque()+nbniveau);
	}
}
