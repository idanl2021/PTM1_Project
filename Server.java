package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server {

	public interface ClientHandler{
		// define...
		void handleClient(InputStream inFromClient, OutputStream outToClient);
	}

	public volatile int clientLimit = 1;
	public volatile int numOfClients = 0;
	volatile boolean stop;
	public Server() {
		stop=false;
	}
	
	
	protected void startServer(int port, ClientHandler ch) {
		// implement here the server...
		try{
			ServerSocket server=new ServerSocket(port);
			try {
				server.setSoTimeout(1000);
				while((!stop) &&  clientLimit != numOfClients){
					try{
						Socket aClient=server.accept(); // blocking call
						numOfClients++;
						try {
							ch.handleClient(aClient.getInputStream(), aClient.getOutputStream());
							//aClient.getInputStream().close();
							//aClient.getOutputStream().close();
							aClient.close();
							//numOfClients--;
						} catch (IOException e) {/*...*/}
					}catch(SocketTimeoutException e) {/*...*/}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				server.close();
			}
		}
		catch (Exception ex){
			return;
		}
	}
	
	// runs the server in its own thread
	public void start(int port, ClientHandler ch) {
		new Thread(()->startServer(port,ch)).start();
	}
	
	public void stop() {
		stop=true;
	}

}
