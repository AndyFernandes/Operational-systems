
import java.rmi.*;
import java.util.Date;
import java.io.*;
import java.util.Scanner;
 
public class Client {  
   public Client(){
        String host = "rmi://localhost:1099/DateServer";
        Scanner reader = new Scanner(System.in);
        String comunicacaoServer;
        
        try {
                MensagemObject mensagem = new MensagemObjectImpl();
                MailBox mailbox = (MailBox)Naming.lookup(host);

                System.out.println("\nDigite o seu identificador: ");
                String id = reader.next();

                System.out.println("\nDigite: \n 1. Enviar mensagem \n 2. Receber mensagem");
                String operacao = reader.next();

                switch(operacao){
                  case "1":
                    System.out.println("\nDigite a sua mensagem: ");
                    String mensagemEnv = reader.next();

                    System.out.println("\nDigite o id do destinatario: ");
                    String id_destinatario = reader.next();

                    comunicacaoServer =  mensagemEnv + ":" + id + ":" + id_destinatario;
                    mensagem.setMensagem(comunicacaoServer);
                    mailbox.send(mensagem);
                    break;

                  case "2":
                    comunicacaoServer = id;
                    mensagem.setMensagem(comunicacaoServer);
                    mailbox.receive(mensagem);
                }

        } catch (Exception e) {
                e.printStackTrace();
        } finally {
		        System.out.println("Done");
	     }
   }

   public static void main(String args[]) { 
      // if (args.length != 1) {
      //   System.err.println("Usage: java Client <IP Address>");
      //   System.exit(0);
      // }
      
      Client server = new Client();
   }
}

