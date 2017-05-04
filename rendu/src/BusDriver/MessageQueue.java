package BusDriver;

/**
 * Cette classe représente la file de messages que posséde chaque ClientManager et permettant de garder un historique
 * des messages en mémoire. La file ainsi crée sera tournante afin de limité le nombre de message et le nombre 
 * d'allocation mémoire. 
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class MessageQueue{

	private BusMessage[] queue;
	private int sizeMax;
	private int last; //indice du dernier message

	/**
	 * Cree une nouvelle file de message de taille MAX initSize
	 * 
	 * @param initSize La taille initial de la file 
	 */
	public MessageQueue(int initSize){
		this.queue = new BusMessage[initSize];
		this.sizeMax = initSize;
		this.last = -1;
	}

	/**
	 * Accesseur sur l'état de la file (Vide ou non-Vide)
	 * 
	 * @return True si la file est vide False sinon
	 */
	public boolean isEmpty(){
		if(last==-1)
			return true;
		else
			return false;
	}
	
	/**
	 * Accesseur sur la tete de file
	 * 
	 * @return Le message en tete de file 
	 */
	public BusMessage getLast(){
		return queue[this.last];
	}
	
	/**
	 * Permet de récupérer un message, identifé par id
	 * 
	 * @param id L'identifiant du message voulu
	 * @return Le message indetifié par id, null s'il n'existe pas
	 */
	public BusMessage getById(int id){
		if(id > last || queue[id] == null)
			return null;
		return queue[id];
	}
	
	/**
	 * Permet de recupérer l'identifiant dans la file de la tete de file
	 * 
	 * @return l'identifiant de la tete de file
	 */
	public int getLastId(){
		return last;
	}
	
	/**
	 * Permet d'ajouter un message à la file 
	 * 
	 * @param mess Le message à ajouter
	 */
	public void add(BusMessage mess){
		if(this.last == -1){
			queue[0] = mess;
			this.last++;
			return ;
		}

		if(this.sizeMax == this.last+1){
			queue[0] = mess;
			this.last = 0;
		}

		else{
			queue[this.last+1] = mess;
			this.last++;
		}
	}

}
