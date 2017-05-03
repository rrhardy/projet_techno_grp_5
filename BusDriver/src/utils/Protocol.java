package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Cette classe implemente les fonctions de lecture/ecriture bas niveau 
 * 
 * @author Robin Hardy
 * @author Paul José-Vedrenne
 *
 */
public class Protocol {
	
	/**
	 * Le port de connexion sur le bus
	 */
	public static final int PORT_NUMBER = 7182;
	
	/**
	 * Methode static permettant l'envoie bas niveau d'une chaine de caractère (encodé en UTF-8)
	 * 
	 * @param toSend La chaine à envoyer
	 * @param out Le stream de sortie
	 */
	public static void sendString(String toSend, OutputStream out){
		try{
			out.write(toSend.getBytes("UTF-8"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Methode static permettant une lecture  bas niveau de chaine de caractères sur le flux d'entrée, 
	 * La lecture s'arrête dès que le carctère '\n' est rencontré
	 * 
	 * @param in Le flux d'entrée
	 * @return La chaine lu
	 */
	public static String readJsonString(InputStream in){
		StringBuffer sb = new StringBuffer();
		int c = 0;
		try{
			while(in.available() == 0)
				;
			while( (c=in.read()) != '\n'){
				sb.append((char)c);//Lecture caract�re par caract�re
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return sb.toString();
	}
}
