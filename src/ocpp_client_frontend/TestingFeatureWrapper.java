package ocpp_client_frontend;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * This class is used, to evaluate the result of a version test and displays it in a new stage.
 * It is also used as the controller of the anchorVersion in ServerVersion.fxml
 */
public class TestingFeatureWrapper implements Observer, Initializable {
	@FXML private TableView<FeatureRow> tableFeatures;
	private Stage stage = new Stage();
	private static FeatureRow[] frs = new FeatureRow[8];
	private int currentRun = 0;
	
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
				Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
				Image icon = new Image("file:icons/ChargePointIcon.png");
				Parent root = null;
				Scene scene = null;
				String message = (String) arg1;
				String feature = message.substring(30, message.indexOf('@'));
				String support = message.substring(message.indexOf('*') + 1);
				
				frs[currentRun] = new FeatureRow(feature, support);
				
				if(currentRun != 7) {
					currentRun++;
					return;
				}
					
				try {
					root = FXMLLoader.load(getClass().getResource("ServerFunction.fxml"));
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

	/**
	 * Used to fill the tableview with the test results.
	 * 
	 * @param arg0 - {@inheritDoc}
	 * @param arg1 - {@inheritDoc} 
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		ObservableList<FeatureRow> list = tableFeatures.getItems();
		list.add(frs[0]);
		list.add(frs[1]);
		list.add(frs[2]);
		list.add(frs[3]);
		list.add(frs[4]);
		list.add(frs[5]);
		list.add(frs[6]);
		list.add(frs[7]);
	}
}
