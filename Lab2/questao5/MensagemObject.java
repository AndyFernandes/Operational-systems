/**
 * DateObject interface.
 *
 */
public interface MensagemObject extends java.rmi.Remote
{
	public void setMensagem(String mensagem) throws java.rmi.RemoteException;
	public String getMensagem() throws java.rmi.RemoteException;
}
