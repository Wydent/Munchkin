
import java.util.ArrayList;

/**
 * Created by jojo on 02/02/2016.
 */
public class Joueur {
    private String nom ;
    private int niveau;
    private ArrayList<Classe> classes;
    private ArrayList<Carte> races;
    private ArrayList<Equipement> equipements;
    private ArrayList<Malediction> maledictions;
    private ArrayList<Carte> main;
    private int sexe;

    public Joueur(String nom){
        setNiveau(1);
        setClasses(new ArrayList<Classe>());
        races=new ArrayList<Carte>();
        races.add(new Carte("Humain","Humain",null,null,null));
        setEquipements(new ArrayList<Equipement>());
        setMaledictions(new ArrayList<Malediction>());
        setMain(new ArrayList<Carte>());
        setSexe(0);

    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public ArrayList<Classe> getClasses() {
        return classes;
    }

    public void setClasses(ArrayList<Classe> classes) {
        this.classes = classes;
    }

    public ArrayList<Carte> getRaces() {
        return races;
    }

    public void setRaces(ArrayList<Carte> races) {
        this.races = races;
    }

    public ArrayList<Equipement> getEquipements() {
        return equipements;
    }

    public void setEquipements(ArrayList<Equipement> equipements) {
        this.equipements = equipements;
    }

    public ArrayList<Malediction> getMaledictions() {
        return maledictions;
    }

    public void setMaledictions(ArrayList<Malediction> maledictions) {
        this.maledictions = maledictions;
    }

    public ArrayList<Carte> getMain() {
        return main;
    }
    public void addCarteMain(Carte c){
    	main.add(c);
    }
    
    public void removeCarteMain(Carte c){
    	main.remove(c);
    }
    
    public void getCarteMain(int index){
    	main.get(index);
    }

    public void setMain(ArrayList<Carte> main) {
        this.main = main;
    }

    public int getSexe() {
        return sexe;
    }

    public void setSexe(int sexe) {
        this.sexe = sexe;
    }
}

