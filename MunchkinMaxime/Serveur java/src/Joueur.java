
import java.util.ArrayList;

/**
 * Classe qui représente un joueur dans le jeu 
 * Elle contient beaucoup d'attribut comme le nom du joueur , son niveau , son attaque, ses classes et ses races
 * ses équipements , ses malédictions, sa main qui correspond à ces cartes qu'il a dans sa main, son sexe , sa puissance de fuite 
 * qui est de base à 0 , c'est à dire que le joueur doit faire 5 ou 6 à un dé s'il veut fuir le combat et si le chiffre passe à 1 grâce par exemple 
 * à une classe lui permettant il faudra qu'il fasse 4,5,6 alors que si il est à -1 il devra faire 6.
 * Le joueur possède aussi des booléens Supermunchkin et SangMélée qui est en fait des cartes spéciales qui permettent d'avoir deux classes et deux races
 * et MonstrePremierePioche pour garder en mémoire si le joueur a eu un monstre dans la première pioche pendant le tour  
 * Created by jojo on 02/02/2016.
 */
public class Joueur {
    private String nom ;
    private int niveau;
    private int attaque;
    private ArrayList<Classe> classes;
    private ArrayList<Race> races;
    private ArrayList<Equipement> equipements;
    private ArrayList<Malediction> maledictions;
    private ArrayList<Carte> main;
    private int sexe;
    private int deguerpir;
    private boolean EstSuperMunckhin;
    private boolean EstSangMelee;
    private boolean MonstrePremierePioche;

	/**
	 * @return the monstrePremierePioche
	 */
	public boolean isMonstrePremierePioche() {
		return MonstrePremierePioche;
	}

	/**
	 * @param monstrePremierePioche the monstrePremierePioche to set
	 */
	public void setMonstrePremierePioche(boolean monstrePremierePioche) {
		MonstrePremierePioche = monstrePremierePioche;
	}

	/**
	 * @return the estSuperMunckhin
	 */
	public boolean isEstSuperMunckhin() {
		return EstSuperMunckhin;
	}

	/**
	 * @param estSuperMunckhin the estSuperMunckhin to set
	 */
	public void setEstSuperMunckhin(boolean estSuperMunckhin) {
		EstSuperMunckhin = estSuperMunckhin;
	}

	/**
	 * @return the estSangMelee
	 */
	public boolean isEstSangMelee() {
		return EstSangMelee;
	}

	/**
	 * @param estSangMelee the estSangMelee to set
	 */
	public void setEstSangMelee(boolean estSangMelee) {
		EstSangMelee = estSangMelee;
	}

	/**
	 * Au début le joueur est de niveau 1 humain de sexe du joueur ici homme 
	 */
	public Joueur(String nom){
    	setNom(nom);
        setNiveau(1);
        setAttaque(1);
        
        ArrayList<Classe> arrayListTest = new ArrayList<Classe>();
        setClasses(arrayListTest);
        
        ArrayList<Race> arrayListTest2 = new ArrayList<Race>();
        arrayListTest2.add(new Race("Humain","Humain",null,null,null));
        setRaces(arrayListTest2);
        
        ArrayList<Equipement> arrayListTest3 = new ArrayList<Equipement>();
        setEquipements(arrayListTest3);
        
        setMaledictions(new ArrayList<Malediction>());
        setMain(new ArrayList<Carte>());
        setSexe(0);
        setDeguerpir(0);
        setEstSangMelee(false);
        setEstSuperMunckhin(false);
        setMonstrePremierePioche(false);
    }
	
	public int getDeguerpir() {
		return deguerpir;
	}

	public void setDeguerpir(int deguerpir) {
		this.deguerpir = deguerpir;
	}
    
    public int getAttaque() {
    	return attaque;
    }
    
    public void setAttaque(int a) {
    	attaque = a;
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

    public ArrayList<Race> getRaces() {
        return races;
    }

    public void setRaces(ArrayList<Race> races) {
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

