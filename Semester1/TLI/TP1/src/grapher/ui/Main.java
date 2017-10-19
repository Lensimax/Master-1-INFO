package grapher.ui;

import grapher.fc.FunctionFactory;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.*;
import javafx.scene.text.Font;
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

        list_function.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                //TODO changement de l'ecriture
            }
        });


        for(String param: getParameters().getRaw()) { // ajout des fonctions en parametre)
            list_function.getItems().add(new Text(param));
        }

        Font font;

        for(Text func: list_function.getSelectionModel().getSelectedItems()){
            // TODO changer le text en BOLD
            font = func.getFont();

            func.setFont(Font.font(font.getName(), FontWeight.BOLD, font.getSize()));

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