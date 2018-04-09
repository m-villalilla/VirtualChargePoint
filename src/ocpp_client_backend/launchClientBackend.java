package ocpp_client_backend;

public class launchClientBackend {

	public static void main(String[] args){
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
		
		System.out.println("launchClientBackend.java main(): Exit.");
	}

}
