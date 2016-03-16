import java.util.ArrayList;



/**
 * Created by jojo on 02/02/2016.
 */
public class Effet {
	public void modifierNiveau(Integer nbniveau,Joueur joueur){
		int niveau=joueur.getNiveau()+nbniveau;
		if(niveau<1){
		joueur.setNiveau(1);
		}else{
		joueur.setNiveau(niveau);
		}
			
	}
	public void modifierAttaque(Integer nbniveau,Joueur joueur){
		joueur.setAttaque(joueur.getAttaque()+nbniveau);
	}
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
	public void perte_equipement(String emplacement,Joueur joueur){
		for(Equipement e:joueur.getEquipements()){
			if(e.getPartie_corps().equals(emplacement)){
				joueur.getEquipements().remove(e);
			}
		}
	}
}
