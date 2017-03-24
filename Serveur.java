package BusDriver;

import java.io.IOException;
import java.net.ServerSocket;


public class Serveur {
	


	public static void main(String[] args) throws IOException{
		ClientManager[] clients;
		int nbClients;
		clients = new ClientManager[1024];
		nbClients = 0;
		
		@SuppressWarnings("resource")
		ServerSocket srv = new ServerSocket(1234);
		
		ClientManager cm = null;
		
		//boucle d'acceptation des connexions
		while(true){
			try{
				cm = new ClientManager(srv.accept(), nbClients,clients);
			}catch(IOException e){
				e.printStackTrace();
			}
			cm.start();
			System.out.println("thread " + nbClients + " started");
			clients[nbClients++] = cm;
		}
		
		/*
		while(true){
			
			//réception de l'objet JSON
			byte[] data = new byte[1024];
			entree.read(data);
			String object = new String(data, "UTF-8");
		
		//affichage sur la sortie standard
		System.out.println(object);
		*/
	}

}
