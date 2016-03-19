import java.util.ArrayList;



/**
 * Cette classe effet contient toutes les méthodes qui sont utilisés pour l'attribut effects dans la classe carte
 * C'est cette classe qui répertorie tous les effets des cartes ainsi que les incidents facheux des monstres
 * Created by jojo on 02/02/2016.
 */
/**
 * @author jojo
 *
 */
public class Effet {
	/**Cette méthode permet de changer le niveau d'un joueur . Remarque dans le munchkin , le joueur ne peut descendre
	 * en dessous du niveau 1
	 * @param nbniveau
	 * @param joueur
	 */
	public void modifierNiveau(Integer nbniveau,Joueur joueur){
			int niveau=joueur.getNiveau()+nbniveau;
			if(niveau<1){
				joueur.setNiveau(1);
			}else{
				joueur.setNiveau(niveau);
			}
	}
	/**
	 * Cette méthode permet de changer l'attaque d'une cible car la cible peut etre un joueur ou un monstre , cette effet
	 * est surtout utilisée sur les cartes combat pour améliorer le joueur ou le monstre.
	 * @param nbniveau
	 * @param cible
	 */
	public void modifierAttaque(Integer nbniveau,Object cible){
		if(cible instanceof Joueur){
			Joueur joueur=(Joueur) cible;
			joueur.setAttaque(joueur.getAttaque()+nbniveau);
		}else if(cible instanceof Carte){
			Monstre monstre=(Monstre) cible;
			monstre.setAttaque(monstre.getAttaque()+nbniveau);
		}
	}
	/**
	 * Vous etes raid , dead , mort ,capoute , chili con carne bref vous avez été tué, vous recommencez niveau 1 et vous
	 * perdez toutes vos cartes.
	 * @param joueur
	 */
	public void mort(Joueur joueur){
		joueur.setNiveau(1);
		joueur.setAttaque(1);
		joueur.setClasses(new ArrayList<Classe>());
		joueur.setRaces(new ArrayList<Race>());
		joueur.setEquipements(new ArrayList<Equipement>());
		joueur.setDeguerpir(0);
		joueur.setEstSangMelee(false);
		joueur.setEstSuperMunckhin(false);
		joueur.setMain(new ArrayList<Carte>());

	}
	/**Cette méthode permet de faire perdre un équipement au joueur, l'équipement est désigné par l'emplacement
	 * @param joueur
	 * @param emplacement
	 */
	public void perte_equipement(Joueur joueur,String emplacement){
		for(Equipement e:joueur.getEquipements()){
			if(e.getPartie_corps().equals(emplacement)){
				joueur.getEquipements().remove(e);
			}
		}
	}
}
