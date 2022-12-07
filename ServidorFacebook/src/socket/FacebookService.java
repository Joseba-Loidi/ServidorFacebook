package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.StringTokenizer;

public class FacebookService extends Thread {
	private DataInputStream in;
	private DataOutputStream out;
	private Socket FacebookSocket;
	private HashMap<String, String> hashFacebook = new HashMap<>();

	private static String DELIMITER = "#";
	
	public FacebookService(Socket socket) {
		try {
			this.FacebookSocket = socket;
		    this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
			this.start();
			
			//Gente que usa Facebook
			hashFacebook.put("iker", "iker");
			hashFacebook.put("irene", "irene");
			
		} catch (Exception e) {
			System.out.println("# FacebookService - TCPConnection IO error:" + e.getMessage());
		}
	}

	public void run() {
		//Echo server
		try {
			//Read request from the client
			String data = this.in.readUTF();			
			System.out.println("   - FacebookService - Received data from '" + FacebookSocket.getInetAddress().getHostAddress() + ":" + FacebookSocket.getPort() + "' -> '" + data + "'");		
			if (data.contains(DELIMITER)) {
				data= this.checkUsuarioFacebook(data);
			}else {
				data= this.checkEmailFacebook(data);
			}
			
			//Send response to the client
			this.out.writeUTF(data);			
			System.out.println("   - FacebookService - Sent data to '" + FacebookSocket.getInetAddress().getHostAddress() + ":" + FacebookSocket.getPort() + "' -> '" + data.toUpperCase() + "'");
		} catch (Exception e) {
			System.out.println("   # FacebookService error" + e.getMessage());
		} finally {
			try {
				FacebookSocket.close();
			} catch (Exception e) {
				System.out.println("   # FacebookService error:" + e.getMessage());
			}
		}
	}
	public String checkUsuarioFacebook(String msg) { //			email@gmail.com#contrasenya
		String translation = null;
		
		if (msg != null && !msg.trim().isEmpty()) {
			try {
				StringTokenizer tokenizer = new StringTokenizer(msg, DELIMITER);		
				String email = tokenizer.nextToken();
				String contrasenya = tokenizer.nextToken();
				System.out.println("   Starting checking of " + email + " from: " + contrasenya);
		
				if (email != null && contrasenya != null) {
					if(hashFacebook.containsKey(email)) {
						if(hashFacebook.get(email).matches(contrasenya)) {
							return "true";
						}
					}
					
					System.out.println("   - Facebook server result: " + translation);
				}
			} catch (Exception e) {
				System.out.println("   # FacebookService - Facebook API error:" + e.getMessage());
				translation = null;
			}
		}
		
		return "false";
	}
	public String checkEmailFacebook(String msg) { //			email@gmail.com
		String translation = null;
		
		if (msg != null) {
			try {
						
				String email = msg;
				System.out.println("   Starting checking of " + email);
		
				if (email != null) {
					if(hashFacebook.containsKey(email)) {
							return "true";
						
					}
					
					System.out.println("   - Facebook server result: " + translation);
				}
			} catch (Exception e) {
				System.out.println("   # FacebookService - Facebook error:" + e.getMessage());
				translation = null;
			}
		}
		
		return "false";
	}
	
}
	
