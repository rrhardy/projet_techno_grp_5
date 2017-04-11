package Viewer;

import javax.json.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.Scanner;

public class Viewer{
	/**
	 * 
	 */
	private Socket conn;
	
	public Viewer(String busAdr){
		try{
			this.conn = new Socket(busAdr,1234);
		}catch(IOException e){
			e.printStackTrace();
			System.exit(-1);
		}
		
	}
	
	private String readJsonString(InputStream input){
		StringBuffer sb = new StringBuffer();
		int c = 0;
		try{
			while(input.available() == 0)
				;
			while( (c=input.read()) != '\n'){
				//System.out.print((char)c);
				sb.append((char)c);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		//System.out.println("Ok");
		return sb.toString();
	}
	
	private JsonObject sendRequest(JsonObject jo){
		StringBuffer data = new StringBuffer();
		JsonReader jr = null;
		data.append(jo.toString());
		data.append('\n');
		try {
			this.conn.getOutputStream().write(data.toString().getBytes("UTF-8"));
			jr = Json.createReader(new StringReader(this.readJsonString(this.conn.getInputStream())));
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
	
	public void menuList(){
		int chx = -1;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		try {
			Runtime.getRuntime().exec("clear");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("1- By Name");
		System.out.println("2- By Class");
		while(chx <0 || chx > 2){
			System.out.println("CHX:(1-2)");
			chx = sc.nextInt();
		}
		JsonObject[] res = this.menuListBy(chx);
		for(int i=0 ; i<res.length ; i++)
			if(res[i] != null)
				System.out.println("id: "+res[i].getInt("sender_id",-1)+ ",sender_class "+res[i].getString("sender_class")+" ,sender_name "+res[i].getString("sender_name")+", last_message_id: "+res[i].getInt("last_message_id",-1));
		System.out.println("Appuyez sur entree pour revenir au menu principal...");
		sc.next();
	}
	
	private JsonObject menuGetLast(){
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Entrez l'id du driver:");
		int chx = sc.nextInt();
		return this.getLastMessage(chx);
	}
	
	private JsonObject[] menuListBy(int chx){
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		if(chx == 1){
			System.out.println("Entrez le nom du driver:");
			String name = sc.next();
			return this.getListByName(name);
		}
		if(chx == 2){
			System.out.println("Entrez la classe du driver:");
			String classe = sc.next();
			return this.getListByClass(classe);
		}
		
		if(chx == 0)
			return this.getAllList();
		return null;
	}
	
	public void mainMenu(){
		int chx = 0;
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		System.out.println("Visionneur:");
		System.out.println("ACTIONS:");
		System.out.println("1- list");
		System.out.println("2- get_last");
		System.out.println("3- get");
		while(chx <1 || chx>3){
			System.out.println("?(1-3)");
			chx = sc.nextInt();
		}
		if(chx == 1)
			this.menuList();
		if(chx == 2){
			JsonObject last = this.menuGetLast();
			System.out.println("Last Msg: "+last.getJsonObject("contents").toString());
			System.out.println("horodatage: "+last.getJsonNumber("date").longValue());
		}
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Viewer v = new Viewer("127.0.0.1");
		
	}

}
