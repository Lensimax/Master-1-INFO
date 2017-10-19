package grapher.ui;

import grapher.fc.FunctionFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;

import java.awt.*;


public class Main extends Application {
	public void start(Stage stage) {
		SplitPane root = new SplitPane();

		BorderPane repere = new BorderPane();
		repere.setCenter(new GrapherCanvas(getParameters()));

		// liste de fonctions
		ListView<Text> list_function = new ListView<Text>();
		list_function.setStyle("-fx-alignment: center; -fx-background-color: white;");
        list_function.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        for(String param: getParameters().getRaw()) { // ajout des fonctions en parametre)
            list_function.getItems().add(new Text(param));
        }

        for(Text func: list_function.getSelectionModel().getSelectedItems()){
            // TODO changer le text en BOLD
//            func.setFont(Font.BOLD);
        }



		// boutons add delete
        BorderPane buttons = new BorderPane();
        buttons.setLeft(new Button("+"));
        buttons.setRight(new Button("-"));
        buttons.setStyle("-fx-alignment: bottom; -fx-background-color: white; -fx-padding: 5px;");

        BorderPane functions = new BorderPane();


        functions.setBottom(buttons);
        functions.setCenter(list_function);

		root.getItems().addAll(functions, repere);
		root.setDividerPositions(0.2);


		
		stage.setTitle("grapher");
		stage.setScene(new Scene(root));
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}


}