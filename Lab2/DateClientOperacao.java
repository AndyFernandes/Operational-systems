/**
 * Client program requesting current date from server.
 *
 */
 
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class DateClientOperacao {
	public static void main(String[] args) throws IOException {
		InputStream in = null;
		BufferedReader bin = null;
		Socket sock = null;
		Scanner reader = new Scanner(System.in); 

		try {
			sock = new Socket("127.0.0.1",6013);
			PrintWriter pout = new PrintWriter(sock.getOutputStream(), true);
			boolean cond = true;
			while(cond){
				System.out.println("\nSelecione a operacao desejada: \n + - Soma \n - - Subtracao \n * - Multiplicacao \n / - Divisao \n 0 - Sair");
				String operacao = reader.next();
		
				System.out.println("\nDigite o primeiro operando: ");
				String op1 = reader.next();

				System.out.println("\nDigite o segundo operando: ");
				String op2 = reader.next();

				String mensagem = operacao + ":" + op1 + ":" + op2;
				pout.println(mensagem);
				
				in = sock.getInputStream();
				bin = new BufferedReader(new InputStreamReader(in));

				String line;
				
				while((line = bin.readLine()) != null){
					System.out.println("\nResultado: " + line);
					break;
				}
			}
			// pout.close();
		}
		catch (IOException ioe) {
				System.err.println(ioe);
		}
                finally {
                    sock.close();
                }
	}
}
