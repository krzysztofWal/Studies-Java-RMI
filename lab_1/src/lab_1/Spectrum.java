package lab_1;

public class Spectrum<T> extends Sequence<T> {

	private static final long serialVersionUID = -175729181179077787L;
	private String scaling;

	public Spectrum() {
		super();
		this.scaling = "linear";
	}
	
	public Spectrum(String device,
					String description, 
					long date,
					int channelNr, 
					String unit, 
					double resolution, 
					T[] buffer,
					String scaling) {
		super(device,
				description, 
				date,
				channelNr, 
				unit, 
				resolution, 
				buffer);
		this.scaling = scaling;
	}
	
	
	
	@Override
	public String getClassStringId() {
		return "Spectrum";
	}
	
	@Override 
	public String toString() { return super.toString() + "Scaling: " + scaling + "\n" + dataToString();}

	public String toStringNoData() {
		return super.toString() + "Scaling: " + scaling + "\n";
	}
	
}