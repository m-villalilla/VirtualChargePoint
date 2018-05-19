package ocpp_client_frontend;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
	@FXML
	private Label label2;
	@FXML
	private Label label1;
	@FXML
	private Button btnOk1;
	
	//Elements in ComboBox
	ObservableList<String> list = FXCollections.observableArrayList("Getting Server functions & Server version", "Testing Authentification", "Testing Transaction");
		
	/**
	 * select one of 3 charging options, print output in 2nd label (right one)
	 * 
	 * @param event
	 */
	public void radioSelect(ActionEvent event) {
		String message = "";
		if(rb1.isSelected()) {
			message += rb1.getText() + "\n";
		}
		if(rb2.isSelected()) {
			message += rb2.getText() + "\n";
		}
		if(rb3.isSelected()) {
			message += rb3.getText() + "\n";	
		}
		label2.setText(message);
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
		label1.setText(combobox.getValue());
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
				case "Getting Server functions & Server version":
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
			//Replace with Regex to check IP-Input
			if(ipAddress.getText().equals("")) return false;
			//Replace with Regex to ckeck ChargepointID
			if(chargePointID.getText().equals("")) return false;
		//Input which only needs to be checked depending on the function
			//Replace with Regex to Check idAuthotization
			if(idAuthorization.getText().equals("")) return false;
		
		//Everything is fine, so return true
		return true;
	}	
}
