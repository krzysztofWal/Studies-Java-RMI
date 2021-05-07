package my_rmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Vector;
import lab_1.TimeHistory;
import lab_1.Pakcet;
import lab_1.Spectrum;

public class MyRmiClient {
	
	private final int sendChoice = 1;
	private final int quitChoice = 2;
	
	protected FirstInterface rmtObj;
	protected ClientCallbackInterface clntObj;
	protected Vector<Pakcet> packetVectorToSend = new Vector<Pakcet>();
	
	
	private MyRmiClient(String host) throws RemoteException, IOException, InterruptedException, NotBoundException {
		// getting names to the registry
		Registry registry = LocateRegistry.getRegistry(host);
		rmtObj = (FirstInterface) registry.lookup("rmiServer");
		// wywo³anie meody register() na rmtObjs
		clntObj = new MyCLientCallback();
		rmtObj.register(clntObj);
	
		populateVector();
		
	//	String response = rmtObj.supMadafaka();
	//	System.out.println("response: " + response);
	//	response = rmtObj.supSamuel();
	//	System.out.println("response: " + response);
		
		boolean userDone = false;
		
		while(!userDone) {
			switch (menu()) {
			case (sendChoice) :
				String result = sendToServer();
				if (!result.isEmpty()) {
					System.out.println(result);
				}
				break;
			case (quitChoice) :
				userDone = true;
			break;
			default:
				System.out.println("Wrong command");
			}
		}
		
		rmtObj.unregister(clntObj);

	}
	
	public static void main(String[] args) {
		
		String host = (args.length < 1) ? null : args[0];
		try {
			new MyRmiClient(host);
			System.exit(0);
		} catch (Exception e) {
			System.err.println("Client exception: " + e.toString());
			e.printStackTrace();
		}
	}

	/*
	 * zwraca nr akcji wybranej przez u¿ytkownika
	 */
	private int  menu() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("[menu] What do you want to do?\n");
        System.out.println("[s]end package, [q]uit");
        String s = br.readLine();
        switch (s) {
        case "s":
        	return sendChoice;
        case "q":
        	return quitChoice;
        default:
        	return 0;
        }
	}
	
	/*
	 * wysy³a dane na serwer wywo³uj¹c metodê zdalnego obiektu rmtObj
	 */
	private String sendToServer() throws IOException, InterruptedException {
		int n = chooseWhatToSend();
		if (n != 0) {
			return (rmtObj.saveRemotely(packetVectorToSend.get(n-1), clntObj));
		}
		return "";
	}

	/*)
	 * zwraca wybór u¿ytkownika, który pakiet chce przes³aæ
	 */
	private int chooseWhatToSend() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("[send] Which element do you want to send to server?");
		int count = 1;
		for (Pakcet el : packetVectorToSend) {
			System.out.print(count + ")");
			String[] sArr = el.toString().split("\n", 4);
			for (int i = 0; i < sArr.length - 1; i++) {
				System.out.println("\t" + sArr[i]);
			}
			count++;
		} 
				 String s = "";
				 do {
					 s =  br.readLine();
					 try {
					 	 int i = Integer.parseInt(s); 
						 if (i > 0 && i <= packetVectorToSend.size()) {
							 return i;
						 } else {
							 System.out.println("[send] Does not exist. Choose again or return to [m]enu");
						 }
					 } catch(NumberFormatException nfe) {
						 if (!s.matches("m")) {
							 System.out.println("[send] Wrong format. Try again or return to [m]enu");
						 }
					 }

				 }	while (!s.matches("m"));
				 
				 return 0;
	}
	
	private  void populateVector() {
		Double[] tab1 = {1.42, 1.5, 1.8};
		TimeHistory<Double> pckt1 = new TimeHistory<Double>("First device",
				"Powerful device",
				12912020,
				3,
				"Volts",
				0.4,
				tab1,
				0.5);
		
		Float[] tab2 = {1.42f, 1.5f, 1.8f};
		TimeHistory<Double> pckt2 = new TimeHistory<Double>("Second device",
				"Not so powerful device",
				12912021,
				3,
				"Current",
				1,
				tab1,
				1.3);
		
		Spectrum<Float> pckt3 = new Spectrum<Float>("Third device",
				"Powerful device",
				12912020,
				3,
				"Amperozwój/Weber",
				0.4,
				tab2,
				"logarythmic");
		
		packetVectorToSend.add(pckt1);
		packetVectorToSend.add(pckt2);
		packetVectorToSend.add(pckt3);
	}
}