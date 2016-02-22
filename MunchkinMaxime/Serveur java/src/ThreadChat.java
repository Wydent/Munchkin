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

				ArrayList<Carte> bonus = new ArrayList<Carte>();
				ArrayList<Carte> maledictions = new ArrayList<Carte>();
				ArrayList<Carte> monstres = new ArrayList<Carte>();
				ArrayList<Carte> races = new ArrayList<Carte>();
				ArrayList<Carte> classes = new ArrayList<Carte>();
				ArrayList<Equipement> equipements = new ArrayList<Equipement>();

				// tri des cartes dans la main du joueur
				for (int i = 0; i < joueur.getMain().size(); i++) {

					System.out.println("test type de carte : " + joueur.getMain().get(i).getClass().toString());
					if (joueur.getMain().get(i).getClass().toString().equals("Carte")) {
						bonus.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("Malediction")) {
						maledictions.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("Monstre")) {
						monstres.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("Race")) {
						races.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("Classe")) {
						classes.add(joueur.getMain().get(i));
					}

					if (joueur.getMain().get(i).getClass().toString().equals("Equipement")) {
						Equipement e = (Equipement) joueur.getMain().get(i);
						equipements.add(e);
					}

				}

				ArrayList<String> attributsClasse = new ArrayList<String>();
				for (int i = 0; i < classes.size(); i++) {

					attributsClasse.add(classes.get(i).getNom());
					attributsClasse.add(classes.get(i).getDescription());
				}

				ArrayList<String> attributsRace = new ArrayList<String>();
				for (int i = 0; i < races.size(); i++) {

					attributsRace.add(races.get(i).getNom());
					attributsRace.add(races.get(i).getDescription());
				}

				ArrayList<String> attributsEquipement = new ArrayList<String>();
				for (int i = 0; i < equipements.size(); i++) {

					attributsEquipement.add(equipements.get(i).getNom());
					attributsEquipement.add(equipements.get(i).getDescription());
					attributsEquipement.add(equipements.get(i).getPartie_corps());
					if (equipements.get(i).isGros())
						attributsEquipement.add("true");
					else
						attributsEquipement.add("false");
				}

				ArrayList<String> attributsMalediction = new ArrayList<String>();
				for (int i = 0; i < maledictions.size(); i++) {

					attributsMalediction.add(maledictions.get(i).getNom());
					attributsMalediction.add(maledictions.get(i).getDescription());
				}

				String chaineAEnvoyer = "afficherMain-" + joueur.getNom() + "-" + joueur.getSexe() + "-"
						+ joueur.getNiveau() + "-" + joueur.getAttaque();

				if (classes.size() != 0) {
					chaineAEnvoyer += "-" + classes.size() + "classe[";
					for (int i = 0; i < classes.size(); i++) {
						chaineAEnvoyer += classes.get(i).getNom() + "-" + classes.get(i).getDescription();
						if (i != (classes.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
					chaineAEnvoyer += "]";
				}
				if (races.size() != 0) {
					chaineAEnvoyer += "-" + races.size() + "race[";
					for (int i = 0; i < races.size(); i++) {
						chaineAEnvoyer += races.get(i).getNom() + "-" + races.get(i).getDescription();
						if (i != (races.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
					chaineAEnvoyer += "]";
				}
				if (equipements.size() != 0) {
					chaineAEnvoyer += "-" + equipements.size() + "equipement[";
					for (int i = 0; i < races.size(); i++) {
						chaineAEnvoyer += equipements.get(i).getNom() + "-" + equipements.get(i).getDescription();
						if (i != (equipements.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
					chaineAEnvoyer += "]";
				}
				if (maledictions.size() != 0) {
					chaineAEnvoyer += "-" + maledictions.size() + "malediction[";
					for (int i = 0; i < maledictions.size(); i++) {
						chaineAEnvoyer += maledictions.get(i).getNom() + "-" + maledictions.get(i).getDescription();
						if (i != (maledictions.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
					chaineAEnvoyer += "]";
				}
				if (monstres.size() != 0) {
					chaineAEnvoyer += "-" + monstres.size() + "monstre[";
					for (int i = 0; i < monstres.size(); i++) {
						chaineAEnvoyer += monstres.get(i).getNom() + "-" + monstres.get(i).getDescription() + "-"
								+ monstres.get(i).getNiveau() + "-" + monstres.get(i).getRecompense_niveau() + "-"
								+ monstres.get(i).getRecompense_tresors();
						if (i != (monstres.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
					chaineAEnvoyer += "]";
				}
				if (bonus.size() != 0) {
					chaineAEnvoyer += "-" + bonus.size() + "bonus[";
					for (int i = 0; i < bonus.size(); i++) {
						chaineAEnvoyer += bonus.get(i).getNom() + "-" + bonus.get(i).getDescription();
						if (i != (bonus.size() - 1)) {
							chaineAEnvoyer += "-";
						}
					}
					chaineAEnvoyer += "]";
				}

				envoi_message(chaineAEnvoyer);

				System.out.println("chaine envoyee (envoyerMain) : " + chaineAEnvoyer);
			}

			// envoi des informations des joueurs et de leurs cartes équipées
			public void envoyerAccordeon() {
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

					String chaineAEnvoyer = "afficherAccordeon-" + key.getNom() + "-" + key.getSexe() + "-"
							+ key.getNiveau() + "-" + key.getAttaque();

					if (attributsClasse.size() != 0) {
						chaineAEnvoyer += "-" + key.getClasses().size() + "classe[";
						int i = 0;
						while (i != attributsClasse.size()) {
							chaineAEnvoyer += attributsClasse.get(i) + "-" + attributsClasse.get(i + 1);
							i += 2;
							if (i != attributsClasse.size()) {
								chaineAEnvoyer += "-";
							}
						}
						chaineAEnvoyer += "]";
					}
					if (attributsRace.size() != 0) {
						chaineAEnvoyer += "-" + key.getRaces().size() + "race[";
						int i = 0;
						while (i != attributsRace.size()) {
							chaineAEnvoyer += attributsRace.get(i) + "-" + attributsRace.get(i + 1);
							i += 2;
							if (i != attributsRace.size()) {
								chaineAEnvoyer += "-";
							}
						}
						chaineAEnvoyer += "]";
					}
					if (attributsEquipement.size() != 0) {
						chaineAEnvoyer += "-" + key.getEquipements().size() + "equipement[";
						int i = 0;
						while (i != attributsEquipement.size()) {
							chaineAEnvoyer += attributsEquipement.get(i) + "-" + attributsEquipement.get(i + 1) + "-"
									+ attributsEquipement.get(i + 2) + "-" + attributsEquipement.get(i + 3);
							i += 4;
							if (i != attributsEquipement.size()) {
								chaineAEnvoyer += "-";
							}
						}
						chaineAEnvoyer += "]";
					}
					if (attributsMalediction.size() != 0) {
						chaineAEnvoyer += "-" + key.getMaledictions().size() + "malediction[";
						int i = 0;
						while (i != attributsMalediction.size()) {
							chaineAEnvoyer += attributsMalediction.get(i) + "-" + attributsMalediction.get(i + 1);
							i += 2;
							if (i != attributsMalediction.size()) {
								chaineAEnvoyer += "-";
							}
						}
						chaineAEnvoyer += "]";
					}

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

				envoyerAccordeon();

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

				// ordre d'affichage du nom du joueur
				envoi_message("afficherNomJoueur-" + joueur.getNom());
				
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
							String typeCarte = list.get(0).getType();

							if (typeCarte == "monstre") {

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
