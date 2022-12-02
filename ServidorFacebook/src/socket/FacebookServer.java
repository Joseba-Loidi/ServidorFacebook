package socket;

import java.net.ServerSocket;

public class FacebookServer {


	private static int numClients = 0;
	
	public static void main(String args[]) {
		if (args.length < 1) {
			System.err.println(" # Usage: Facebook [PORT]");
			System.exit(1);
		}
		
		//args[1] = Server socket port
		int serverPort = Integer.parseInt(args[0]);
		
		/**
		 * NOTE: try-with resources Statement - https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		 * Try statement that declares one or more resources. A resource is an object that must be closed after the program is 
		 * finished with it. The try-with-resources statement ensures that each resource is closed at the end of the statement.
		 * Any object that implements java.lang.AutoCloseable, which includes all objects which implement java.io.Closeable, 
		 * can be used as a resource.
		 */
		
		//Declaration of the ServerSocket (only a port number is needed)
		try (ServerSocket FacebookServerSocket = new ServerSocket(serverPort);) {
			System.out.println(" - FacebookServer: Waiting for connections '" + FacebookServerSocket.getInetAddress().getHostAddress() + ":" + FacebookServerSocket.getLocalPort() + "' ...");
			
			//The main thread is always waiting for connections
			while (true) {
				//When a connection from a client is received, a new EchoService is created. The "accept()" method returns the socket to
				//communicate with the client.
				new FacebookService(FacebookServerSocket.accept());
				System.out.println(" - FacebookServer: New client connection accepted. Client Number: " + numClients++);
			}
		} catch (Exception e) {
			System.out.println("# FacebookServer: IO error:" + e.getMessage());
		}
	}
}
