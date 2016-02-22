
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
	ArrayList<Participation> liste_participants;

	static final int PORT = 6666;

	// ancienne HashMap d'enregistrement des joueurs
	// public HashMap<String,ThreadChat> chat=new HashMap<String,ThreadChat>();

	// nouvelle HashMap d'enregistrement des joueurs
	public HashMap<Joueur, ThreadChat> chat = new HashMap<Joueur, ThreadChat>();

	public Partie() {
		
		init_cartes("cartes.txt",this);
		/*System.out.println("taille paquet donjon " + paquet_donjons.size());
		System.out.println("Carte : "+ paquet_donjons.get(0).getNom());*/
		TrierPaquetDonjonEtTresor();
		//System.out.println("Carte : "+ paquet_donjons.get(0).getNom());

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
				/*for(int i=0;i<chaine.length;i++){
					System.out.println(i +" : "+chaine[i]);
				}*/
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
				/*if(chaine[5].contains(".")){
				String effet[]=chaine[5].split(".");
				Class[] classes=new Class[effet.length];
				for(int i=0;i<effet.length;i++){
					classes[i]=Class.forName(effet[i].split(":")[2]);
				}
				Method m =Effet.class.getDeclaredMethod(effet[0],classes);
				Object[] parametres=new Object[effet.length];
				for(int i=0;i<effet.length;i++){
					//parametres[i]=int.class.newInstance()
				}
				}*/
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

	public static void main(String[] args) {
		new Partie();
	}
}
