package ocpp_client_backend;

import ocpp_client_backend.JSONClientSamplev0_5;

public class launchClientBackend {

	public static void main(String[] args){
		//Configuration
		String serverURL = "test-ocpp.ddns.net:8080/steve/websocket/CentralSystemService/";
		String clientName = "TestPoint";
		String CPVendor = "TestVendor";
		String CPModel = "TestModel";
		String testID = "1234567890";
		
		JSONClientSamplev0_5 client = new JSONClientSamplev0_5();
		
		try {
			client.connect(serverURL, clientName);
		} catch (Exception e) {
			System.out.println("Error while trying to connect to the server.");
			e.printStackTrace();
		}
		
		try {
			client.sendBootNotification(CPVendor, CPModel);
		} catch (Exception e) {
			System.out.println("Error while trying to sent a boot notification");
			e.printStackTrace();
		}
		
		try {
			client.sendAuthorizeRequest(testID);
		} catch (Exception e) {
			System.out.println("Error while trying to authorize an ID");
			e.printStackTrace();
		}
		
		try {
			client.sendStartTransactionRequest(1, testID);
		} catch (Exception e) {
			System.out.println("Error while trying to start a transaction");
			e.printStackTrace();
		}
		
	}

}
