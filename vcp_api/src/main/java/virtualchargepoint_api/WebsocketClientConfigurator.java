package virtualchargepoint_api;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;

public class WebsocketClientConfigurator extends ClientEndpointConfig.Configurator {
	static String version;
	
	public static void setVersion(String version2) {
		version = version2;
	}
	public static String getVersion() {
		return version;
	}
    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("Sec-WebSocket-Protocol", Arrays.asList(version));
    }
}
