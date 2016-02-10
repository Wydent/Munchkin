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



public class ThreadChat{
	public Socket soc;
	int id;
	

	public ThreadChat(final int id, final Socket socket,final Partie serveur, final String etiquette) {
		soc=socket;
		this.id = id;
		Runnable runnable = new Runnable() {
			String deco=new String("");
			InputStream stream = null;
			String lui=new String("");
			HashMap<Joueur,ThreadChat> v=serveur.envoi_liste();
			
			
			
			public void envoi_liste(){
				for(ThreadChat tc:v.values()){
					PrintWriter writer = null;
					try {
						writer = new PrintWriter(tc.soc.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					writer.println("liste:"+v.keySet());
					writer.flush();
				}
			}
			
			
			public void envoi_message(String message){

				for(ThreadChat tc:v.values()){
					PrintWriter writer = null;
					try {
						writer = new PrintWriter(tc.soc.getOutputStream());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					writer.print(message+"\n");
					writer.flush();
				}
			}
			
			
			public void test_mes_special(String line){
				if(line.contains("déconnecté")){
					deco=line.split(" ")[0];	
				}
				if(line.contains("connecté")){
					if(!deco.equals("")){
						v.remove(deco);
					}
					
				}
			}
			
			
			
			public void run() {
				try {
					stream = socket.getInputStream();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						stream));
				String line = "";
				int i = 0;
				try {
					while ((line = reader.readLine()) != null) {
						System.out.println("Client dit : "+line);
						
						// gestion clic sur pioche donjon
						if(line.equals("clicPiocheDonjon")) {
							
							for (Map.Entry<Joueur, ThreadChat> e : v.entrySet()) {
							    Joueur key = e.getKey();
							    ThreadChat value = e.getValue();
						    	
							    if(value.getId() == getId()) {
							    	
							    	ArrayList<String> list = Partie.piocher(1, "donjon", key);
							    	String nomCarte = list.get(0);
							    	envoi_message("actionClicPiocheDonjon-"+nomCarte);
							    	
							    }
							}
						}
					}
					System.out.println("étiquette : "+etiquette);
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};

		Thread ta=new Thread(runnable,"thread");
		ta.start();
	}
	
	public int getId() {
		return id;
	}
}
