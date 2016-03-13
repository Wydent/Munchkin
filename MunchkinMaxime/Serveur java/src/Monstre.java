
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jojo on 02/02/2016.
 */
public class Monstre extends Carte{
	int niveau;
	int attaque;
	Method incident_facheux;
	int recompense_niveau;
	int recompense_tresors;
	Object[] parametre_incident;

	public Monstre(String nom, String description, String moment, ArrayList<Method> effects, String type, int recompense_tresors, int recompense_niveau, Method incident_facheux, int niveau) {
		super(nom, description, moment, effects, type);
		this.attaque = niveau;
		this.recompense_tresors = recompense_tresors;
		this.recompense_niveau = recompense_niveau;
		this.incident_facheux = incident_facheux;
		this.niveau = niveau;
		

	}

	public int getAttaque() {
		return attaque;
	}

	public void setAttaque(int a) {
		attaque = a;
	}

	public int getNiveau() {
		return niveau;
	}

	public void setNiveau(int niveau) {
		this.niveau = niveau;
	}

	public Method getIncident_facheux() {
		return incident_facheux;
	}

	public void setIncident_facheux(Method incident_facheux) {
		this.incident_facheux = incident_facheux;
	}

	public int getRecompense_niveau() {
		return recompense_niveau;
	}

	public void setRecompense_niveau(int recompense_niveau) {
		this.recompense_niveau = recompense_niveau;
	}

	public int getRecompense_tresors() {
		return recompense_tresors;
	}

	public void setRecompense_tresors(int recompense_tresors) {
		this.recompense_tresors = recompense_tresors;
	}

	public Object[] getParametres_incident(){
		return parametre_incident;
	}

	public void setParametres_incident(Object[] objects){
		parametre_incident=new Object[objects.length];
		for(int i=0 ;i<objects.length;i++){
			parametre_incident[i]=objects[i];
		}

	}


	public void changerParametreIncident(int numero_parametre,Object objet){

		parametre_incident[numero_parametre]=objet;
	}

	public void changerJoueurIncident(Joueur j){
		if(incident_facheux!=null){
			for(int i=0;i<parametre_incident.length;i++){
				if(parametre_incident[i] instanceof Joueur){
					parametre_incident[i]=j;
				}
			}
		}

	}

	public void declencher_incident(){
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
		try {
			incident_facheux.invoke(t,parametre_incident);
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

