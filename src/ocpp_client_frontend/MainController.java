package ocpp_client_frontend;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javafx.scene.image.Image;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
	
	//Elements in ComboBox
	ObservableList<String> list = FXCollections.observableArrayList("Getting Server Functions & Server Version", "Testing Authentification", "Testing Transaction");
	
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
	public void start(ActionEvent event) throws Exception {
		if(isInputValid()) {
			Stage primaryStage = new Stage();
			Parent root = null;
			Scene scene;
			
			//test variable boolean - transaction successful running or failed
			//only for development
			int transactionSuccessful = 1;
			int transactionFailed = 2;
			int transactionRunning = 3;
			int transaction = transactionRunning;
			
			switch(combobox.getValue()) {
				case "Testing Authentification":
					root = FXMLLoader.load(getClass().getResource("Authentification.fxml"));
					break;
				case "Testing Transaction":
					//testing - transaction successful, still running, failed
					if (transaction == transactionSuccessful ) {
						root = FXMLLoader.load(getClass().getResource("TestingTransaction.fxml"));
					}
					else if (transaction == transactionFailed) {
						root = FXMLLoader.load(getClass().getResource("TestingTransactionFailed.fxml"));
					}
					else if (transaction == transactionRunning) {
						root = FXMLLoader.load(getClass().getResource("TestingTransactionRunning.fxml"));
					}
					break;
				case "Getting Server Functions & Server Version":
					root = FXMLLoader.load(getClass().getResource("ServerFunctionVersion.fxml"));
					break;
				default:
					break;
			}
			
			scene = new Scene(root,580,357);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			Image icon = new Image("file:icons/iconMini.png");
			primaryStage.getIcons().add(icon);
			primaryStage.show();
		}
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
			return true;
		}
		
		Stage stage = (Stage) inputError.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image("file:icons/iconMini.png"));
		
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
