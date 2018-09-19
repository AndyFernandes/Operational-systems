/**
 * Time-of-day server listening to port 6013.
 *
 */
 
import java.net.*;
import java.io.*;

public class DateServer
{

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

	public static void main(String[] args) throws IOException {
		Socket client = null;
		ServerSocket sock = null;
		BufferedReader bin = null;
		InputStream in = null;

		try {
			sock = new ServerSocket(6013);
			// now listen for connections
			while (true) {
				client = sock.accept();
				System.out.println("server = " + sock);
				System.out.println("client = " + client);

				in = client.getInputStream();
				bin = new BufferedReader(new InputStreamReader(in));
				
				PrintWriter pout = new PrintWriter(client.getOutputStream(), true);

				String line;
				String[] parts = null;
				while( (line = bin.readLine()) != null){
					parts = line.split(":");
					// System.out.println(parts[1]);
					pout.println(calculadora(parts[0], parts[1], parts[2]));
				}

				pout.close();
				client.close();
			}
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
		finally {
			if (sock != null)
				sock.close();
			if (client != null)
				client.close();
		}
	}
}
