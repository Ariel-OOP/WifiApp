package components;

import java.util.ArrayList;
import java.util.List;

public class WifiPointsTimePlace {

	List<WIFISample> WifiPoints;

	private String FirstSeen;
	private String Device;
	private String Lat;
	private String Lon;
	private String Alt;

	public WifiPointsTimePlace() {
		WifiPoints = new ArrayList();;
	}

	public void addPoint(WIFISample point)
	{
		FirstSeen = point.getWIFI_FirstSeen();
		Device = point.getWIFI_Device();
		Lat = point.getWIFI_Lat();
		Lon = point.getWIFI_Lon();
		Alt = point.getWIFI_Alt();

		WifiPoints.add(point);
	}

	/**
	 * The method enters into a list the common parameters of these WIFI points (time, place, device)
	 * and in addition the parameters of the points (MAC, SSID, Frequency, RSSI).
	 * @return the List<String> that contain all parameters above.
	 */
	public List<String> getWifiPoints() {

		List<String> line = new ArrayList();
		line.add(FirstSeen);
		line.add(Device);
		line.add(Lat);
		line.add(Lon);
		line.add(Alt);
		line.add("" + WifiPoints.size());

		for(WIFISample wifi : WifiPoints) {
			line.add(wifi.getWIFI_SSID());
			line.add(wifi.getWIFI_MAC());
			line.add(wifi.getWIFI_Frequency());
			line.add(wifi.getWIFI_RSSI());
		}

		return line;
	}

	/**
	 *
	 * @return int number of points in this line
	 */
	public String toString()
	{
		return "" + WifiPoints.size();
	}

}
