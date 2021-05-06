package my_rmi;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;

public class MyRmiServer{
	Registry registry;
	MyRmiServant servant;
	public MyRmiServer() {}
	
	protected void newServant() throws RemoteException{
		servant = new MyRmiServant();
	}
	
	protected MyRmiServant getServant() {
		return servant;
	}
	
	public static void main(String args[]) {
		try {
			MyRmiServer srv = new MyRmiServer();
			//FirstInterface stub = (FirstInterface) UnicastRemoteObject.exportObject(srv, 0);
			
			Registry registry = LocateRegistry.createRegistry(1099);
			System.out.println(registry);
			srv.newServant();
			registry.bind("rmiServer",srv.getServant());
			System.out.println("Server ready");
		} catch (Exception e) {
			System.out.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
