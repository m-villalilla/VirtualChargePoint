package ocpp_client_backend;

public class OCPPServerStressTest {
	private static Chargepoint_stable [] clients;
	private static long bootTimeResults[]; 
	private static long authorizeTimeResults[];
	
	/**
	 * Tests how the server behaves with a certain amount of clients
	 * 
	 * @param nrClients - specifies the number of clients to connect
	 * @param serverURL - specifies the URL of the server to test
	 * @param CPVendor - specifies the ChargePoint vendor
	 * @param CPModel - specifies the ChargePoint model
	 */
	public static void startTest(int nrClients, String serverURL, String CPVendor, String CPModel) {
		clients = new Chargepoint_stable[nrClients];
		bootTimeResults = new long[nrClients];
		authorizeTimeResults = new long[nrClients];
		
		System.out.println("Test is running, please wait...");
		for (int i = 0; i < nrClients; i++) {
			clients[i] = new Chargepoint_stable();
			clients[i].setMeasureMode(true);
			clients[i].setStressTest(true);
			try {
				clients[i].connect(serverURL, ("TestPoint" + i));
				clients[i].sendBootNotification(CPVendor, CPModel);
				if(i < 10) clients[i].sendAuthorizeRequest(("0FFFFFF" + i));
				if(i >= 10 && i <20) clients[i].sendAuthorizeRequest(("1FFFFFF" + (i%10)));
				if(i >= 20 && i <30) clients[i].sendAuthorizeRequest(("2FFFFFF" + (i%10)));
				if(i >= 30 && i <40) clients[i].sendAuthorizeRequest(("3FFFFFF" + (i%10)));
				if(i >= 40 && i <50) clients[i].sendAuthorizeRequest(("4FFFFFF" + (i%10)));
				if(i >= 50 && i <60) clients[i].sendAuthorizeRequest(("5FFFFFF" + (i%10)));
				if(i >= 60 && i <70) clients[i].sendAuthorizeRequest(("6FFFFFF" + (i%10)));
				if(i >= 70 && i <80) clients[i].sendAuthorizeRequest(("7FFFFFF" + (i%10)));
				if(i >= 80 && i <90) clients[i].sendAuthorizeRequest(("8FFFFFF" + (i%10)));
				if(i >= 90 && i <100) clients[i].sendAuthorizeRequest(("9FFFFFF" + (i%10)));
			} catch (Exception e) {
				System.out.println("Error while torture testing");
			}
		}		
		
		try {
			//Prepare disconnect and disconnect all clients
			Thread.sleep(2000);
			System.out.println("Done testing.");
						
			for (int i = 0; i < nrClients; i++) {
				authorizeTimeResults[i] = clients[i].getNextTime();
				bootTimeResults[i] = clients[i].getNextTime();
				clients[i].disconnect();
			}
		} catch (Exception e) {
			System.out.println("Error while disconnecting");
		}
		
		evaluate(nrClients);
	}
	
	public static long[] getBootTimeResults() {
		return bootTimeResults;
	}
	
	public static long[] getAuthorizeTimeResults() {
		return authorizeTimeResults;
	}
	
	private static void evaluate(int nrClients) {
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
		
		System.out.println("\nResults for boot notifications:");
		System.out.println("\tMin:     " + minBoot + "ms");
		System.out.println("\tAverage: " + (sumBoot/nrClients) + "ms");
		System.out.println("\tMax:     " + maxBoot + "ms");
		
		System.out.println("Results for authorization:");
		System.out.println("\tMin:     " + minAuthorize + "ms");
		System.out.println("\tAverage: " + (sumAuthorize/nrClients) + "ms");
		System.out.println("\tMax:     " + maxAuthorize + "ms");
	}
}
