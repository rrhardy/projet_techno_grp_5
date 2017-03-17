import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.json.*;


public class TestSrvJSON {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocket srv = null;
		Socket cli = null;
		try {
			srv = new ServerSocket(1234);
			cli = srv.accept();
			InputStream input = cli.getInputStream();
			JsonReader jr = Json.createReader(input);//récupération du flux json
			JsonObject obj = jr.readObject();//lecture du flux
	     	System.out.println(obj.getString("type"));//affichage d'un champ
	     	System.out.println(obj.getString("sender_class"));
	     	System.out.println(obj.getString("sender_name"));
	     	srv.close();//fermeture du socket
	    	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
