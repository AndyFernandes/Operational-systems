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

public class DateClientMailBox{
	public static void main(String[] args) throws IOException {
		InputStream in = null;
		BufferedReader bin = null;
		Socket sock = null;
		String comunicacaoServer;
		Scanner reader = new Scanner(System.in);  

		try {
			sock = new Socket("127.0.0.1",6013);
			PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);

			System.out.println("\nDigite o seu identificador: ");
			String id = reader.next();

			System.out.println("\nDigite: \n 1. Enviar mensagem \n 2. Receber mensagem");
			String operacao = reader.next();

			switch(operacao){
				case "1":
					System.out.println("\nDigite a sua mensagem: ");
					String mensagem = reader.next();

					System.out.println("\nDigite o id do destinatario: ");
					String id_destinatario = reader.next();

					comunicacaoServer = operacao + ":" + mensagem + ":" + id + ":" + id_destinatario;
					break;
				case "2":
					comunicacaoServer = operacao + ":" + id;
			}
			
			pout.println(comunicacaoServer);
			// pout.println("1:Qualquer coisa:5:10");
			// System.out.println("AI PAPAI");
			// pout.println("2:10");
			
			in = sock.getInputStream();
			bin = new BufferedReader(new InputStreamReader(in));

			String line;
			while((line = bin.readLine()) != null){
				System.out.println(line);
			}

			pout.close();
			sock.close();
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
                finally {
                    sock.close();
                }
	}
}
