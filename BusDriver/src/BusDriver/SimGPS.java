import java.lang.Thread;
import java.util.Random;

public class SimGPS extends Thread{
	private double lat;
	private double lng;
	
	public SimGPS(){
		this.lat = 0.0;
		this.lng = 0.0;
	}
	
	//génère toutes les secondes 2 nombres entre -100 et 100
	public void run(){
		Random gen = new Random();
		while(true){
			System.out.println("lat : " + this.lat + " | lng : " + this.lng);
			this.lat = gen.nextDouble() * 200 - 100;
			this.lng = gen.nextDouble() * 200 - 100;
			try{
				this.sleep(1000);
			}catch(InterruptedException e){
				System.out.println(e);
			}
		}
	}
	
	public static void main(String[] args){
		SimGPS gps = new SimGPS();
		gps.run();
	}
}
