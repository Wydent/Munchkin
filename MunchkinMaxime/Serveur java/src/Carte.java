

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jojo on 02/02/2016.
 */
public class Carte {
    int id;
    String nom;
    String description;
    String moment;
    ArrayList<Method> effects;
    String type;
    HashMap<Integer,Object[]> parametres_effets;

    public Carte(String nom, String description, String moment, ArrayList<Method> effects, String type) {
        this.nom = nom;
        this.description = description;
        this.moment = moment;
        this.effects =new ArrayList<Method>();
        this.type = type;
        parametres_effets=new HashMap<Integer,Object[]>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMoment() {
        return moment;
    }

    public void setMoment(String moment) {
        this.moment = moment;
    }

    public ArrayList<Method> getEffects() {
        return effects;
    }

    public void setEffects(ArrayList<Method> effects) {
        this.effects = effects;
    }
    
    public void ajouterEffect(Method effect) {
        effects.add(effect);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public int getRecompense_tresors() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getRecompense_niveau() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Method getIncident_facheux() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNiveau() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Object[] getParametres_effets(int i){
		return parametres_effets.get(i);
	}
	
	public void setParametres_effets(int i,Object[] objects){
		parametres_effets.replace(i,objects);
	}
	
	public void ajouterParametre_effect(Object[] objet){
		parametres_effets.put(parametres_effets.size(), objet);
	}
	
	public void changerParametre(int numero_methode,int numero_parametre,Object objet){
		
		Object[] parametres=getParametres_effets(numero_methode);
		parametres[numero_parametre]=objet;
		setParametres_effets(numero_methode, parametres);
	}
	
	public void changerJoueurcible(Joueur j){
		if(effects.size()>0){
			for(int i=0;i<effects.size();i++){
				changerParametre(i,1,j);	
			}
		}
		
	}
	
	public void joueur_effets(){
		Object t = null;
		try {
			t = Effet.class.newInstance();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i=0;i<getEffects().size();i++){
			try {
				getEffects().get(i).invoke(t,parametres_effets.get(i));
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
