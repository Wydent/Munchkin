
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
	ArrayList<Participation> liste_participants;

	static final int PORT = 6666;

	// ancienne HashMap d'enregistrement des joueurs
	// public HashMap<String,ThreadChat> chat=new HashMap<String,ThreadChat>();

	// nouvelle HashMap d'enregistrement des joueurs
	public HashMap<Joueur, ThreadChat> chat = new HashMap<Joueur, ThreadChat>();

	public Partie() {

		// test d'ajout de cartes a supprimer plus tard
		paquet_donjons = new ArrayList<Carte>();
		paquet_donjons.add(new Carte("carte_test1", "descriptionTest", "momentTest", null, "monstre"));
		paquet_donjons.add(new Carte("carte_test2", "descriptionTest2", "momentTest2", null, "monstre"));
		paquet_donjons.add(new Carte("carte_test3", "descriptionTest3", "momentTest3", null, "monstre"));

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
				System.out.println(etiquette);

				chat.put(new Joueur(etiquette), new ThreadChat(i, socket, this, etiquette));

				i++;
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

	public boolean findepartie() {
		boolean b = false;
		for (int i = 0; i < joueurs.size(); i++) {
			if (joueurs.get(i).getNiveau() >= 10) {
				b = true;
			}
		}
		return b;
	}

	public static void main(String[] args) {
		new Partie();
	}
}
