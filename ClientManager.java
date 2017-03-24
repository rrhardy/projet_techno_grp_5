package BusDriver;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.ServerSocket;
import javax.json.*;

public class ClientManager extends Thread{

	private OutputStream out;
	private InputStream in;
	private String deviceClass;
	private String deviceName;
	private int id;
	private boolean status;//Auth status
	private ClientManager[] tabClients;
	private MessageQueue queue;
	
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
	
	public int getLastMessageId(){
		return 0;//TODO implements fonction
	}
	
	public void setLastMessageId(int lastMessageId){
		;//TODO implements fonction
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
	
	private void addContent(JsonObject contents){
		if(this.deviceClass.equals("GPS")){
			if(contents.isNull("lat") || contents.isNull("lng")){
				sendError("send",400);
			}
			else{
				GpsMessage mess = new GpsMessage(contents.getJsonNumber("lat").doubleValue(),contents.getJsonNumber("lng").doubleValue());
				queue.add(mess);
				JsonObjectBuilder job = Json.createObjectBuilder();
				job.add("type","send");
				this.createOkResponseBuilder(job);
				this.writeJsonString(job.build());
			}
		}
		
		else if(this.deviceClass.equals("Accelerometer")){
			if(contents.isNull("x") || contents.isNull("y") || contents.isNull("z")){
				sendError("send",400);
			}
			
		}
		
		else if(this.deviceClass.equals("Gyroscope")){
			if(contents.isNull("x") || contents.isNull("y") || contents.isNull("z")){
				sendError("send",400);
			}
		}
		
		else
			sendError("send",400);
	}
	
	private void sendError(String repType, int code){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		JsonObjectBuilder ack = Json.createObjectBuilder();
		jb.add("type",repType);
			ack.add("resp","error");
			ack.add("error_id",code);
		jb.add("ack",ack.build());
		writeJsonString(jb.build());
		
	
	}
	
	private void sendString(String toSend){
		try{
			this.out.write(toSend.getBytes("UTF-8"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void ackConnect(){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("type","register");
		jb.add("ack","ok");
		jb.add("id",this.id);
		JsonObject res = jb.build();
		this.writeJsonString(res);
		this.status = true;
	}
	
	private void writeJsonString(JsonObject obj){
		StringBuffer sb = new StringBuffer(obj.toString());
		sb.append('\n');
		sendString(sb.toString());
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
	
	private void getConnect(JsonObject jobConnect){
		this.deviceClass = jobConnect.getString("sender_class");
		this.deviceName = jobConnect.getString("sender_name");
		this.ackConnect();
	}
	
	private JsonObject getClientJsonObject(ClientManager client){
		JsonObjectBuilder job = Json.createObjectBuilder();
		job.add("sender_id",client.getID());
		job.add("sender_class",client.getDeviceClass());
		job.add("sender_name",client.getDeviceName());
		job.add("last_message_id",client.getLastMessageId());
		
		return job.build();
	}
	
	private JsonArray getConnectedDevice(){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(int i=0 ; i<this.tabClients.length;i++)
			if(tabClients[i].isConnected())
				jab.add(this.getClientJsonObject(tabClients[i]));
		return jab.build();
	}
	
	private JsonArray getConnectedDeviceByClass(String deviceClass){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(int i=0 ; i< this.tabClients.length;i++)
			if(tabClients[i].isConnected() && deviceClass.equals(tabClients[i].getDeviceClass()))
				jab.add(this.getClientJsonObject(tabClients[i]));
		return jab.build();
	}
	
	private JsonArray getConnectedDeviceByName(String deviceName){
		JsonArrayBuilder jab = Json.createArrayBuilder();
		for(int i=0 ; i < tabClients.length; i++)
			if(tabClients[i].isConnected() && deviceName.equals(tabClients[i].getDeviceName()))
				jab.add(this.getClientJsonObject(tabClients[i]));
		return jab.build();
	}
	
	private void createOkResponseBuilder(JsonObjectBuilder job){
		JsonObjectBuilder jobAck = Json.createObjectBuilder();
		jobAck.add("resp","ok");
		job.add("resp",jobAck.build());
	}
	
	private void treatCmd(String cmd,JsonObject obj)
	{
		JsonObjectBuilder job = Json.createObjectBuilder();
		
		if(cmd.equals("register")){
			System.out.println("Demande de connexion");
			this.getConnect(obj);
			return;
		}
		
		else if(cmd.equals("list")){
			job.add("type","list");
			if(obj.isNull("sender_name")&&obj.isNull("sender_class")){
				createOkResponseBuilder(job);
				job.add("results",this.getConnectedDevice());
			}
			
			else if(obj.isNull("sender_name")){
				createOkResponseBuilder(job);
				job.add("result",this.getConnectedDeviceByClass(obj.getString("sender_class")));
			}
			
			else if(obj.isNull("sender_class")){
				createOkResponseBuilder(job);
				job.add("result",this.getConnectedDeviceByName(obj.getString("sender_name")));
			}
			
			else{
				this.sendError("list",400);		
				return;
			}
		}
		
		else if(cmd.equals("get")){
			
		}
		
		else if(cmd.equals("get_last")){
			if(obj.isNull("sender_id") || obj.isNull("msg_id")){
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
			BusMessage mess = tmp.queue.getLast();
			createOkResponseBuilder(job);
			job.add("msg_id",tmp.queue.getLastId());
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
			
		this.writeJsonString(job.build());
	}

	
	public void run()
	{
		JsonReader jr;
		JsonObject obj;
		while(true){
			jr = Json.createReader(new StringReader(this.readJsonString()));
			obj = jr.readObject();
			treatCmd(obj.getString("type"),obj);
		}
	}
	
	public static void main(String[] args){
		ClientManager cm = null;
		try{
			ServerSocket srv = new ServerSocket(1234);
			//cm = new ClientManager(srv.accept(),0);
		}catch(IOException e){
			e.printStackTrace();
		}
		cm.start();
		System.out.println("thread started");
		while(true)
			;			
	}
	
}
				
