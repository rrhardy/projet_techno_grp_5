package BusDriver;

import javax.json.*;

/**
 * Interface des type de messages d�fini dans la spec
 * @author Robin Hardy
 * @author Paul Jos�-Vedrenne
 *
 */
public interface BusMessage{
	/**
	 * 
	 * @return Le timestamp correpondant � la date d'ajout du message sur le bus 
	 */
	long getDate();
	
	/**
	 * 
	 * @param date La date d'ajout � definir
	 */
	void setDate(long date);
	
	/**
	 * 
	 * @return Le message sous forme d'objet JSON
	 */
	JsonObject getContent();
}	
