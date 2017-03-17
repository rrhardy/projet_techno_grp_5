package BusDriver;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.net.Socket;

import javax.json.*;

public class ClientManager extends Thread{

	private OutputStream out;
	private InputStream in;
	private Socket cli;
	private String deviceClass;
	private String deviceName;
	private int id;
	private boolean status;//Auth status
	
	public ClientManager(Socket client, int id){
		this.cli = client;
		try{
			this.out = client.getOutputStream();
			this.in = client.getInputStream();
		}catch(IOException e){
			e.printStackTrace();
		}
		this.id = id;
		this.status = false;
	}
	
	public String getDeviceClass(){
		return new String(deviceClass);
	}
	
	public void setDeviceClass(String deviceClass){
		this.deviceClass = new String(deviceClass);
	}
	
	public String getDeviceName(){
		return new String(deviceName);
	}
	
	public void setDeviceName(String deviceName){
		this.deviceName = new String(deviceName);
	}
	
	public int getID(){
		return this.id;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public boolean isConnected(){
		return this.status;
	}
	
	public void setConnected(boolean status){
		this.status = status;
	}
	
	private void sendError(String repType, int code){
		JsonWriter jw = Json.createWriter(out);
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("type","register");
		jb.add("ack","error");
		jb.add("code",code);
		JsonObject res = jb.build();
		jw.writeObject(res);
	}
	
	private void ackConnect(){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("type","register");
		jb.add("ack","ok");
		jb.add("id",this.id);
		JsonObject res = jb.build();
		try{
			this.out.write(this.writeJsonString(res).getBytes("UTF-8"));
		}
		catch(IOException e){
			e.printStackTrace();
		}
		this.status = true;
	}
	
	private String writeJsonString(JsonObject obj){
		StringBuffer sb = new StringBuffer(obj.toString());
		sb.append('\n');
		return sb.toString();
	}
	
	private String readJsonString(){
		StringBuffer sb = new StringBuffer();
		int c = 0;
		try{
			while(this.in.available() == 0)
				;
			while( (c=this.in.read()) != '\n'){
				System.out.print((char)c);
				sb.append((char)c);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		System.out.println("Ok");
		return sb.toString();
	}
	
	private void getConnect()
	{
		JsonReader jr = Json.createReader(new StringReader(this.readJsonString()));
		JsonObject obj = jr.readObject();
		if(!obj.getString("type").equals("register")){
			this.sendError("register",350);
		}
		else{
			this.deviceClass = obj.getString("sender_class");
			this.deviceName = obj.getString("sender_name");
			this.ackConnect();
		}
	}
	
	private void treatCmd(String cmd)
	{
		if(cmd.equals("list"){
		
		}
		if(cmd.equals("send"){
		
		}
		if(cmd.equals("get"){
		
		}
		if(cmd.equals("get_last"){
		
		}
	}
	
	public void run()
	{
		this.getConnect();
		JsonReader jr;
		JsonObject obj;
		String reqType;
		while(true)
			jr = Json.createReader(new StringReader(this.readJsonString()));
			obj = jr.readObject();
			treatCmd(obj.getString("type");
		}
	}
	
	public static void main(String[] args){
		ClientManager cm = null;
		try{
			ServerSocket srv = new ServerSocket(1234);
			cm = new ClientManager(srv.accept(),0);
		}catch(IOException e){
			e.printStackTrace();
		}
		cm.start();
		System.out.println("thread started");
		while(true)
			;			
	}
	
}
				
