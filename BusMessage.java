package BusDriver;

import javax.json.*;

public interface BusMessage{
	int getMsgId();
	void setMsgId(int msgId);
	int getDate();
	void setDate(int date);
	JsonObject getContent();
}	
