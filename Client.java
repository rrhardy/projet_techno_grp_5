import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.StringReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import javax.json.*;

public class TestJSON {

	/**
	*	@resum: methode permettant de lire une chaine au format JSON se terminant par un '\n'
	* @param input: le stream sur lequel lire la requête
	* @return: La chaine lue sur le flux au format JSON
	*/
	public static String readJsonString(InputStream input){
		StringBuffer sb = new StringBuffer();
		int c = 0;
		try{
			while(input.available() == 0)
				;
			while( (c=input.read()) != '\n'){
				System.out.print((char)c);
				sb.append((char)c);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Ok");
		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			//exemple de requête JSON de type register 
			Socket s = new Socket("127.0.0.1",1234);
			s.setKeepAlive(true);
			OutputStream out = s.getOutputStream();
			JsonObjectBuilder jb = Json.createObjectBuilder();
			JsonWriter jw = Json.createWriter(out);//Gère le flux json
			jb.add("type","register");//ajout d'un champ 'type' de valeur 'register' dans la requête json
			jb.add("sender_class","GPS");//************** 'sender_class' ** 'GPS' ***********************
			jb.add("sender_name","GPS1.5");//************* 'sender_name' ** 'GPS1.5' ********************
			JsonObject res = jb.build();//création de l'objet json
			StringBuffer sb = new StringBuffer(res.toString());//création d'une chaine au format JSON
			sb.append('\n');//ajout d'un '/n' à la fin
			out.write(sb.toString().getBytes("UTF-8"));//ecriture sur le flux de la chaine au format JSON
			//Partie pour traiter une eventuelle réponse
			/*
			System.out.println("Request sent !"+res.toString());
			InputStream input = s.getInputStream();
			JsonReader jr = Json.createReader(new StringReader(TestJSON.readJsonString(input)));//récupération du flux json
			JsonObject obj = jr.readObject();//lecture du flux
	    System.out.println(obj.getString("type"));//affichage d'un champ
	    System.out.println(obj.getString("ack"));
	    System.out.println(obj.getInt("id"));
	    s.close();//fermeture du socket
	    */
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

