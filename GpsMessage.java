package BusDriver;

import javax.json.*;

public class GpsMessage extends BusMessageAbstract{
	private double lat;
	private double lng;
	
	public GpsMessage(double lat, double lng){
		super(System.currentTimeMillis());
		this.lat = lat;
		this.lng = lng;
	}
	
	public GpsMessage(){
		new GpsMessage(0.0,0.0);
	}
	
	public double getLat(){
		return this.lat;
	}
	
	public void setLat(double lat){
		this.lat =lat;
	}
	
	public double getLng(){
		return this.lng;
	}
	
	public void setLng(double lng){
		this.lng = lng;
	}
	
	public JsonObject getContent(){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("lat",this.lat);
		jb.add("lng",this.lng);
		return jb.build();
	}
}
