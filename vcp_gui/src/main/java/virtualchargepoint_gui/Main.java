package virtualchargepoint_gui;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image; 

public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
			Scene scene = new Scene(root,792,656);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			Image icon = new Image("file:src/main/resources/ChargePointIcon.png");
			primaryStage.getIcons().add(icon);
			primaryStage.setTitle("Virtual Charge Point");
			primaryStage.setResizable(false);
			
			//position the primary stage at the center of the screen
			Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
			primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
	        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
				        
	        primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * On close the application shall disconnect the server connection, if there is any.
	 */
	@Override
	public void stop() {
		MainController.chargepoint.disconnect();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
