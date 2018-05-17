package ocpp_client_backend;

import ocpp_client_backend.Chargepoint_stable;
import java.io.FileInputStream;
import java.io.IOException;
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
		
		Chargepoint_stable client = new Chargepoint_stable();
		
		OCPPServerStressTest.startTest(100, serverURL, CPVendor, CPModel);
		client.setMeasureMode(true);
		
		try {
			client.connect(serverURL, ChargeBoxID);
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
			Thread.sleep(2000);														//Give the server time to respond to ongoing requests
			client.disconnect();
		} catch (InterruptedException e) {
			System.out.println("Error while trying to disconnect");
			e.printStackTrace();
		}
	}
	
	public static Properties getConfig() {
		return config;
	}
    
}
