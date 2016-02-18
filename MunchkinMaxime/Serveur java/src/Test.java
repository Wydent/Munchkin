import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;



public class Test {
	public static void init_cartes(String path,Partie p){
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
				Carte c = null ;
				if(chaine[1]=="carte"){
					c=new Carte(chaine[2],chaine[3],chaine[4],null,chaine[0]);
				}
				else if(chaine[1]=="monstre"){
					c=new Monstre(chaine[2],chaine[3],chaine[4],null,chaine[0],Integer.parseInt(chaine[8]),Integer.parseInt(chaine[9]),null,Integer.parseInt(chaine[6]));
				}
				else if(chaine[1]=="equipement"){
					c=new Equipement(chaine[2],chaine[3],chaine[4],null,chaine[0],chaine[6],Boolean.getBoolean(chaine[7]),null);
				}
				else if(chaine[1]=="malediction"){
					c=new Malediction(chaine[2],chaine[3],chaine[4],null,chaine[0],null,Integer.parseInt(chaine[7]));
				}
				else if(chaine[1]=="race"){
					
				}
				else if(chaine[1]=="classe"){
					
				}
				
				if(c.getType()=="donjon"){
					donjons.add(c);
				}
				else if(c.getType()=="tresor"){
					tresors.add(c);
				}
				p.setPaquet_donjons(donjons);
				p.setPaquet_tresors(tresors);
			}
			br.close(); 
			System.out.println("taille paquet donjon " + p.getPaquet_donjons().size());
			System.out.println("Carte : "+p.getPaquet_donjons().get(0));
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		
	}
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException{
/*	Object t = Effet.class.newInstance();
	Carte c1= new Monstre("justin bibiere","justin qui boit de la biere","special",new ArrayList<Method>(),"monstre",4,2,null,12);
	Carte c2 = new Carte("laver la voiture du mj","+1 niveau", "tontour", new ArrayList<Method>(), "bonus");
	Joueur j1 = new Joueur("jojo");
	System.out.println("Test avant effet : "+j1.getNiveau());
	Method m =Effet.class.getDeclaredMethod("modifierNiveau",int.class,Joueur.class);
	System.out.println(m.toString());
	HashMap<Integer,Object[]> jojo=new HashMap<Integer,Object[]>();
	Object[] test=new Object[2];
	test[0]=1;
	test[1]=j1;
	jojo.put(0,test);
	c2.ajouterEffect(m);
	
	for(int i=0;i<c2.getEffects().size();i++){
		try {
			c2.getEffects().get(i).invoke(t,jojo.get(i));
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
	
	System.out.println("Test après effet : "+j1.getNiveau());*/
	Partie p = new Partie();
	init_cartes("cartes.txt",p);
	}
}
