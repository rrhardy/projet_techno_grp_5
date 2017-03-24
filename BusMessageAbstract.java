package BusDriver;

public abstract class BusMessageAbstract implements BusMessage{
	private long date;
	
	public BusMessageAbstract(){
		this.date = 0;
	}
	
	public BusMessageAbstract(long date){
		this.date = date;
	}
	
	public long getDate(){
		return this.date;
	}
	
	public void setDate(long date){
		this.date = date;
	}
	
}
