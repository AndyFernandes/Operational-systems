import java.util.Date;
import java.rmi.*;

public interface MailBox extends Remote{
	public abstract void send(MensagemObject mensagem) throws RemoteException;
    public abstract void receive(MensagemObject id_user) throws RemoteException;
}
