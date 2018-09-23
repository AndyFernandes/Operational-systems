/*
    mensagem: id_remetente: id_dest
*/

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;

public class MailBoxImpl extends UnicastRemoteObject implements MailBox{
    ArrayList<String> mailbox = new ArrayList();

    public MailBoxImpl() throws RemoteException {} 

    public void buscarMensagem(String id){
        String[] parts = null;
        int i = 0;

        while(i < this.mailbox.size()){
            parts = this.mailbox.get(i).split(":");
            if(Integer.parseInt(parts[2]) == Integer.parseInt(id)){
                System.out.println(parts[0] + ".\nEnviado por: " + parts[1]);
            }
            i++;
        }
    }

    public void send(MensagemObject mensagem) throws RemoteException{
        this.mailbox.add(mensagem.getMensagem());
        System.out.println("Mensagem enviada!");
    }

    public void receive(MensagemObject id_user) throws RemoteException{
        System.out.println("Caixa de entrada: \n\n");
        buscarMensagem(id_user.getMensagem());
    }

   public static void main(String[] args)  {
        try {
            MailBox mailbox = new MailBoxImpl();
            Naming.rebind("//localhost:1099/DateServer", mailbox);
            System.out.println("DateServer bound in registry");
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
