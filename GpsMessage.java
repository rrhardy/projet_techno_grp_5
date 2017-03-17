package BusDriver;

import javax.json.*;

public class GpsMessage extends BusMessageAbstract{
	private float lat;
	private float lng;
	
	public GpsMessage(){
		GpsMessage(0.0,0.0);
	}
	
	public GpsMessage(float lat, float lng){
		this.super();
		this.lat = lat;
		this.lng = lng;
	}
	
	public float getLat(){
		return this.lat;
	}
	
	public void setLat(float lat){
		this.lat =lat;
	}
	
	public float getLng(){
		return this.lng;
	}
	
	public void setLng(float lng){
		this.lng = lng;
	}
	
	public JsonObect getContent(){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("lat",this.lat);
		jb.add("lng",this.lng);
		return jb.build();
	}
}
