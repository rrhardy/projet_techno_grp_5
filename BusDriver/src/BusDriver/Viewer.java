package BusDriver;

import javax.json.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class Viewer {
	private Socket conn;
	
	public Viewer(String busAdr){
		try{
			this.conn = new Socket(busAdr,1234);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private String readJsonString(InputStream input){
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
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
	}

}
