package my_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class MyCLientCallback  extends UnicastRemoteObject implements ClientCallbackInterface{
	
	private static final long serialVersionUID = 7897576664568313628L;

	public MyCLientCallback() throws RemoteException{
		super();
	}

}
