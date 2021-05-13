package my_rmi;

import java.io.Serializable;

/*
 * pojemnik na kryteria wyszukiwania
 */
public class SearchData implements Serializable{

	private static final long serialVersionUID = -3341024789462891979L;
	protected String deviceName;
	protected Integer channel;
	protected Long aquisitionTime;
	
	public SearchData(String dN, Integer c, Long aT) {
		deviceName = dN;
		channel = c;
		aquisitionTime = aT;
	}
	
	public String getDeviceName() {
		return deviceName;
	}
	public Integer getChannel() {
		return channel;
	}
	
	public Long getAquisitionTime() {
		return aquisitionTime;
	}
	
}
