package ocpp_client_backend;

import java.util.Calendar;
import java.util.LinkedList;

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
public class Chargepoint_stable {
    private IClientAPI client;
    private ClientCoreProfile core;
    private LinkedList<Long> measurements = new LinkedList<>();
    private int transactionId;
    private boolean measureMode;
    private boolean stressTest;

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
    public void sendBootNotification(String CPVendor, String CPModel) throws Exception {
        long startTime = System.nanoTime();
    	Request request = core.createBootNotificationRequest(CPVendor, CPModel);
        client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, startTime));
    }

    /**
     * Sends a AuthorizeRequest to the OCPP server 
     * 
     * @param token - authorization identifier
     * @param measureMode - sets a flag to print the elapsed time or not
     * @throws Exception
     */
    public void sendAuthorizeRequest(String token) throws Exception {
    	long startTime = System.nanoTime();
    	Request request = core.createAuthorizeRequest(token);
    	client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, startTime));
    }
    
    /**
     * Sends a StartTransactionRequest to the OCPP server.
     *  
     * @param connectorId - used connector of the CP
     * @param token - authorization identifier
     * @param measureMode - sets a flag to print the elapsed time or not
     * @throws Exception
     */
    public void sendStartTransactionRequest(int connectorId, String token) throws Exception {
    	int meterStart = 0;
    	long startTime = System.nanoTime();
    	
    	Calendar timestamp = Calendar.getInstance();
		Request request = core.createStartTransactionRequest(connectorId, token, meterStart, timestamp);
		client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, startTime));
    }
    
    /**
     * Sends a StartTransactionRequest to the OCPP server.
     * 
     * @param connectorId 	- used connector of the CP
     * @param token 		- authorization identifier
     * @param meterStop		- meter value in Wh on stop
     * @param measureMode 	- sets a flag to print the elapsed time or not
     * @return 
     * @return 
     * @throws Exception
     */
    public void sendStartTransactionRequest(int connectorId, String token, int meterStart) throws Exception {
    	long startTime = System.nanoTime();
    	Calendar timestamp = Calendar.getInstance();
		Request request = core.createStartTransactionRequest(connectorId, token, meterStart, timestamp);
		client.send(request).whenComplete((s, ex) -> { 
			functionComplete(s, ex, startTime);
			setTranscationId(((StartTransactionConfirmation) s).getTransactionId());
		});
    }
    
    /**
     * Sends a StopTransactionRequest to the OCPP server.
     * 
     * @param meterStop		- meter value in Wh on stop
     * @param transactionId	- transaction identifier received from the server
     * @param measureMode	- sets a flag to print the elapsed time or not
     * @throws Exception
     */
    public void sendStopTransactionRequest(int transactionId, int meterStop) throws Exception {
    	long startTime = System.nanoTime();
    	Calendar timestamp = Calendar.getInstance();
    	Request request = core.createStopTransactionRequest(meterStop, timestamp, transactionId);
    	client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, startTime));
    }
    
    /**
     * Checks transaction procedure
     * 
     * @param authorizationID
     * @param measureMode
     * @throws Exception
     */
	public void checkTransactionSupport(String authorizationID) throws Exception {
		sendStartTransactionRequest(1, authorizationID, 0);
		Thread.sleep(1000);	
		sendStopTransactionRequest(getTransactionId(), 100);
	}
    
    /**
     * Called when a request is completed
     * 
     * @param s
     * @param ex
     * @param measureMode - flag for time measuring output
     * @param startTime - time the function started
     */
    public void functionComplete(Confirmation s, Throwable ex, long startTime) {
    	if(!stressTest) System.out.println(s);
    	if(measureMode) {
    		long timeElapsed = (System.nanoTime() - startTime)/1000000;
    		if(!stressTest) System.out.println("\tElapsed time: " + timeElapsed + "ms");
    		measurements.add(timeElapsed);
    	}
    }
    
    /**
     * Disconnects the client from the OCPP server
     */
    public void disconnect() {
        client.disconnect();
    } 
    
    /**
     * Sets the transactionId returned by sendStartTransactionRequest
     * 
     * @param transactionId
     */
    public void setTranscationId(int transactionId) {
    	this.transactionId = transactionId;
    }
    
    
    public long getNextTime() {
		return measurements.pop();
	}
    
    /**
     * 
     * @return
     */
    public int getTransactionId() {
    	return this.transactionId;
    }

	public boolean isMeasureMode() {
		return measureMode;
	}

	public void setMeasureMode(boolean measureMode) {
		this.measureMode = measureMode;
	}

	public boolean isStressTest() {
		return stressTest;
	}

	public void setStressTest(boolean stressTest) {
		this.stressTest = stressTest;
	}
}
