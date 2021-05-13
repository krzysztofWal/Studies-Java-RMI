package my_rmi;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import lab_1.Pakcet;
import lab_1.Spectrum;
import lab_1.TimeHistory;

public class MyRmiServant extends UnicastRemoteObject implements FirstInterface {
	private static final long serialVersionUID = -4270368481502787095L;
	protected Map<String, PacketAndInfo> thMap = new HashMap<>();
	protected Map<String, PacketAndInfo> spMap = new HashMap<>();
	protected Vector<ClientCallbackInterface> clients = new Vector<ClientCallbackInterface>();
	protected String localMadafaka = "Not yo";
	protected String localSamuel = "Not";
	protected long id_base = 0;
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
				if (!thMap.containsKey(pckt.toStringNoData())) {
					thMap.put(pckt.toStringNoData(),new PacketAndInfo(pckt, clbck));
					saveRemotelyResult = "Saved sent TimeHistory packet";
				} else {
					saveRemotelyResult = "TimeHistory packet already saved";
				}
			} else if (pckt.getClassStringId().equals((new Spectrum<Integer>()).getClassStringId())) {
				if (!spMap.containsKey(pckt.toStringNoData())) {
					spMap.put(pckt.toStringNoData(),new PacketAndInfo(pckt, clbck));
					saveRemotelyResult = "Saved sent Spectrum packet";
				} else {
					saveRemotelyResult = "Spectrum packet already saved";
				}
			} else {
				saveRemotelyResult = "Failed saving package";
			}
		}
		return saveRemotelyResult;
	}
	
	public String saveToFileRemotely(long id, ListDataWrapper.packetType pckType ) throws RemoteException {
		if (pckType == ListDataWrapper.packetType.th) {
			for (Map.Entry<String, PacketAndInfo> el : thMap.entrySet()) {
				if (el.getValue().getId() == id) {
					if (!el.getValue().getIfSaved()) {
						String name = el.getValue().getPakcet().getDeviceName().replaceAll("\\s","") +
								"Channel" + el.getValue().getPakcet().getChannelNr() +
								el.getValue().getPakcet().getAqusitionTime();
						saveToFile(name, "thi", el.getValue().getPakcet());
						el.getValue().saved();
						return "File " + name + ".thi saved remotely";
					} else {
						return "Packet has been already saved to file";
					}
				}
			}
		} else if (pckType == ListDataWrapper.packetType.sp) {
			for (Map.Entry<String, PacketAndInfo> el : spMap.entrySet()) {
				if (el.getValue().getId() == id) {
					if (!el.getValue().getIfSaved()) {
						String name = el.getValue().getPakcet().getDeviceName().replaceAll("\\s","") +
								"_Channel" + el.getValue().getPakcet().getChannelNr() +
								"_" + el.getValue().getPakcet().getAqusitionTime();
						saveToFile(name, "spc", el.getValue().getPakcet());
						el.getValue().saved();
						return "File " + name + ".spc saved remotely";
					} else {
						return "Packet has been already saved to file";
					}
				}
			}
		}
		return "Request failed";
	}
	
	public Pakcet receiveLocally(long id, ListDataWrapper.packetType pckType) throws RemoteException {
		if (pckType == ListDataWrapper.packetType.th) {
			for (Map.Entry<String, PacketAndInfo> el : thMap.entrySet()) {
				if (el.getValue().getId() == id) {
					return el.getValue().getPakcet();
				}
			}
		} else if (pckType == ListDataWrapper.packetType.sp) {
			for (Map.Entry<String, PacketAndInfo> el : spMap.entrySet()) {
				if (el.getValue().getId() == id) {
					return el.getValue().getPakcet();
				}
			}
		}
		return null;
	}
	
	public ArrayList<ListDataWrapper> findRemotely(SearchData data) throws RemoteException{
		synchronized(this) {
			ArrayList<ListDataWrapper> temp = new ArrayList<ListDataWrapper>();
			if (!(data.getDeviceName() == null && data.getAquisitionTime() == null && data.getChannel() == null)) {
				temp.addAll(searchThroughPacketMap(thMap, data, ListDataWrapper.packetType.th));
				temp.addAll(searchThroughPacketMap(spMap, data, ListDataWrapper.packetType.sp));
			} else {
				for (Map.Entry<String, PacketAndInfo> el : thMap.entrySet()) {
						temp.add(new ListDataWrapper(el.getValue().getPakcet().getDeviceName(),
											el.getValue().getPakcet().getChannelNr(),
											el.getValue().getPakcet().getAqusitionTime(),
											ListDataWrapper.packetType.th,
											el.getValue().getPakcet().getDescription(),
											el.getValue().getPakcet(),
											el.getValue().getId()));
					}
				for (Map.Entry<String, PacketAndInfo> el : spMap.entrySet()) {
					temp.add(new ListDataWrapper(el.getValue().getPakcet().getDeviceName(),
										el.getValue().getPakcet().getChannelNr(),
										el.getValue().getPakcet().getAqusitionTime(),
										ListDataWrapper.packetType.sp,
										el.getValue().getPakcet().getDescription(),
										el.getValue().getPakcet(),
										el.getValue().getId()));
				}
			}
			return temp;
		}
	}
	
	/*
	 * == metody pomocnicze ==
	 */
	/*
	 * przeszukuje hashmape poszukujac obiektu spelniajacego podane kryteria
	 */
	private ArrayList<ListDataWrapper> searchThroughPacketMap(Map<String, PacketAndInfo> map,
																SearchData data,
																ListDataWrapper.packetType type) {
		ArrayList<ListDataWrapper> temp = new ArrayList<ListDataWrapper>();
		
		boolean checkForNullRegister[] = {false, false, false};
		if (data.getDeviceName() == null) {
			checkForNullRegister[0] = true;
			//System.out.println("name is null");
		}
		else {
			//System.out.println(data.getDeviceName());
		}
		if (data.getChannel() == null) {
			checkForNullRegister[1] = true;
			//System.out.println("channel is null");
		}
		if (data.getAquisitionTime() == null) {
			checkForNullRegister[2] = true;
			//System.out.println("time is null");
		}
		
		for (Map.Entry<String, PacketAndInfo> el : map.entrySet()) {
			
			boolean matchesGiven[] = {false, false, false};
			if (!checkForNullRegister[0]) {
				matchesGiven[0] = (el.getValue().getPakcet().getDeviceName().equals(data.getDeviceName())) ? true : false;
				//System.out.println(matchesGiven[0]);
			}
			if (!checkForNullRegister[1])
				matchesGiven[1] = (el.getValue().getPakcet().getChannelNr() ==  data.getChannel()) ? true : false;
			if (!checkForNullRegister[2])
				matchesGiven[2] = (el.getValue().getPakcet().getAqusitionTime() == data.getAquisitionTime()) ? true : false;
			
			boolean writeToList = false;
			
			for (int i = 0; i < 3; i++) {
				if (!checkForNullRegister[i]) {
					//System.out.print("checkForNullRegister[" + i + "] = " + checkForNullRegister[i] + "\t");
					//System.out.println("matchesGiven[ " + i + "] = " + matchesGiven[i]);
					if (matchesGiven[i]) {
					
						if (!writeToList)
							writeToList = true;
					} else {
						writeToList = false;
						break;
					}
				}
			}
			
			if (writeToList) {
				temp.add(new ListDataWrapper(el.getValue().getPakcet().getDeviceName(),
									el.getValue().getPakcet().getChannelNr(),
									el.getValue().getPakcet().getAqusitionTime(),
									type,
									el.getValue().getPakcet().getDescription(),
									el.getValue().getPakcet(),
									el.getValue().getId()));
				//System.out.println("Dodano do listy");
			}
			
		}

		
		
		return temp;
	}
	
	private boolean saveToFile(String name, String extension, Pakcet pckt) {
		byte[] binaryData = serialize(pckt);
		try {
			if (binaryData != null) {
				Files.write(new File(name + "." + extension).toPath(), binaryData);
				return true;
			} else {
				return false;
			}
		} catch (IOException ioe){
			ioe.printStackTrace();
			return false;
		}
	}
	
	/**
	 * serializing the object passed as the argument
	 * to a byte array which is returned as the result 
	 * @param obj - object to serialize
	 * @return - byte array
	 */
	private byte[] serialize(Object obj) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
		  out = new ObjectOutputStream(bos);   
			out.writeObject(obj);
			out.flush();
		  return bos.toByteArray();
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		finally {
		  try {
		    bos.close();
		  } catch (IOException e) {
			  e.printStackTrace();
			  return null;
		  }
	  }
	}
	
	/*
	 * klasa przechowuj¹ca pakiet, przez którego klienta zosta³ zapisany, i jego id
	 */
	 class PacketAndInfo {
		private Pakcet packet;
		private ClientCallbackInterface clbck;
		private long id;
		private boolean ifSaved = false;
		
		public PacketAndInfo(Pakcet pck, ClientCallbackInterface clbck) {
			this.packet = pck;
			this.clbck = clbck;
			this.id = id_base++;
		}
		
		public Pakcet getPakcet() {
			return this.packet;
		}
		
		public long getId() {
			return id;
		}
		
		public void saved() {
			this.ifSaved = true;
		}
		
		public boolean getIfSaved() {
			return this.ifSaved;
		}
		
		public ClientCallbackInterface getCallbackObj() {
			return this.clbck;
		}
	}




}