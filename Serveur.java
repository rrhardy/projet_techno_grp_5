import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import javax.json.*;


public class Serveur {

	public static void main(String[] args) throws IOException{
		ServerSocket s = new ServerSocket(1234);
		
		Socket socket = s.accept();
		InputStream entree = socket.getInputStream();
		OutputStream sortie = socket.getOutputStream();
		
		//réception de l'objet JSON
		byte[] data = new byte[1024];
		entree.read(data);
		String object = new String(data, "UTF-8");
		
		//affichage sur la sortie standard
		System.out.println(object);
		
		s.close();
	}

}
