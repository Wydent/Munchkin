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

				try {
					while ((line = reader.readLine()) != null) {
						System.out.println("Client dit : " + line);

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

						// a supprimer plus tard
						envoi_message("afficherNomJoueur-" + joueur.getNom());
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
