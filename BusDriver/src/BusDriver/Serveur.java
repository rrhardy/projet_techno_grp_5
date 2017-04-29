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
		System.out.println("Serveur démarré");
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
		
	}

}
