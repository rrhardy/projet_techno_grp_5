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
	
	public static void printCMTab(ClientManager[] cms){
		for(int i=0 ; i< cms.length ; i++){
			if(cms[i]!=null)
				System.out.println("Id: "+i+" CM: "+cms[i].toString());
			else 
				System.out.println("Id: "+i+"(NULL)");
		}
	}
	public static int getNextEmpty(ClientManager[] cms){
		
		for(int i=0 ; i<cms.length ; i++){
			if(cms[i] == null)
				return i;
			if(cms[i].toClean())
				return i;
		}
		return -1;
	}
	
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
			int nextEmpty = Serveur.getNextEmpty(clients);
			if(nextEmpty == -1)
				System.err.println("Capacité max atteinte");
			
			else{
				try{
					cm = new ClientManager(srv.accept(), nextEmpty,clients);
				}catch(IOException e){
					e.printStackTrace();
				}
				cm.start();
				System.out.println("thread " + nbClients + " started");
		
				clients[nextEmpty] = cm;
				nbClients++;
			}
			System.out.println("next "+nextEmpty);
		}
		
	}

}
