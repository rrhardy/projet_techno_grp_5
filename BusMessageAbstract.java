package BusDriver;

public abstract class BusMessageAbstract implements BusMessage{
	private int msgId;
	private int date;
	
	public BusMessageAbstract(){
		this.msgId = 0;
		this.date = 0;
	}
	
	public BusMessageAbstract(int msgId, int date){
		this.msgId = msgId;
		this.date = date;
	}
	
	public int getMsgId(){
		return this.msgId;
	}
	
	public void setMsgId(int msgId){
		this.msgId = msgId;
	}
	
	public int getDate(){
		return this.date;
	}
	
	public void setDate(int date){
		this.date = date;
	}
	
}
