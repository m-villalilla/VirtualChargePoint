package ocpp_server;

import ocpp_server.JSONServerSample;

public class launchServer{

	public static void main(String[] args) {
		System.out.println("DEBUG: launchServer.java main(): Called.");
		
		JSONServerSample s = new JSONServerSample();
		try {
			s.started();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("DEBUG: launchServer.java main(): Return.");
	}
}
