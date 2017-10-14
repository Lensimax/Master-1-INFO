package grapher.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;



public class Main extends Application {
	public void start(Stage stage) {
		BorderPane root = new BorderPane();
		root.setCenter(new GrapherCanvas(getParameters()));


		
		stage.setTitle("grapher");
		stage.setScene(new Scene(root));
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}