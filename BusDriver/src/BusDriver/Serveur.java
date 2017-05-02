package BusDriver;

import java.io.IOException;
import java.net.ServerSocket;

import utils.Protocol;

/**
 * Cette classe permet de lancer le serveur sur toute les ip disponible sur le port 7182
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class Serveur {
	

	public static void main(String[] args) throws IOException{
		ClientManager[] clients;
		int nbClients;
		clients = new ClientManager[1024];
		nbClients = 0;
		
		@SuppressWarnings("resource")
		ServerSocket srv = new ServerSocket(Protocol.PORT_NUMBER);
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
