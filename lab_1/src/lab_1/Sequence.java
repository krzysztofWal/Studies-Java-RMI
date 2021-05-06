package lab_1;

public abstract class Sequence<T> extends Pakcet{
	protected int channelNr;
	protected String unit;
	protected double resolution;
	protected T[] buffer;

	@SuppressWarnings("unchecked") 
	public Sequence() {
		super();
		this.channelNr = 0;
		this.unit = "not given";
		this.resolution = 0.0;
		this.buffer = (T[]) new Object[10];
	}
	
	@SuppressWarnings("unchecked") /* unchecked cast from Object to T type*/
	public Sequence(String device, String description, long date, int channelNr, String unit, double resolution, T[] buffer) {
		super(device, description, date);
		this.channelNr = channelNr;
		this.unit = unit;
		this.resolution = resolution;
		this.buffer = (T[]) new Object[buffer.length];
		/* przepisanie jednej tablicy do drugiej */
		for (int i = 0; i < buffer.length; i++) {
			this.buffer[i] = buffer[i];
		}
	}
	
	protected String dataToString() {
		String temp = "Data :\n";
		byte counter = 0;
		for (T el : buffer) {
			if (el != null) {
				temp += el + "\n";
				counter++;
			}
		}
		if (counter == 0) {
			temp += "\tNo data recorded\n";
		}
		return temp;
	}
	
	@Override
	public String toString() {
		return super.toString() + 
				"Channel Number : " + this.channelNr +
				"\nUnit :" + this.unit +
				"\nResolution : " + this.resolution +
				"\n";
	}

}
