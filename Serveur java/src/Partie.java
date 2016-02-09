
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by jojo on 02/02/2016.
 */
public class Partie {
    ArrayList<Joueur> joueurs;
    ArrayList<Carte> paquet_donjons;
    ArrayList<Carte> paquet_tresors;
    ArrayList<Carte> defausse_donjons;
    ArrayList<Carte> defausse_tresors;
    int tourjoueur;
    String EtatPartie;
    ArrayList<Participation> liste_participants;

    
    static final int PORT = 6666;
	public HashMap<String,ThreadChat> ListeThreads=new HashMap<String,ThreadChat>();
	
	
    public Partie(){
    	try {
    	String nomJoueur=new String("");
		ServerSocket serverSocket = new ServerSocket(PORT);
		System.out.println("Serveur prêt ");
		while (true) {
			System.out.println("Serveur en Attente");
			Socket socket = serverSocket.accept();
			System.out.println(new Date() + " Client connecté");
			InputStream stream = socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			nomJoueur=reader.readLine();
			//Verification nom de joueur non utilis�
			if (! ListeThreads.containsKey(nomJoueur)){
				System.out.println(nomJoueur);
				ListeThreads.put(nomJoueur,new ThreadChat(socket,this,nomJoueur));
				Joueur j= new Joueur(nomJoueur);
				joueurs.add(j);
			}			
		}
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    public Partie(ArrayList<Joueur> joueurs, ArrayList<Carte> paquet_donjons, ArrayList<Carte> paquet_tresors, ArrayList<Carte> defausse_donjons, ArrayList<Carte> defausse_tresors, int tourjoueur, ArrayList<Participation> liste_participants) {
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
    
    public HashMap<String,ThreadChat> getListeThreads(){
		return ListeThreads;
	}
    public void piocher(int nombrecarte, String paquet,Joueur j){
    	for(int i=0;i<nombrecarte;i++){
    		if(paquet.equals("tresor")){
    			j.addCarteMain(paquet_tresors.get(0));
    			paquet_tresors.remove(0);
    		}
    		else if(paquet.equals("donjon")){
    			j.addCarteMain(paquet_donjons.get(0));
    			paquet_donjons.remove(0);
    		}
    	}
    }

    public int finDePartie(){
    	int numeroJoueurGagnant=-1;
    	for(int i=0;i<joueurs.size();i++){
    		if(joueurs.get(i).getNiveau()>=10){
    			numeroJoueurGagnant=i;
    		}
    	}
    	return numeroJoueurGagnant;
    }
    
    public void JouerCarte(Joueur j, Carte c, Joueur cible){
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
    				*/
    				if (j.getClasses().size()<nb_classes_possibles){
    					j.getClasses().add(cla);
    				}
    			} 
    			/*else if (c instanceof Race) {
    				
    			}
    			*/
    		}
    		
    	}
    	else //Envoi d'un message d'erreur comme quoi la carte n'est pas dans sa main.
    	{
    		ListeThreads.get(j.getNom());
    	}
    	
    	
    }
    
    
	public static void main(String[] args) {
		new Partie();
	}
}
