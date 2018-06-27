package virtualchargepoint_gui;

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
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;
import virtualchargepoint_api.Chargepoint;

public class MainController implements Initializable {
	@FXML private TextField ipAddress;
	@FXML private TextField chargePointID;
	@FXML private TextField idAuthorization;
	@FXML private TextField chargePointVendor;
	@FXML private TextField chargePointModel;
	@FXML private Button btnStart;
	@FXML private Button btnSettings;
	@FXML private ComboBox<String> combobox;
	
	private ObservableList<String> list = FXCollections.observableArrayList("Getting Server Functions", "Getting Server Version", "Testing Authentification", "Testing Transaction");
	static Chargepoint chargepoint = new Chargepoint();
	
	/**
	 * fill comboBox with predefined values from an observable list
	 * and the advanced settings window with default values
	 * 
	 * @param arg0 - {@inheritDoc}
	 * @param arg1 - {@inheritDoc}
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(arg0.getPath().contains("Main")) {
			//If this function is called because of the Main window
			combobox.setItems(list);
		} else if(arg0.getPath().contains("Advanced")){
			//If this function is called because of the advanced settings window 
			chargePointVendor.setPromptText("e.g. Siemens");
			chargePointVendor.setFocusTraversable(false);
			chargePointModel.setPromptText("e.g. CT4000");
			chargePointModel.setFocusTraversable(false);
		}
	}
	
	/**
	 * Event Handler for Button Advanced Settings
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void settings(ActionEvent event) throws Exception {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		Parent root = FXMLLoader.load(getClass().getResource("AdvancedSettings.fxml"));
		Image icon = new Image("file:src/main/resources/ChargePointIcon.png");
		Stage settingStage = new Stage();
		Scene scene = new Scene(root,580,370);
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		settingStage.setScene(scene);
		settingStage.getIcons().add(icon);
		settingStage.show();
		
		//position at center of the screen
	    settingStage.setX((primScreenBounds.getWidth() - settingStage.getWidth()) / 2); 
	    settingStage.setY((primScreenBounds.getHeight() - settingStage.getHeight()) / 2);
	}
	
	/**
	 * Event handler for the save button of the advanced settings window
	 * 
	 * @param event - {@inheritDoc}
	 * @throws Exception - {@inheritDoc}
	 */
	public void saveAdvanced(ActionEvent event) throws Exception {
	    if(chargePointModel.getText() != "") chargepoint.setModel(chargePointModel.getText());
	    if(chargePointVendor.getText() != "") chargepoint.setVendor(chargePointVendor.getText());
	}
	
	/**
	 * regarding selected combobox value pressing start button leads to new message window 
	 * 
	 * @param event - {@inheritDoc}
	 * @throws Exception - {@inheritDoc}
	 */
	public void start(ActionEvent event) throws IOException {
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
		Image icon = new Image("file:src/main/resources/ChargePointIcon.png");
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
				fxmll.getNamespace().put("transImgUrl", "file:src/main/resources/TrafficlightYellow.png");
				startTest(stage, "trans");
				break;
			case "Getting Server Functions":
				fxmll = new FXMLLoader(getClass().getResource("TestingFunctions.fxml"));
				startTest(stage, "func");
				break;
			case "Getting Server Version":	
				fxmll = new FXMLLoader(getClass().getResource("TestingVersion.fxml"));
				startTest(stage, "version");
				break;
		default:
				break;
		}
		
		root = fxmll.load();
		scene = new Scene(root,580,357);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		stage.getIcons().add(icon);
		stage.show();
		
		//Message Windows position at center of screen
	    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2); 
	    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
	}
	

	/**
	 * Creates thread to start the test and starts it
	 * 
	 * @param stage - Previously opened stage, so you can close it if needed
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
								chargepoint.addObserver(new TestingFeatureWrapper());
								chargepoint.testServerFeatures(idAuthorization.getText());
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
		Pattern ip = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b([-a-zA-Z0-9@:%_\\\\+.~#?&//=]*)");	
		Pattern ipPort = Pattern.compile("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?):\\d{1,5}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		Pattern url = Pattern.compile("[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		Pattern chargePoint = Pattern.compile("[0-9A-Za-z]+");
		Pattern id = Pattern.compile("[0-9A-F]{6,8}");
		Matcher mIp = ip.matcher(ipAddress.getText());
		Matcher mIPPort = ipPort.matcher(ipAddress.getText());
		Matcher mUrl = url.matcher(ipAddress.getText());
		Matcher mChargePoint = chargePoint.matcher(chargePointID.getText());
		Matcher mId = id.matcher(idAuthorization.getText());
		Alert inputError = new Alert(AlertType.ERROR);
		Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
		DialogPane dialogPane = null;
		Stage stage = null;
		
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
			return true;
		}
		  
		stage = (Stage) inputError.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:src/main/resources/ChargePointIcon.png"));
		dialogPane = inputError.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		dialogPane.getStyleClass().add("application");
		inputError.setHeaderText("ERROR");
		inputError.setResizable(false);
		inputError.getDialogPane().setPrefSize(480, 280);
		
		//Alert Window position center of screen - not exactly center
	    inputError.setX((int)screenSize.getWidth()/2 - (int)screenSize.getWidth()/8);
	    inputError.setY((int)screenSize.getHeight()/2 - (int)screenSize.getHeight()/8);
		inputError.showAndWait();
		return false;
	}	
}
