package my_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import lab_1.Pakcet;

public interface FirstInterface extends Remote{
	boolean register(ClientCallbackInterface clbck) throws RemoteException;
	boolean unregister(ClientCallbackInterface clbck) throws RemoteException;
	String saveRemotely(Pakcet pckt, ClientCallbackInterface clbck) throws RemoteException, InterruptedException;
	
}
