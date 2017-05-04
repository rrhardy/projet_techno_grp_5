package BusDriver;

/**
 * Classe abstraite implementant l'interface BusMessage
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public abstract class BusMessageAbstract implements BusMessage{
	private long date;
	
	/**
	 * Cree un nouveau message, ayant comme horodatage l'heure systeme du bus
	 */
	public BusMessageAbstract(){
		this.date = System.currentTimeMillis();
	}
	
	/**
	 * Permet de cree un nouveau message avec un horodatage précis
	 * 
	 * @param date L'heurodatage voulu
	 */
	public BusMessageAbstract(long date){
		this.date = date;
	}
	
	/**
	 * Accesseur sur la date du message
	 * 
	 * @return L'heurodatage du message sous forme de timestamp (nb de seconde depuis le 01/01/1970)
	 */
	public long getDate(){
		return this.date;
	}
	
	/**
	 * Mutateur sur la date du message
	 * 
	 * @param date La nouvelle date
	 */
	public void setDate(long date){
		this.date = date;
	}
	
}
