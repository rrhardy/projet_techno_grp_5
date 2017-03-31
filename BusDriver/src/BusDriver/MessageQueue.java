package BusDriver;

//file de messages implémentée par un tableau
public class MessageQueue{

	private BusMessage[] queue;
	private int sizeMax;
	private int last; //indice du dernier message

	public MessageQueue(int initSize){
		this.queue = new BusMessage[initSize];
		this.sizeMax = initSize;
		this.last = -1;
	}

	public boolean isEmpty(){
		if(last==-1)
			return true;
		else
			return false;
	}
	
	public BusMessage getLast(){
		return queue[this.last];
	}
	
	public BusMessage getById(int id){
		if(id > last || queue[id] == null)
			return null;
		return queue[id];
	}
	public int getLastId(){
		return last;
	}
	public void add(BusMessage mess){
		if(this.last == -1){
			queue[0] = mess;
			this.last++;
			return ;
		}

		if(this.sizeMax == this.last){
			queue[0] = mess;
			this.last = 0;
		}

		else{
			queue[this.last+1] = mess;
			this.last++;
		}
	}

}
