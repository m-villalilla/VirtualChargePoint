package ocpp_client_backend;

import ocpp_client_backend.Chargepoint;
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
		
		//String serverURL  		= config.getProperty("serverURL");
		String serverURL = "192.168.0.3:8080/steve/websocket/CentralSystemService/";
		String ChargeBoxID 		= config.getProperty("ChargeBoxID.00");				//Use ChargeBoxID.00 as example
		String CPVendor   		= config.getProperty("CPVendor");
		String CPModel 	  		= config.getProperty("CPModel");
		String authorizationID	= config.getProperty("authorizationID.00");			//Use authorizationID.00 as example
		//String authorizationID = "1234";
		
		String whatToTest = "FT"; //STRESSTEST or SINGLECLIENT or WS or VT or FT
		Chargepoint client = new Chargepoint(ChargeBoxID, CPVendor, CPModel, false, false);

		switch (whatToTest) {
			case "STRESSTEST":
				String[] cps = new String[40];
				for (int i = 0; i < 40; i++) {
					cps[i] = config.getProperty("ChargeBoxID." + (i+10));
				}
				OCPPServerStressTest.startTest(serverURL, cps, authorizationID);
				break;
			case "SINGLECLIENT":
				try {
					client.connect(serverURL);
					System.out.println("Client connected.");
					Thread.sleep(2000);
					client.sendBootNotification();
					Thread.sleep(2000);
					client.sendAuthorizeRequest(authorizationID);
					Thread.sleep(2000);
					client.checkTransactionSupport(authorizationID);
					Thread.sleep(2000);	// Give the server time to respond to ongoing requests
					client.disconnect();
					System.out.println("Client disconnected.");
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			case "WS":
		        try {
		            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://" + serverURL + "TestPoint00"));
		            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
		                public void handleMessage(String message) {
		                    System.out.println(message);
		                }
		            });
		            clientEndPoint.sendMessage("{'message': null}");

		            // Wait 5 seconds for messages from websocket
		            Thread.sleep(5000);
		        } catch (InterruptedException ex) {
		            System.out.println("InterruptedException exception: " + ex.getMessage());
		        } catch (URISyntaxException ex) {
		            System.out.println("URISyntaxException exception: " + ex.getMessage());
		        }
				break;
			case "VT":
				client.testAllVersions(serverURL);
				break;
			case "FT":
				try {
					client.connect(serverURL);
					Thread.sleep(2000);
					client.testServerFeatures(authorizationID);
					Thread.sleep(2000);
					client.disconnect();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("Please specify what you want to do.");
				break;
			}
	}
	
	public static Properties getConfig() {
		return config;
	}
    
}
