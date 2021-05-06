package my_rmi;
import java.rmi.Remote;
import java.rmi.RemoteException;
import lab_1.Pakcet;

public interface ClientCallbackInterface extends Remote{
	public Pakcet sendToServer() throws RemoteException;
}
