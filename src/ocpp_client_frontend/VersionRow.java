package ocpp_client_frontend;


public class VersionRow {private String version;
	private String supported;
	
	public VersionRow()
	{
		this("","");
	}
	
	public VersionRow(String v, String s) {version = v;
		supported = s;
	}
	
	public String getVersion() {
		return version;
	}
	public String getSupported() {
		return supported;
	}
}
