package ocpp_client_frontend;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
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
	
	
	//select one of 3 charging options, print output in 2nd label (right one)
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

	//fill comboBox with predefined values from an observable list
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		combobox.setItems(list);
	}
	
	//print selected value in 1st label (left)
	public void comboChanged(ActionEvent event){
		label1.setText(combobox.getValue());
	}

	
	//regarding selected combobox value pressing start button leads to new message window 
	public void start(ActionEvent event)throws Exception {
		//if (!ipAddress.getText().equals("") && !chargePointID.getText().equals("") && !idAuthorization.getText().equals("")){
		if (combobox.getValue().equals("Testing Authentification") && (!idAuthorization.getText().equals(""))){	
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("Authentification.fxml"));
			Scene scene = new Scene(root,580,357);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} 
		
		else if (combobox.getValue().equals("Testing Transaction")&& (!ipAddress.getText().equals("")) && (!idAuthorization.getText().equals("")) && (!chargePointID.getText().equals(""))){	
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("TestingTransaction.fxml"));
			Scene scene = new Scene(root,580,357);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		else if (combobox.getValue().equals("Getting Server functions & Server version") && (!ipAddress.getText().equals("")) && (!idAuthorization.getText().equals(""))){	
			Stage primaryStage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource("ServerFunctionVersion.fxml"));
			Scene scene = new Scene(root,580,357);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		}
		
	}
	
	
}
