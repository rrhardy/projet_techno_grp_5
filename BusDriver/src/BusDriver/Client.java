package BusDriver;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.json.*;

public class Client {

	public static void writeJsonString(OutputStream out, JsonObject job){
		StringBuffer sb = new StringBuffer(job.toString());
		sb.append('\n');
		try{
			out.write(sb.toString().getBytes("UTF-8"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
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
				sb.append((char)c);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int id = 0;
		try {
			//exemple de requête JSON de type register 
			@SuppressWarnings("resource")
			Socket s = new Socket("127.0.0.1",1234);
			s.setKeepAlive(true);
			OutputStream out = s.getOutputStream();
			JsonObjectBuilder jb = Json.createObjectBuilder();
			System.out.print("Demande d'enregistrement...");
			jb.add("type","register");//ajout d'un champ 'type' de valeur 'register' dans la requête json
			jb.add("sender_class","GPS");//************** 'sender_class' ** 'GPS' ***********************
			jb.add("sender_name","GPS1.5");//************* 'sender_name' ** 'GPS1.5' ********************
			JsonObject res = jb.build();//création de l'objet json
			writeJsonString(out,res);
			String ret = readJsonString(s.getInputStream());
			//System.out.println(ret);
			JsonObject resp = Json.createReader(new StringReader(ret)).readObject();
			if(resp.getString("type").compareTo("register") == 0){
				if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
					System.out.println(" Enregistré ! id:"+resp.getInt("sender_id"));
					id = resp.getInt("sender_id");
				}
				else
					System.out.println("Erreur : "+resp.getJsonObject("ack").getInt("error_id"));
			}
			else
				System.out.println("Mauvaise réponse serveur");
			
			GpsMessage gm = new GpsMessage(50.25212,-45.25652);
			jb = Json.createObjectBuilder();
			jb.add("type", "send");
			jb.add("sender_id", id);
			jb.add("contents",gm.getContent());
			jb.build();
			res = jb.build();
			writeJsonString(out,res);
			ret = readJsonString(s.getInputStream());
			resp = Json.createReader(new StringReader(ret)).readObject();
			if(resp.getString("type").compareTo("send") == 0){
				if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
					System.out.println("Message envoyé et reçu par le bus!");
				}

				else{
					System.out.println("Error: "+resp.getJsonObject("ack").getInt("error_id"));
				}
			}
			else{
				System.out.println("Mauvaise Réponse Serveur");
			}
			jb = Json.createObjectBuilder();
			jb.add("type","list");
			jb.add("sender_name","GPS1.5");
			res = jb.build();
			writeJsonString(out,res);
			ret = readJsonString(s.getInputStream());
			int a = 0;
			resp = Json.createReader(new StringReader(ret)).readObject();
			if(resp.getString("type","").compareTo("list") == 0){
				if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
					JsonArray list = resp.getJsonArray("results");
					a = Json.createReader( new StringReader(list.get(0).toString())).readObject().getInt("sender_id");
					System.out.println("Listing recu, nombre de rep reçu: "+list.size());
				}
				else{
					System.out.println("Error: "+resp.getJsonObject("ack").getInt("error_id"));
				}
			}
			else{
				System.out.println("Mauvaise Réponse Serveur");
			}
			jb = Json.createObjectBuilder();
			jb.add("type","get_last");
			jb.add("sender_id", a);
			res = jb.build();
			writeJsonString(out,res);
			ret = readJsonString(s.getInputStream());
			resp = Json.createReader(new StringReader(ret)).readObject();
			if(resp.getString("type","").compareTo("get_last") == 0){
				if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
					System.out.println("Reception du resultat: ");
					System.out.println(resp.toString());
				}
				else{
					System.out.println("Error: "+resp.getJsonObject("ack").getInt("error_id"));
				}
			}
			else{
				System.out.println("Mauvaise reponse Serveur");
			}
			//JsonObject job = Json.createReader(new StringReader(ret));
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

