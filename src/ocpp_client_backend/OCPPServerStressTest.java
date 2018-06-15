package ocpp_client_backend;

public class OCPPServerStressTest {
	private static Chargepoint [] clients = null;
	private static long bootTimeResults[] = null; 
	private static long authorizeTimeResults[] = null;
	
	/**
	 * Tests how the server behaves with a certain amount of clients in terms of responds time
	 * 
	 * @param serverURL - specifies the URL of the server to test
	 * @param cpIDs - specifies the number of clients to connect
	 */
	public static void startTest(String serverURL, String[] cpIDs, String authID) {
		clients = new Chargepoint[cpIDs.length];
		bootTimeResults = new long[cpIDs.length];
		authorizeTimeResults = new long[cpIDs.length];
		
		System.out.println("Test is running, please wait...");
		
		//Split into two for loops, so that the object preparation doesn't give the server time to answer faster
		for (int i = 0; i < cpIDs.length; i++) {
			clients[i] = new Chargepoint();
			clients[i].setMeasureMode(true);
			clients[i].setStressTest(true);
			clients[i].setChargeBoxId(cpIDs[i]);
		}
		for (int i = 0; i < cpIDs.length; i++) {
			try {
				clients[i].connect(serverURL);
				clients[i].sendBootNotification();
				clients[i].sendAuthorizeRequest(authID);
			} catch (Exception e) {
				System.out.println("Error while stress testing");
			}
		}
		
		try {
			//Prepare disconnect and disconnect all clients
			Thread.sleep(2000);
			System.out.println("Done testing, please wait...");
						
			for (int i = 0; i < cpIDs.length; i++) {
				authorizeTimeResults[i] = clients[i].getNextTime();
				bootTimeResults[i] = clients[i].getNextTime();
				clients[i].disconnect();
			}
		} catch (Exception e) {
			System.out.println("Error while disconnecting");
		}
		
		evaluate(cpIDs.length);
	}
	
	/**
	 * Internal function to evaluate the resulted times
	 * 
	 * @param nrClients Defines how many clients are used
	 */
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
	
	/**
	 * Returns the array with the boot times. 
	 * Can be used to perform further analysis
	 * 
	 * @return Array with the results of boot time
	 */
	public static long[] getBootTimeResults() {
		return bootTimeResults;
	}
	
	/**
	 * Returns the array with the authorization times. 
	 * Can be used to perform further analysis
	 * 
	 * @return Array with the results of authorization time
	 */
	public static long[] getAuthorizeTimeResults() {
		return authorizeTimeResults;
	}
}
