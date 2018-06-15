package ocpp_client_frontend;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import ocpp_client_backend.Chargepoint;

public class MainController implements Initializable {
	@FXML
	private TextField ipAddress;
	@FXML
	private TextField chargePointID;
	@FXML
	private TextField idAuthorization;
	@FXML
	private TextField chargePointVendor;
	@FXML
	private TextField chargePointModel;
	@FXML
	public ComboBox<String> combobox;
	@FXML
	private RadioButton rb1;
	@FXML
	private RadioButton rb2;
	@FXML
	private RadioButton rb3;
	@FXML
	private Button btnStart;
	@FXML
	private Button btnSettings;
	
	
	//Elements in ComboBox
	ObservableList<String> list = FXCollections.observableArrayList("Getting Server Functions", "Getting Server Version", "Testing Authentification", "Testing Transaction");
	static Chargepoint chargepoint = new Chargepoint();
	
	/**
	 * fill comboBox with predefined values from an observable list
	 * 
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(arg0.getPath().contains("Main")) {
			combobox.setItems(list);

			// Default values for input fields for development
			idAuthorization.setText("0FFFFFF0");
			ipAddress.setText("test-ocpp.ddns.net:8080/steve/websocket/CentralSystemService/");
			chargePointID.setText("TestPoint00");
		} else if(arg0.getPath().contains("Advanced")){
			chargePointVendor.setPromptText("e.g. Siemens");
			chargePointVendor.setFocusTraversable(false);
			chargePointModel.setPromptText("e.g. CT4000");
			chargePointModel.setFocusTraversable(false);
		}
	}
	
	//Event Handler for Button Advanced Settings
	public void settings(ActionEvent event) throws Exception {
		Stage settingStage = new Stage();
		Parent root = FXMLLoader.load(getClass().getResource("AdvancedSettings.fxml"));
		Scene scene = new Scene(root,580,370);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		settingStage.setScene(scene);
		Image icon = new Image("file:icons/ChargePointIcon.png");
		settingStage.getIcons().add(icon);
		
		//position at center of the screen
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
	    settingStage.setX((primScreenBounds.getWidth() - settingStage.getWidth()) / 2); 
	    settingStage.setY((primScreenBounds.getHeight() - settingStage.getHeight()) / 2);
	    
	    settingStage.show();
	}
	
	public void saveAdvanced(ActionEvent event) throws Exception {
	    if(chargePointModel.getText() != "") chargepoint.setModel(chargePointModel.getText());
	    if(chargePointVendor.getText() != "") chargepoint.setVendor(chargePointVendor.getText());
	}
	
	/**
	 * regarding selected combobox value pressing start button leads to new message window 
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void start(ActionEvent event) throws IOException {
		Stage stage = new Stage();
		Parent root = null;
		Scene scene = null;
		FXMLLoader fxmll = null;
		
		if(!isInputValid()) return;
		btnStart.setDisable(true);
		
		chargepoint.setChargeBoxId(chargePointID.getText());
		
		switch(combobox.getValue()) {
			case "Testing Authentification":
				fxmll = new FXMLLoader(getClass().getResource("TestingAuthentification.fxml"));
				fxmll.getNamespace().put("authLabelText", "Test is running...");
				startTest(stage, "auth");
				break;
	    	case "Testing Transaction":
				fxmll = new FXMLLoader(getClass().getResource("TestingTransaction.fxml"));
				fxmll.getNamespace().put("transLabelText", "Test is running...");
				fxmll.getNamespace().put("transImgUrl", "file:icons/TrafficlightYellow.png");
				//startTest(stage, "trans");
				break;
			case "Getting Server Functions":
				fxmll = new FXMLLoader(getClass().getResource("ServerFunction.fxml"));
				startTest(null, "func");
				break;
			case "Getting Server Version":				
				startTest(null, "version");
				return; //replace with break as soon as we have a "running" window for version check
		default:
				break;
		}
		
		root = fxmll.load();
		scene = new Scene(root,580,357);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		Image icon = new Image("file:icons/ChargePointIcon.png");
		stage.getIcons().add(icon);
		stage.show();
		//Message Windows position at center of screen
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
	    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2); 
	    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
	    
	}
	

	/**
	 * Creates thread to start the test and starts it
	 * 
	 * @param stage Previously opened stage, so you can close it if needed
	 */
	private void startTest(Stage stage, String test) {
		final Thread t = new Thread(new Runnable() {
	        @Override
	        public void run() {
	        	Platform.runLater(new Runnable() {
					@Override
					public void run() {
						chargepoint.deleteObservers();
						chargepoint.connect(ipAddress.getText());
						switch(test) {
							case "auth":
								chargepoint.addObserver(new TestingAuthenticationWrapper());
								chargepoint.sendAuthorizeRequest(idAuthorization.getText());
								break;
							case "trans":
								chargepoint.addObserver(new TestingTransactionWrapper());
								chargepoint.checkTransactionSupport(idAuthorization.getText());
								break;
							case "func":
								//chargepoint.addObserver(new TestingTransactionWrapper());
								//chargepoint.checkTransactionSupport(idAuthorization.getText());	//Insert call here when done
								break;
							case "version":
								chargepoint.addObserver(new TestingVersionsWrapper());
								chargepoint.testAllVersions(ipAddress.getText());
								break;
							default:
								break;
						}
						
						if(stage != null) stage.close();
						btnStart.setDisable(false);
					}
	        	});
	        }
	    });
		t.start();
	}
		
	/**
	 * Checks if the input fields have valid inputs
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		//checking IP:
		Pattern ip = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b([-a-zA-Z0-9@:%_\\\\+.~#?&//=]*)");
		

		//checking IP:Port:
		Pattern ipPort = Pattern.compile("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):\\d{1,5}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		
		//cheking URL:
		Pattern url = Pattern.compile("[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		
		
		
		Pattern chargePoint = Pattern.compile("[0-9A-Za-z]+");
		Pattern id = Pattern.compile("[0-9A-F]{6,8}");
		
		Matcher mIp = ip.matcher(ipAddress.getText());
		Matcher mIPPort = ipPort.matcher(ipAddress.getText());
		Matcher mUrl = url.matcher(ipAddress.getText());
		
		Matcher mChargePoint = chargePoint.matcher(chargePointID.getText());
		Matcher mId = id.matcher(idAuthorization.getText());
		
		Alert inputError = new Alert(AlertType.ERROR);
		
		if((!(mUrl.find() && mUrl.group().equals(ipAddress.getText()))) && (!(mIp.find() && mIp.group().equals(ipAddress.getText())))&& (!(mIPPort.find() && mIPPort.group().equals(ipAddress.getText()))) ) {
			inputError.setTitle("Incorrect IP Address or URL");
			inputError.setContentText("\nPlease enter a correct IP Address or URL!");
		}
		else if (!(mChargePoint.find() && mChargePoint.group().equals(chargePointID.getText()))) {
			inputError.setTitle("Incorrect Charge Point ID");
			inputError.setContentText("\nPlease enter a correct Charge Point ID!");
		}
		else if (!(mId.find() && mId.group().equals(idAuthorization.getText()))) {
			inputError.setTitle("Incorrect Authorization ID");
			inputError.setContentText("\nPlease enter a correct Authorization ID!");
		}
		else if (combobox.getValue() == null) {
			inputError.setTitle("No test selected");
			inputError.setContentText("\nPlease select a test!");
		}
		else {
			chargepoint.setChargeBoxId(chargePointID.getText()) ;
			return true;
		}
		  
		Stage stage = (Stage) inputError.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:icons/ChargePointIcon.png"));
		
		DialogPane dialogPane = inputError.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		dialogPane.getStyleClass().add("application");
		
		inputError.setHeaderText("ERROR");
		inputError.setResizable(false);
		inputError.getDialogPane().setPrefSize(480, 280);
		
		//Alert Window position center of screen - not exactly center
		Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
	    int centerX = (int)screenSize.getWidth()/2 - (int)screenSize.getWidth()/8;
	    int centerY = (int)screenSize.getHeight()/2 - (int)screenSize.getHeight()/8;
	    inputError.setX(centerX);
	    inputError.setY(centerY);
	    
		
		inputError.showAndWait();
			
		return false;
	}	
}
