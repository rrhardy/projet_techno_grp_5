package BusDriver;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import javax.json.*;

import utils.Protocol;

/** 
 * Cette classe permet de g�rer le socket d'un client connect� au Bus
 * Ce socket est g�r� par un thread (fonction run)
 * 
 * @author Robin Hardy 
 * @author Paul Jos�-Vedrenne
 */
public class ClientManager extends Thread{

	//PRIVATE VARIABLE
	private OutputStream out;//socket output Stream
	private InputStream in;//socket input stream
	private String deviceClass;//Device class
	private String deviceName;//device name
	private int id;//identifiant 
	private boolean status;//Auth status
	private ClientManager[] tabClients;//tableau des clients 
	private MessageQueue queue;//file de message
	
	/**
	 * Cree une instance de ClientManger servant a gerer un socket connecte au bus
	 *  
	 * @param client Le socket a gerer
	 * @param id L'identifiant du socket a gerer (represente le sender_id pour un sender)
	 * @param tabClients Le tableau qui contient les autre instance ClientManager 
	 * demarre sur le serveur, permet a un clients observateur de recuperer des informations sur les autre client 
	 */
	public ClientManager(Socket client, int id, ClientManager[] tabClients){
		try{
			this.out = client.getOutputStream();
			this.in = client.getInputStream();
		}catch(IOException e){
			e.printStackTrace();
		}
		this.id = id;
		this.status = false;
		this.tabClients = tabClients;
		this.queue = new MessageQueue(50);
	}
	
	/**
	 * @return L'identifiant du dernier message envoye par le client connecte
	 */
	public int getLastMessageId(){
		return this.queue.getLastId();
	}
	
	/**
	 * @return La classe du device connecte (GPS,gyroscope,acclerometer,...)
	 */
	public String getDeviceClass(){
		return new String(deviceClass);
	}
	
	/**
	 * @param deviceClass La classe du device 
	 */
	public void setDeviceClass(String deviceClass){
		this.deviceClass = new String(deviceClass);
	}
	
	/**
	 * @return Le nom du device
	 */ 
	public String getDeviceName(){
		return new String(deviceName);
	}
	
	/**
	 * @param deviceName Le nouveau nom du device
	 */
	public void setDeviceName(String deviceName){
		this.deviceName = new String(deviceName);
	}
	
	/**
	 * @return L'identifiant du client connecte au bus (sender_id)
	 */
	public int getID(){
		return this.id;
	}
	
	/**
	 * @param id Le nouvelle identifiant du device connecte au bus
	 */
	public void setID(int id){
		this.id = id;
	}
	
	/**
	 * @return True si enregistré (registrer ok) False sinon
	 */
	public boolean isConnected(){
		return this.status;
	}
	
	/**
	 * @param status Le nouveau status de connection
	 */
	public void setConnected(boolean status){
		this.status = status;
	}
	
	
	/*
	 * PRIVATE FUNCTIONS FOR INTERNAL CLASS TREATMENT
	*/
	
	/*Ajoute un nouveau message (contents) à la file*/
	private void addContent(JsonObject contents){
		BusMessage mess = null;
		if(this.deviceClass.equals("GPS")){
			if(contents.isNull("lat") || contents.isNull("lng")){
				sendError("send",400);
			}
			else{
				mess = (BusMessage)new GpsMessage(contents.getJsonNumber("lat").doubleValue(),contents.getJsonNumber("lng").doubleValue());
			}
		}
		
		else if(this.deviceClass.equals("Accelerometer") || this.deviceClass.equals("Gyroscope")){
			if(contents.isNull("x") || contents.isNull("y") || contents.isNull("z")){
				sendError("send",400);
			}
			else{
				mess = (BusMessage)new TriPointMessage(contents.getJsonNumber("x").doubleValue(), contents.getJsonNumber("y").doubleValue(),contents.getJsonNumber("z").doubleValue());
			}
			
		}
		
		else
			sendError("send",400);
		
		queue.add(mess);
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("type", "send");
		this.createOkResponseBuilder(job);
		this.writeJsonString(job.build());
	}
	
	/*Envoie une erreur identifi� par repType et code au client*/
	private void sendError(String repType, int code){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		JsonObjectBuilder ack = Json.createObjectBuilder();
		jb.add("type",repType);
			ack.add("resp","error");
			ack.add("error_id",code);
		jb.add("ack",ack.build());
		writeJsonString(jb.build());
		
	
	}
	
	/*Methode d'envoie d'un acquitement positif de connection*/
	private void ackConnect(){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("type","register");
		createOkResponseBuilder(jb);
		jb.add("sender_id",this.id);
		JsonObject res = jb.build();
		this.writeJsonString(res);
		this.status = true;
	}
	
	/*Adaptation de la reponse obj pour un envoie bas niveau + envoie*/
	private void writeJsonString(JsonObject obj){
		StringBuffer sb = new StringBuffer(obj.toString());
		sb.append('\n');//Ajout d'un caract�re de fin de trame
		Protocol.sendString(sb.toString(),this.out);
	}
	
	/*
	 * Traitement d'une demande de connection, lecture de jobConnect contenant
	 * Contenant une requete de type connect
	 * 
	 * */
	private void getConnect(JsonObject jobConnect){
		this.deviceClass = jobConnect.getString("sender_class");
		this.deviceName = jobConnect.getString("sender_name");
		this.ackConnect();
	}
	
	/*Recuperation de info d'un device connect� sous forme 
	 * d'object JSON
	 */
	private JsonObject getClientJsonObject(ClientManager client){
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("sender_id",client.getID());
		job.add("sender_class",client.getDeviceClass());
		job.add("sender_name",client.getDeviceName());
		job.add("last_message_id",client.getLastMessageId());
		
		return job.build();
	}
	
	/*Recup�ration pour une commande de type list de tous les clients connect� sous forme
	 * d'un tableau JSON
	 */
	private JsonArray getConnectedDevice(){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(int i=0 ; i<this.tabClients.length;i++)
			if(tabClients[i] != null)
				if(tabClients[i].isConnected())
					jab.add(this.getClientJsonObject(tabClients[i]));
		return jab.build();
	}
	
	/*Similaire � getConnectedDevice mais applique un filtrage sur le ClassName*/
	private JsonArray getConnectedDeviceByClass(String deviceClass){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(int i=0 ; i< this.tabClients.length;i++)
			if(tabClients[i] != null)
				if(tabClients[i].isConnected() && deviceClass.compareTo(tabClients[i].getDeviceClass()) == 0)
					jab.add(this.getClientJsonObject(tabClients[i]));
		return jab.build();
	}
	
	/*///////////////////////////////////////////////////////////// DeviceName*/
	private JsonArray getConnectedDeviceByName(String deviceName){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(int i=0 ; i < tabClients.length; i++)
			if(tabClients[i] != null)
				if(tabClients[i].isConnected() && deviceName.compareTo(this.tabClients[i].getDeviceName()) == 0)
					jab.add(this.getClientJsonObject(tabClients[i]));
		return jab.build();
	}
	
	/*Creer une reponse de type OK sur l'entete job*/
	private void createOkResponseBuilder(JsonObjectBuilder job){
		JsonObjectBuilder jobAck = Json.createObjectBuilder();
		jobAck.add("resp","ok");
		job.add("ack",jobAck.build());
	}
	
	/*Fonction permettant de traiter une commande recu par le BUS*/
	private void treatCmd(String cmd,JsonObject obj)
	{
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		if(cmd.equals("register")){
			System.out.println("Demande de connexion");
			this.getConnect(obj);
			return;
		}
		
		else if(cmd.equals("list")){
			System.out.println("Demande de listing");
			job.add("type","list");
			String name = obj.getString("sender_name","");
			String dclass = obj.getString("sender_class","");
			if(name.compareTo("") == 0 && dclass.compareTo("") == 0){
				createOkResponseBuilder(job);
				job.add("results",this.getConnectedDevice());
			}
			
			else if(name.compareTo("") == 0){
				createOkResponseBuilder(job);
				job.add("results",this.getConnectedDeviceByClass(obj.getString("sender_class")));
			}
			
			else{
				createOkResponseBuilder(job);
				job.add("results",this.getConnectedDeviceByName(obj.getString("sender_name")));
			}
			
		}
		
		else if(cmd.equals("get")){
			job.add("type", "get");
			if(obj.getInt("sender_id",-1) == -1){
				sendError("get",400);
				return;
			}
			ClientManager tmp = this.tabClients[obj.getInt("sender_id")];
			if(tmp==null){
				sendError("get",500);
				return;
			}
			if(tmp.queue.isEmpty()){
				sendError("get",500);
				return;
			}
			if(tabClients[obj.getInt("sender_id")] == null){
				sendError("get",450);
				return;
			}
			BusMessage mess = tabClients[obj.getInt("sender_id")].queue.getById(obj.getInt("msg_id"));
			if(mess == null)
				sendError("get", 404);
			createOkResponseBuilder(job);
			job.add("msg_id", obj.getInt("msg_id"));
			job.add("date", mess.getDate());
			job.add("contents", mess.getContent());
		}
		
		else if(cmd.equals("get_last")){
			job.add("type", "get_last");
			if(obj.getInt("sender_id",-1) == -1){
				sendError("get_last",400);
				return;
			}
			ClientManager tmp = this.tabClients[obj.getInt("sender_id")];
			if(tmp==null){
				sendError("get_last",500);
				return;
			}
			if(tmp.queue.isEmpty()){
				sendError("get_last",500);
				return;
			}
			if(tabClients[obj.getInt("sender_id")] == null){
				sendError("get_last",450);
				return;
			}
			
			BusMessage mess = tabClients[obj.getInt("sender_id")].queue.getLast();
			createOkResponseBuilder(job);
			job.add("msg_id",tabClients[obj.getInt("sender_id")].queue.getLastId());
			job.add("date",mess.getDate());
			job.add("contents",mess.getContent());
			
		}
		
		else{
			if(!this.isConnected()){
				this.sendError("register",450);
				return;
			}
		}
		
		if(this.isConnected()){
			
			if(cmd.equals("send")){
				System.out.println("Reception d'un message");
				if(obj.isNull("sender_id") || obj.isNull("contents")){
					sendError("send",400);
					return ;
				}
				if(obj.getInt("sender_id") != this.getID()){
					sendError("send",401);
					return ;
				}
				 
				JsonObject contents = obj.getJsonObject("contents");
				this.addContent(contents);
				
			}
			
		}
		
		if(!job.build().isEmpty()){
			System.out.println(job.build().toString());
			this.writeJsonString(job.build());
		}
	}

	
	//FONCTION PRINCIPALE DU THREAD
	/**
	* Boucle tant que le client n'as pas envoy� de req�te de type deregister
	* 
	* @see java.lang.Thread#run()
	*/
	public void run()
	{
		JsonReader jr;
		JsonObject obj;
		while(true){
			jr = Json.createReader(new StringReader(Protocol.readJsonString(this.in)));
			obj = jr.readObject();
			treatCmd(obj.getString("type"),obj);
		}
	}
}
				
