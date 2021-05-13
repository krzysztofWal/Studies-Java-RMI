package my_rmi;

import lab_1.Pakcet;

public class ListDataWrapper extends SearchData{

	private static final long serialVersionUID = -6955111607554082620L;
	protected packetType type;
	protected String description;
	protected Pakcet objReference;
	protected long id;
	
	ListDataWrapper(String s, Integer i, Long l, packetType type, String description, Pakcet objReference, long id){
		super(s,i,l);
		this.type = type;
		this.description = description;
		this.objReference = objReference;
		this.id = id;
		
	}
	
	public packetType getType() {
		return type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Pakcet getPacket() {
		return objReference;
	}
	
	public long getId() {
		return id;
	}
	
	public static enum packetType{
		th, sp
	}
}
