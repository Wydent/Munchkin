
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jojo on 02/02/2016.
 */
public class Partie {
	ArrayList<Joueur> joueurs;
	static ArrayList<Carte> paquet_donjons;
	 static ArrayList<Carte> paquet_tresors;
	ArrayList<Carte> defausse_donjons;
	ArrayList<Carte> defausse_tresors;
	int tourjoueur;
	Monstre monstre_a_combattre;
	String EtatPartie;
	ArrayList<Participation> liste_participants;

	static final int PORT = 6666;

	// ancienne HashMap d'enregistrement des joueurs
	// public HashMap<String,ThreadChat> chat=new HashMap<String,ThreadChat>();

	// nouvelle HashMap d'enregistrement des joueurs
	public HashMap<Joueur, ThreadChat> chat = new HashMap<Joueur, ThreadChat>();

	public Partie() {
		
		init_cartes("cartes.txt",this);
		System.out.println("taille paquet donjon " + paquet_donjons.size());
		Joueur jojo=new Joueur("jojo");
		paquet_tresors.get(0).changerJoueurcible(jojo);
		try {
			System.out.println("Niveau avant :"+ jojo.getNiveau());
			paquet_tresors.get(0).joueur_effets();
			System.out.println("Niveau après :"+ jojo.getNiveau());
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("paquet tresors " + paquet_tresors.get(0).nom);
		TrierPaquetDonjonEtTresor();

		try {
			String etiquette = new String("");
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Serveur prÃªt ");
			int i = 1;
			while (chat.size() < 2) {
				System.out.println("Serveur en Attente");
				Socket socket = serverSocket.accept();
				System.out.println(new Date() + " Client connectÃ©");
				InputStream stream = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				etiquette = reader.readLine();
				System.out.println(etiquette);
				if (!chat.containsKey(etiquette)){
					System.out.println(etiquette);
					Joueur j= new Joueur(etiquette);
					chat.put(new Joueur(etiquette),new ThreadChat(i,socket,this,etiquette));	
					joueurs.add(j);
					i++;
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Partie(ArrayList<Joueur> joueurs, ArrayList<Carte> paquet_donjons, ArrayList<Carte> paquet_tresors,
			ArrayList<Carte> defausse_donjons, ArrayList<Carte> defausse_tresors, int tourjoueur,
			ArrayList<Participation> liste_participants) {
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

	public HashMap<Joueur, ThreadChat> envoi_liste() {
		return chat;
	}

	public static ArrayList<Carte> piocher(int nombrecarte, String paquet, Joueur j) {

		ArrayList<Carte> list = new ArrayList<Carte>();

		for (int i = 0; i < nombrecarte; i++) {
			if (paquet.equals("tresor")) {
				j.addCarteMain(paquet_tresors.get(0));
				list.add(paquet_tresors.get(0));
				paquet_tresors.remove(0);
			} else if (paquet.equals("donjon")) {
				j.addCarteMain(paquet_donjons.get(0));
				list.add(paquet_donjons.get(0));
				paquet_donjons.remove(0);
			}
		}

		return list;
	}

	public int findepartie() {
	    	int numeroJoueurGagnant=-1;
		for (int i = 0; i < joueurs.size(); i++) {
			if (joueurs.get(i).getNiveau() >= 10) {
				numeroJoueurGagnant=i;
			}
		}
		return numeroJoueurGagnant;
	}
	public void TrierPaquetDonjonEtTresor() {
    	Integer nombreAleatoire;
    	ArrayList<Integer> NombreUtilises = new ArrayList<Integer>();
    	
    	ArrayList<Carte> paquet_donjons_tmp= new ArrayList<Carte>();
    	ArrayList<Carte> paquet_tresors_tmp= new ArrayList<Carte>();
    	
    	
    	for (int i = 0 ; i < paquet_donjons.size(); i++ ){    	
    		
    		nombreAleatoire= (int)(Math.random() * (paquet_donjons.size()));
    		
    		while (NombreUtilises.contains(nombreAleatoire)){
    			nombreAleatoire= (int)(Math.random() * (paquet_donjons.size()));
    		}
    		
    		paquet_donjons_tmp.add(paquet_donjons.get(nombreAleatoire));
    		NombreUtilises.add(nombreAleatoire);
    	
    	}
    	paquet_donjons=paquet_donjons_tmp;
    	
    	NombreUtilises.clear();
    	
    	for (int i = 0 ; i < paquet_tresors.size(); i++ ){    		
    		nombreAleatoire= (int)(Math.random() * (paquet_tresors.size()));
    		while (NombreUtilises.contains(nombreAleatoire)){
    			nombreAleatoire= (int)(Math.random() * (paquet_tresors.size()));
    		}
    		paquet_tresors_tmp.add(paquet_tresors.get(nombreAleatoire));
    		NombreUtilises.add(nombreAleatoire);
    	}
    	paquet_tresors=paquet_tresors_tmp;
    }
    
    public void distribuerMains(){
    	for(Joueur j:joueurs){
			piocher(2,"tresor",j);
			piocher(2,"donjon",j);
		}
    }
    public void init_cartes(String path,Partie p){
		String[] chaine;
		
		ArrayList<Carte> tresors=new ArrayList<Carte>();
		ArrayList<Carte> donjons=new ArrayList<Carte>();
		try{
			InputStream ips=new FileInputStream(path); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				chaine=ligne.split(";");
				for(int i=0;i<chaine.length;i++){
					System.out.println(i +" : "+chaine[i]);
				}
				Carte c = null ;
				if(chaine[1].equals("carte")){
					
					c=new Carte(chaine[2],chaine[3],chaine[4],null,chaine[0]);
					
				}
				else if(chaine[1].equals("monstre")){
					
					c=new Monstre(chaine[2],chaine[3],chaine[4],null,chaine[0],Integer.parseInt(chaine[8]),Integer.parseInt(chaine[9]),null,Integer.parseInt(chaine[6]));
				}
				else if(chaine[1].equals("equipement")){
					
					c=new Equipement(chaine[2],chaine[3],chaine[4],null,chaine[0],chaine[6],Boolean.getBoolean(chaine[7]),null);
				}
				else if(chaine[1].equals("malediction")){
					
					c=new Malediction(chaine[2],chaine[3],chaine[4],null,chaine[0],null,Integer.parseInt(chaine[7]));
				}
				else if(chaine[1].equals("race")){
					
				}
				else if(chaine[1].equals("classe")){
					
				}
				if(chaine[5].contains("/")){
				String effet[]=chaine[5].split("/");
				System.out.println("------------------------------------------------");
				Class[] classes=new Class[effet.length-1];
				for(int i=1;i<effet.length;i++){
					classes[i-1]=Class.forName(effet[i].split(":")[1]);
				}
				Method m =Effet.class.getDeclaredMethod(effet[0],classes);
				c.ajouterEffect(m);
				Object[] parametres=new Object[effet.length-1];
				for(int i=1;i<effet.length;i++){
					System.out.println(classes[i-1].getConstructors()[0].toString());
					if(classes[i-1].equals(Joueur.class)){
						parametres[i-1]=classes[i-1].getConstructors()[0].newInstance("pardéfaut");	
					}
					else{
						parametres[i-1]=classes[i-1].getConstructors()[0].newInstance(Integer.parseInt(effet[i].split(":")[0]));
					}
				}
				c.ajouterParametre_effect(parametres);
				}
				if(c.getType().equals("donjon")){
					donjons.add(c);
				}
				else if(c.getType().equals("tresor")){
					tresors.add(c);
				}
			}
				p.setPaquet_donjons(donjons);
				p.setPaquet_tresors(tresors);
				
			
			br.close();
		}		
	
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		
	}
    
    public void Combattre (){
    	int totalAttaque;
    	totalAttaque=joueurs.get(tourjoueur).getAttaque();
    	
    	for(Participation p:liste_participants){
    			totalAttaque=totalAttaque+ p.getJoueur_allie().getAttaque();
    		}
    		
    	if (monstre_a_combattre.getAttaque()>=totalAttaque) {
    		joueurs.get(tourjoueur).setNiveau(joueurs.get(tourjoueur).getNiveau()-1);
    		//Appliquer l'effet facheux jojo? 
    	}
    	else{ //Victoire des joueurs , répartition des récompenses.
    		int total_niveaux_attribues=0;
    		int total_tresors_attribues=0;
    		
    		for(Participation p:liste_participants){
    			total_niveaux_attribues+=p.getRecompense_niveau_donne();
    			total_tresors_attribues+=p.getRecompense_tresor_donne();
    			piocher(p.getRecompense_tresor_donne(), "tresor", p.getJoueur_allie());
    			p.getJoueur_allie().setNiveau(p.getJoueur_allie().getNiveau()+p.getRecompense_niveau_donne());
    		}
    		//Attribution des niveaux et des tresors au joueur qui joue en ce moment: c'est le reste.
    		joueurs.get(tourjoueur).setNiveau(monstre_a_combattre.getRecompense_niveau()-total_niveaux_attribues);
    		piocher(monstre_a_combattre.getRecompense_niveau()-total_tresors_attribues, "tresor",joueurs.get(tourjoueur));
    	}
    }
    
    
    /*public void JouerCarte(Joueur j, Carte c, Joueur cible){
    	if (j.getMain().contains(c)){
    		if ((c.getMoment()=="Tout") || (EtatPartie==c.getMoment())) {
    			if (c instanceof Monstre) {
    				
    			}
    			else if (c instanceof Equipement) {
    				Equipement equ=(Equipement)c;
    				j.getEquipements().add(equ);
    			}
    			else if (c instanceof Malediction) {
    				Malediction mal=(Malediction)c;
    				mal.setCible(cible);
    				mal.setTempsRestant(mal.getTempsInitial());
    				cible.getMaledictions().add(mal);
    			}
    			else if (c instanceof Classe) {
    				Classe cla=(Classe)c;
    				int nb_classes_possibles=1;
    				/*if (j.EstSuperMunchKin()){ //<-- jojo tu peux rajouter l'attribut superMunchKin au joueur?
    					nb_classes_possibles=2;
    				}
    				
    				if (j.getClasses().size()<nb_classes_possibles){
    					j.getClasses().add(cla);
    				}
    			} 
    			else if (c instanceof Race) {
    				
    			}
    			
    		}
    		
    	}
    	else //Envoi d'un message d'erreur comme quoi la carte n'est pas dans sa main.
    	{
    		chat.get(j.getNom());
    	}
    	
    	
    }*/

	public static void main(String[] args) {
		new Partie();
	}
}
