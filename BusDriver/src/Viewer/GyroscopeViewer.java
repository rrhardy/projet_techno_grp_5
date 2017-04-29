package Viewer;

import java.sql.Timestamp;

import javax.json.JsonObject;
import javax.swing.JFrame;

public class GyroscopeViewer extends GViewAbstract{
	
	public GyroscopeViewer(JFrame frame,int id, Viewer v){
		super(frame,id,v);
	}
	
	public void run(){
		while(true){
			JsonObject resp = v.getLastMessage(this.id);
			try{
				Thread.sleep(1000);
			}catch(Exception e){
				e.printStackTrace();
			}
			Timestamp t = new Timestamp(resp.getJsonNumber("date").longValue());
			JsonObject pos = resp.getJsonObject("contents");
			System.out.println("le "+ t.toString()+" x:"+pos.getJsonNumber("x").doubleValue()+ "y:"+pos.getJsonNumber("y").doubleValue()+ " z:"+pos.getJsonNumber("z").doubleValue());
		}
	}
}
