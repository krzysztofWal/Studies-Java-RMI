package lab_1;

import java.io.Serializable;

public abstract class Pakcet implements Serializable {
	private static final long serialVersionUID = 4053089504880839526L;
	protected String device;
	protected String description;
	protected long date;
	
	public Pakcet() {
		device = "not given";
		description = "not given";
		date = 0;
	}
	
	public Pakcet(String device, String description, long date) {
		this.device = device;
		this.description = description;
		this.date = date;
	}
	
	public String getClassStringId() {
		return "Pakcet";
	}
	
	public int getChannelNr() {
		return -1;
	}
	
	public String getDeviceName() {
		return device;
	}
	
	public String getDescription() {
		return description;
	}
	
	public long getAqusitionTime() {
		return date;
	}
	
	@Override
	public String toString() {
		return "Device : " + this.device +
				"\nDescription : " + this.description +
				"\nDate : " + this.date +
				"\n";
	}
	
	public String toStringNoData() {
		return null;
	}
	
	
	
}
