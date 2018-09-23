/**
 * RemoteDate interface iplementation.
 */
public class MensagemObjectImpl extends java.rmi.server.UnicastRemoteObject implements MensagemObject
{
	private String mensagem;

	public MensagemObjectImpl() throws java.rmi.RemoteException { }

	public void setMensagem(String mensagem) throws java.rmi.RemoteException {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}

	public String toString() {
		return mensagem.toString();
	}
}
