package ocpp_client_backend;

import java.util.Calendar;

import eu.chargetime.ocpp.IClientAPI;
import eu.chargetime.ocpp.JSONClient;
import eu.chargetime.ocpp.feature.profile.ClientCoreEventHandler;
import eu.chargetime.ocpp.feature.profile.ClientCoreProfile;
import eu.chargetime.ocpp.model.Confirmation;
import eu.chargetime.ocpp.model.Request;
import eu.chargetime.ocpp.model.core.*;

/*
 * ChargeTime.eu - Java-OCA-OCPP
 * Copyright (C) 2015-2016 Thomas Volden <tv@chargetime.eu>
 *
 * MIT License
 *
 * Copyright (C) 2016-2018 Thomas Volden
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
public class JSONClientSamplev0_5 {
    private IClientAPI client;
    private ClientCoreProfile core;

    /**
     * Called to connect to a OCPP server
     * 
     * @param serverURL - specifies the URL of the OCPP server
     * @param clientName - name of the charge point
     * @throws Exception
     */
    public void connect(String serverURL, String clientName) throws Exception {

        // The core profile is mandatory
        core = new ClientCoreProfile(new ClientCoreEventHandler() {
            @Override
            public ChangeAvailabilityConfirmation handleChangeAvailabilityRequest(ChangeAvailabilityRequest request) {

                System.out.println(request);
                // ... handle event

                return new ChangeAvailabilityConfirmation(AvailabilityStatus.Accepted);
            }

            @Override
            public GetConfigurationConfirmation handleGetConfigurationRequest(GetConfigurationRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ChangeConfigurationConfirmation handleChangeConfigurationRequest(ChangeConfigurationRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ClearCacheConfirmation handleClearCacheRequest(ClearCacheRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public DataTransferConfirmation handleDataTransferRequest(DataTransferRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public RemoteStartTransactionConfirmation handleRemoteStartTransactionRequest(RemoteStartTransactionRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public RemoteStopTransactionConfirmation handleRemoteStopTransactionRequest(RemoteStopTransactionRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public ResetConfirmation handleResetRequest(ResetRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }

            @Override
            public UnlockConnectorConfirmation handleUnlockConnectorRequest(UnlockConnectorRequest request) {

                System.out.println(request);
                // ... handle event

                return null; // returning null means unsupported feature
            }
        });
        
        client = new JSONClient(core);
        client.connect("ws://" + serverURL + clientName, null);
    }

    /**
     * Sends a BootNotification to the OCPP server
     * 
     * @param CPVendor
     * @param CPModel
     * @param measureMode - sets a flag to print the elapsed time or not
     * @throws Exception
     */
    public void sendBootNotification(String CPVendor, String CPModel, boolean measureMode) throws Exception {
        long startTime = System.nanoTime();
    	Request request = core.createBootNotificationRequest(CPVendor, CPModel);
        client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, measureMode, startTime));
    }

    /**
     * Sends a AuthorizeRequest to the OCPP server 
     * 
     * @param token - authorization identifier
     * @param measureMode - sets a flag to print the elapsed time or not
     * @throws Exception
     */
    public void sendAuthorizeRequest(String token, boolean measureMode) throws Exception {
    	long startTime = System.nanoTime();
    	Request request = core.createAuthorizeRequest(token);
    	client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, measureMode, startTime));
    }
    
    /**
     * Sends a StartTransactionRequest to the OCPP server.
     *  
     * @param connectorId - used connector of the CP
     * @param token - authorization identifier
     * @param measureMode - sets a flag to print the elapsed time or not
     * @throws Exception
     */
    public void sendStartTransactionRequest(int connectorId, String token, boolean measureMode) throws Exception {
    	int meterStart = 0;
    	long startTime = System.nanoTime();
    	
    	Calendar timestamp = Calendar.getInstance();
		Request request = core.createStartTransactionRequest(connectorId, token, meterStart, timestamp);
		client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, measureMode, startTime));
    }
    
    /**
     * Disconnects the client from the OCPP server
     */
    public void disconnect() {
        client.disconnect();
    }
    
    /**
     * Called when a request is completed
     * 
     * @param s
     * @param ex
     * @param measureMode - flag for time measuring output
     * @param startTime - time the function started
     */
    public void functionComplete(Confirmation s, Throwable ex, boolean measureMode, long startTime) {
    	System.out.println(s);
    	if(measureMode) System.out.println("\tElapsed time: " + ((System.nanoTime() - startTime)/1000000) + "ms");
    }
}
