package my_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import lab_1.Pakcet;
import lab_1.Spectrum;
import lab_1.TimeHistory;

public class MyRmiServant extends UnicastRemoteObject implements FirstInterface {
	private static final long serialVersionUID = -4270368481502787095L;
	protected Map<String, PcktClientPair> thMap = new HashMap<>();
	protected Map<String, PcktClientPair> spMap = new HashMap<>();
	protected Vector<ClientCallbackInterface> clients = new Vector<ClientCallbackInterface>();
	protected String localMadafaka = "Not yo";
	protected String localSamuel = "Not";
	//protected Map<Integer,String> saveRemotelyResult = new HashMap<>();
	
	public MyRmiServant() throws RemoteException {}
	
	/*
	 * Zdalne metody udostêpniane przez serwer
	 */
	public boolean register(ClientCallbackInterface clbck) throws RemoteException {
		System.out.println("Server.register(): " + clbck.hashCode());
		//jeœli jest nie ma takiego delikwenta to dodaj
		boolean is = false;
		for (ClientCallbackInterface el : clients) {
			is = (el.hashCode() == clbck.hashCode()) ? true : false;
		}
		if (!is) {
			clients.add(clbck);
		}
		
		return false;
	}
	
	public boolean unregister(ClientCallbackInterface clbck) throws RemoteException {
		// wypisz delikwenta do wektora
		if (clients.remove(clbck)) {
			System.out.println("Server.unregister(): " + clbck.hashCode());
			return true;
		}
		return false;
	}

	public String saveRemotely(Pakcet pckt, ClientCallbackInterface clbck) throws RemoteException,InterruptedException {
		String saveRemotelyResult = "";
		synchronized(this) {
			if (pckt.getClassStringId().equals((new TimeHistory<Integer>()).getClassStringId())) {
				if (!thMap.containsKey(pckt.toString())) {
					thMap.put(pckt.toString(),new PcktClientPair(pckt, clbck));
					saveRemotelyResult = "Saved sent TimeHistory packet";
				} else {
					saveRemotelyResult = "TimeHistory packet already sent";
				}
			} else if (pckt.getClassStringId().equals((new Spectrum<Integer>()).getClassStringId())) {
				if (!spMap.containsKey(pckt.toString())) {
					spMap.put(pckt.toString(),new PcktClientPair(pckt, clbck));
					saveRemotelyResult = "Saved sent Spectrum packet";
				} else {
					saveRemotelyResult = "Spectrum packet already sent";
				}
			} else {
				saveRemotelyResult = "Failed saving package";
			}
		}
		return saveRemotelyResult;
	}
	
	/*
	 * klasa przechowuj¹ca pakiet i przez którego klienta zosta³ zapisany
	 */
	static class PcktClientPair {
		private Pakcet packet;
		private ClientCallbackInterface clbck;
		
		public PcktClientPair(Pakcet pck, ClientCallbackInterface clbck) {
			this.packet = pck;
			this.clbck = clbck;
		}
		
		public Pakcet getPakcet() {
			return this.packet;
		}
		
		public ClientCallbackInterface getCallbackObj() {
			return this.clbck;
		}
	}
	
}