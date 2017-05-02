package BusDriver;

import javax.json.*;

/**
 * Cette classe représente les message envoyé par des drivers de type GPS.
 * Ces messages contienent des informations de type geo-localisation (latitude, longitude). Cette classe hérite de BusMessageAbstract
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class GpsMessage extends BusMessageAbstract{
	private double lat;
	private double lng;
	
	/**
	 * Permet de creer un nouveau message GPS 
	 * 
	 * @param lat La latitude
	 * @param lng La longitude
	 */
	public GpsMessage(double lat, double lng){
		super();
		this.lat = lat;
		this.lng = lng;
	}
	
	/**
	 * Constructeur par défault
	 */
	public GpsMessage(){
		new GpsMessage(0.0,0.0);
	}
	
	/**
	 * Accesseur sur la latitude
	 * 
	 * @return la latitude contenu dans le message
	 */
	public double getLat(){
		return this.lat;
	}
	
	/**
	 * Mutateur sur la latitude
	 * 
	 * @param lat La nouvelle latitude
	 */
	public void setLat(double lat){
		this.lat =lat;
	}
	
	/**
	 * Accesseur sur la longitude
	 * 
	 * @return La longitude contenu dans le message 
	 */
	public double getLng(){
		return this.lng;
	}
	
	/**
	 * Mutateur sur la longitude
	 * 
	 * @param lng La nouvelle longitude
	 */
	public void setLng(double lng){
		this.lng = lng;
	}
	
	/**
	 * Retourne le message sous forme d'objet JSON de ce type:
	 * contents:{lat:LA_LATITUDE, lng:LA_LONGITUDE
	 * 
	 * @return L'objet JSON représentant le message
	 */
	public JsonObject getContent(){
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("lat",this.lat);
		jb.add("lng",this.lng);
		return jb.build();
	}
}
