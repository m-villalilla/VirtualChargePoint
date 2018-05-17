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
    private int transactionId;
    private LinkedList<Long> measurements;
    private boolean measureMode;
    private boolean stressTest;
    private String vendor;
    private String model;
    private String chargeBoxId;

	/**
	 * Default constructor for Chargepoint_stable
	 */
    public Chargepoint_stable() {
		this.measurements = new LinkedList<>();
		this.measureMode = false;
		this.stressTest = false;
		this.vendor = "DefaultVendor";
		this.model = "DefaultModel";
		this.chargeBoxId = "DefaultId";
	}
	
	/**
	 * Parameterized constructor for Chargepoint_stable
	 * 
	 * @param chargeBoxId 
	 * @param vendor Sets the vendor of the chargepoint
	 * @param model Sets the model of the chargepoint
	 * @param measureMode Sets if the time shall be measured
	 * @param stressTest Sets if the chargepoint is used in the stress test
	 */
    public Chargepoint_stable(String chargeBoxId, String vendor, String model, boolean measureMode, boolean stressTest) {
		this.measurements = new LinkedList<>();
		this.measureMode = measureMode;
		this.stressTest = stressTest;
		this.vendor = vendor;
		this.model = model;
		this.chargeBoxId = chargeBoxId;
	}
    
    /**
     * Called to connect to a OCPP server
     * 
     * @param serverURL - specifies the URL of the OCPP server
     * @throws Exception
     */
    public void connect(String serverURL) throws Exception {

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
        client.connect("ws://" + serverURL + chargeBoxId, null);
    }

    /**
     * Sends a BootNotification to the OCPP server
     * 
     * @throws Exception
     */
    public void sendBootNotification() throws Exception {
        long startTime = System.nanoTime();
    	Request request = core.createBootNotificationRequest(vendor, model);
        client.send(request).whenComplete((s, ex) -> functionComplete(s, ex, startTime));
    }

    /**
     * Sends a AuthorizeRequest to the OCPP server 
     * 
     * @param token - authorization identifier
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
     * @param transactionId	- transaction identifier received from the server
     * @param meterStop		- meter value in Wh on stop
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
     * @param authorizationID Sets the authorization id used in the transaction
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
     * @param s Returned string from the OCPP server
     * @param ex Returned exception from the OCPP server
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
    
    /**
     * Used to get the next measured time from the linked list
     * 
     * @return last time from the linked list
     */
    public long getNextTime() {
		return measurements.pop();
	}
    
    /**
     * Used to get the transaction id of the client
     * 
     * @return
     */
    public int getTransactionId() {
    	return this.transactionId;
    }

	/**
	 * Used to get the current value of measureMode of the client
	 * 
	 * @return value of variable measureMode
	 */
    public boolean isMeasureMode() {
		return measureMode;
	}

	/**
	 * Used to set the measureMode variable of the client
	 * 
	 * @param measureMode
	 */
    public void setMeasureMode(boolean measureMode) {
		this.measureMode = measureMode;
	}

	/**
	 * Used to get the current value of stressTest of the client
	 * 
	 * @return value of variable stressTest
	 */
    public boolean isStressTest() {
		return stressTest;
	}

	/**
	 * Used to set the stressTest variable of the client
	 * 
	 * @param stressTest
	 */
    public void setStressTest(boolean stressTest) {
		this.stressTest = stressTest;
	}

	/**
	 * Used to get the vendor of the client
	 * 
	 * @return value of the variable vendor
	 */
    public String getVendor() {
		return vendor;
	}

	/**
	 * Used to set the vendor of the client
	 * 
	 * @param vendor
	 */
    public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	/**
	 * Used to get the model of the client
	 * 
	 * @return value of the variable model
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Used to set the model of the client
	 * 
	 * @param model
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * Used to get the chargeBoxId of the client
	 * 
	 * @return value of the variable chargeBoxId
	 */
	public String getChargeBoxId() {
		return chargeBoxId;
	}

	/**
	 * Used to set the chargeBoxid of the client
	 * 
	 * @param chargeBoxId
	 */
	public void setChargeBoxId(String chargeBoxId) {
		this.chargeBoxId = chargeBoxId;
	}
}
