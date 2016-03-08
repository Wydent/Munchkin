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

public class ThreadChat {
	public Socket soc;
	int id;
	Joueur joueur = null;

	public ThreadChat(final int id, final Socket socket, final Partie serveur, final String etiquette) {
		soc = socket;
		this.id = id;
		Runnable runnable = new Runnable() {
			String deco = new String("");
			InputStream stream = null;
			String lui = new String("");
			HashMap<Joueur, ThreadChat> v = serveur.envoi_liste();

			public void envoi_liste() {
				for (ThreadChat tc : v.values()) {
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

			public void envoi_message(String message) {
				for (ThreadChat tc : v.values()) {
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
								+ equipements.get(i).getPartie_corps() + "-" + equipements.get(i).isGros();
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
				for (Map.Entry<Joueur, ThreadChat> e : v.entrySet()) {
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

					String chaineAEnvoyer = prefixe + "Accordeon" + e.getValue().getId() + "-" + key.getNom() + "-" + key.getSexe() + "-"
							+ key.getNiveau() + "-" + key.getAttaque();

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
				for (Map.Entry<Joueur, ThreadChat> e : v.entrySet()) {
					Joueur key = e.getKey();
					ThreadChat value = e.getValue();

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
				
				envoi_message("idJoueur-"+id);
				
				System.out.println("id envoyé : "+id);

				envoyerAccordeon("init");
				
				envoi_message("nombreJoueurs-"+v.size());

				// on attend que le client ait bien pris en compte tout
				// l'accordéon
				synchronized (this) {
					try {
						wait(5000);
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

							ArrayList<Carte> list = Partie.piocher(1, "donjon", joueur);
							String nomCarte = list.get(0).getNom();
							String typeCarte = list.get(0).getClass().toString();
							System.out.println("typeCarte : " + typeCarte);

							if (typeCarte.equals("class Monstre")) {

								Monstre m = new Monstre(list.get(0).getNom(), list.get(0).getDescription(),
										list.get(0).getMoment(), list.get(0).getEffects(), list.get(0).getType(),
										list.get(0).getRecompense_tresors(), list.get(0).getRecompense_niveau(),
										list.get(0).getIncident_facheux(), list.get(0).getNiveau());

								int levelMonstre = m.getNiveau();
								int attaqueMonstre = m.getAttaque();

								envoi_message("actionClicPiocheDonjon-" + joueur.getNom() + "-" + joueur.getNiveau()
										+ "-" + joueur.getAttaque() + "-" + nomCarte + "-" + typeCarte + "-"
										+ levelMonstre + "-" + attaqueMonstre);
							}
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

								System.out.println("fuite échouée");

								envoi_message("fuiteEchouee-" + randomNum);

							}

						}
						
						if(line.contains("clicCarteMain")) {
							
							String nomCarte = line.split("-")[1];
							Carte c = null;
							
							// récupération de la carte
							for(int j = 0; j < joueur.getMain().size(); j ++) {
								
								if(joueur.getMain().get(j).getNom().equals(nomCarte)) {
									
									System.out.println("nom de la carte jouée : "+nomCarte);
									c = joueur.getMain().get(j);
									
								}
								
							}
							
							serveur.JouerCarte(joueur, c, joueur);
							
							envoyerMain();
							envoyerAccordeon("afficher");
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
