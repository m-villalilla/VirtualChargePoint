package virtualchargepoint_gui;

/**
 * This class is used for the feature test to be displayed in a tableview.
 * Each object contains a feature an if the server supports this version.
 */
public class FeatureRow {
	private String feature;
	private String supported;
	
	/**
	 * Default constructor for FeatureRow
	 */
	public FeatureRow()
	{
		this("", "");
	}
	
	/**
	 * Parameterized constructor for FeatureRow
	 * 
	 * @param f - Used to set the feature
	 * @param s - Used to set if the server supports it or not
	 */
	public FeatureRow(String f, String s) {
		feature = f;
		supported = s;
	}
	
	/**
	 * @return Returns the feature of FeatureRow
	 */
	public String getFeature() {
		return feature;
	}
	
	/**
	 * @return Returns if the server supports the feature or not
	 */
	public String getSupported() {
		return supported;
	}
}
