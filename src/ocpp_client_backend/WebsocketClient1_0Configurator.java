package ocpp_client_backend;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.websocket.ClientEndpointConfig;

public class WebsocketClient1_0Configurator extends ClientEndpointConfig.Configurator {
	
    @Override
    public void beforeRequest(Map<String, List<String>> headers) {
        headers.put("Sec-WebSocket-Protocol", Arrays.asList("ocpp1.0"));
    }
}
