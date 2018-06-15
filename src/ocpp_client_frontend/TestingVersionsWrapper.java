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

public class TestingVersionsWrapper implements Observer, Initializable {
	private Stage stage = new Stage();
	private VersionRow[] vrs = new VersionRow[5];
	private int currentRun = 0;
	private String[] versions = {"Version 1.0", "Version 1.2", "Version 1.5", "Version 1.6", "Version 2.0"};
	
	@FXML
	private TableView<VersionRow> tableVersions;
	
	@Override
	public void update(Observable arg0, Object arg1) {
		Platform.runLater( () -> {
				Parent root = null;
				Scene scene;
				String status = (String) arg1;
				
				vrs[currentRun] = new VersionRow(versions[currentRun], status);
				
				if(currentRun != 4) {
					currentRun++;
					return;
				}
					
				try {
					FXMLLoader fxmll = new FXMLLoader(getClass().getResource("ServerVersion.fxml"));
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
