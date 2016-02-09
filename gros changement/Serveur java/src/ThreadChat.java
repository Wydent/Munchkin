import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;



public class ThreadChat{
	public Socket soc;
	

	public ThreadChat(final Socket socket,final ServeurChat serveur, final String etiquette) {
		soc=socket;
		Runnable runnable = new Runnable() {
			String deco=new String("");
			InputStream stream = null;
			String lui=new String("");
			HashMap<String,ThreadChat> v=serveur.envoi_liste();
			
			
			
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
						if(line.equals("clicPiocheDonjon")) {
							envoi_message("actionClicPiocheDonjon");
						}
					}
					System.out.println("étiquette : "+etiquette);
					/*serveur.envoi_liste().remove(etiquette);
					envoi_liste();*/
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		};

		Thread ta=new Thread(runnable,"thread");
		ta.start();
	}
}
