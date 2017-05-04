package BusDriver;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * Cette classe représente les message envoyé par des drivers récupérant des informations sur des point dans le plan 3D
 * (Gyroscope, Accelerometre,...).
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class TriPointMessage extends BusMessageAbstract{

	private double x;
	private double y;
	private double z;
	
	/**
	 * Cree un nouveau message contenant x y et z comme info sur le plan
	 * 
	 * @param x La valeur de x
	 * @param y La valeur de y
	 * @param z La valeur de z
	 */
	public TriPointMessage(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructeur par default (initialise tout à 0.0)
	 * 
	 */
	public TriPointMessage(){
		this(0.0,0.0,0.0);
	}
	
	/**
	 * Accesseur sur X
	 * 
	 * @return La valeur de x contenu dans le message
	 */
	public double getX(){
		return this.x;
	}
	
	/**
	 * Mutateur sur X
	 * 
	 * @param x La nouvelle valeur de x
	 */
	public void setX(double x){
		this.x = x;
	}
	
	/**
	 * Accesseur sur Y
	 * 
	 * @return La valeur de y contenu dans le message 
	 */
	public double getY(){
		return this.y;
	}
	
	/**
	 * Mutateur sur Y
	 * 
	 * @param y La nouvelle valeur de Y
	 */
	public void setY(double y){
		this.y = y;
	}
	
	/**
	 * Accesseur sur Z
	 * 
	 * @return La valeur de Z contenu dans le message
	 */
	public double getZ(){
		return this.z;
	}
	
	/**
	 * Mutateur sur Z
	 * 
	 * @param z La nouvelle valeur de Z
	 */
	public void setZ(double z){
		this.z = z;
	}
	
	@Override
	/**
	 * Retourne le message sous forme d'objet JSON de ce type:
	 * contents:{x:LA_VALEUR_DE_X, y:LA_VALEUR_DE_Y, z:LA_VALEUR_DE_Z
	 */
	public JsonObject getContent() {
		// TODO Auto-generated method stub
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("x",this.x);
		jb.add("y",this.y);
		jb.add("z", this.z);
		return jb.build();
	}

}
