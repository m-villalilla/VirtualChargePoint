package virtualchargepoint_gui;

/**
 * This class is used for the version test to be displayed in a tableview.
 * Each object contains a version an if the server supports this version.
 */
public class VersionRow {
	private String version;
	private String supported;
	
	/**
	 * Default constructor for VersionRow
	 */
	public VersionRow()
	{
		this("", "");
	}
	
	/**
	 * Parameterized constructor for VersionRow
	 * 
	 * @param v - Used to set the version
	 * @param s - Used to set if the server supports it or not
	 */
	public VersionRow(String v, String s) {
		version = v;
		supported = s;
	}
	
	/**
	 * @return Returns the version of VersionRow
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * @return Returns if the server supports the version or not
	 */
	public String getSupported() {
		return supported;
	}
}
