package my_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import lab_1.Pakcet;

public interface FirstInterface extends Remote{
	String supMadafaka(String SaySth) throws RemoteException, InterruptedException;
	String supSamuel() throws InterruptedException, RemoteException;
	boolean register(ClientCallbackInterface clbck) throws RemoteException;
	boolean unregister(ClientCallbackInterface clbck) throws RemoteException;
	boolean saveRemotely(Pakcet pckt) throws RemoteException;
	
}
