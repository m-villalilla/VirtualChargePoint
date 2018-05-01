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
		
		String serverURL  		= config.getProperty("serverURL");
		String ChargeBoxID 		= config.getProperty("ChargeBoxID.00");				//Use ChargeBoxID.00 as example
		String CPVendor   		= config.getProperty("CPVendor");
		String CPModel 	  		= config.getProperty("CPModel");
		String authorizationID	= config.getProperty("authorizationID.00");			//Use authorizationID.00 as example
		boolean measureMode 	= true;												//Set if you want to measure and print the elapsed time of server calls
		
		JSONClientSamplev0_5 client = new JSONClientSamplev0_5();
		
		//tortureTest(100, serverURL, CPVendor, CPModel);
		
		try {
			client.connect(serverURL, ChargeBoxID);
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
			client.sendAuthorizeRequest(authorizationID, measureMode);
		} catch (Exception e) {
			System.out.println("Error while trying to authorize an ID");
			e.printStackTrace();
		}
		
		try {
			//client.sendStartTransactionRequest(1, authorizationID, measureMode);
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
	
	/**
	 * Tests how the server behaves with a certain amount of clients
	 * 
	 * @param nrClients - specifies the number of clients to connect
	 * @param serverURL - specifies the URL of the server to test
	 * @param CPVendor - specifies the ChargePoint vendor
	 * @param CPModel - specifies the ChargePoint model
	 */
	public static void tortureTest(int nrClients, String serverURL, String CPVendor, String CPModel) {
		// 100 Clients are no problem for the server, is that intentionally?
		// TODO: Discuss what to do about this
		JSONClientSamplev0_5 [] clients = new JSONClientSamplev0_5[nrClients];
		long bootTimeResults[] = new long[nrClients];
		long authorizeTimeResults[] = new long[nrClients];
				
		for (int i = 0; i < nrClients; i++) {
			clients[i] = new JSONClientSamplev0_5();
			try {
				clients[i].connect(serverURL, ("TestPoint" + i));
				clients[i].sendBootNotification(CPVendor, CPModel, true);
				if(i < 10) clients[i].sendAuthorizeRequest(("0FFFFFF" + i), true);
				if(i >= 10 && i <20) clients[i].sendAuthorizeRequest(("1FFFFFF" + (i%10)), true);
				if(i >= 20 && i <30) clients[i].sendAuthorizeRequest(("2FFFFFF" + (i%10)), true);
				if(i >= 30 && i <40) clients[i].sendAuthorizeRequest(("3FFFFFF" + (i%10)), true);
				if(i >= 40 && i <50) clients[i].sendAuthorizeRequest(("4FFFFFF" + (i%10)), true);
				if(i >= 50 && i <60) clients[i].sendAuthorizeRequest(("5FFFFFF" + (i%10)), true);
				if(i >= 60 && i <70) clients[i].sendAuthorizeRequest(("6FFFFFF" + (i%10)), true);
				if(i >= 70 && i <80) clients[i].sendAuthorizeRequest(("7FFFFFF" + (i%10)), true);
				if(i >= 80 && i <90) clients[i].sendAuthorizeRequest(("8FFFFFF" + (i%10)), true);
				if(i >= 90 && i <100) clients[i].sendAuthorizeRequest(("9FFFFFF" + (i%10)), true);
			} catch (Exception e) {
				System.out.println("Error while torture testing");
			}
		}		
		
		try {
			//Prepare disconnect and disconnect all clients
			System.out.println("Done testing.");
			Thread.sleep(2000);
			
			for (int i = 0; i < nrClients; i++) {
				authorizeTimeResults[i] = clients[i].getNextTime();
				bootTimeResults[i] = clients[i].getNextTime();
				clients[i].disconnect();
			}
		} catch (Exception e) {
			System.out.println("Error while disconnecting");
		}
		
		//Evaluation starts here
		long sumBoot = 0;
		long minBoot = 10000;
		long maxBoot = 0;
		long sumAuthorize = 0;
		long minAuthorize = 100000;
		long maxAuthorize = 0;
		
		for (int i = 0; i < nrClients; i++) {
			sumBoot += bootTimeResults[i];
			if(minBoot >= bootTimeResults[i]) minBoot = bootTimeResults[i];
			if(maxBoot < bootTimeResults[i]) maxBoot = bootTimeResults[i];
			sumAuthorize += authorizeTimeResults[i];
			if(minAuthorize >= authorizeTimeResults[i]) minAuthorize = authorizeTimeResults[i];
			if(maxAuthorize < authorizeTimeResults[i]) maxAuthorize = authorizeTimeResults[i];
		}
		
		System.out.println("Results for boot notifications:");
		System.out.println("\tAverage: " + (sumBoot/nrClients) + "ms\n");
		System.out.println("\tMin:     " + minBoot + "ms\n");
		System.out.println("\tMax:     " + maxBoot + "ms\n");
		
		System.out.println("Results for authorization:");
		System.out.println("\tAverage: " + (sumAuthorize/nrClients) + "ms\n");
		System.out.println("\tMin:     " + minAuthorize + "ms\n");
		System.out.println("\tMax:     " + maxAuthorize + "ms\n");
		System.exit(0);
	}
    
    
}
