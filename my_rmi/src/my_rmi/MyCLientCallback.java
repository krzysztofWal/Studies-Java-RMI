package my_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import lab_1.Pakcet;
import lab_1.TimeHistory;

public class MyCLientCallback  extends UnicastRemoteObject implements ClientCallbackInterface{
	
	private static final long serialVersionUID = 7897576664568313628L;

	public MyCLientCallback() throws RemoteException{
		super();
	}
	
	public Pakcet sendToServer() throws RemoteException {
		return new TimeHistory();
	}
	
}
