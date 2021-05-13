package my_rmi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Vector;
import lab_1.TimeHistory;
import lab_1.Pakcet;
import lab_1.Spectrum;

public class MyRmiClient {
	
	private final int sendChoice = 1;
	private final int quitChoice = 2;
	private final int findChoice = 3;
	private final int toFileChoice = 4;
	private final int receiveChoice = 5;
	private final int returnToFind = -1;
	private final int printListAgain = 6;
	private final int printReceivedPackets = 7;
	
	protected FirstInterface rmtObj;
	protected ClientCallbackInterface clntObj;
	protected Vector<Pakcet> packetVectorToSend = new Vector<Pakcet>();
	protected Vector<Pakcet> receivedPackets = new Vector<Pakcet>();
	
	
	private MyRmiClient(String host) throws RemoteException, IOException, InterruptedException, NotBoundException {
		// getting names to the registry
		Registry registry = LocateRegistry.getRegistry(host);
		rmtObj = (FirstInterface) registry.lookup("rmiServer");
		// wywo³anie meody register() na rmtObjs
		clntObj = new MyCLientCallback();
		rmtObj.register(clntObj);
	
		populateVector();
		boolean userDone = false;
		
		while(!userDone) {
			switch (menu()) {
				case (sendChoice) :
					String result = sendToServer();
					if (!result.isEmpty()) {
						System.out.println("\t" + result);
					}
					break;
				case (findChoice) :
					ArrayList<ListDataWrapper> arr = findOnServer();
					if (arr != null) {
							if (arr.isEmpty()) {
								System.out.println("Didn't find what you were looking for\n");
							} else {
								System.out.println("\nPackets found with your request:\n\t(device name, device description, channel number, when aqusition took place, packet type)");
								printList(arr);
								System.out.println();
								int chosen;
								boolean quitSubmenu = false;
								boolean firstTime = true;
								while (!quitSubmenu) {
									switch(fileOrReceive(firstTime)) {
									case(toFileChoice):
										chosen = whichToFileOrReceive("[file]", "save to a file on the server?", arr.size());
										if (chosen != returnToFind) {
											System.out.println(rmtObj.saveToFileRemotely(arr.get(chosen).getId(), arr.get(chosen).getType()));
										} 
										firstTime=false;
										break;
									case(receiveChoice):
										chosen = whichToFileOrReceive("[receive]", "receive locally?", arr.size());
										if (chosen != returnToFind) {
											Pakcet temp = rmtObj.receiveLocally(arr.get(chosen).getId(), arr.get(chosen).getType());
											if (temp != null) {
												boolean rcvPcktAlreadyContains = false;
												for (Pakcet el : receivedPackets) {
													if (el.toStringNoData().equals(temp.toStringNoData())) {
														rcvPcktAlreadyContains = true;
													}
												}
												if (!rcvPcktAlreadyContains) {
													receivedPackets.add(temp);
													System.out.println("Received required packet");
												} else {
													System.out.println("Packet has been already received");
												}
											} else {
												System.out.println("Failed to receive required packet");
											}
										} 
										firstTime = false;
										break;
									case (printListAgain):
										printList(arr);
										System.out.println();
										break;
									default:
										quitSubmenu = true;
										break;
									}
								}
							}
					}
					break;
				case (printReceivedPackets) :
					if (!receivedPackets.isEmpty()) {
						System.out.println();
						printPacketVector(receivedPackets);
						System.out.println();
					} else {
						System.out.println("There are no locally stored packets from the server");
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
        System.out.println("\t[s]ave package on the server, [f]ind packet on the server, [p]rint locally stored packets from the server, [q]uit");
        System.out.print("\t");
        String s = br.readLine();
        switch (s) {
        case "s":
        	return sendChoice;
        case "q":
        	return quitChoice;
        case "f":
        	return findChoice;
        case "p":
        	return printReceivedPackets;
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
	
	/*
	 * zwraca wybór u¿ytkownika, który pakiet chce przes³aæ
	 */
	private int chooseWhatToSend() throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("[save] Which element do you want to save on server? (return to [m]enu)");
		System.out.println("\t");
		int count = 1;
		for (Pakcet el : packetVectorToSend) {
			System.out.print(count + ")");
			String[] sArr = el.toString().split("\n", 5);
			for (int i = 0; i < sArr.length - 1; i++) {
				System.out.println("\t" + sArr[i]);
			}
			count++;
		} 
		String s = "";
		do {
			System.out.print("\t");
			 s =  br.readLine();
			 try {
			 	 int i = Integer.parseInt(s); 
				 if (i > 0 && i <= packetVectorToSend.size()) {
					 return i;
				 } else {
					 System.out.println("[save] Does not exist. Choose again or return to [m]enu");
				 }
			 } catch(NumberFormatException nfe) {
				 if (!s.matches("m")) {
					 System.out.println("[save] Wrong format. Try again or return to [m]enu");
				 }
			 }
		} while (!s.matches("m"));
			return 0;
	}
	
	/*
	
	/*
	 * wyszukuje pakiety na serwerze i zwraca liste, kiedy uzytkownik decyduje na powrot do menu
	 * zwraca null, w przypadku nie znalezienia na serwerze zwraca pusta liste
	 */
	private ArrayList<ListDataWrapper> findOnServer() throws IOException {
		
		SearchData sD = whatImLookingFor();
		if (sD != null) {
			return rmtObj.findRemotely(sD);
		}
		return null;
	}

	/*
	 * zwraca opis pakietu szukanego przez uzytkownika, null kiedy uzytkownik decyduje na powrot do menu
	 */
	private SearchData whatImLookingFor() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("[find] what data are you looking for?\n"
				+ "\tdevice name, channel number, aqusition time - separeted by comma, may omit parameters but keep the separator (return to [m]enu)");
		
		String s = "";
		do {
			System.out.print("\t");
			 s = br.readLine();
			 if (!s.matches("m")) {
				 
				 
				 if (!s.isEmpty()) {
				 	if (s.charAt(s.length()-1) == ',') {
						 s = s + " ";
					 }
				 }
				 
				 if (s.strip().equals(",,,")) {
					 return new SearchData(null, null, null);
				 }
				 
				 String[] splitted = s.split(",");
				 // 
				 if (splitted.length != 3) {
					 System.out.println("[find] Wrong input, try again or return to [m]enu");
				 } else {
					
					Integer i = null;
					Long j = null;
					try {
						// jesli obydwa wyrazy niepuste
						if (!splitted[1].isBlank() && !splitted[2].isBlank()) {	 
							 i = Integer.parseInt(splitted[1].strip());
							 j = Long.parseLong(splitted[2].strip());
						// jesli drugi wyraz pusty
						} else if (splitted[1].isBlank() && !splitted[2].isBlank()) {
							j = Long.parseLong(splitted[2].strip());
						//jesli trzeci pusty
						} else if (!splitted[1].isBlank() && splitted[2].isBlank()) {
							i = Integer.parseInt(splitted[1].strip());	 
						}
						
						 if (splitted[0].isBlank()) {
							 splitted[0] = null;
						 } else {
							 splitted[0] = splitted[0].strip();
						 }
						 return new SearchData(splitted[0], i, j);
						 
					} catch (NumberFormatException nfe) {
						System.out.println("[find] Wrong input, try again or return to [m]enu");
					}

				 }
				
			 }
		} while (!s.matches("m"));
		return null;
	}
	
	/*
	 * wyswietla na konsoli otrzymana liste obiektow typu ListDataWrapper
	 */
	private	void printList(ArrayList<ListDataWrapper> arr) {
		int index = 1;
		for (ListDataWrapper el : arr) {
				System.out.print(index++ + ") ");
				System.out.printf("%-30s", el.getDeviceName());
				System.out.printf("%-30s", el.getDescription());
				System.out.print(el.getChannel() + "\t");
				System.out.print(el.getAquisitionTime() + "\t");
				System.out.print(el.getType() == ListDataWrapper.packetType.th ? "Time History\n" : "Spectrum\n");
		}
	}
	
	/*
	 * zwraca preferencje uzytkownika czy chce zapisac pakiet do pliku na serwerze czy zapisac go lokalnie, zwraca zero kiedy uzytkownik decyduje na powrot do menu
	 */
	private int fileOrReceive(boolean firstTime) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		if (firstTime) {
			System.out.println("[find] What do you want to do? (return to [m]enu - search results will be lost!)\n"
				+ "\t" + "save to [f]ile on server, [r]eceive data locally");
		} else {
			System.out.println("[find] Do you want to do anything else with found packages?"
					+ " (save to [f]ile on server, [r]eceive data locally, [p]rint the list again)"
					+ "\n\t if not return to [m]enu");
		}
		String s = "";
		do {
			System.out.print("\t");
			 s =  br.readLine();
			 if(!s.matches("m")) {
			 	if (s.strip().equals("f")) {
					 return toFileChoice;
				} else if (s.strip().equals("r")) {
					return receiveChoice;
				} else if (s.strip().matches("p") && !firstTime) {
					return printListAgain;
				} else {
					System.out.println("[find] Wrong command. Choose again or return to [m]enu (search results will be lost!)");
				}
			 }
		} while (!s.matches("m"));
		return 0;
	}
	
	/*
	 * zwraca wybrany przez uzytkownika pakiet lub wartosc returnToFind kiedy uzytkownik zdecyduje wrocic do wyzszego poziomu menu 
	 */
	private int whichToFileOrReceive(String submenu, String action, int size) throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.println(submenu + " Which element do you want to " + action + " (return to [f]ind)");
		String s = "";
		do {
			System.out.print("\t");
			 s =  br.readLine();
			 try {
			 	 int i = Integer.parseInt(s); 
				 if (i > 0 && i <= size) {
					 return i - 1;
				 } else {
					 System.out.println(submenu + " Does not exist. Choose again or return to [f]ind)");
				 }
			 } catch(NumberFormatException nfe) {
				 if (!s.matches("f")) {
					 System.out.println(submenu + " Wrong format. Try again or return to [f]ind)");
				 }
			 }
		} while (!s.matches("f"));
			return returnToFind;
	}
	
	/*
	 * wyswietla na konsoli wektor otrzymanych pakietow
	 */
	private void printPacketVector(Vector<Pakcet> vec) {
		int count = 1;
		for (Pakcet el : vec) {
			System.out.print(count + ") ");
			System.out.printf("%-30s", el.getDeviceName());
			System.out.printf("%-30s", el.getDescription());
			System.out.print(el.getChannelNr() + "\t");
			System.out.print(el.getAqusitionTime() + "\t");
			System.out.print( el.getClassStringId() == "TimeHistory" ? "Time History\n" : "Spectrum\n");
		}
	}
	
	/*
	 * zapelnia przykladowy wektor pakietow
	 */
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