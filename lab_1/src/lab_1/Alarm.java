package lab_1;

public class Alarm<T> extends Pakcet {
	private int channelNr;
	private T threshold;
	private int direction; // 0 - dowolny, -1 - w dó³, 1 - w górê
	
	public Alarm() {
		super();
		this.channelNr = 0;
		this.threshold = null;
		this.direction = 0;
	}
	
	public Alarm(String device, String description, long date, int channelNr, T threshold, int direction) {
		super(device, description, date);
		this.channelNr = channelNr;
		this.threshold = threshold;
		this.direction = direction;
	}
	
	@Override 
	public String toString() { 
		String temp = super.toString() + "Channel Nr: " + channelNr + "\nThreshold: " + threshold + "\nDirection: ";
		if (direction == 0) {
			temp += "Up or down";
		} else if (direction == 1) {
			temp += "Up";
		} else if (direction == -1) {
			temp += "Down";
		} else {
			temp += "Unknown direction";
		}
		return temp + "\n";
		}
	
	
}
