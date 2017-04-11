package Viewer;
import java.sql.Timestamp;

import javax.json.JsonObject;

public class GPSMapView extends GViewAbstract{
	
	public GPSMapView(int id, Viewer v){
		super(id,v);
		//this.jmv = new JMapViewer();
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
			System.out.println("le "+ t.toString()+" lat:"+pos.getJsonNumber("lat").doubleValue()+ "lng:"+pos.getJsonNumber("lng").doubleValue());
			//MapMarkerDot mmc = new MapMarkerDot(resp.getJsonNumber("lat").doubleValue(),resp.getJsonNumber("lng").doubleValue());
			//this.jmv.addMapMarker(mmc);
		}
	}
}
