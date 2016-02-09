import java.awt.TextArea;
import java.io.BufferedReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

public class ServeurChat {
	static final int PORT = 6666;
	public HashMap<String,ThreadChat> chat=new HashMap<String,ThreadChat>();

	public ServeurChat() {
		try {
			String etiquette=new String("");
			ServerSocket serverSocket = new ServerSocket(PORT);
			System.out.println("Serveur prêt ");
			while (true) {
				System.out.println("Serveur en Attente");
				Socket socket = serverSocket.accept();
				System.out.println(new Date() + " Client connecté");
				InputStream stream = socket.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
				etiquette=reader.readLine();
				System.out.println(etiquette);
				chat.put(etiquette,new ThreadChat(socket,this,etiquette));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashMap<String,ThreadChat> envoi_liste(){
		return chat;
	}

	public static void main(String[] args) {
		new ServeurChat();
	}
}
