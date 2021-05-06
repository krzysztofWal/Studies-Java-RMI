package lab_1;

public abstract class Pakcet {
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
	
	@Override
	public String toString() {
		return "Device : " + this.device +
				"\nDescription : " + this.description +
				"\nDate : " + this.date +
				"\n";
	}
	
	
}
