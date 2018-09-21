/**
 * Client program requesting current date from server.
 *
 */

/*
	Formato de envio: "operacao:mensagem:id_remetente:id_destinatario"
	Formato receber: "operacao:id_cliente"

	Operacao 1 - Enviar
	Operacao 2 - Receber
*/
 
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class DateClientMailBox
{
	public static void main(String[] args) throws IOException {
		InputStream in = null;
		BufferedReader bin = null;
		Socket sock = null;

		Scanner reader = new Scanner(System.in);  

		try {
			sock = new Socket("127.0.0.1",6013);
			
			PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
			pout.println("1:Qualquer coisa:5:10");
			System.out.println("AI PAPAI");
			pout.println("2:10");
			in = sock.getInputStream();
			bin = new BufferedReader(new InputStreamReader(in));

			String line;
			while( (line = bin.readLine()) != null){
				System.out.println(line);
			}

			pout.close();
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
                finally {
                    sock.close();
                }
	}
}
