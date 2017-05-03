package Drivers;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;

import javax.json.*;

import BusDriver.BusMessage;

import utils.Protocol;

/**
 * Cette classe représente l'interface réseau du driver, elle permet d'envoyer des requête spécifique
 * au device de type sender
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class Client {
	
	private int id;
	public String senderClass;
	public String senderName;
	private InputStream in;
	private OutputStream out;
	
	/**
	 * Cree une nouvelle interface réseau pour une adresse donné, une classe et un nom de device
	 * 
	 * @param adr L'adresse du Bus sur lequel se connecter
	 * @param senderClass La classe du device
	 * @param senderName Le nom du device
	 */
	public Client(String adr,String senderClass, String senderName){
		Socket s;
		try {
			s = new Socket(adr,Protocol.PORT_NUMBER);
			this.in = s.getInputStream();
			this.out = s.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.id = 0;
		this.senderClass = senderClass;
		this.senderName = senderName;
	}
	
	/**
	 * Accesseur sur l'id du device donné par le bus
	 * 
	 * @return L'identifiant du device attribué par le bus
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * Permet d'envoyer une requete de type register, Si une erreur se produit l'erreur est affiché sur la sortie
	 * standard sous forme de code 
	 * 
	 * @return True si le bus à accepter l'enregistrement, False sinon
	 */
	public boolean register(){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		System.out.print("Demande d'enregistrement...");
		jb.add("type","register");//ajout d'un champ 'type' de valeur 'register' dans la requête json
		jb.add("sender_class",senderClass);//************** 'sender_class' ** 'GPS' ***********************
		jb.add("sender_name",senderName);//************* 'sender_name' ** 'GPS1.5' ********************
		JsonObject res = jb.build();//création de l'objet json
		writeJsonString(out,res);
		String ret = Protocol.readJsonString(this.in);
		JsonObject resp = Json.createReader(new StringReader(ret)).readObject();
		if(resp.getString("type").compareTo("register") == 0){
			if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
				this.id = resp.getInt("sender_id");
				System.out.println("id: "+this.id);
				return true;
			}

			else{
				System.out.println("Error: "+resp.getJsonObject("ack").getInt("error_id"));
				return false;
			}
		}
		System.err.println("Mauvaise Reponse Serveur");
		return false;
		
	}
	
	/**
	 * Permet d'envoyer une demande de deconnexion au bus
	 * 
	 * @return true si la demande à été accepté false sinon
	 */
	public boolean deRegister(){
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("type", "deregister");
		job.add("sender_id",this.id);
		JsonObject res = job.build();
		writeJsonString(out,res);
		return this.analyzeResponse("deregister");
	}
	
	/**
	 * Permet d'envoyer le message bm sur le bus
	 * 
	 * @param bm Le message à envoyer
	 * @return True si le message s'est enregistré sur le bus, False sinon
	 */
	public boolean sendMessage(BusMessage bm){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("type", "send");
		jb.add("sender_id", id);
		jb.add("contents",bm.getContent());
		jb.build();
		JsonObject res = jb.build();
		writeJsonString(out,res);
		return this.analyzeResponse("send");
	}
	
	public static void writeJsonString(OutputStream out, JsonObject job){
		StringBuffer sb = new StringBuffer(job.toString());
		sb.append('\n');
		Protocol.sendString(sb.toString(), out);
	}

	private boolean analyzeResponse(String type){
		String ret = Protocol.readJsonString(this.in);
		JsonObject resp = Json.createReader(new StringReader(ret)).readObject();
		if(resp.getString("type").compareTo(type) == 0){
			if(resp.getJsonObject("ack").getString("resp").compareTo("ok") == 0){
				return true;
			}

			else{
				System.out.println("Error: "+resp.getJsonObject("ack").getInt("error_id"));
				return false;
			}
		}
		System.err.println("Mauvaise Reponse Serveur");
		return false;
	}
}

