package Viewer;

import java.sql.Timestamp;

import javax.json.JsonObject;

public class GyroscopeViewer extends GViewAbstract{
	
	public GyroscopeViewer(int id, Viewer v){
		super(id,v);
	}
	
	public void startView(){
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
