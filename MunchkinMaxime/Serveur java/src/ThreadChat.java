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
			
			// envoi des informations des joueurs et de leurs cartes équipées
			public void envoyerAccordeon() {
				for (Map.Entry<Joueur, ThreadChat> e : v.entrySet()) {
					Joueur key = e.getKey();
					ThreadChat value = e.getValue();

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
					
					String chaineAEnvoyer = "afficherAccordeon-" + key.getNom() + "-" + key.getSexe() + "-" + key.getNiveau() + "-"
							+ key.getAttaque();
					
					if(attributsClasse.size() != 0) {
						chaineAEnvoyer += "-"+key.getClasses().size()+"classe[" + attributsClasse.get(0) + "-" + attributsClasse.get(1) + "]";
					}
					if(attributsRace.size() != 0) {
						chaineAEnvoyer += "-"+key.getRaces().size()+"race[" + attributsRace.get(0) + "-" + attributsRace.get(1) + "]";
					}
					if(attributsEquipement.size() != 0) {
						chaineAEnvoyer += "-"+key.getEquipements().size()+"equipement[" + attributsEquipement.get(0) + "-" + attributsEquipement.get(1) + "-" + attributsEquipement.get(2) + "-" + attributsEquipement.get(3) + "]";
					}
					if(attributsMalediction.size() != 0) {
						chaineAEnvoyer += "-"+key.getMaledictions().size()+"malediction[" + attributsMalediction.get(0) + "-" + attributsMalediction.get(1) + "]";
					}

					envoi_message(chaineAEnvoyer);
					
					System.out.println("chaine envoyee : "+chaineAEnvoyer);
				}
			}

			public void run() {

				Joueur joueur = null;

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
				
				// on attend que le client ait bien pris en compte tout l'accordéon
				synchronized(this) {
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

				try {
					while ((line = reader.readLine()) != null) {
						System.out.println("Client dit : " + line);
						
						if(line.contains("clicBoutonChat")) {
							
							envoi_message("afficherDansLeChat-"+joueur.getNom()+" dit : "+line.substring(14));
							
							System.out.println("chaine chat : "+"afficherDansLeChat-"+joueur.getNom()+" dit : "+line.substring(14));
							
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
										+ "-" + joueur.getAttaque() + "-" + nomCarte + "-" + typeCarte + "-" + levelMonstre
										+ "-" + attaqueMonstre);
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
