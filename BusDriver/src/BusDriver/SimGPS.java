package BusDriver;

import java.lang.Thread;
import java.util.Random;

public class SimGPS extends Thread{
	private double lat;
	private double lng;
	private Client c;
	
	public SimGPS(){
		this.lat = 0.0;
		this.lng = 0.0;
		this.c = new Client("127.0.0.1","GPS","SIMGps");
	}
	
	//génère toutes les secondes 2 nombres entre -100 et 100
	@SuppressWarnings("static-access")
	public void run(){
		Random gen = new Random();
		if(!c.register()){
			System.err.println("Erreur de connexion!");
			System.exit(-1);
		}
		
		while(true){
			System.out.println("lat : " + this.lat + " | lng : " + this.lng);
			this.lat = gen.nextDouble() * 200 - 100;
			this.lng = gen.nextDouble() * 200 - 100;
			GpsMessage gm = new GpsMessage(this.lat,this.lng);
			this.c.sendMessage((BusMessage)gm);
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
