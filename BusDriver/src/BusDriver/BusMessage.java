package BusDriver;

import javax.json.*;

/**
 * Interface des type de messages défini dans la spec
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public interface BusMessage{
	/**
	 * 
	 * @return Le timestamp correpondant à la date d'ajout du message sur le bus 
	 */
	long getDate();
	
	/**
	 * 
	 * @param date La date d'ajout à definir
	 */
	void setDate(long date);
	
	/**
	 * 
	 * @return Le message sous forme d'objet JSON
	 */
	JsonObject getContent();
}	
