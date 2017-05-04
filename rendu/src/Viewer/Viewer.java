package Viewer;

import javax.json.*;

import utils.Protocol;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

/**
 * Cette classe fonctionne comme Client mais est spécifique aux Observateurs 
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class Viewer{

	private Socket conn;
	
	/**
	 * Cree un nouvel interfacage de type Viewer avec le bus
	 * 
	 * @param busAdr L'ip du bus
	 */
	public Viewer(String busAdr){
		try{
			this.conn = new Socket(busAdr,Protocol.PORT_NUMBER);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	/**
	 * Retourne un tableau contenant la liste des device identifé par name connecté au bus sous forme d'objet JSON
	 * 
	 * @param name Le nom du device voulu
	 * @return La liste des device identifé par name
	 */
	public JsonObject[] getListByName(String name){
		System.out.println("Recherche de "+name);
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("type", "list");
		job.add("sender_name",name);
		JsonObject resp = this.sendRequest(job.build());
		if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
			JsonObject[] res = new JsonObject[1000];
			for(int i=0 ; i < resp.getJsonArray("results").size() ; i++){
				res[i] = resp.getJsonArray("results").getJsonObject(i);
			}
			return res;
		}
		else{
			System.err.println("Erreur: "+resp.getJsonObject("ack").getInt("error_id"));
			return null;
		}
	}
	
	/**
	 * Comme getListByName mais pour la classe$
	 * 
	 * @param s_class La classe des devices voulu
	 * @return La liste des devices connecté au bus et identifé par s_class
	 */
	public JsonObject[] getListByClass(String s_class){
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("type", "list");
		job.add("sender_class",s_class);
		JsonObject resp = this.sendRequest(job.build());
		if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
			JsonObject[] res = new JsonObject[1000];
			for(int i=0 ; i < resp.getJsonArray("results").size() ; i++){
				res[i] = resp.getJsonArray("results").getJsonObject(i);
			}
			return res;
		}
		else{
			System.err.println("Erreur: "+resp.getJsonObject("ack").getInt("error_id"));
			return null;
		}
	}
	
	/**
	 * Permet de récupérer toute la liste des devices connectés au bus
	 * 
	 * @return La liste entière des device connectés
	 */
	public JsonObject[] getAllList(){
		System.out.println("Demande de toute la list");
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("type", "list");
		JsonObject resp = this.sendRequest(job.build());
		if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
			JsonObject[] res = new JsonObject[1000];
			for(int i=0 ; i < resp.getJsonArray("results").size() ; i++){
				res[i] = resp.getJsonArray("results").getJsonObject(i);
			}
			return res;
		}
		else{
			System.err.println("Erreur: "+resp.getJsonObject("ack").getInt("error_id"));
			return null;
		}
	}
	
	/**
	 * Permet de récupérer le dernier message d'un sender spécifique 
	 * 
	 * @param sender_id L'identifiant du device sender
	 * @return Le message sous forme d'objet JSON, vaut null si le device n'est pas trouvé sur le bus
	 */
	public JsonObject getLastMessage(int sender_id){
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("type", "get_last");
		job.add("sender_id", sender_id);
		JsonObject resp = this.sendRequest(job.build());
		if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
			JsonObjectBuilder tmpjob = Json.createObjectBuilder();
			long date = resp.getJsonNumber("date").longValue();
			tmpjob.add("date", date);
			tmpjob.add("contents", resp.getJsonObject("contents"));
			return tmpjob.build();
		}
		else{
			System.err.println("Erreur "+resp.getJsonObject("ack").getInt("error_id"));
			return null;
		}
	}
	
	/**
	 * Permet de récupérer un message précis d'un device connecté au bus
	 * 
	 * @param sender_id L'identifiant du device connecté
	 * @param msg_id L'identifiant du message voulu
	 * @return L'objet JSON représtant le message, null si le message ou le sender n'est pas trouvé sur le bus
	 */
	public JsonObject getMessage(int sender_id, int msg_id){
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("type","get");
		job.add("sender_id", sender_id);
		job.add("msg_id", msg_id);
		JsonObject resp = this.sendRequest(job.build());
		if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
			JsonObjectBuilder tmpjob = Json.createObjectBuilder();
			long date = resp.getJsonNumber("date").longValue();
			tmpjob.add("date", date);
			tmpjob.add("contents", resp.getJsonObject("contents"));
			return tmpjob.build();
		}
		else{
			System.err.println("Erreur "+resp.getJsonObject("ack").getInt("error_id"));
			return null;
		}
	}
	
	private JsonObject sendRequest(JsonObject jo){
		StringBuffer data = new StringBuffer();
		JsonReader jr = null;
		data.append(jo.toString());
		data.append('\n');
		try {
			Protocol.sendString(data.toString(), this.conn.getOutputStream());
			jr = Json.createReader(new StringReader(Protocol.readJsonString(this.conn.getInputStream())));
			return jr.readObject();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
