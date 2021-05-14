package my_rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import lab_1.Packet;

public interface RemoteMethodsInterface extends Remote{
	boolean register(ClientCallbackInterface clbck) throws RemoteException;
	boolean unregister(ClientCallbackInterface clbck) throws RemoteException;
	String saveRemotely(Packet pckt, ClientCallbackInterface clbck) throws RemoteException, InterruptedException;
	ArrayList<ListDataWrapper> findRemotely(SearchData data) throws RemoteException;
	String saveToFileRemotely(long id, ListDataWrapper.packetType pckType ) throws RemoteException;
	Packet receiveLocally(long id, ListDataWrapper.packetType pckType) throws RemoteException;
	
}
