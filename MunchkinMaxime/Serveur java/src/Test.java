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
	
	public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException{
	/*Object t = Effet.class.newInstance();
	Carte c1= new Monstre("justin bibiere","justin qui boit de la biere","special",new ArrayList<Method>(),"monstre",4,2,null,12);
	Carte c2 = new Carte("laver la voiture du mj","+1 niveau", "tontour", new ArrayList<Method>(), "bonus");
	Joueur j1 = new Joueur("jojo");
	Joueur j2 = new Joueur("pipi");
	System.out.println("Test avant effet : "+j1.getNiveau());
	Class[] testt=new Class[2];
	testt[0]=int.class;
	testt[1]=Joueur.class;
	Method m =Effet.class.getDeclaredMethod("modifierNiveau",testt);
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
	String jojo = "Joueur";
	try {
		System.out.println(Class.forName(jojo).toString());
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	}
}
