package BusDriver;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class TriPointMessage extends BusMessageAbstract{

	private double x;
	private double y;
	private double z;
	
	public TriPointMessage(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public TriPointMessage(){
		this(0.0,0.0,0.0);
	}
	
	public double getX(){
		return this.x;
	}
	
	public void setX(double x){
		this.x = x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public void setY(double y){
		this.y = y;
	}
	
	public double getZ(){
		return this.z;
	}
	
	public void setZ(double z){
		this.z = z;
	}
	@Override
	public JsonObject getContent() {
		// TODO Auto-generated method stub
		JsonObjectBuilder jb = Json.createObjectBuilder();
		jb.add("x",this.x);
		jb.add("y",this.y);
		jb.add("z", this.z);
		return jb.build();
	}

}
