package ocpp_client_backend;

public class launchClientBackend {

	public static void main(String[] args){
		System.out.println("DEBUG: launchClientBackend.java main(): Called.");
		
		JSONClientSample s = new JSONClientSample();
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
		
		System.out.println("DEBUG: launchClientBackend.java main(): Exit.");
	}

}
