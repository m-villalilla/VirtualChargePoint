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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ocpp_client_backend.*;

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
	//@FXML
	//private Label label2;
	//@FXML
	//private Label label1;
	//@FXML
	private Button btnOk1;
	
	//Elements in ComboBox
	ObservableList<String> list = FXCollections.observableArrayList("Getting Server Functions & Server Version", "Testing Authentification", "Testing Transaction");
		
	/**
	 * select one of 3 charging options, print output in 2nd label (right one)
	 * 
	 * @param event
	 */
	public void radioSelect(ActionEvent event) {
		//String message = "";
		//if(rb1.isSelected()) {
			//message += rb1.getText() + "\n";
		//}
		//if(rb2.isSelected()) {
			//message += rb2.getText() + "\n";
		//}
		//if(rb3.isSelected()) {
			//message += rb3.getText() + "\n";	
		//}
		//label2.setText(message);
	}

	/**
	 * fill comboBox with predefined values from an observable list
	 * 
	 * @param arg0
	 * @param arg1
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		combobox.setItems(list);
		Chargepoint_stable chargepoint = new Chargepoint_stable();
		// Default values for input fields for development
		idAuthorization.setText("0FFFFFF0");
		ipAddress.setText("test-ocpp.ddns.net:8080/steve/websocket/CentralSystemService/");
		chargePointID.setText("TestPoint00");
	}
	
	/**
	 * print selected value in 1st label (left)
	 * 
	 * @param event
	 */
	public void comboChanged(ActionEvent event){
	//	label1.setText(combobox.getValue());
	}
	
	/**
	 * regarding selected combobox value pressing start button leads to new message window 
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void start(ActionEvent event)throws Exception {
		if(isInputValid()) {
			Stage primaryStage = new Stage();
			Parent root = null;
			Scene scene;
			
			switch(combobox.getValue()) {
				case "Testing Authentification":
					root = FXMLLoader.load(getClass().getResource("Authentification.fxml"));
					break;
				case "Testing Transaction":
					root = FXMLLoader.load(getClass().getResource("TestingTransaction.fxml"));
					break;
				case "Getting Server Functions & Server Version":
					root = FXMLLoader.load(getClass().getResource("ServerFunctionVersion.fxml"));
					break;
				default:
					//Do something, even if this should never be executed
					break;
			}
			
			scene = new Scene(root,580,357);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	/**
	 * Checks if the input fields have valid inputs
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		//Input which needs to be checked always
			
		    //Regex to check IP-Input
			Pattern ip = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
			Matcher mIp = ip.matcher(ipAddress.getText());
			
			//Regex to ckeck ChargepointID
			Pattern chargePoint = Pattern.compile("[0-9A-Za-z]+");
			Matcher mChargePoint = chargePoint.matcher(chargePointID.getText());
			
			//Regex to Check idAuthotization
			Pattern id = Pattern.compile("[0-9A-F]{6,8}");
			Matcher mId = id.matcher(idAuthorization.getText());
			
			if(!(mIp.find() && mIp.group().equals(ipAddress.getText()))) {
				Alert alertIp = new Alert(AlertType.WARNING);
				alertIp.setTitle("Warning");
				alertIp.setHeaderText(null);
				alertIp.setContentText("Please enter a correct IP Address.\n\nA correct IP Address consists of 4 decimal values separated by a point.");
				alertIp.showAndWait();
				return false;
			}
			else if (!(mChargePoint.find() && mChargePoint.group().equals(chargePointID.getText()))) {
				Alert alertCharge = new Alert(AlertType.WARNING);
				alertCharge.setTitle("Warning");
				alertCharge.setHeaderText(null);
				alertCharge.setContentText("Please enter a correct ChargePointID.");
				alertCharge.showAndWait();
				return false;
			}
			else if (!(mId.find() && mId.group().equals(idAuthorization.getText()))) {
				Alert alertId = new Alert(AlertType.WARNING);
				alertId.setTitle("Warning");
				alertId.setHeaderText(null);
				alertId.setContentText("Please enter a correct authorization id.\n\nA correct authorization id is a sequence of 6 to 8 hex numbers.");
				alertId.showAndWait();
				return false;
			}
			else {
				return true;
			}
			
	}	
}
