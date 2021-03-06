/**
 * Time-of-day server listening to port 6013.
 *
 */
 
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;

public class DateServerMailBox
{

	public static void buscarMensagem(String id, ArrayList<String> mailbox, PrintWriter pout){
		// Iterator<String> iterator = mailbox.iterator();
		String[] parts = null;
		int i = 0;

		while(i < mailbox.size()){
			parts = mailbox.get(i).split(":");
			if(Integer.parseInt(parts[3]) == Integer.parseInt(id)){
				pout.println(parts[1] + ". \n Enviado por: " + parts[2]);
			}
			i++;
		}
		String n = null;
		pout.println(n);
	}

	public static void verificarMensagem(String mensagem, ArrayList<String> mailbox, PrintWriter pout){
		String[] parts = null;
		parts = mensagem.split(":");

		switch(parts[0]){
			case "1":
				mailbox.add(mensagem);
				pout.println("Mensagem enviada!");
				break;
			case "2":
				pout.println("Caixa de entrada: \n\n");
				buscarMensagem(parts[1], mailbox, pout);
				break;
			default:
				pout.println("Operacao inválida!\n\n");
				break;
		}
	}

	public static void main(String[] args) throws IOException {
		Socket client = null;
		ServerSocket sock = null;
		BufferedReader bin = null;
		InputStream in = null;
		ArrayList<String> mailbox = new ArrayList();
		String line;

		try {
			sock = new ServerSocket(6013);
			
			while (true) {
				client = sock.accept();
				System.out.println("server = " + sock);
				System.out.println("client = " + client);
	
				in = client.getInputStream();
				bin = new BufferedReader(new InputStreamReader(in));
				PrintWriter pout = new PrintWriter(client.getOutputStream(), true);
	
				while((line = bin.readLine()) != null){
					verificarMensagem(line, mailbox, pout);
				}

				pout.close();
				client.close();
			}
		} catch (IOException ioe) {
				System.err.println(ioe);
		} finally {
			if (sock != null)
				sock.close();
			if (client != null)
				client.close();
		}
	}
}
