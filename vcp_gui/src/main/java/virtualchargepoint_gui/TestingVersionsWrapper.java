package virtualchargepoint_gui;

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
public class TestingVersionsWrapper implements Observer, Initializable {
	@FXML private TableView<VersionRow> tableVersions;
	private Stage stage = new Stage();
	private static VersionRow[] vrs = new VersionRow[5];
	private int currentRun = 0;
	private String[] versions = {"Version 1.0", "Version 1.2", "Version 1.5", "Version 1.6", "Version 2.0"};	
	
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
				Image icon = new Image("file:src/main/resources/ChargePointIcon.png");
				Parent root = null;
				Scene scene = null;
				String status = (String) arg1;
				
				vrs[currentRun] = new VersionRow(versions[currentRun], status);
				
				if(currentRun != 4) {
					currentRun++;
					return;
				}
					
				try {
					root = FXMLLoader.load(getClass().getResource("ServerVersion.fxml"));
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
		ObservableList<VersionRow> list = tableVersions.getItems();
		list.add(vrs[0]);
		list.add(vrs[1]);
		list.add(vrs[2]);
		list.add(vrs[3]);
		list.add(vrs[4]);		
	}
}
