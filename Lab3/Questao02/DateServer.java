/**
 * Time-of-day server listening to port 6013.
 *
 */
 
import java.net.*;
import java.io.*;

class Server implements Runnable{
	ServerSocket sock;
	Socket client;

	public Server(){}

	public Server(ServerSocket sock, Socket client){
		this.sock = sock;
		this.client = client;
	}

	public void run(){
		System.out.println("server = " + this.sock);
		System.out.println("client = " + this.client);
		try {
			// we have a connection
			PrintWriter pout = new PrintWriter(this.client.getOutputStream(), true);
			// write the Date to the socket
			pout.println(new java.util.Date().toString());

			pout.close();
			this.client.close();
		} catch (IOException ioe) {
				System.err.println(ioe);
		}
		
	}

}

class DateServer{	

	public static void main(String[] args){
		ServerSocket sock = null;
		try {
			sock = new ServerSocket(6013);
			// now listen for connections
			while (true) {
				Socket client = sock.accept();
				Thread work = new Thread(new Server(sock, client));
				work.start();		
			}
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
	}
}
