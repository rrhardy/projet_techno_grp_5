package BusDriver;

import javax.json.*;

public interface BusMessage{
	long getDate();
	void setDate(long date);
	JsonObject getContent();
}	
