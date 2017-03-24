package BusDriver;

public class MessageQueue{

	private BusMessage[] queue;
	private int sizeMax;
	private int last;

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
