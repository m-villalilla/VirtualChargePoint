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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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
	public ComboBox<String> combobox;
	@FXML
	private RadioButton rb1;
	@FXML
	private RadioButton rb2;
	@FXML
	private RadioButton rb3;
	@FXML
	private Button btnStart;
	
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
		combobox.setItems(list);

		// Default values for input fields for development
		idAuthorization.setText("0FFFFFF0");
		ipAddress.setText("test-ocpp.ddns.net:8080/steve/websocket/CentralSystemService/");
		chargePointID.setText("TestPoint00");
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
		Scene scene;
		
		if(!isInputValid()) return;
		btnStart.setDisable(true);
		
		switch(combobox.getValue()) {
			case "Testing Authentification":
				//root = FXMLLoader.load(getClass().getResource("Authentification.fxml"));
				startTest(null, "auth");
				return;	//Only now needed, since we have no running window yet
	    	case "Testing Transaction":
				root = FXMLLoader.load(getClass().getResource("TestingTransactionRunning.fxml"));
				startTest(stage, "trans");
				break;
			case "Getting Server Functions":
				root = FXMLLoader.load(getClass().getResource("ServerFunctionVersion.fxml"));
				startTest(null, "func");
				break;
			case "Getting Server Version":
				root = FXMLLoader.load(getClass().getResource("ServerVersion.fxml"));
				startTest(null, "func");
				break;
			default:
				break;
		}
		
		scene = new Scene(root,580,357);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		stage.setScene(scene);
		Image icon = new Image("file:icons/Tankladesaeule.png");
		stage.getIcons().add(icon);
		stage.show();
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
								chargepoint.addObserver(new TestingTransactionWrapper());
								//chargepoint.checkTransactionSupport(idAuthorization.getText());	//Insert call here when done
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
		Pattern ip = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
		Pattern url = Pattern.compile("[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)");
		Pattern chargePoint = Pattern.compile("[0-9A-Za-z]+");
		Pattern id = Pattern.compile("[0-9A-F]{6,8}");
		
		Matcher mIp = ip.matcher(ipAddress.getText());
		Matcher mUrl = url.matcher(ipAddress.getText());
		Matcher mChargePoint = chargePoint.matcher(chargePointID.getText());
		Matcher mId = id.matcher(idAuthorization.getText());
		
		Alert inputError = new Alert(AlertType.ERROR);
		
		if( (!(mIp.find() && mIp.group().equals(ipAddress.getText())))&& (!(mUrl.find() && mUrl.group().equals(ipAddress.getText()))) ) {
			inputError.setTitle("Incorrect IP Address or URL");
			inputError.setContentText("Please enter a correct IP Address or URL!\n\nA correct IP Address consists of 4 decimal values separated by a point.");
		}
		else if (!(mChargePoint.find() && mChargePoint.group().equals(chargePointID.getText()))) {
			inputError.setTitle("Incorrect Charge Point ID");
			inputError.setContentText("Please enter a correct Charge Point ID!");
		}
		else if (!(mId.find() && mId.group().equals(idAuthorization.getText()))) {
			inputError.setTitle("Incorrect Authorization ID");
			inputError.setContentText("Please enter a correct Authorization ID!\n\nA correct Authorization ID is a sequence of 6 to 8 hex numbers.");
		}
		else if (combobox.getValue() == null) {
			inputError.setTitle("No test selected");
			inputError.setContentText("Please select a test!");
		}
		else {
			chargepoint.setChargeBoxId(chargePointID.getText()) ;
			return true;
		}
		
		Stage stage = (Stage) inputError.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:icons/Tankladesaeule.png"));
		
		DialogPane dialogPane = inputError.getDialogPane();
		dialogPane.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		dialogPane.getStyleClass().add("application");
		
		inputError.setHeaderText("ERROR");
		inputError.setResizable(false);
		inputError.getDialogPane().setPrefSize(480, 320);
		inputError.showAndWait();
		
		return false;
	}	
}
