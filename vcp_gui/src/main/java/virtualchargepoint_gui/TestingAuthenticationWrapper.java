package virtualchargepoint_gui;

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

/**
 * This class is used, to evaluate the result of a authentication test and displays it in a new stage.
 */
public class TestingAuthenticationWrapper implements Observer{
	private Stage stage = new Stage();
	
	/**
	 * This function is called multiple times while the test is running.
	 * It collects the results and combines them into one displayable result.
	 * 
	 *  @param arg0 - {@inheritDoc}
	 *  @param arg1 - {@inheritDoc}
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		Platform.runLater( () -> {
				String status = ((AuthorizeConfirmation) arg1).getIdTagInfo().getStatus().toString();
				FXMLLoader fxmll = new FXMLLoader(getClass().getResource("TestingAuthentification.fxml"));
				Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
				Image icon = new Image("file:src/main/resources/ChargePointIcon.png");
				Parent root = null;
				Scene scene = null;
				
				if(status != "Accepted") {
					fxmll.getNamespace().put("authLabelText", "The entered authorization ID is invalid.\nStatus: " + status);
				} else {
					fxmll.getNamespace().put("authLabelText", "The entered authorization ID is valid.");
				}
								
				try {
					root = fxmll.load();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				scene = new Scene(root,580,357);
				scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
				stage.setScene(scene);
				stage.getIcons().add(icon);
				stage.show();
				
				//Message Windows position at center of screen
			    stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2); 
			    stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
			}
		);
	}
}
