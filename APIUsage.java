import virtualchargepoint_api.Chargepoint;

public class APIUsage {	
	public static void main(String[] args) {		
		String serverURL 		= "your.url";
		String ChargeBoxID 		= "YourChargeBoxId";				//Use ChargeBoxID.00 as example
		String CPVendor   		= "Your vendor";
		String CPModel 	  		= "Your model";
		String authorizationID	= "Your authorization id";
		Chargepoint client = new Chargepoint(ChargeBoxID, CPVendor, CPModel, false, false);

		try {
			client.connect(serverURL);
			client.sendBootNotification();
			client.sendAuthorizeRequest(authorizationID);
			client.checkTransactionSupport(authorizationID);
			
			Thread.sleep(2000);	// Give the server time to respond to ongoing requests
			client.disconnect();
			System.out.println("Client disconnected.");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		
	}
}
