package ocpp_client_frontend;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import eu.chargetime.ocpp.model.core.AuthorizeConfirmation;
import eu.chargetime.ocpp.model.core.StopTransactionConfirmation;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class TestingTransactionWrapper implements Observer {
	private Stage stage = new Stage();
	private boolean errorOccured = false;
	
	@Override
	public void update(Observable arg0, Object arg1) {
		Platform.runLater( () -> {
				Parent root = null;
				Scene scene;
				FXMLLoader fxmll = new FXMLLoader(getClass().getResource("TestingTransaction.fxml"));
				
				if(errorOccured) return;
				if(arg1 instanceof AuthorizeConfirmation) {
					if(((AuthorizeConfirmation) arg1).getIdTagInfo().getStatus().toString() == "Accepted") {
						return;
					}
					else {
						try {
							errorOccured = true;
							fxmll.getNamespace().put("transLabelText", "Test was not successful.\nReason: ID invalid.");
							fxmll.getNamespace().put("transImgUrl", "file:icons/TrafficlightRed.png");
							root = fxmll.load();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if(arg1 instanceof StopTransactionConfirmation && !errorOccured){
					try { 
						fxmll.getNamespace().put("transLabelText", "Test was successful.");
						fxmll.getNamespace().put("transImgUrl", "file:icons/TrafficlightGreen.png");
						
						root = fxmll.load();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else return;
				
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
