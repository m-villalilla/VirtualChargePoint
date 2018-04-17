package ocpp_client_backend;

import ocpp_client_backend.JSONClientSamplev0_5;
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
		
		String serverURL  = config.getProperty("serverURL");
		String clientName = config.getProperty("clientName");
		String CPVendor   = config.getProperty("CPVendor");
		String CPModel 	  = config.getProperty("CPModel");
		String testID     = config.getProperty("testID");
		boolean measureMode = true;	//Set if you want to measure and print the elapsed time of server calls
		
		JSONClientSamplev0_5 client = new JSONClientSamplev0_5();
		
		try {
			client.connect(serverURL, clientName);
		} catch (Exception e) {
			System.out.println("Error while trying to connect to the server.");
			e.printStackTrace();
		}
		
		try {
			client.sendBootNotification(CPVendor, CPModel, measureMode);
		} catch (Exception e) {
			System.out.println("Error while trying to sent a boot notification");
			e.printStackTrace();
		}
		
		try {
			client.sendAuthorizeRequest(testID, measureMode);
		} catch (Exception e) {
			System.out.println("Error while trying to authorize an ID");
			e.printStackTrace();
		}
		
		try {
			client.sendStartTransactionRequest(1, testID, measureMode);
		} catch (Exception e) {
			System.out.println("Error while trying to start a transaction");
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(2000);	//Give the server time to respond to ongoing requests
			client.disconnect();
		} catch (InterruptedException e) {
			System.out.println("Error while trying to disconnect");
			e.printStackTrace();
		}
	}
	
	// TODO: Discuss: Is this function needed? It is not called anywhere
	public static Properties getConfig() {
		return config;
	}
}
