package ocpp_client_backend;

import ocpp_client_backend.JSONClientSamplev0_5;

public class launchClientBackend {

	//Global configuration
	public static String serverURL = "test-ocpp.ddns.net:8080/steve/websocket/CentralSystemService/";
	public static String clientName = "TestPoint";
	
	public static void main(String[] args){
		JSONClientSamplev0_5 s = new JSONClientSamplev0_5();
		
		try {
			s.connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			s.sendBootNotification();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
