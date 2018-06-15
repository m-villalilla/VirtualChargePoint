package ocpp_client_frontend;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import eu.chargetime.ocpp.model.core.AuthorizeConfirmation;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TestingAuthenticationWrapper implements Observer{
	private Stage stage = new Stage();
	
	@Override
	public void update(Observable arg0, Object arg1) {
		Platform.runLater( () -> {
				Parent root = null;
				Scene scene;
				String status = ((AuthorizeConfirmation) arg1).getIdTagInfo().getStatus().toString();
				
				FXMLLoader fxmll = new FXMLLoader(getClass().getResource("TestingAuthentification.fxml"));
				
				try {
					if(status != "Accepted") {
						fxmll.getNamespace().put("authLabelText", "The entered authorization ID is invalid.\nStatus: " + status);
					} else {
						fxmll.getNamespace().put("authLabelText", "The entered authorization ID is valid.");
					}
					
					root = fxmll.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
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
		);
	}
}
