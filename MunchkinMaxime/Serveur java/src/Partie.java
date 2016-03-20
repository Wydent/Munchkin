
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;



/**

 * Elle permet de gérer les connexions et de lancer la partie à partir d'un certain nombre de joueurs.
 * Elle implémente aussi les règles du munchkin, le jeu contient un paquet de carte donjon et trésors et leurs défausses respectives
 * Elle contient aussi la liste des participants à un combat lorsque le joueur demande de l'aide contre un monstre
 * Elle possède aussi certains attribut comme tourjoueur qui désigne le tour du joueur qui joue actuellement,le monstre 
 * à combattre qui est le monstre qui est en combat actuellement pendant un combat, l'Etat de la partie qui représente le moment actuel de la partie
 * (voir rapport pour les différents moments possibles) et carteTirée qui représente lcarte tirée pendant les moments de pioche.
 */
/**
*
 */
public class Partie {
	static ArrayList<Carte> paquet_donjons;
	static ArrayList<Carte> paquet_tresors;
	ArrayList<Carte> defausse_donjons;
	ArrayList<Carte> defausse_tresors;
	int tourjoueur;
	Monstre monstre_a_combattre;
	static String EtatPartie;
	ArrayList<Participation> liste_participants;
	Carte carteTiree;

	static final int PORT = 6666;

	// ancienne HashMap d'enregistrement des joueurs
	// public HashMap<String,ThreadChat> chat=new HashMap<String,ThreadChat>();

	// nouvelle HashMap d'enregistrement des joueurs
	public HashMap<Joueur, ThreadChat> chat = new HashMap<Joueur, ThreadChat>();

	/**
	 * constructeur par défaut de la partie , elle gère les demandes de connexions et quand le nombre de connexions maximum est 
	 * atteint elle lance la partie en initialisant la pioche et distribue les cartes au joueur 
	 * La partie commence aussi avec le tour du joueur qui a crée la partie 
	 */
	public Partie() {
		carteTiree=new Carte(null, null, null, null, null);
		EtatPartie=new String("init");
		tourjoueur=0;
		defausse_donjons=new ArrayList<Carte>();
		defausse_tresors=new ArrayList<Carte>();
		liste_participants=new ArrayList<Participation>();
		monstre_a_combattre=new Monstre("vide", null, null, null, null, tourjoueur, tourjoueur, null, tourjoueur);
		
		
		init_cartes("cartes.txt",this);
		
		
		TrierPaquetDonjonEtTresor();


		try {
			String etiquette = new String("");
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Serveur prêt ");
			int i = 1;
			while (chat.size() < 1) {
				System.out.println("Serveur en Attente");
				Socket socket = serverSocket.accept();
				System.out.println(new Date() + " Client connecté");
				InputStream stream = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				etiquette = reader.readLine();
				if (!chat.containsKey(etiquette)){
					System.out.println(etiquette);
					Joueur j= new Joueur(etiquette);
					chat.put(new Joueur(etiquette),new ThreadChat(i,socket,this,etiquette));	
					i++;
				}	
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		distribuerMains();
		setEtatPartie("pioche1");
	}

	public Partie(ArrayList<Carte> paquet_donjons, ArrayList<Carte> paquet_tresors,
			ArrayList<Carte> defausse_donjons, ArrayList<Carte> defausse_tresors, int tourjoueur,
			ArrayList<Participation> liste_participants) {
		this.paquet_donjons = paquet_donjons;
		this.paquet_tresors = paquet_tresors;
		this.defausse_donjons = defausse_donjons;
		this.defausse_tresors = defausse_tresors;
		this.tourjoueur = tourjoueur;
		this.liste_participants = liste_participants;

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

	/**
	 * Méthode qui permet de piocher nombrecarte dans un paquet pour un joueur j , la méthode retourne un string qui 
	 * sert uniquement quand un combat est à lancer donc lorsque on pioche un monstre en première pioche
	 * @param nombrecarte
	 * @param paquet
	 * @param j
	 * @return
	 */
	public String piocher(int nombrecarte, String paquet, Joueur j) {
		
		String retour="";
		if (paquet.equals("tresor")) {
			for (int i = 0; i < nombrecarte; i++) {
				j.addCarteMain(paquet_tresors.get(0));
				paquet_tresors.remove(0);
			} 
		}
		else if (paquet.equals("donjon")) {
			
			Carte c=paquet_donjons.get(0);
			System.out.println("---------------------------PIOCHE---------------------------------");
			System.out.println("Carte Piochée : "+c.getNom());
			System.out.println("Moment de la Partie : "+EtatPartie);
			System.out.println("------------------------------------------------------------------");
			if(EtatPartie.equals("pioche1")){
				carteTiree=c;
				if(c instanceof Malediction){
					((Malediction) c).setCible(j);
					JouerCarte(j, c, j);
				}
				else if(c instanceof Monstre){
					monstre_a_combattre=new Monstre(c.getNom(), c.getDescription(), c.getMoment(), c.getEffects(), c.getType(), c.getRecompense_tresors(), c.getRecompense_niveau(), c.getIncident_facheux(), c.getNiveau());
					for (int i=0;i<c.getEffects().size();i++){
						monstre_a_combattre.setParametres_effets(i,c.getParametres_effets(i));
					}
					monstre_a_combattre.setParametre_incident(c.getParametre_incident());
					retour="lancerlinterfacecombat-" + j.getNom() + "-" + j.getNiveau()
							+ "-" + j.getAttaque() + "-" + monstre_a_combattre.getNom() + "-" + monstre_a_combattre.getType() + "-"
							+ monstre_a_combattre.getNiveau() + "-" + monstre_a_combattre.getNiveau() + "-" + c.getDescription();
					j.setMonstrePremierePioche(true);
				}
				else{
					j.addCarteMain(paquet_donjons.get(0));

				}

			}
			else{
				j.addCarteMain(paquet_donjons.get(0));
			}
			changerMoment();
			paquet_donjons.remove(0);
		}
		return retour;
	}
	/**
	 * Méthode retournant la liste des joueurs connectés à la partie
	 * @return
	 */
	public ArrayList<Joueur> getJoueurs(){
		ArrayList<Joueur> joueurs=new ArrayList<Joueur>();
		joueurs.addAll(chat.keySet());
		return joueurs;
	}

	/**
	 * Méthode renvoyant -1 si la partie n'est pas finie ou l'indice du joueur gagnant
	 * @return
	 */
	public int findepartie() {
		int numeroJoueurGagnant=-1;
		for (int i = 0; i < chat.size(); i++) {
			if (getJoueurs().get(i).getNiveau() >= 10) {
				numeroJoueurGagnant=i;
			}
		}
		return numeroJoueurGagnant;
	}
	
	/**
	 * Methode permettant de trier au hasard les cartes des pioches donjons et trésors
	 */
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

	/**
	 * Methode permettant de distribuer les mains des joueurs , on l'utilise la méthode pioche pour cela
	 */
	public void distribuerMains(){
		for(Joueur j:getJoueurs()){
			piocher(2,"tresor",j);
			piocher(1,"donjon",j);
			piocher(1,"donjon",j);
		}
	}
	/**
	 * Methode permettant de créer les cartes à partir d'un fichier texte
	 * On utilise un système de recuperation de méthode en passant par la classe Effet pour les effets des cartes
	 * @param path
	 * @param p
	 */
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
					String effet[]=chaine[10].split("/");
					System.out.println("------------------------Incident Facheux------------------------");
					Class[] classes=new Class[effet.length-1];
					for(int i=1;i<effet.length;i++){
						classes[i-1]=Class.forName(effet[i].split(":")[1]);
					}
					Method m =Effet.class.getDeclaredMethod(effet[0],classes);
					((Monstre) c).setIncident_facheux(m);
					Object[] parametres=new Object[effet.length-1];
					for(int i=1;i<effet.length;i++){
						if(classes[i-1].equals(Joueur.class)){
							parametres[i-1]=classes[i-1].getConstructors()[0].newInstance("pardefaut");	
						}

						else if(classes[i-1].equals(String.class)){
							parametres[i-1]=classes[i-1].getConstructors()[11].newInstance(effet[i].split(":")[0]);
						}
						else{
							parametres[i-1]=classes[i-1].getConstructors()[0].newInstance(Integer.parseInt(effet[i].split(":")[0]));
						}
					}

					((Monstre) c).setParametres_incident(parametres);

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
						if(classes[i-1].equals(Joueur.class)){
							parametres[i-1]=classes[i-1].getConstructors()[0].newInstance("pardefaut");	
						}
						else if(classes[i-1].equals(Object.class)){
							parametres[i-1]=classes[i-1].getConstructors()[0].newInstance();	
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
	/**
	 * Méthode permettant d'attribuer les récompenses en niveau et en carte à un joueur ayant gagné un combat
	 * On donne les récompenses aussi à ceux qui ont aidé par rapport à ce qui a été décidé par les joueurs
	 * @param participants
	 */
	public void attribuer_recompense(ArrayList<Participation> participants){
		int total_niveaux_attribues=0;
		int total_tresors_attribues=0;

		for(Participation p:participants){
			total_niveaux_attribues+=p.getRecompense_niveau_donne();
			total_tresors_attribues+=p.getRecompense_tresor_donne();
			piocher(p.getRecompense_tresor_donne(), "tresor", p.getJoueur_allie());
			p.getJoueur_allie().setNiveau(p.getJoueur_allie().getNiveau()+p.getRecompense_niveau_donne());
		}
		//Attribution des niveaux et des tresors au joueur qui joue en ce moment: c'est le reste.
		getJoueurs().get(tourjoueur).setNiveau(monstre_a_combattre.getRecompense_niveau()-total_niveaux_attribues);
		piocher(monstre_a_combattre.getRecompense_niveau()-total_tresors_attribues, "tresor",getJoueurs().get(tourjoueur));
	}

	/**
	 * Méthode qui permet de simuler le combat , il s'agit seulement de la comparaison d'attaque du monstre avec celle 
	 * des participants . Si elle est supérieure, les joueurs subissent l'incident facheux ,si elle est inférieure les joueurs 
	 * battent le monstre et gagne les récompenses et si il y'a match nul , cela dépend si un joueur du groupe est guerrier
	 */
	public void Combattre (){
		int totalAttaque;
		totalAttaque=getJoueurs().get(tourjoueur).getAttaque();

		if(liste_participants.size()>0){
		for(Participation p:liste_participants){
			totalAttaque=totalAttaque+ p.getJoueur_allie().getAttaque();
		}
		}

		if (monstre_a_combattre.getAttaque()>totalAttaque) {
			for(Participation p:liste_participants){
				monstre_a_combattre.changerJoueurIncident(p.getJoueur_allie());
				monstre_a_combattre.declencher_incident();
			}

		}
		else if(monstre_a_combattre.getAttaque()==totalAttaque){
			boolean guerrier=false;
			int i=0;
			for(int j=0;j<getJoueurs().get(tourjoueur).getClasses().size();j++){
				if(getJoueurs().get(tourjoueur).getClasses().get(j).getNom().equals("guerrier")){
					guerrier=true;
				}
			}
			while((i<liste_participants.size())&&(guerrier==false)){
				Joueur joueur=liste_participants.get(i).getJoueur_allie();
				for(int j=0;j<joueur.getClasses().size();j++){
					if(joueur.getClasses().get(j).getNom().equals("guerrier")){
						guerrier=true;
					}
				}
				i++;
			}
			if(guerrier==false){
				for(Participation p:liste_participants){
					monstre_a_combattre.changerJoueurIncident(p.getJoueur_allie());
					monstre_a_combattre.declencher_incident();
				}
			}
			else{
				attribuer_recompense(liste_participants);
			}
		}
		else{ //Victoire des joueurs , r�partition des r�compenses.
			attribuer_recompense(liste_participants);
		}
		changerMoment();
	}

/**
 * Méthode permettant de changer le tour de jeu . La méthode est appelé lorsque le joueur appuie sur fin de tour
 */
public void changertour(){
		if(tourjoueur==chat.size()-1){
			setTourjoueur(0);
		}
		else{
			setTourjoueur(getTourjoueur()+1);
		}
	}

	/**
	 * Méthode permettant de changer le moment elle utlise aussi la fonction changetour car elle gère tous les cas de changement de moment
	 * 
	 */
	public void changerMoment(){
		Joueur joueuractuel=getJoueurs().get(tourjoueur);
		if(EtatPartie.equals("pioche1")){
			if(joueuractuel.isMonstrePremierePioche()==false){
				setEtatPartie("pioche2");
				System.out.println("Je vais de p1 vers p2");
			}
			else{
				setEtatPartie("combat");
				System.out.println("Je vais de p1 vers c");
			}
		}
		else if(EtatPartie.equals("pioche2")){
			if(monstre_a_combattre.getNom().equals("vide")){
				setEtatPartie("tour");
				System.out.println("Je vais de p2 vers t");
			}
			else{
				setEtatPartie("combat");
				System.out.println("Je vais de p2 vers c");
			}
		}
		else if(EtatPartie.equals("combat")){
			defausse_donjons.add(monstre_a_combattre);
			monstre_a_combattre=new Monstre("vide", null, null, null, null, tourjoueur, tourjoueur, null, tourjoueur);
			setEtatPartie("tour");
			rafraichirAttaque(joueuractuel);
		}
		else if(EtatPartie.equals("tour")){
			changertour();
			getJoueurs().get(tourjoueur).setMonstrePremierePioche(false);
			setEtatPartie("pioche1");
		}
	}


	/**
	 * @return the monstre_a_combattre
	 */
	public Monstre getMonstre_a_combattre() {
		return monstre_a_combattre;
	}

	/**
	 * @param monstre_a_combattre the monstre_a_combattre to set
	 */
	public void setMonstre_a_combattre(Monstre monstre_a_combattre) {
		this.monstre_a_combattre = monstre_a_combattre;
	}

	/**
	 * @return the etatPartie
	 */
	public static String getEtatPartie() {
		return EtatPartie;
	}

	/**
	 * @param etatPartie the etatPartie to set
	 */
	public static void setEtatPartie(String etatPartie) {
		EtatPartie = etatPartie;
	}

	/**Cette méthode est au coeur du jeu , elle permet de jouer une carte c'est à dire de vérifier tout d'abord
	 * si la carte valide toutes les conditions pour qu'elle puisse etre jouer comme le moment , si elle est bien dans la 
	 * main etc ensuite par rapport au type de la carte à jouer , il se passe un code différent par exemple si c un monstre
	 * on enverra au thread un ordre de combat si cest une carte bonus on appliquera les effets de celui-ci sur la cible
	 * 
	 * @param j
	 * @param c
	 * @param cible
	 * @return
	 */
	public String JouerCarte(Joueur j, Carte c, Object cible){
		String erreur=" ";
		String action=" ";
		if (j.getMain().contains(c)){
			if(getJoueurs().get(tourjoueur).getNom().equals(j.getNom())){
				System.out.println("Moment de la carte : "+c.getMoment() +" et moment du tour : "+EtatPartie);
				if ((c.getMoment().equals("tous")) || (EtatPartie.equals(c.getMoment()))) {
					if (c instanceof Monstre) {
						monstre_a_combattre=new Monstre(c.getNom(), c.getDescription(), c.getMoment(), c.getEffects(), c.getType(), c.getRecompense_tresors(), c.getRecompense_niveau(), c.getIncident_facheux(), c.getNiveau());
						for (int i=0;i<c.getEffects().size();i++){
							monstre_a_combattre.setParametres_effets(i,c.getParametres_effets(i));
						}
						monstre_a_combattre.setParametre_incident(c.getParametre_incident());
						action="lancerlinterfacecombat-" + j.getNom() + "-" + j.getNiveau()
								+ "-" + j.getAttaque() + "-" + monstre_a_combattre.getNom() + "-" + monstre_a_combattre.getType() + "-"
								+ monstre_a_combattre.getNiveau() + "-" + monstre_a_combattre.getNiveau() + "-" + c.getDescription();
						changerMoment();
					}
					else if (c instanceof Equipement) {
						Equipement equ=(Equipement)c;
						
						boolean nain=false;
						ArrayList<Equipement> armes= new ArrayList<Equipement>();
						ArrayList<Equipement> suppression= new ArrayList<Equipement>();
						for(int i=0;i<j.getClasses().size();i++){
							if(j.getRaces().get(i).getNom().equals("nain")){
								nain=true;
							}
						}
						//check contraintes
						boolean compatible=true;
						int arme=0;
						int i=0;
						
						while((i<j.getEquipements().size())&&(compatible==true)){
							
							Equipement equipement=j.getEquipements().get(i);
							System.out.println("PLACE EQUIPEMENT "+equ.getPartie_corps()+" ET "+equipement.getPartie_corps());
							if((equipement.gros==true)&&(equ.gros==true)&&(nain==false)){
								compatible=false;
							}
							if(equipement.getPartie_corps().contains("arme")){
								arme=arme+Integer.parseInt(equipement.getPartie_corps().substring(4,5));
								System.out.println("ahah j'ai plein d'armes j'en ai :"+arme);
								armes.add(equipement);

							}
							if((!equ.getPartie_corps().contains("arme"))&&(equ.getPartie_corps().equals(equipement.getPartie_corps()))){
								if(equ!=null){
									suppression.add(equipement);
								}
							}
							if((equ.getPartie_corps().contains("arme"))&&(arme<=2)){
								if(equ.getPartie_corps().equals("arme1")){
									if(arme==2){
										suppression.add(equipement);
									}
								}
								else if(equ.getPartie_corps().equals("arme2")){
									if(arme==1){
										suppression.add(equipement);
									}
									else if(arme==2){
										suppression.addAll(armes);
									}

								}
							}
							i++;
						}
						if(compatible==true){
							
							j.getEquipements().removeAll(suppression);
							equ.changerCible(j);
							equ.joueur_effets();
							j.getEquipements().add(equ);
							
						}
						else{
							erreur="Impossible d'�quiper!Vous avez d�ja un �quipement gros et vous n'�tes pas un nain!";
						}	
					}
					else if (c instanceof Malediction) {
						Malediction mal=(Malediction)c;
						if(EtatPartie.equals("pioche1")){
							mal.setCible(j);
						}
						else{
							mal.setCible((Joueur)cible);

						}
						mal.changerCible(mal.getCible());
						mal.joueur_effets();
						mal.setTempsRestant(mal.getTempsInitial());
						mal.getCible().getMaledictions().add(mal);

					}
					else if (c instanceof Classe) {
						Classe cla=(Classe)c;
						int nb_classes=j.getClasses().size();
						if (nb_classes==0){
							j.getClasses().add(cla);

						}
						else if(nb_classes==1){
							if(j.isEstSuperMunckhin()){
								j.getClasses().add(cla);

							}
							else{
								String type =j.getClasses().get(0).getType();
								if(type.equals("donjon")){
									getDefausse_donjons().add(j.getClasses().get(0));
								}
								else{
									getDefausse_tresors().add(j.getClasses().get(0));	
								}
								j.getClasses().remove(0);
								j.getClasses().add(cla);
							}
						}
						else{
							Classe classe_remplace=(Classe)cible;
							String type =classe_remplace.getType();
							if(type.equals("donjon")){
								getDefausse_donjons().add(j.getClasses().get(j.getClasses().indexOf(classe_remplace)));
							}
							else{
								getDefausse_tresors().add(j.getClasses().get(j.getClasses().indexOf(classe_remplace)));	
							}
							j.getClasses().remove(j.getClasses().indexOf(classe_remplace));
							j.getClasses().add(cla);
						}

					}
					else if (c instanceof Race) {
						Race cla=(Race)c;
						int nb_races=j.getRaces().size();
						if (nb_races==0){
							j.getRaces().add(cla);

						}
						else if(nb_races==1){
							if(j.isEstSangMelee()){
								j.getRaces().add(cla);

							}
							else{
								String type =j.getRaces().get(0).getType();
								if(type.equals("donjon")){
									getDefausse_donjons().add(j.getRaces().get(0));
								}
								else{
									getDefausse_tresors().add(j.getRaces().get(0));	
								}
								j.getRaces().remove(0);
								j.getRaces().add(cla);
							}
						}
						else{
							Race race_remplace=(Race)cible;
							String type =race_remplace.getType();
							if(type.equals("donjon")){
								getDefausse_donjons().add(j.getRaces().get(j.getRaces().indexOf(race_remplace)));
							}
							else{
								getDefausse_tresors().add(j.getRaces().get(j.getRaces().indexOf(race_remplace)));	
							}
							j.getRaces().remove(j.getRaces().indexOf(race_remplace));
							j.getRaces().add(cla);
						}	
					}
					else{
						if(cible instanceof Monstre){
							c.changerCible((Monstre)cible);
							c.joueur_effets();

						}
						else{
							c.changerCible((Joueur)cible);
							c.joueur_effets();
						}
					}
					j.removeCarteMain(c);
				}
				else //pas le tour du joueur
				{
					if ((c.getMoment().equals("tous")) || ((EtatPartie.equals(c.getMoment()))&&(!c.getMoment().equals("tour"))&&(!c.getMoment().equals("pioche2")))) {
						if (c instanceof Malediction) {
							Malediction mal=(Malediction)c;
							mal.setCible((Joueur)cible);
							mal.changerCible(mal.getCible());
							mal.joueur_effets();
							mal.setTempsRestant(mal.getTempsInitial());
							mal.getCible().getMaledictions().add(mal);
						}
						else if((c instanceof Carte)){
							if(cible instanceof Monstre){

								c.changerCible((Monstre)cible);
								c.joueur_effets();

							}
							else{
								c.changerCible((Joueur)cible);
								c.joueur_effets();
							}
						}
						j.removeCarteMain(c);
					}else{
						System.out.println("erreur");
						erreur="Impossible de jouer cette carte � ce moment de la partie";
					}
				}
			}
		}
		System.out.println("------------------------JOUER CARTE------------------------------");
		rafraichirAttaque(j);
		System.out.println("Cartes en main : "+j.getMain().size());
		System.out.println("Joueur de niveau : "+j.getNiveau());
		System.out.println("Joueur d'attaque : "+j.getAttaque());
		System.out.println("-----------------------------------------------------------------------");
		
		return erreur+";"+action;
		
	}
	
	/**
	 * Méthode permettant de rafraichir l'attaque car l'attaque se constitue du niveau + des équipements liées+ bonus(pendant combat)
	 * @param j
	 */
	public void rafraichirAttaque(Joueur j){
		int attaquetotale=0;
		for(int i=0;i<j.getEquipements().size();i++){
			attaquetotale=attaquetotale+j.getEquipements().get(i).getAttaque();
		}
		attaquetotale=attaquetotale+j.getNiveau();
		j.setAttaque(attaquetotale);
	}
	/**
	 * Méthode permettant de connaitre le nombre de trésors restants à distribuer
	 * @param j
	 */
	public int tresors_a_distribuer(){
		int tresors=monstre_a_combattre.getRecompense_tresors();
		for(Participation p:liste_participants){
			tresors=tresors-p.getRecompense_tresor_donne();
		}
		return tresors;
	}


	public static void main(String[] args) {
		new Partie();
	}
}
