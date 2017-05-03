package Drivers;

import java.lang.Thread;
import java.util.Random;

import BusDriver.BusMessage;
import BusDriver.GpsMessage;

/**
 * Cette classe permet de lancer un simulateur GPS envoyant des message de type GPS aléatoire sur le bus
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class SimGPS extends Thread{
	private double lat;
	private double lng;
	private Client c;
	
	/**
	 * Cree un nouveau simulateur connecté au bus
	 * 
	 * @param ip L'ip du bus
	 */
	public SimGPS(String ip){
		this.lat = 0.0;
		this.lng = 0.0;
		this.c = new Client(ip,"GPS","SIMGps");
	}
	
	/**
	 * Permet de générer 10 messages aléatoire ayant comme lat et lng des valeurs comprisent entre
	 * -100 et 100
	 * 
	 */
	@SuppressWarnings("static-access")
	public void run(){
		Random gen = new Random();
		if(!c.register()){
			System.err.println("Erreur de connexion!");
			System.exit(-1);
		}
		int count = 0;
		while(true){
			System.out.println("lat : " + this.lat + " | lng : " + this.lng);
			this.lat = gen.nextDouble() * 200 - 100;
			this.lng = gen.nextDouble() * 200 - 100;
			GpsMessage gm = new GpsMessage(this.lat,this.lng);
			if(!this.c.sendMessage((BusMessage)gm)){
				System.err.println("Erreur de transmission");
			}
			try{
				this.sleep(1000);
			}catch(InterruptedException e){
				System.out.println(e);
			}
			count++;
			if(count == 10)
				if(this.c.deRegister()){
					System.out.println("Terminé");
					break;
				}
		}
	}
	
	public static void main(String[] args){
		if(args.length < 1){
			System.err.println("Usage: "+SimGPS.class + " <ip adr>");
			System.exit(-1);
		}
		SimGPS gps = new SimGPS(args[0]);
		gps.run();
	}
}
