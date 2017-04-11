package Viewer;

public class Device {
	private int id;
	private String classe;
	private String name;
	
	public Device(int id, String classe, String name){
		this.id = id;
		classe = new String(classe);
		name = new String(name);
	}
	
	public Device(){
		this(0,"","");
	}
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getClasse(){
		return this.classe;
	}
	
	public void setClasse(String classe){
		this.classe = classe;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(id).append(" ").append(this.name).append(" ").append(this.classe);
		return sb.toString();
	}
}
