/**
 * Time-of-day server listening to port 6013.
 *
 */

// Modelo tamanho limitado
import java.net.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server implements Runnable{
	ServerSocket sock;
	Socket client;

	public Server(){}

	public Server(ServerSocket sock, Socket client){
		this.sock = sock;
		this.client = client;
	}

	public static String calculadora(String operador, String operando1, String operando2){
		float op1 = Float.parseFloat(operando1);
		float op2 = Float.parseFloat(operando2);
		switch(operador){
			case "+":
				return Float.toString(op1+op2);
			case "-":
				return Float.toString(op1-op2);
			case "*":
				return Float.toString(op1*op2);
			case "/":
				return Float.toString(op1/op2);
			default:
				return "operador inválido";
		}
	}

	public void run(){
		try {
				System.out.println("server = " + sock);
				System.out.println("client = " + client);

				InputStream in = client.getInputStream();
				BufferedReader bin = new BufferedReader(new InputStreamReader(in));
				
				PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

				String line;
				String[] parts = null;
				while( (line = bin.readLine()) != null){
					parts = line.split(":");
					pout.println(calculadora(parts[0], parts[1], parts[2]));
				}

				pout.close();
				client.close();
		} catch (IOException ioe) {
				System.err.println(ioe);
		}
	}
}

public class DateServerOperacao {
	public static void main(String[] args) throws IOException {
		try {
			ServerSocket sock = new ServerSocket(6013);
			// now listen for connections
			ExecutorService pool = Executors.newSingleThreadExecutor();
			while (true) {
				Socket client = sock.accept();
				Runnable thread = new Server(sock, client);
				pool.execute(thread);
			}
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
	}
}
