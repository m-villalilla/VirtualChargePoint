package ocpp_client_backend;

import ocpp_client_backend.Chargepoint_stable;
import ocpp_client_backend.WebsocketClientEndpoint;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.io.BufferedInputStream;
import java.util.Properties;

public class launchClientBackend {
	private static Properties config;
	
	public static void main(String[] args) throws IOException {
		//Reads configuration out of the configuration file
		BufferedInputStream stream = new BufferedInputStream(new FileInputStream("client.properties"));
		config = new Properties();
		config.load(stream);
		stream.close();
		
		String serverURL  		= config.getProperty("serverURL");
		String ChargeBoxID 		= config.getProperty("ChargeBoxID.00");				//Use ChargeBoxID.00 as example
		String CPVendor   		= config.getProperty("CPVendor");
		String CPModel 	  		= config.getProperty("CPModel");
		String authorizationID	= config.getProperty("authorizationID.00");			//Use authorizationID.00 as example
		
		//OCPPServerStressTest.startTest(100, serverURL);
		
		Chargepoint_stable client = new Chargepoint_stable(ChargeBoxID, CPVendor, CPModel, true, false);
			
		try {
			client.connect(serverURL);
			System.out.println("Client connected.");
		} catch (Exception e) {
			System.out.println("Error while trying to connect to the server.");
			e.printStackTrace();
		}
		
		try {
			client.sendBootNotification();
		} catch (Exception e) {
			System.out.println("Error while trying to sent a boot notification");
			e.printStackTrace();
		}
		
		try {
			client.sendAuthorizeRequest(authorizationID);
		} catch (Exception e) {
			System.out.println("Error while trying to authorize an ID");
			e.printStackTrace();
		}
		
		try {
			client.checkTransactionSupport(authorizationID);
		} catch (Exception e) {
			System.out.println("Error while trying to start a transaction");
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(2000);	// Give the server time to respond to ongoing requests
			client.disconnect();
			System.out.println("Client disconnected.");
		} catch (InterruptedException e) {
			System.out.println("Error while trying to disconnect");
			e.printStackTrace();
		}
		
        client.testVersions(serverURL);
	}
	
	public static Properties getConfig() {
		return config;
	}
    
}
