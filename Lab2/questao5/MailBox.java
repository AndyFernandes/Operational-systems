import java.util.Date;
import java.rmi.*;

public interface MailBox extends Remote{
	public abstract void send(String mensagem) throws RemoteException;
    public abstract void receive(String id_user) throws RemoteException;
}
