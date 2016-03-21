import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;

public class ThreadJoueur {
	public Socket soc;
	int id;
	Joueur joueur = null;

	public ThreadJoueur(final int id, final Socket socket, final Partie serveur, final String etiquette) {
		soc = socket;
		this.id = id;
		Runnable runnable = new Runnable() {
			String deco = new String("");
			InputStream stream = null;
			String lui = new String("");
			HashMap<Joueur, ThreadJoueur> v = serveur.envoi_liste();

			public void envoi_liste() {
				for (ThreadJoueur tc : v.values()) {
					PrintWriter writer = null;
					try {
						writer = new PrintWriter(tc.soc.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					writer.println("liste:" + v.keySet());
					writer.flush();
				}
			}
			
			public void envoi_mp(ThreadJoueur tj, String message) {
				
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(tj.soc.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				writer.print(message + "\n");
				writer.flush();
				
			}

			public void envoi_message(String message) {
				for (ThreadJoueur tc : v.values()) {
					PrintWriter writer = null;
					try {
						writer = new PrintWriter(tc.soc.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					writer.print(message + "\n");
					writer.flush();
				}
			}

			public void envoi_message_seulement_au_socket_courant(String message) {
				PrintWriter writer = null;
				try {
					writer = new PrintWriter(soc.getOutputStream());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				writer.print(message + "\n");
				writer.flush();
			}

			public void test_mes_special(String line) {
				if (line.contains("déconnecté")) {
					deco = line.split(" ")[0];
				}
				if (line.contains("connecté")) {
					if (!deco.equals("")) {
						v.remove(deco);
					}

				}
			}

			public void envoyerMain() {

				ArrayList<Carte> main = joueur.getMain();

				System.out.println("main du joueur : " + joueur.getMain().toString());

				ArrayList<Carte> bonus = new ArrayList<Carte>();
				ArrayList<Carte> maledictions = new ArrayList<Carte>();
				ArrayList<Carte> monstres = new ArrayList<Carte>();
				ArrayList<Carte> races = new ArrayList<Carte>();
				ArrayList<Carte> classes = new ArrayList<Carte>();
				ArrayList<Carte> equipements = new ArrayList<Carte>();

				// tri des cartes dans la main du joueur
				for (int i = 0; i < joueur.getMain().size(); i++) {

					System.out.println("test type de carte : " + joueur.getMain().get(i).getClass().toString());
					if (joueur.getMain().get(i).getClass().toString().equals("class Carte")) {
						bonus.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("class Malediction")) {
						maledictions.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("class Monstre")) {
						monstres.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("class Race")) {
						races.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("class Classe")) {
						classes.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("class Equipement")) {
						equipements.add(joueur.getMain().get(i));
					}

				}

				String chaineAEnvoyer = "afficherMain-" + joueur.getNom() + "-" + joueur.getSexe() + "-"
						+ joueur.getNiveau() + "-" + joueur.getAttaque();

				chaineAEnvoyer += "-" + classes.size() + "classe[";
				if (classes.size() != 0) {
					for (int i = 0; i < classes.size(); i++) {
						chaineAEnvoyer += classes.get(i).getNom() + "-" + classes.get(i).getDescription();
						if (i != (classes.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
				}
				chaineAEnvoyer += "]";

				chaineAEnvoyer += "-" + races.size() + "race[";
				if (races.size() != 0) {
					for (int i = 0; i < races.size(); i++) {
						chaineAEnvoyer += races.get(i).getNom() + "-" + races.get(i).getDescription();
						if (i != (races.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
				}
				chaineAEnvoyer += "]";

				chaineAEnvoyer += "-" + equipements.size() + "equipement[";
				if (equipements.size() != 0) {
					for (int i = 0; i < equipements.size(); i++) {
						chaineAEnvoyer += equipements.get(i).getNom() + "-" + equipements.get(i).getDescription() + "-"
								+ ((Equipement) equipements.get(i)).getPartie_corps() + "-"
								+ ((Equipement) equipements.get(i)).isGros();
						if (i != (equipements.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
				}
				chaineAEnvoyer += "]";

				chaineAEnvoyer += "-" + maledictions.size() + "malediction[";
				if (maledictions.size() != 0) {
					for (int i = 0; i < maledictions.size(); i++) {
						chaineAEnvoyer += maledictions.get(i).getNom() + "-" + maledictions.get(i).getDescription();
						if (i != (maledictions.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
				}
				chaineAEnvoyer += "]";

				chaineAEnvoyer += "-" + monstres.size() + "monstre[";
				if (monstres.size() != 0) {
					for (int i = 0; i < monstres.size(); i++) {
						chaineAEnvoyer += monstres.get(i).getNom() + "-" + monstres.get(i).getDescription() + "-"
								+ monstres.get(i).getNiveau() + "-" + monstres.get(i).getRecompense_niveau() + "-"
								+ monstres.get(i).getRecompense_tresors();
						if (i != (monstres.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
				}
				chaineAEnvoyer += "]";

				chaineAEnvoyer += "-" + bonus.size() + "bonus[";
				if (bonus.size() != 0) {
					for (int i = 0; i < bonus.size(); i++) {
						chaineAEnvoyer += bonus.get(i).getNom() + "-" + bonus.get(i).getDescription();
						if (i != (bonus.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
				}
				chaineAEnvoyer += "]";

				envoi_message(chaineAEnvoyer);

				System.out.println("chaine envoyee (envoyerMain) : " + chaineAEnvoyer);
			}

			// envoi des informations des joueurs et de leurs cartes équipées
			public void envoyerAccordeon(String prefixe) {
				for (Map.Entry<Joueur, ThreadJoueur> e : v.entrySet()) {
					Joueur key = e.getKey();

					ArrayList<String> attributsClasse = new ArrayList<String>();
					for (int i = 0; i < key.getClasses().size(); i++) {

						attributsClasse.add(key.getClasses().get(i).getNom());
						attributsClasse.add(key.getClasses().get(i).getDescription());
					}

					ArrayList<String> attributsRace = new ArrayList<String>();
					for (int i = 0; i < key.getRaces().size(); i++) {

						attributsRace.add(key.getRaces().get(i).getNom());
						attributsRace.add(key.getRaces().get(i).getDescription());
					}

					ArrayList<String> attributsEquipement = new ArrayList<String>();
					for (int i = 0; i < key.getEquipements().size(); i++) {

						attributsEquipement.add(key.getEquipements().get(i).getNom());
						attributsEquipement.add(key.getEquipements().get(i).getDescription());
						attributsEquipement.add(key.getEquipements().get(i).getPartie_corps());
						if (key.getEquipements().get(i).isGros())
							attributsEquipement.add("true");
						else
							attributsEquipement.add("false");
					}

					ArrayList<String> attributsMalediction = new ArrayList<String>();
					for (int i = 0; i < key.getMaledictions().size(); i++) {

						attributsMalediction.add(key.getMaledictions().get(i).getNom());
						attributsMalediction.add(key.getMaledictions().get(i).getDescription());
					}

					String chaineAEnvoyer = prefixe + "Accordeon" + e.getValue().getId() + "-" + key.getNom() + "-"
							+ key.getSexe() + "-" + key.getNiveau() + "-" + key.getAttaque();

					chaineAEnvoyer += "-" + key.getClasses().size() + "classe[";
					if (attributsClasse.size() != 0) {
						int i = 0;
						while (i != attributsClasse.size()) {
							chaineAEnvoyer += attributsClasse.get(i) + "-" + attributsClasse.get(i + 1);
							i += 2;
							if (i != attributsClasse.size()) {
								chaineAEnvoyer += "-";
							}
						}
					}
					chaineAEnvoyer += "]";

					chaineAEnvoyer += "-" + key.getRaces().size() + "race[";
					if (attributsRace.size() != 0) {
						int i = 0;
						while (i != attributsRace.size()) {
							chaineAEnvoyer += attributsRace.get(i) + "-" + attributsRace.get(i + 1);
							i += 2;
							if (i != attributsRace.size()) {
								chaineAEnvoyer += "-";
							}
						}
					}
					chaineAEnvoyer += "]";

					chaineAEnvoyer += "-" + key.getEquipements().size() + "equipement[";
					if (attributsEquipement.size() != 0) {
						int i = 0;
						while (i != attributsEquipement.size()) {
							chaineAEnvoyer += attributsEquipement.get(i) + "-" + attributsEquipement.get(i + 1) + "-"
									+ attributsEquipement.get(i + 2) + "-" + attributsEquipement.get(i + 3);
							i += 4;
							if (i != attributsEquipement.size()) {
								chaineAEnvoyer += "-";
							}
						}
					}
					chaineAEnvoyer += "]";

					chaineAEnvoyer += "-" + key.getMaledictions().size() + "malediction[";
					if (attributsMalediction.size() != 0) {
						int i = 0;
						while (i != attributsMalediction.size()) {
							chaineAEnvoyer += attributsMalediction.get(i) + "-" + attributsMalediction.get(i + 1);
							i += 2;
							if (i != attributsMalediction.size()) {
								chaineAEnvoyer += "-";
							}
						}
					}
					chaineAEnvoyer += "]";

					envoi_message(chaineAEnvoyer);

					System.out.println("chaine envoyee (envoyerAccordeon) : " + chaineAEnvoyer);
				}
			}

			public void run() {

				// récupération du joueur
				for (Map.Entry<Joueur, ThreadJoueur> e : v.entrySet()) {
					Joueur key = e.getKey();
					ThreadJoueur value = e.getValue();

					if (value.getId() == getId()) {

						joueur = key;

					}
				}

				try {
					stream = socket.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				String line = "";
				int i = 0;

				envoi_message("idJoueur-" + id);

				System.out.println("id envoyé : " + id);

				envoyerAccordeon("init");

				envoi_message("nombreJoueurs-" + v.size());

				// on attend que le client ait bien pris en compte tout
				// l'accordéon
				synchronized (this) {
					try {
						wait(7000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				// ordre d'animation de l'accordéon
				envoi_message("animationAccordeon");

				// définition du tour du joueur pour le premier qui se connecte
				if (v.size() == 1) {
					envoi_message("auTourDe-" + joueur.getNom());
				}

				envoyerMain();

				// on attend que le client ait bien pris en compte toute la main
				synchronized (this) {
					try {
						wait(7000);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				/* envoi du menu contextuel contenant la liste des joueurs */
				String s = "actualiserMenu-monstre-";
				int temp = 0;

				for (Map.Entry<Joueur, ThreadJoueur> e : v.entrySet()) {

					temp++;

					s += e.getKey().getNom();

					if (temp != v.size()) {

						s += "-";

					}

				}

				envoi_message(s);

				try {
					while ((line = reader.readLine()) != null) {
						System.out.println("Client dit : " + line);

						if (line.contains("clicBoutonChat")) {

							envoi_message("afficherDansLeChat-" + joueur.getNom() + " dit : " + line.substring(14));

							System.out.println("chaine chat : " + "afficherDansLeChat-" + joueur.getNom() + " dit : "
									+ line.substring(14));

						}

						// gestion clic sur pioche donjon
						if (line.equals("clicPiocheDonjon")) {

							String retour = serveur.piocher(1, "donjon", joueur);
							Carte carteTiree = serveur.carteTiree;
							String nomCarte = carteTiree.getNom();
							String typeCarte = carteTiree.getClass().toString();
							String descriptionCarte = carteTiree.getDescription();
							int levelCarte = carteTiree.getNiveau();
							int recompensesTresor = carteTiree.getRecompense_tresors();
							int recompensesNiveau = carteTiree.getRecompense_niveau();
							System.out.println("typeCarte : " + typeCarte);

							if (!retour.equals("")) {

								envoi_message(retour);

							}
							
							envoi_message("cartePiochee-" + joueur.getNom() + "-" + joueur.getNiveau() + "-"
									+ joueur.getAttaque() + "-" + nomCarte + "-" + typeCarte + "-" + descriptionCarte + "-" + levelCarte + "-" + recompensesTresor + "-" + recompensesNiveau);

							envoyerAccordeon("afficher");

							envoyerMain();
						}

						// gestion clic pioche trésor
						if (line.contains("clicPiocheTresor")) {

							serveur.piocher(1, "tresor", joueur);

							envoyerMain();

						}

						// gestion clic sur bouton fuir
						if (line.equals("clicBoutonFuir")) {

							Random rand = new Random();
							int randomNum = rand.nextInt((6 - 1) + 1) + 1;

							// cas fuite réussie
							if (randomNum + joueur.getDeguerpir() > 4) {

								System.out.println("fuite réussie");

								envoi_message("fuiteReussie-" + randomNum);

							} else {

								// incindent fâcheux
								Monstre m = serveur.monstre_a_combattre;
								System.out.println("nom monstre : "+m.getNom());
								m.changerJoueurIncident(joueur);
								m.declencher_incident();

								System.out.println("fuite échouée");

								envoi_message("fuiteEchouee-" + randomNum);
								
								// raffraichissement de l'affichage
								envoyerAccordeon("affichage");
								envoyerMain();

							}
							
							serveur.changerMoment();

						}

						if (line.contains("clicCarteMainEquipement")) {

							String nomCarte = line.split("-")[1];
							Carte c = null;

							// récupération de la carte
							for (int j = 0; j < joueur.getMain().size(); j++) {

								if (joueur.getMain().get(j).getNom().equals(nomCarte)) {

									System.out.println("nom de la carte jouée : " + nomCarte);
									c = joueur.getMain().get(j);

								}

							}

							String retour = serveur.JouerCarte(joueur, c, joueur);
							String[] t = retour.split(";");
							String erreur = t[0];
							String action = t[1];
							if (erreur.equals(" ")) {
								if (!action.equals(" ")) {
									envoi_message("lancerlinterfacecombat-" + joueur.getNom() + "-" + joueur.getNiveau()
											+ "-" + joueur.getAttaque() + "-" + nomCarte + "-" + c.getType() + "-"
											+ c.getNiveau() + "-" + c.getNiveau() + "-" + c.getDescription());
								}

								envoyerMain();
								envoyerAccordeon("afficher");
								
							} else {
								envoi_message("ErreurJouerCarte-" + erreur);
							}

						}

						if (line.contains("clicCarteMainBonusMalediction")) {

							String nomCarte = line.split("-")[1];
							String nomJoueurCible = line.split("-")[2];
							Carte c = null;
							Joueur j = null;
							Monstre m = serveur.monstre_a_combattre;

							System.out.println("joueur cible : " + nomJoueurCible);

							// récupération de la carte
							for (int k = 0; k < joueur.getMain().size(); k++) {

								if (joueur.getMain().get(k).getNom().equals(nomCarte)) {

									System.out.println("nom de la carte jouée : " + nomCarte);
									c = joueur.getMain().get(k);

								}

							}

							// récupération du joueur (ou pas si c'est sur un monstre)
							if(nomJoueurCible.equals("monstre")) {
								
								serveur.JouerCarte(joueur, c, m);
								
							} else {
								for (Map.Entry<Joueur, ThreadJoueur> e : v.entrySet()) {

									if (e.getKey().getNom().equals(nomJoueurCible)) {

										System.out.println("nom du joueur cible : " + e.getKey().getNom());
										j = e.getKey();

									}

								}
								
								serveur.JouerCarte(joueur, c, j);
							}

							
							/* actualisation des affichages */
							envoyerMain();
							envoyerAccordeon("afficher");

						}
						
						if(line.contains("changementTour")) {
							
							// changement de tour
							serveur.changerMoment();
							
							// affichage du tour du joueur
							envoi_message("auTourDe-" + serveur.getJoueurs().get(serveur.tourjoueur).getNom());
							
						}
						
						if(line.contains("clicBoutonCombattre")) {
							
							serveur.Combattre();
							
							envoyerMain();
							envoyerAccordeon("afficher");
							envoi_message("auTourDe-" + serveur.getJoueurs().get(serveur.tourjoueur).getNom());
							envoi_message("afficherInfobulle-Monstre combattu !");
							
						}
						
						if(line.contains("demanderAide")) {
							
							String joueurCible = line.split("-")[1];
							String tresorsDemandes = line.split("-")[2];
							ThreadJoueur tc = null;
							Joueur j = null;
							
							// récupération du joueur
							for (Map.Entry<Joueur, ThreadJoueur> e : v.entrySet()) {

								if (e.getKey().getNom().equals(joueurCible)) {

									System.out.println("nom du joueur cible (aide) : " + e.getKey().getNom());
									j = e.getKey();
									tc = e.getValue();

								}

							}
							
							// envoi da la commande de lancement de réponse au joueur ciblé
							String message = "lancerlinterfacereponse-" + joueur + "-" + joueurCible + "-" + tresorsDemandes;
							
							envoi_mp(tc, message);
							
						}
						
						if(line.contains("validerAide")) {
							
							String joueurCible = line.split("-")[1];
							String tresorsDemandes = line.split("-")[2];
							Joueur j = null;
							
							// récupération du joueur
							for (Map.Entry<Joueur, ThreadJoueur> e : v.entrySet()) {

								if (e.getKey().getNom().equals(joueurCible)) {

									System.out.println("nom du joueur cible (aide) : " + e.getKey().getNom());
									j = e.getKey();

								}

							}
							
							serveur.getListe_participants().add(new Participation(j, 0, Integer.parseInt(tresorsDemandes)));
							
							System.out.println("Participation ok - "+j.getNom());
							
							envoi_message("lancerlinterfacecombat-" + joueur.getNom() + "-" + joueur.getNiveau()
							+ "-" + joueur.getAttaque() + "-" + serveur.carteTiree.getNom() + "-" + serveur.carteTiree.getType() + "-"
							+ serveur.carteTiree.getNiveau() + "-" + serveur.carteTiree.getNiveau() + "-" + serveur.carteTiree.getDescription());
							
						}
						
						if(line.contains("refuserAide")) {
							
							envoi_message("lancerlinterfacecombat-" + joueur.getNom() + "-" + joueur.getNiveau()
							+ "-" + joueur.getAttaque() + "-" + serveur.carteTiree.getNom() + "-" + serveur.carteTiree.getType() + "-"
							+ serveur.carteTiree.getNiveau() + "-" + serveur.carteTiree.getNiveau() + "-" + serveur.carteTiree.getDescription());
							
						}
						
					}

					System.out.println("étiquette : " + etiquette);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};

		Thread ta = new Thread(runnable, "thread");
		ta.start();
	}

	public int getId() {
		return id;
	}
}
