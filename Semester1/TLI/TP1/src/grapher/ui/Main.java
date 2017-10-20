package grapher.ui;

import grapher.fc.FunctionFactory;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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

        list_function.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> {
            list_function.getSelectionModel().getSelectedItems();

            // back to normal
            Font old_font;
            Text t;
            FontWeight fontw;
            ObservableList<Integer> obs_tab = list_function.getSelectionModel().getSelectedIndices();


            for(int i=0; i<list_function.getItems().size(); i++){
                t = list_function.getItems().get(i);
                old_font = t.getFont();

                if(obs_tab.contains(i)){
                    fontw = FontWeight.BOLD;
                } else {
                    fontw = FontWeight.NORMAL;
                }

                t.setFont(Font.font(old_font.getName(), fontw, old_font.getSize()));
            }

        });


        for(String param: getParameters().getRaw()) { // ajout des fonctions en parametre)
            list_function.getItems().add(new Text(param));
        }

		// boutons add delete
        Button but_add = new Button("+");
        but_add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                // TODO ajout de fonction
                System.out.println("Ajout de fonction");
            }
        });

        Button but_delete = new Button("-");
        but_delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
               for(Text t: list_function.getSelectionModel().getSelectedItems()){
                   list_function.getItems().remove(t);
                   // TODO remove du repere
                   ((GrapherCanvas) repere.getCenter()).delete_function(t.getText());
                   ((GrapherCanvas) repere.getCenter()).redraw();
               }
            }
        });

        ToolBar buttons = new ToolBar();
        buttons.getItems().addAll(but_add,new Separator(), but_delete);
        buttons.setStyle("-fx-background-color: white; -fx-padding: 5px;");

        BorderPane functions = new BorderPane();


        functions.setBottom(buttons);
        functions.setCenter(list_function);

		root.getItems().addAll(functions, repere);
		root.setDividerPositions(0.2);


		
		stage.setTitle("Grapher");
		stage.setScene(new Scene(root));
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}


}