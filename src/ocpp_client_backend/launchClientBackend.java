package ocpp_client_backend;

import ocpp_client_backend.JSONClientSamplev0_5;

public class launchClientBackend {

	//Global configuration
	public static String serverURL = "test-ocpp.ddns.net:8080/steve/websocket/CentralSystemService/";
	public static String clientName = "TestPoint";
	public static String CPVendor = "TestVendor";
	public static String CPModel = "TestModel";
	
	public static void main(String[] args){
		JSONClientSamplev0_5 s = new JSONClientSamplev0_5();
		
		try {
			s.connect();
		} catch (Exception e) {
			System.out.println("Error while trying to connect to the server.");
			e.printStackTrace();
		}
		
		try {
			s.sendBootNotification();
		} catch (Exception e) {
			System.out.println("Error while trying to sent a boot notification");
			e.printStackTrace();
		}
	}

}
