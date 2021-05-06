package lab_1;

public class TimeHistory<T> extends Sequence<T>{
	private double sensitivity;
	
	public TimeHistory() {
		super();
		this.sensitivity = 0.0;
	}
	
	public TimeHistory(String device,
						String description, 
						long date,
						int channelNr, 
						String unit, 
						double resolution, 
						T[] buffer,
						double sensitivity) {
		super(device,
				description, 
				date,
				channelNr, 
				unit, 
				resolution, 
				buffer);
		this.sensitivity = sensitivity;
	}
	
	
	@Override 
	public String toString() { 
		return super.toString() + "Sensitivity: " + sensitivity + "\n" + dataToString();
		
	}
	
}
