package my_rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import lab_1.Pakcet;
import lab_1.TimeHistory;

public class MyRmiServant extends UnicastRemoteObject implements FirstInterface {
	private static final long serialVersionUID = -4270368481502787095L;
	protected Map<ClientCallbackInterface, TimeHistory> thMap = new HashMap<>();
	protected Vector<ClientCallbackInterface> clients = new Vector<ClientCallbackInterface>();
	protected String localMadafaka = "Not yo";
	protected String localSamuel = "Not";
	
	public MyRmiServant() throws RemoteException {}
	
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

	public String supMadafaka(String saySth) throws InterruptedException{
		supMadafakaThread mdfTh= new supMadafakaThread(saySth);
		mdfTh.join();
		return localMadafaka;
	}

	public String supSamuel() throws InterruptedException{
		supSamuelThread susThr = new supSamuelThread();
		susThr.join();
		return localSamuel;
	}
	
	/*
	 * public String saveRemotely(Pakcet pckt) throws InterruptedException {
	 * SaveRemotelyThread svThr = new SaveRemotelyThread(pckt); svThr.join(); return
	 * "Package saved on server"; }
	 */	
	public boolean saveRemotely(Pakcet pckt) throws RemoteException {
		
		
		return true;
	}
	
	class supMadafakaThread extends Thread{
		String localSay = "";
		
		public supMadafakaThread(String saySth) {
			super("SupMadafakaThread");
			start();
			localSay = saySth;
		}
		
		/*
		 * private String supMadafaka() { String temp = "yo"; return temp; }
		 */
		
		public void run( ) {
			localMadafaka = localSay;
		}
	}
	
	class supSamuelThread extends Thread{
		public supSamuelThread() {
			super("SupSamuelThread");
			start();
		}
	
		private String locSupSamuel() {
			String temp = "Who stole my madafaka?";
			return temp;
		}
		
		public void run() {
			localSamuel = this.locSupSamuel();
		}
	}
	
	class SaveRemotelyThread extends Thread{
		Pakcet locObj;
		
		public SaveRemotelyThread(Pakcet pckt) {
			super("saveRemotelyThread");
			synchronized(this) {
				locObj = pckt;
				start();
			}
		}
	
		public void run() {}
	}
	
}